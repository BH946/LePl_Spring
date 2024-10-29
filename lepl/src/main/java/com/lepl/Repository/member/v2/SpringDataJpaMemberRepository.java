package com.lepl.Repository.member.v2;

import com.lepl.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Jpa 사용목적
 */
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {
  //CRUD 자동 제공
  Member findByUid(String uid); // select m from Member m where m.uid := uid
}
