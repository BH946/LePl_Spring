package com.lepl.Repository.task;

import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
public class TaskRepositoryTest {

  @Autowired
  TaskRepository taskRepository;
  @Autowired
  EntityManager em;
  static Long taskId;

  /**
   * save, findOne, findAll, findOneWithMember, remove
   */
  @Test
  @Order(1)
  @Transactional // 자동 롤백
  @Rollback(false) // 삭제() 테스트 위해 잠시 롤백 제거
  public void 일정_저장과조회() throws Exception {
    // given
    Task task = Task.createTask("테스트입니다.", LocalDateTime.now(), LocalDateTime.now(),
        TaskStatus.createTaskStatus(false, false));

    // when
    taskRepository.save(task); // persist
    taskId = task.getId();
    Task findTask = taskRepository.findOne(task.getId()); // 캐시에서 가져오는
    List<Task> taskList = taskRepository.findAll(); // flush

    // then
    Assertions.assertEquals(task.getId(), findTask.getId());
    log.info("taskList size : {}", taskList.size());
    log.info("taskID : {}", taskId);
    for (Task t : taskList) {
      log.info(t.getContent());
    }
  }

  @Test
  @Transactional
  public void 멤버의_일정조회() throws Exception {
    // given
    Task task = Task.createTask("멤버 테스트", LocalDateTime.now(), LocalDateTime.now(),
        TaskStatus.createTaskStatus(false, false));
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
    Task findTask = taskRepository.findOneWithMember(member.getId(), task.getId()); // flush

    // then
    Assertions.assertEquals(task, findTask); // em.clear() 없으므로 주소 동일
    Assertions.assertEquals(task.getId(), findTask.getId());
    Assertions.assertEquals(findTask.getContent(), "멤버 테스트");
  }

  @Test
  @Order(2)
  @Transactional // 자동 롤백
  @Rollback(false) // db 확인용
  public void 일정_삭제() throws Exception {
    // given
    Task task = taskRepository.findOne(taskId); // 위에서 저장했던 Task 찾기

    // when
    taskRepository.remove(task); // persist(delete)
    em.flush(); // 강제 flush()
    em.clear();
    task = taskRepository.findOne(taskId);

    // then
    Assertions.assertEquals(task, null);
  }
}