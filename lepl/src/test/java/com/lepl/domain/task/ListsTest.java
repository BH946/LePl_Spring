package com.lepl.domain.task;

import com.lepl.domain.member.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ListsTest {

  static Member member; // 전역

  @BeforeEach
  public void 멤버_추가() {
    member = Member.createMember("12", "Lists테스트");
  }


  @Test
  public void 연관관계_편의메서드() throws Exception {
    // given
    Lists lists = Lists.createLists(member, null, new ArrayList<>());

    // when
    lists.addTask(new Task());

    // then
    Assertions.assertEquals(lists.getTasks().size(), 1);
  }

  @Test
  public void 생성_편의메서드() throws Exception {
    // given
    Lists lists;

    // when
    lists = Lists.createLists(member, null, new ArrayList<>());

    // then
    Assertions.assertInstanceOf(Lists.class, lists);
    Assertions.assertEquals(LocalDateTime.now().toLocalDate(),
        lists.getListsDate()); // 금일날짜 자동적용 (날짜만 비교)
  }

  @Test
  public void 비지니스_편의메서드() throws Exception {
    // given
    Lists lists;
    List<Task> tasks = new ArrayList<>();
    LocalDateTime date = LocalDateTime.now();
    LocalDateTime date2 = LocalDateTime.of(2023, 11, 4, 1, 0);
    Task task = Task.createTask("임시 테스트", date, date, new TaskStatus());
    Task task2 = Task.createTask("임시 테스트", date2, date2, new TaskStatus());
    tasks.add(task);
    tasks.add(task2);

    // when
    lists = Lists.createLists(member, date, tasks); // 내부에 compareDate 테스트
    Lists lists2 = Lists.createLists(member, date2, tasks); // 내부에 compareDate 테스트

    // then
    List<Task> findTasks = lists.getTasks(); // compareDate 에 의해 lists<->task 가 동일한 날짜만 기록되어야 정상
    List<Task> findTasks2 = lists2.getTasks();
    for (Task t : findTasks) {
      Assertions.assertEquals(lists.getListsDate(), t.getStartTime());
      Assertions.assertNotEquals(lists.getListsDate(),
          task2.getStartTime()); // lists<->task2 날짜는 달라야 정상
    }
    for (Task t2 : findTasks2) {
      Assertions.assertEquals(lists2.getListsDate(), t2.getStartTime());
      Assertions.assertNotEquals(lists2.getListsDate(),
          task.getStartTime()); // lists2<->task 날짜는 달라야 정상
    }
  }
}