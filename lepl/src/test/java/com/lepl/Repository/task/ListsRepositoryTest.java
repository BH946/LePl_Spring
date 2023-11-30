package com.lepl.Repository.task;

import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
public class ListsRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    ListsRepository listsRepository;
    @Autowired
    TaskRepository taskRepository;
    static List<Task> tasks; // 전역
    static Long memberId;
    static Long listsId;

    @BeforeEach // @Test 마다 수행 전 호출
    public void beforeEach() {
        tasks = new ArrayList<>(); // init
        // 2023-11-1~3 일정 임시로 추가 -> 각 task 는 2개씩
        for (int i = 1; i <= 3; i++) {
            LocalDateTime date_s = LocalDateTime.of(2023, 11, i, 1, 0); // 1시
            LocalDateTime date_e = LocalDateTime.of(2023, 11, i, 3, 0); // 3시
            LocalDateTime date2_s = LocalDateTime.of(2023, 11, i, 5, 0); // 5시
            LocalDateTime date2_e = LocalDateTime.of(2023, 11, i, 8, 0); // 8시
            TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
            Task task = Task.createTask("조회 테스트입니다.", date_s, date_e, taskStatus);
            Task task2 = Task.createTask("조회 테스트입니다.2", date2_s, date2_e, taskStatus);
            tasks.add(task);
            tasks.add(task2);
        }
    }

    /**
     * findAllWithMemberTask, findByDateWithMemberTask, save, findByCurrent, findOneWithMemberTask, remove
     */
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = false) // 롤백 취소
    public void 멤버의_일정저장과조회_한개와지정날짜() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        Member member = Member.createMember("Lists 테스트", "닉네임테스트");
        em.persist(member);
        memberId = member.getId(); // 전역 기록

        // when
        List<Lists> listsList = new ArrayList<>();
        log.info("첫 taskId {}", tasks.get(0).getId());
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists lists = Lists.createLists(member, listsDate, tasks);
            listsRepository.save(lists); // persist
            listsList.add(lists);
        }
        listsId = listsList.get(0).getId();
        for (Task t : tasks) {
            log.info("taskId {}",t.getId());
//            em.persist(t.getTaskStatus()); // FK
//            em.persist(t);
            /**
             * 잠시 알게된 부분 정리
             * Lists.createLists 에서 tasks 내용이 수정이 되기 때문에 tasks 는 꼭 나중에 persist 적용!!
             * 만약 먼저 persist 적용된 상태에서 Lists.createLists 에서 tasks 내용 수정되면 더티체킹 발생하므로 update 쿼리 추가
             * 즉, insert + update 쿼리 전송되기 때문에 아래에서 persist 적용
             * => 이런 부분 Controller 에 있을수도 있으니 확인필요.
             */
        }

        // then
        for (int i = 1; i <= 3; i++) {
            LocalDateTime listsDate = LocalDateTime.of(2023, 11, i, 1, 0);
            Lists findLists = listsRepository.findByCurrent(member.getId(), listsDate); // flush
            Long listsId = findLists.getId();
            Lists findLists2 = listsRepository.findOneWithMemberTask(member.getId(), listsId); // flush
            Assertions.assertEquals(findLists.getId(), listsList.get(i - 1).getId());
            Assertions.assertEquals(findLists2.getId(), listsList.get(i - 1).getId());
        }
        log.info("마지막 taskId {}", tasks.get(0).getId());
    }

    @Test
    @Order(2)
    @Transactional
    public void 멤버의_일정조회_전체와날짜별() throws Exception {
        // given
        log.info("size : {}", tasks.size());
        // 2023-11-1~3 일정 조회위해 start, end 를 1~4
        LocalDateTime start = LocalDateTime.of(2023, 11, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2023, 11, 4, 1, 0);

        // when
        List<Lists> findLists = listsRepository.findAllWithMemberTask(memberId);
        List<Lists> findLists2 = listsRepository.findByDateWithMemberTask(1L, start, end);

        // then
        for (Lists l : findLists) {
            log.info("findLists : {}", l.getId());
        }
        for (Lists l : findLists2) {
            log.info("findLists2 : {}", l.getListsDate());
        }
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback(value = false) // db 적용 확인위해
    public void 멤버의_일정_삭제() throws Exception {
        // given
        Lists findLists = listsRepository.findOneWithMemberTask(memberId, listsId); // flush

        // when
        listsRepository.remove(findLists); // persist(delete)
        Lists lists3 = listsRepository.findOne(3L);
        log.info("delete 쿼리문 날라가는 시점 체크1");
        findLists = listsRepository.findOneWithMemberTask(memberId, listsId); // flush
        log.info("delete 쿼리문 날라가는 시점 체크2");

        // then
        Assertions.assertEquals(findLists, null);
    }
}