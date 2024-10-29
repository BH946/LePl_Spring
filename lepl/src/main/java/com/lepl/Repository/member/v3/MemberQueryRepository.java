package com.lepl.Repository.member.v3;

import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.member.Member;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.lepl.domain.member.QMember.*;

@Repository
//@RequiredArgsConstructor
public class MemberQueryRepository {
  private final JPAQueryFactory query;
  private final EntityManager em;

  public MemberQueryRepository(EntityManager em) {
    query = new JPAQueryFactory(em);
    this.em = em;
  }

  //일반 JPA
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

  //QueryDSL 사용해보기 -> 자동생성 Q클래스 덕분에 member 가 사용 가능
  public List<Member> findAllByNickname(String nickname) {
    return query.select(member)
        .from(member)
        .where(
            likeNickname(nickname)
        )
        .fetch();
  }

  private BooleanExpression likeNickname(String nickname) {
    if (StringUtils.hasText(nickname)) {
      return member.nickname.like("%" + nickname + "%");
    }
    return null;
  }
}
