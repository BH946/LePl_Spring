package com.lepl.Repository.member.v2;

import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * MemberRepository 구현체 -> SpringDataJpa 여기서 추가 사용!! (레포지토리 단)
 */
@Repository
@RequiredArgsConstructor // 생성자 주입 + 엔티티매니저 주입 제공
public class JpaMemberRepository implements MemberRepository {
  private final SpringDataJpaMemberRepository repository;
  private final EntityManager em;

  @Override
  public void save(Member member) {
    repository.save(member);
  }

  @Override
  public Member findOne(Long id) {
    return repository.findById(id).orElse(null); //orElse() 안하면 Optional<T> 반환
  }

  @Override
  public Member findByUid(String uid) {
    return repository.findByUid(uid);
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
