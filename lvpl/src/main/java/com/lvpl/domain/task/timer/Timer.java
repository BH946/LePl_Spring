package com.lvpl.domain.task.timer;

import com.lvpl.domain.task.Task;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Timer {
    @Id @GeneratedValue
    @Column(name = "timer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id") // FK
    private Task task;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TimerStatus timerStatus; // 집중상태 [허용앱 상태, 최대집중 상태]
}
