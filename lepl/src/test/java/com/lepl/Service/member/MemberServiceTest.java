package com.lepl.Service.member;

import com.lepl.Service.member.v2.MemberService;
import com.lepl.api.member.dto.FindMemberResponseDto;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
public class MemberServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  MemberService memberService;
  static final String UID = "12345";
  static final String MESSAGE = "이미 존재하는 회원입니다.";
  static Long memberId;

  /**
   * join(중복검증 포함), findOne, findByUid, {findAllWithPage, initCacheMembers}(=회원 최신순_페이징 조회+캐시)
   */

  @Test
  @Order(1)
  @Rollback(value = false)
  public void 회원가입_조회() throws Exception {
    // given
    Member member = Member.createMember(UID, "테스트 닉네임");
    Exp exp = Exp.createExp(0L, 0L, 1L);
    em.persist(exp);
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    em.persist(character);
    member.setCharacter(character);

    // when
    memberService.join(member);
    Member findMember = memberService.findOne(member.getId());
    Member findMember2 = memberService.findByUid(UID);

    // then
    Assertions.assertEquals(member.getId(), findMember.getId());
    Assertions.assertEquals(member.getId(), findMember2.getId());
    memberId = member.getId();
  }

  @Test
  @Order(2)
  public void 중복검증_예외() throws Exception {
    // given
    Member member = memberService.findOne(memberId);

    // when
    // then
    Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
      memberService.join(member); // 예외발생 로직
    });
    Assertions.assertEquals(MESSAGE, exception.getMessage());
    log.info("exception.getMessage() : {}", exception.getMessage());
  }

  @Test
  @Order(3)
  public void 회원_페이징_캐시_조회() throws Exception {
    // given
    // when
    List<FindMemberResponseDto> dto = memberService.findAllWithPage(1);
    log.info("캐시되었으면 쿼리 안날라감1");
    memberService.findAllWithPage(1);
    log.info("캐시되었으면 쿼리 안날라감2");
    memberService.findAllWithPage(1);
    log.info("캐시되었으면 쿼리 안날라감3");
    memberService.initCacheMembers(); // 캐시 초기화
    log.info("캐시 초기화 했으므로 쿼리 날라가야 함");
    memberService.findAllWithPage(1);

    // then
    for (FindMemberResponseDto m : dto) {
      log.info("member.id : {}", m.getId());
      log.info("member.nickName : {}", m.getNickname());
    }
  }

  @Test
  @Order(4)
  public void QueryDSL테스트() throws Exception {
    //given
    List<Member> members;

    //when
    members = memberService.findAllByNickname("테스트 닉네임");

    //then
    Assertions.assertEquals(members.get(0).getNickname(), "테스트 닉네임");
  }
}