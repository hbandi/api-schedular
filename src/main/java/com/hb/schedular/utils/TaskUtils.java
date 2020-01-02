package com.hb.schedular.utils;

import com.hb.schedular.config.ConfigConstants;
import com.hb.schedular.task.ScheduleTaskManager;
import com.hb.schedular.task.Task;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TaskUtils {

  private static Logger log = LoggerFactory.getLogger(TaskUtils.class);

  public static Task toTask(JSONObject json) {
    if (Objects.isNull(json) || json.isEmpty()) {
      log.debug(" received task request is null,so return null");
      return null;
    }
    Task task = new Task();
    task.setId((String) json.get(ConfigConstants.ID));
    task.setContext((String) json.get(ConfigConstants.CONTEXT));
    task.setEndPoint((String) json.get(ConfigConstants.END_POINT));
    task.setSchedule((String) json.get(ConfigConstants.SCHEDULE));
    task.setSuccessCallBackEndPoint((String) json.get(ConfigConstants.SUCCESS_END_POINT));
    task.setFailureCallBackEndPoint((String) json.get(ConfigConstants.FAILURE_END_POINT));
    return task;
  }

  public static JSONObject feomTask(Task task){
      if (Objects.isNull(task) ) {
          log.debug(" Received task is null,so return null");
          return null;
      }
      JSONObject doc=new JSONObject();
      doc.put(ConfigConstants.ID, task.getId());
      doc.put(ConfigConstants.CONTEXT, task.getContext());
      doc.put(ConfigConstants.SCHEDULE, task.getSchedule());
      doc.put(ConfigConstants.END_POINT, task.getEndPoint());
      doc.put(ConfigConstants.SUCCESS_END_POINT, task.getSuccessCallBackEndPoint());
      doc.put(ConfigConstants.FAILURE_END_POINT, task.getFailureCallBackEndPoint());
      return doc;
  }
}
