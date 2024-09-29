package com.lepl.domain.member;

import com.lepl.domain.task.Lists;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberTest {

  @Test
  public void 생성_편의메서드() throws Exception {
    // given
    Member member = null;

    // when
    member = Member.createMember("test", "테스트1");

    // then
    Assertions.assertInstanceOf(Member.class, member);
  }

  @Test
  public void 연관관계_편의메서드() throws Exception {
    // given
    Member member = Member.createMember("test", "테스트2");
    Lists lists = Lists.createLists(member, LocalDateTime.now(), new ArrayList<>());
    log.info("{}", member.getLists().size()); //=0

    // when
    member.addLists(lists);
    log.info("{}", member.getLists().size()); //=1

    // then
    Assertions.assertEquals(member.getLists().size(), 1);
  }

}