package com.lepl.Service.task.config;

import com.lepl.Repository.task.TaskRepository;
import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.TaskServiceV1;

/**
 * 미사용
 */
//@Configuration
public class TaskConfigV1 {

    //    @Bean
    TaskService taskService(TaskRepository taskRepository) {
        return new TaskServiceV1(taskRepository);
    }
}
