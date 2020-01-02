package com.hb.schedular.db;

import com.hb.schedular.config.ConfigConstants;
import com.hb.schedular.controller.ScheduleController;
import com.hb.schedular.task.Task;
import org.dizitart.no2.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.util.resources.cldr.nd.CalendarData_nd_ZW;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.dizitart.no2.filters.Filters.and;
import static org.dizitart.no2.filters.Filters.eq;

@Component
public class NitriteDBManager {

  private static Logger log = LoggerFactory.getLogger(NitriteDBManager.class);
  private Nitrite db;

  @Value("${nitrite.db.name}")
  private String dbName;

  @Value("${nitrite.db.path}")
  private String dbPath;

  @Value("${nitrite.db.user}")
  private String dbUser;

  @Value("${nitrite.db.password}")
  private String password;

  @Value("${nitrite.db.collection}")
  private String taskCollection;

  @PostConstruct
  public void init() {
    log.debug("Initialising task database ");
    db = Nitrite.builder().compressed().filePath(dbPath + dbName).openOrCreate(dbUser, password);
  }

  public boolean postTask(Task task) {
    log.debug("NitriteDBManager.postTask been called with taskId {} ", task.getId());
    try {
      Document doc = new Document();
      doc.put(ConfigConstants.ID, task.getId());
      doc.put(ConfigConstants.CONTEXT, task.getContext());
      doc.put(ConfigConstants.SCHEDULE, task.getSchedule());
      doc.put(ConfigConstants.END_POINT, task.getEndPoint());
      doc.put(ConfigConstants.SUCCESS_END_POINT, task.getSuccessCallBackEndPoint());
      doc.put(ConfigConstants.FAILURE_END_POINT, task.getFailureCallBackEndPoint());
      WriteResult result = getCollection(taskCollection).insert(doc);
      return result.getAffectedCount() > 0 ? true : false;
    } catch (Exception ex) {
      log.error("NitriteDBManager.postTask found an exception ,ex is {} ", ex.getMessage());
    }
    return false;
  }

  public JSONObject getTask(String id, String context) {
    log.debug("NitriteDBManager.getTask been called with id {} , context {} ", id, context);
    try {
      Cursor cursor =
          getCollection(taskCollection)
              .find(and(eq(ConfigConstants.ID, id), eq(ConfigConstants.CONTEXT, context)));
      if (cursor == null || cursor.size() == 0) {
        return null;
      }
      Document doc = cursor.firstOrDefault();
      JSONObject json = new JSONObject();
      for (String key : doc.keySet()) {
        json.put(key, doc.get(key));
      }
      return json;
    } catch (Exception ex) {
      log.error("NitriteDBManager.getTask Found an exception ,{}  ", ex.getMessage());
    }
    return null;
  }

  /** When start of this service load all the needed tasks from temporary nitrite db. */
  public List<Task> loadAllTasks() {
    log.debug("NitriteDBManager.loadAllTasks called loading all the tasks into context");
    try {
      Cursor cursor = getCollection(taskCollection).find();
      Document doc = cursor.firstOrDefault();
      List<Task> list = new ArrayList<>();
      for (Document d : cursor) {
        Task task = new Task();
        task.setId((String) doc.get(ConfigConstants.ID));
        task.setContext((String) doc.get(ConfigConstants.CONTEXT));
        task.setEndPoint((String) doc.get(ConfigConstants.END_POINT));
        task.setSchedule((String) doc.get(ConfigConstants.SCHEDULE));
        task.setSuccessCallBackEndPoint((String) doc.get(ConfigConstants.SUCCESS_END_POINT));
        task.setFailureCallBackEndPoint((String) doc.get(ConfigConstants.FAILURE_END_POINT));
        list.add(task);
      }
      return list;
    } catch (Exception ex) {
      log.error(
          "NitriteDBManager.loadAllTasks found an exception while loading tasks from temp db");
    }
    return Collections.EMPTY_LIST;
  }

  public NitriteCollection getCollection(String collectionName) {
    return db.getCollection(collectionName);
  }
}
