package com.lepl.Service.task;

import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
public class TaskServiceTest {
    @Autowired
    TaskService taskService;
    @Autowired
    EntityManager em;
    static Long taskId;


    /**
     * join, findOne, findOneWithMember, remove, update, updateState
     */
    @Test
    @Order(1)
    @Rollback(false) // 삭제() 테스트 위해 잠시 롤백 제거
    public void 일정_저장과조회() throws Exception {
        // given
        TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
        em.persist(taskStatus);
        Task task = Task.createTask("테스트입니다.", LocalDateTime.now(), LocalDateTime.now(), taskStatus);

        // when
        taskService.join(task);
        Task findTask = taskService.findOne(task.getId());

        // then
        Assertions.assertEquals(task.getId(), findTask.getId());
        taskId = task.getId();
    }

    @Test
    public void 멤버의_일정조회() throws Exception {
        // given
        Task task = Task.createTask("멤버 테스트", LocalDateTime.now(), LocalDateTime.now(), TaskStatus.createTaskStatus(false,false));
        Member member = Member.createMember("UID", "닉네임");
        em.persist(member); // id 위해(FK 오류 방지)
        Lists lists = Lists.createLists(member, LocalDateTime.now(), new ArrayList<>());
        em.persist(task); // id
        lists.addTask(task);
        member.addLists(lists);
        em.persist(lists);
        log.info("전");
        em.flush(); // insert
        log.info("후");

        // when
        Task findTask = taskService.findOneWithMember(member.getId(), task.getId()); // flush

        // then
        Assertions.assertEquals(task, findTask); // em.clear() 없으므로 주소 동일
        Assertions.assertEquals(task.getId(), findTask.getId());
        Assertions.assertEquals(findTask.getContent(), "멤버 테스트");
    }

    @Test
    @Order(3)
//    @Rollback(false) // db 확인용
    public void 일정_삭제() throws Exception {
        // given
        Task findTask = taskService.findOne(taskId); // 위에서 저장했던 Task 찾기
        log.info("findTask : {}", findTask);

        // when
        taskService.remove(findTask);
        findTask = taskService.findOne(taskId);

        // then
        Assertions.assertEquals(findTask, null);
        log.info("findTask : {}", findTask);
    }

    @Test
    @Order(2)
    @Rollback(false) // db 확인용
    public void 일정_업데이트_상태() throws Exception {
        // given
        Task findTask = taskService.findOne(taskId);

        // when
        taskService.update(findTask, "일정 수정해보기", LocalDateTime.now(), LocalDateTime.now());
        em.flush(); // 쿼리까지 함께 확인위해
        em.clear();
        Task findTask2 = taskService.findOne(taskId);
        taskService.updateStatus(findTask2, true, true, 5L);
        em.flush(); // 쿼리까지 함께 확인위해
        em.clear();
        Task findTask3 = taskService.findOne(taskId);

        // then
        Assertions.assertEquals(findTask.getId(), findTask2.getId());
        Assertions.assertEquals(findTask.getId(), findTask3.getId());
        Assertions.assertEquals(findTask2.getContent(), "일정 수정해보기");
        Assertions.assertEquals(findTask3.getTaskStatus().getId(), findTask.getTaskStatus().getId());
    }
}