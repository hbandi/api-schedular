package com.hb.schedular.controller;

import com.hb.schedular.task.ScheduleTaskManager;
import com.hb.schedular.utils.TaskUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/1.0")
public class ScheduleController {

  private static Logger log = LoggerFactory.getLogger(ScheduleController.class);

  @Autowired private ScheduleTaskManager scheduleTaskManager;

  @PostMapping(value = "/task")
  public @ResponseBody ResponseEntity<JSONObject> post(JSONObject task) {
    log.debug("ScheduleController.post been called with task {} ", task);
    JSONObject response = scheduleTaskManager.postTask(TaskUtils.toTask(task));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping(value = "/task")
  public @ResponseBody ResponseEntity<String> put(JSONObject task) {
    return null;
  }

  @DeleteMapping(value = "/task")
  public @ResponseBody ResponseEntity<String> delete(JSONObject task) {
    return null;
  }

  @GetMapping(value = "/task")
  public @ResponseBody ResponseEntity<String> get(JSONObject task) {
    return null;
  }
}
