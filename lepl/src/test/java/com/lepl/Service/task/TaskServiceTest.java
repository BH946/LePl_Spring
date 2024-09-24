package com.lepl.Service.task;

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
    Task findTask = taskService.findOneWithMember(member.getId(), task.getId()); // flush

    // then
    Assertions.assertEquals(task, findTask); // em.clear() 없으므로 주소 동일
    Assertions.assertEquals(task.getId(), findTask.getId());
    Assertions.assertEquals(findTask.getContent(), "멤버 테스트");
  }

  @Test
  @Order(4)
//  @Rollback(false) // db 확인용
  public void 일정_삭제() throws Exception {
    // given
    Task findTask = taskService.findOne(taskId); // 위에서 저장했던 Task 찾기. Task 만 조회함
    log.info("findTask : {}", findTask);

    // when
    //1. 영속성 컨텍스트에서 삭제 된걸 확인했기 때문에 find 에서 db 조회까지 안가고 종료
    taskService.remove(findTask); // 영속성 컨텍스트에서 상태4개 중 "제거 상태"로 운영 됨. (taskStatus 랑 cascade 이므로 지연로딩이였어서 여기서 taskStatus 조회가 발생)
    findTask = taskService.findOne(taskId); // 로그 없음. (삭제되어서) -> 영속성에서 이미 삭제된걸 확인해서 그럼. 이미 삭제한 걸 아는데 뭐하러 db 를 확인하겠음?

    //2. 원래 find 는 영속성 컨텍스트에 엔티티 없으면 db 조회까지 하기 때문에 그 부분을 로그로 보려고 함.
    em.flush();  // 삭제된 엔티티를 DB에 반영 (강제 플러시)
    em.clear();  // 영속성 컨텍스트 초기화

    // 이제 다시 조회를 시도하면 DB에서 조회
    findTask = taskService.findOne(taskId); // 삭제된 엔티티 조회 시도. 쿼리 발생! (null이어야 함)

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

  @Test
  @Order(3)
  @Rollback(false) // db 확인용
  public void 일정_일괄_업데이트_상태() throws Exception {
    //given
    List<Task> taskList = new ArrayList<>();
    for (int i = 0; i < 10; i++) { //end 날짜가 하루씩 늘어나게 저장
      Task task = Task.createTask("일괄" + i, LocalDateTime.now(), LocalDateTime.now().plusDays(i), null);
      taskService.join(task);
      taskList.add(task);
    }

    //when -> taskService.updateAll() 함수 추가 전 방식과 추가 후 방식 비교 (날라가는 쿼리 수가 다름)
    for (Task t : taskList) {
      taskService.update(t, "일정 1개씩 수정", t.getStartTime(), t.getEndTime());
    }
    em.flush(); em.clear();
    Task findTask1 = taskService.findOne(taskList.get(0).getId());
    taskService.updateAll(taskList, "일정 일괄 수정(벌크연산)", LocalDateTime.now(), LocalDateTime.now().plusDays(9L));
    em.flush(); em.clear(); //다른 주소 사용 위해 clear 까지
    Task findTask2 = taskService.findOne(taskList.get(0).getId());
    //then
    Assertions.assertEquals(taskList.get(0).getId(), findTask1.getId());
    Assertions.assertEquals(taskList.get(0).getId(), findTask2.getId());
    Assertions.assertEquals(findTask1.getContent(), "일정 1개씩 수정");
    Assertions.assertEquals(findTask2.getContent(), "일정 일괄 수정(벌크연산)");
  }
}