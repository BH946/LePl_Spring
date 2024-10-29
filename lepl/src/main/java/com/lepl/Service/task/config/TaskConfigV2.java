package com.lepl.Service.task.config;

import com.lepl.Repository.task.TaskRepository;
import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.TaskServiceV2;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 빈 등록
@Configuration
public class TaskConfigV2 {

  @Bean
  TaskService taskService(TaskRepository taskRepository, MeterRegistry registry) {
    return new TaskServiceV2(taskRepository, registry);
  }

  @Bean
  public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry); // @Timed 사용 위해서 반드시 필수 -> AOP 사용
  }
}
