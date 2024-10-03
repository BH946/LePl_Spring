package com.lepl.Repository.task;

import com.lepl.domain.task.Task;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티매니저 주입
public class TaskRepository {

  private final EntityManager em;

  /**
   * save, findOne, findAll, findOneWithMember, remove, updateAll
   */
  public void save(Task task) {
    if (task.getId() == null) { // null 인 경우 db에 없다는 의미(db에 insert 할 때 id 생성)
      em.persist(task); // merge 사용 x => 더티 체킹
    }
  }

  public Task findOne(Long id) {
    return em.find(Task.class, id);
  }

  public List<Task> findAll() {
    return em.createQuery("select t from Task t", Task.class) // 올바르게 매핑해서 조회하기 위해서는 Task.class 가 필요
        .getResultList();
  }

  public Task findOneWithMember(Long memberId, Long taskId) {
    List<Task> tasks = em.createQuery(
              "select t from Task t" +
                " join fetch t.lists l" +
                " where t.id = :taskId and" +
                " l.member.id = :memberId", Task.class)
        .setParameter("taskId", taskId)
        .setParameter("memberId", memberId)
        .getResultList();
      if (tasks.isEmpty()) {
          return null;
      } else {
          return tasks.get(0);
      }
  }

  public void remove(Task task) {
    em.remove(task);
  }

  // in 절로 task_id 값 비교하게 했고, start~end 날짜들 내용(content) 일괄 수정
  public void updateAll(List<Task> taskList, String content, LocalDateTime startTime, LocalDateTime endTime) {
    startTime = startTime.toLocalDate().atTime(0, 0, 0);
    endTime = endTime.toLocalDate().atTime(23, 59, 59);
    List<Long> idList = taskList.stream().map(o -> o.getId()).collect(Collectors.toList());
    int updatedCount = em.createQuery(
            "update Task t set t.content = :content" +
                " where t.startTime >= :startTime and t.endTime <= :endTime" +
                " and t.id in :idList")
        .setParameter("content", content)
        .setParameter("idList", idList)
        .setParameter("startTime", startTime)
        .setParameter("endTime", endTime)
        .executeUpdate();

    System.out.println("Updated count: " + updatedCount); // 업데이트된 개수 확인
  }

}