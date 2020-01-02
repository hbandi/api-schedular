package com.hb.schedular.task;

import com.hb.schedular.db.NitriteDBManager;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class ScheduleTaskManager {

  private static Logger log = LoggerFactory.getLogger(ScheduleTaskManager.class);

  @Autowired private NitriteDBManager nitriteDBManager;
  @Autowired private ScheduleTaskConfigurator scheduleTaskConfigurator;

  public JSONObject postTask(Task task) {
    log.debug("On demand task has been called with taskId {} ", task.getId());
    JSONObject jsonObject = new JSONObject();
    try {
      JSONObject existingTask = nitriteDBManager.getTask(task.getId(), task.getContext());
      if (Objects.isNull(existingTask)) {
        jsonObject.put("status", "DUPLICATE_TASK");
        return jsonObject;
      }
      if (scheduleTaskConfigurator.scheduleTask(task)) {
        nitriteDBManager.postTask(task);
        jsonObject.put("status", "SUCCESS");
        return jsonObject;
      }
    } catch (Exception ex) {
      log.error(
          "Found an exception while adding task to context ,exception is {} ", ex.getMessage());
    }
    jsonObject.put("status", "FAILED");
    return jsonObject;
  }
}
