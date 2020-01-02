package com.hb.schedular.task.cron;

import org.springframework.scheduling.config.CronTask;

import java.util.Objects;

public class ScheduledCronTask extends CronTask {

  private String taskId;
  private String context;

  public ScheduledCronTask(Runnable runnable, String taskId, String context, String expression) {
    super(runnable, expression);
    this.taskId = taskId;
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScheduledCronTask that = (ScheduledCronTask) o;
    return taskId.equals(that.taskId) && context.equals(that.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(taskId, context);
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }
}
