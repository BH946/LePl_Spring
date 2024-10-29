package com.lepl.Repository.member.v1;

import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.member.Member;
import java.util.List;

public interface MemberRepositoryCustom {

  Member findOne(Long id);
  Member findByUid(String uid);
  List<FindMemberResponseDto> findAllWithPage(int pageId);

}
