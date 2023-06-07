package team.lepl_team.Domain.Task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TaskStatus {

    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private Boolean completeStatus; //일정 완료 여부
    private Boolean timerOnOff; //타이머 사용 여부

    //생성 편의 메서드
    public static TaskStatus createTaskStatus(Boolean completeStatus, Boolean timerOnOff) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setCompleteStatus(completeStatus);
        taskStatus.setTimerOnOff(timerOnOff);
        return taskStatus;
    }
}
