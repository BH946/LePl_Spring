package com.lepl.Service.task;

import com.lepl.domain.task.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
public class TaskStatusServiceTest {

  @Autowired
  TaskStatusService taskStatusService;

  /**
   * join, findOne
   */

  @Test
  public void 일정상태_저장과조회() throws Exception {
    // given
    TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);

    // when
    taskStatusService.join(taskStatus);
    TaskStatus findTaskStatus = taskStatusService.findOne(taskStatus.getId());

    // then
    Assertions.assertEquals(taskStatus.getId(), findTaskStatus.getId());
    Assertions.assertEquals(findTaskStatus.getTimerOnOff(), false);
    Assertions.assertEquals(findTaskStatus.getCompletedStatus(), false);
  }
}