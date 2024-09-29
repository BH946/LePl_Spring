package com.lepl.Service.character;

import com.lepl.Service.member.MemberService;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
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
class CharacterServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  CharacterService characterService;
  @Autowired
  ExpService expService;
  @Autowired
  MemberService memberService;
  static Long characterId; // 전역

  /**
   * join, findOne, findCharacterWithMember, remove
   */
  @Test
  @Order(1)
  @Rollback(value = false)
  public void 캐릭저_저장과조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());

    // when
    expService.join(exp);
    characterService.join(character);
    Character findCharacter = characterService.findOne(character.getId()); //영속성 컨텍스트에서 바로 가져오니까 select 쿼리가 없는거임~!

    // then
    Assertions.assertEquals(character.getId(), findCharacter.getId());
    log.info("character Id : {}", character.getId());
    characterId = character.getId();
  }


  @Test
  @Order(2)
  public void 회원의_캐릭터조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    Member member = Member.createMember("캐릭터서비스 테스트", "TEST");
    expService.join(exp);
    characterService.join(character);
    member.setCharacter(character);
    memberService.join(member);

    // when
    Character findCharacter = characterService.findCharacterWithMember(member.getId());

    // then
    Assertions.assertEquals(character.getId(), findCharacter.getId());
  }

  @Test
  @Order(3)
  @Rollback(value = false) //롤백 false 하면 커밋 시점의 flush 인 db 쿼리 다 보여줌 (여기선 delete 일거고)
  public void 캐릭터_삭제() throws Exception {
    // given
    Character character = characterService.findOne(characterId); // 캐릭터_저장과조회() 에서 저장했던 캐릭터 조회

    // when
    characterService.remove(character); // persist (영속성 컨텍스트의 삭제 상태로 관리)
    log.info("character : {}", character); // 위에서 찾은 character 주소 그대로 사용
    Character findCharacter = characterService.findOne(characterId);
    log.info("findCharacter : {}", findCharacter); // null -> 영속성 컨텍스트에서 이미 삭제되었다는 것

    // then
    Assertions.assertEquals(character.getId(), characterId);
    Assertions.assertEquals(findCharacter, null);
  }
}