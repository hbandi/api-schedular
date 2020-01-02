package com.hb.schedular.config;

import com.hb.schedular.db.NitriteDBManager;
import com.hb.schedular.task.Task;
import com.hb.schedular.utils.TaskUtils;
import org.dizitart.no2.NitriteCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
public class ConfigUtils {

  private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

  @Autowired private NitriteDBManager nitriteDBManager;
  @Autowired private ConfigLoaderUtil configLoaderUtil;

  @PostConstruct
  public void getTasks() {
    logger.debug("ConfigUtils started Loading task configurations ");
    List<JSONObject> tasks = new ArrayList<>();
    try {
      JSONParser jsonParser = new JSONParser();
      File containerRegistries = configLoaderUtil.getTaskConfigs();
      JSONObject jsonConfig = (JSONObject) jsonParser.parse(new FileReader(containerRegistries));
      JSONArray configsArray = (JSONArray) jsonConfig.get("tasks");
      if (configsArray == null || configsArray.size() == 0) {
        logger.debug("No Tasks configured to traverse,so exit");
      }
      Iterator<JSONObject> iterator = configsArray.iterator();
      while (iterator.hasNext()) {
        tasks.add(iterator.next());
      }
      for (JSONObject json : tasks) {
        nitriteDBManager.postTask(TaskUtils.toTask(json));
      }
    } catch (Exception ex) {
      logger.debug("Found Exception while Loading task configurations {} ", ex.getMessage());
    }
  }

  public List<Task> getTasksFromDB() {
    return nitriteDBManager.loadAllTasks();
  }
}
