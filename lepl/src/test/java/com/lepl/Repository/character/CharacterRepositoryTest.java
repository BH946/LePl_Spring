package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest // 스프링 빈 사용하려면 통합테스트 사용 필수
@Slf4j
class CharacterRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    CharacterRepository characterRepository;
    static Long characterId; // 전역

    /**
     * save, findOne, remove, findCharacterWithMember
     */
    @Order(1) // 실행순서!
    @Test
    @Transactional // 롤백 false 없으면 쿼리 보내기전에 롤백
    @Rollback(value = false) // 롤백 false 이므로 insert 쿼리까지 전송 - flush() 강제로 써도 로그 볼 수 있지만 "삭제" 에 사용위해 false
    public void 캐릭터_저장과조회() throws Exception {
        // given
        // EXP id 없으면 FK 오류
        Exp exp = Exp.createExp(0L,0L,1L);
        em.persist(exp); // id 생성위해
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // when
        characterRepository.save(character); // em.persist()
        characterId = character.getId();
        log.info("character id : {}", characterId);
        Character findCharacter = characterRepository.findOne(character.getId());

        // then
        Assertions.assertEquals(character, findCharacter);
        Assertions.assertEquals(character.getId(), findCharacter.getId());
    }

    @Order(2) // 실행 순서!
    @Test
    @Transactional // 롤백
    public void 멤버의_캐릭터_조회() throws Exception {
        // given
        Member member = Member.createMember("test1", "test1");
        Member member2 = Member.createMember("test2", "test2");
        log.info("{}", characterId);
        Character character = characterRepository.findOne(characterId); // 캐릭터_저장과조회() 에서 만든 캐릭터 호출
        member2.setCharacter(character); // FK 위해 Character 는 꼭 먼저 persist
        em.persist(member);
        em.persist(member2);
//        em.flush(); // 강제 flush -> findCharacterWithMember 는 DB 에서 검색하기 때문에 먼저 flush() 진행

        // when
        Character findCharacter = characterRepository.findCharacterWithMember(member.getId());
        Character findCharacter2 = characterRepository.findCharacterWithMember(member2.getId());
        log.info("findCharacter2 id : {}, member2.getCharacter() id : {}", findCharacter2.getId(), member2.getCharacter().getId());

        // then
        Assertions.assertEquals(findCharacter, null); // member.getCharacter() 는 null 이 정상
        Assertions.assertEquals(findCharacter2, member2.getCharacter());
        Assertions.assertEquals(findCharacter2.getId(), member2.getCharacter().getId());
        log.info("member nickname : {}", member.getNickname());
        log.info("member2 nickname : {}", member2.getNickname());
    }

    @Order(3) // 실행 순서!
    @Test
    @Transactional
    public void 캐릭터_제거() throws Exception {
        // given
        Character character = characterRepository.findOne(characterId); // 캐릭터_저장과조회() 에서 만든 캐릭터 호출

        // when
        characterRepository.remove(character);
        Character findCharacter = characterRepository.findOne(characterId);

        // then
        Assertions.assertEquals(findCharacter, null);
    }
}