package com.lepl.Repository.member.v3;

import com.lepl.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 단순ver 예시 -> JPA+Spring Data JPA+QueryDSL 다 나타내보겠다. -> 서비스단이 수정!
 * 참고로 Spring Data JPA+QueryDSL(+JPA) 구조
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
  //CRUD 자동 제공
  Member findByUid(String uid); // select m from Member m where m.uid := uid
}
