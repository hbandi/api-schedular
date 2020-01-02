package com.hb.schedular.task;

import com.hb.schedular.db.NitriteDBManager;
import okhttp3.Call;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.Callable;

public class ScheduleTask implements Runnable {

  private static Logger log = LoggerFactory.getLogger(ScheduleTask.class);
  private Task task;
  private RestTemplate restTemplate;

  ScheduleTask(Task taskContext, RestTemplate restTemplate) {
    this.task = taskContext;
    this.restTemplate = restTemplate;
  }

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    try {
      if (Objects.isNull(task)) {
        log.debug("Passed task is null,so returning from here ");
        return;
      }
      JSONObject request = new JSONObject();
      ResponseEntity<JSONObject> response = null;
      try {
        response = restTemplate.postForEntity(task.getEndPoint(), request, JSONObject.class);
        log.debug(
            "ScheduledTask initial call been returned response is {} for taskId {} ",
            response,
            task.getId());
      } catch (Exception ex) {
        log.error(
            "Found exception while running  task ,taskId {} ,so calling failure end point",
            task.getId());
        restTemplate.postForEntity(task.getSuccessCallBackEndPoint(), request, JSONObject.class);
        return;
      }
      if (response == null
          || response.getBody().isEmpty()
          || response.getBody().get("responseCode") != "200") {
        log.debug("ScheduledTask found success in first call calling success end point ");
        restTemplate.postForEntity(task.getFailureCallBackEndPoint(), request, JSONObject.class);
      } else {
        log.debug("ScheduledTask found failure in first call calling success end point ");
        restTemplate.postForEntity(task.getSuccessCallBackEndPoint(), request, JSONObject.class);
      }
    } catch (Exception ex) {
      log.error("Found exception while running  task ,taskId {} ", task.getId());
    }
    log.debug(
        " Scheduled Task Id {} took time in millis {} ",
        task.getId(),
        System.currentTimeMillis() - startTime);
  }
}
