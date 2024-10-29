package com.lepl.Repository.member.v2;

import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * v2: 어댑터ver 첫번째 예시 -> 레포지토리 단만 수정!!
 */
public interface MemberRepository {
  void save(Member member);
  Member findOne(Long id);
  Member findByUid(String uid);
  List<FindMemberResponseDto> findAllWithPage(int pageId);
}
