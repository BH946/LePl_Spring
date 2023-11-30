package com.lepl.Service.character;

import com.lepl.Service.member.MemberService;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
class ExpServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    ExpService expService;
    @Autowired
    CharacterService characterService;
    @Autowired
    MemberService memberService;
    static Long expId; // 전역
    static Long memberId;

    /**
     * join, findOne, findOneWithMember, remove, update, initPointToday
     */
    @Test
    @Order(1)
    @Rollback(value = false)
    public void 경험치_저장과조회() throws Exception {
        // given
        Exp exp = Exp.createExp(0L,0L,1L);
        Member member = Member.createMember("경험치테스트", "TEST!!");
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        member.setCharacter(character);

        // when
        // exp, character, member 순으로 저장해야 더티체킹(update 쿼리) 없이 한번에 insert
        expService.join(exp);
        characterService.join(character);
        memberService.join(member);

        Exp findExp = expService.findOne(exp.getId());

        // then
        Assertions.assertEquals(exp.getId(), findExp.getId());
        expId = exp.getId();
        memberId = member.getId();
        log.info("expId : {}, memberId : {}", expId, memberId);
    }

    @Test
    @Order(2)
    public void 멤버의_경험치_조회() throws Exception {
        // given
        Exp findExp;

        // when
        log.info("memberId : {}", memberId);
        findExp = expService.findOneWithMember(memberId);

        // then
        Assertions.assertEquals(findExp.getId(), expId);
    }

    @Test
    @Order(5)
    public void 겅혐치_제거() throws Exception {
        // given
        Exp findExp = expService.findOne(expId);

        // when
        expService.remove(findExp); // 영속성에서 없애므로
        findExp = expService.findOne(expId); // 바로 null

        // then
        Assertions.assertEquals(findExp, null);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void 경험치_업데이트() throws Exception {
        // given
        Exp findExp = expService.findOne(expId);

        // when
        expService.update(findExp, 5L, 5L);
        findExp = expService.findOne(expId);

        // then
        Assertions.assertEquals(findExp.getPointTodayTask(), 5L);
        Assertions.assertEquals(findExp.getPointTodayTimer(), 5L);
    }

    @Test
    @Order(4)
    public void 전체_경험치_일일제한_초기화() throws Exception {
        // given
        Exp findExp = expService.findOne(expId);

        // when
        expService.initPointToday();
        em.clear(); // db 에 갱신된 값으로 새로 가져와야 하므로 한번 clear
        findExp = expService.findOne(expId);

        // then
        Assertions.assertEquals(findExp.getPointTodayTimer(), 0);
        Assertions.assertEquals(findExp.getPointTodayTask(), 0);
    }

}