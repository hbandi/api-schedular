package com.hb.schedular.task;

import com.hb.schedular.config.ConfigUtils;
import com.hb.schedular.task.cron.ScheduledCronTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class ScheduleTaskConfigurator implements SchedulingConfigurer {

  private static Logger log = LoggerFactory.getLogger(ScheduleTaskConfigurator.class);

  @Value("${schedule.task.count}")
  private int scheduleTaskCount;

  private static AtomicInteger totalTasks = new AtomicInteger();

  private ScheduledTaskRegistrar scheduledTaskRegistrar;
  @Autowired private ConfigUtils configUtils;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    log.debug("ScheduleTaskManager configure tasks  has been initialised ");
    if (Objects.isNull(scheduledTaskRegistrar)) {
      scheduledTaskRegistrar = taskRegistrar;
    }
    if (Objects.isNull(scheduledTaskRegistrar.getScheduler())) {
      scheduledTaskRegistrar.setScheduler(poolScheduler());
    }
    List<Task> tasks = configUtils.getTasksFromDB();
    for (Task task : tasks) {
      schedule(task);
      totalTasks.getAndIncrement();
    }
    log.debug("Total tasks added to scheduler is {}", totalTasks.get());
  }

  public boolean scheduleTask(Task task) {
    log.debug("On demand task has been scheduled with task {} ", task.getId());
    if (schedule(task)) {
      totalTasks.getAndIncrement();
      return true;
    }
    return false;
  }

  private boolean schedule(Task task) {
    try {
      ScheduleTask scheduleTask = new ScheduleTask(task, new RestTemplate());
      ScheduledCronTask cronTask =
          new ScheduledCronTask(scheduleTask, task.getId(), task.getContext(), task.getSchedule());
      scheduledTaskRegistrar.addCronTask(cronTask);
      return true;
    } catch (Exception ex) {
      log.error("ScheduleTaskManager.schedule found an exception ,ex {} ", ex.getMessage());
    }
    return false;
  }

  @PreDestroy
  private void destroy() {
    scheduledTaskRegistrar.destroy();
    totalTasks.set(0);
  }

  private TaskScheduler poolScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("api-scheduler");
    scheduler.setPoolSize(scheduleTaskCount);
    scheduler.initialize();
    return scheduler;
  }

  public int getScheduleTaskCount() {
    return totalTasks.get();
  }
}
