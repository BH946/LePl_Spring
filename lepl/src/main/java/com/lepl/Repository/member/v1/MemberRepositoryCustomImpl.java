package com.lepl.Repository.member.v1;

import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data Jpa + 일반 JPA 혼합 테스트 위해
 * MemberRepositoryCustom 구현 + save() 주석 -> 인터페이스 구현체로 바꿨다보니 @Override 필수 
 */

@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티매니저 주입 제공
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

  private final EntityManager em;

  /**
   * save, findOne, findByUid, findAllWithPage
   */

//  public void save(Member member) {
//    if (member.getId() == null) {
//      em.persist(member); // db 저장
//    }
//  }
  @Override
  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }
  @Override
  public Member findByUid(String uid) {
    List<Member> findMembers = em.createQuery("select m from Member m where m.uid = :uid",
            Member.class)
        .setParameter("uid", uid)
        .getResultList(); // List로 반환 받아야 null처리가 쉬움
    return findMembers.isEmpty() ? null : findMembers.get(0);
  }
  @Override
  public List<FindMemberResponseDto> findAllWithPage(int pageId) {
    int offset = (pageId - 1) * 10;
    int limit = 10;
    List<Object[]> objects = em.createNativeQuery("select m.member_id, m.nickname, e.level " +
            "from (select * from member order by member_id desc limit " + offset + "," + limit + ") m "
            +
            "inner join character c on m.character_id=c.character_id " +
            "inner join exp e on c.exp_id=e.exp_id;")
        .getResultList();
    return objects.stream()
        .map(o -> new FindMemberResponseDto((Long) o[0], (String) o[1], (Long) o[2]))
        .collect(Collectors.toList());
  }
}
