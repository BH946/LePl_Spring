package com.lepl.Service.task;

import com.lepl.Repository.task.TaskRepository;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/*
어노테이션 사용시 아래와같은 빌더 작성을 생략가능 -> 따라서 @Timed 사용
Counter.builder("my.task")
        .tag("class", this.getClass().getName())
        .tag("method", "addTask")
        .description("task")
        .register(registry).increment();
 */
@Timed("my.task") // 모니터링
@Transactional(readOnly = true) // 읽기모드 기본 사용
@RequiredArgsConstructor // 생성자 주입 + 엔티티 매니저(서비스에서는 안씀)
public class TaskServiceV2 implements TaskService {

  private final TaskRepository taskRepository;
  private final MeterRegistry registry;

  /**
   * join, findOne, findOneWithMember, remove, update, updateState
   */

  /**
   * 일정 추가
   */
  @Override
  @Transactional // 쓰기모드 사용 위해
  public void join(Task task) {
    taskRepository.save(task);
    sleep(500);
  }

  private static void sleep(int l) {
    try {
      Thread.sleep(l + new Random().nextInt(200));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 일정 조회 일정 전체 조회
   */
  @Override
  public Task findOne(Long taskId) {
    return taskRepository.findOne(taskId);
  }

  @Override
  public Task findOneWithMember(Long memberId, Long taskId) {
    return taskRepository.findOneWithMember(memberId, taskId);
  }

  /**
   * 일정 삭제
   */
  @Override
  @Transactional // 쓰기모드 사용 위해
  public void remove(Task task) {
    taskRepository.remove(task);
  }

  @Override
  @Transactional // 더티체킹 - db 적용
  public void update(Task task, String content, LocalDateTime startTime, LocalDateTime endTime) {
    task.updateTask(content, startTime, endTime);
  }

  @Override
  @Transactional // 일정 완료 & 타이머 종료 - db 적용
  public void updateStatus(Task task, Boolean completedStatus, Boolean timerOnOff,
      Long remainTime) {
    TaskStatus taskStatus = task.getTaskStatus().update(completedStatus, timerOnOff);
    task.updateTaskStatus(taskStatus, remainTime);
    sleep(200);
  }

  @Override
  @Transactional // 벌크연산 - db 적용
  public void updateAll(List<Task> taskList, String content, LocalDateTime startTime, LocalDateTime endTime) {
    taskRepository.updateAll(taskList, content, startTime, endTime);
  }
}
