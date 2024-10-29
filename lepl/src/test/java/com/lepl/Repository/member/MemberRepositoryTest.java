package com.lepl.Repository.member;


//import com.lepl.Repository.member.v1.MemberRepository;
import com.lepl.Repository.member.v2.MemberRepository;
import com.lepl.Repository.member.v3.MemberQueryRepository;
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
@Slf4j
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  MemberQueryRepository queryRepository;
  @Autowired
  EntityManager em;

  /**
   * save, findOne, findByUid, findAllWithPage
   */

  @Test
  @Order(1)
  @Transactional // 롤백
  @Rollback(value = false)
  public void 회원가입_조회() throws Exception {
    // given
    Member member = Member.createMember("test", "test1");
    // 혹시모를 FK 에러 방지
    Exp exp = Exp.createExp(0L, 0L, 1L);
    em.persist(exp); // FK id 위해
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    em.persist(character); // FK id 위해
    member.setCharacter(character);

    // when
    memberRepository.save(member); // persist
    log.info("{}", member.getId());
//        em.flush(); // 롤백 true 때문에 insert 쿼리 생략시 flush 추가로 볼 수 있음
//        em.clear(); // flush 사용시 이것까지 해줘야 아래 select 문 전송 쿼리도 볼 수 있음
    Member findMember = memberRepository.findOne(member.getId());
    log.info("{}", member.getId());

    // then
    Assertions.assertEquals(member.getId(), findMember.getId());
    // 단, 위에서 em.clear를 한 경우 영속성이(캐시) 비어있으므로 findMember가 새로운 주소!!
    // 따라서 아래 출력으로 틀리다는 결론이 나온다.
    Assertions.assertEquals(member, findMember);
  }

  @Test
  @Order(2)
  @Transactional
  public void 회원조회_UID() throws Exception {
    // given
    Member findMember;

    // when
    findMember = memberRepository.findByUid("test");
    Member findMember2 = memberRepository.findByUid("testtest");

    // then
    Assertions.assertEquals(findMember.getNickname(), "test1");
    Assertions.assertEquals(findMember2, null);
  }

  @Test
  @Order(3)
  @Transactional
  public void 회원조회_Page() throws Exception {
    // given
    List<FindMemberResponseDto> memberList = new ArrayList<>();

    // when
    memberList = memberRepository.findAllWithPage(1); // order by desc
    log.info("memberList : {}", memberList.size());
    log.info("memberList.get(0) : {}", memberList.get(0));

    // then
    for (FindMemberResponseDto dto : memberList) {
      log.info("member id : {}, nickname : {}", dto.getId(), dto.getNickname());
    }
    Assertions.assertEquals(memberList.get(0).getNickname(), "test1");
  }

  @Test
  @Order(4)
  @Transactional
  public void QueryDSL테스트() throws Exception {
    //given
    List<Member> members;

    //when
    members = queryRepository.findAllByNickname("test1");

    //then
    Assertions.assertEquals(members.get(0).getNickname(), "test1");
  }
}