package com.lepl.Service.task;

import com.lepl.domain.task.Task;

import java.time.LocalDateTime;

/**
 * 모니터링 위해 임의로 인터페이스 추가
 * 실제로 전부 인터페이스로 구현했으면 하는데 이미 없이 구현했으니
 * 대표적으로 Task 만 인터페이스로 구현해보겠음!
 */
public interface TaskService {
    void join(Task task); // 일정 등록

    Task findOne(Long taskId);

    Task findOneWithMember(Long memberId, Long taskId);

    void remove(Task task);

    void update(Task task, String content, LocalDateTime startTime, LocalDateTime endTime);

    void updateStatus(Task task, Boolean completedStatus, Boolean timerOnOff, Long remainTime); // 일정 완료
}
