package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest // Bean 사용 위해
@Slf4j
public class ExpRepositoryTest {

  @Autowired
  ExpRepository expRepository;
  @Autowired
  EntityManager em;
  static Long expId; // 전역

  /**
   * save, findOne, remove, findOneWithMember, initPointToday
   */
  @Test
  @Order(1)
  @Transactional
  @Rollback(value = false) // "삭제" 메소드에 활용하기 위해 롤백 취소
  public void 경험치_저장과조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);

    // when
    expRepository.save(exp); // persist
    log.info("save 시점 확인");
    expId = exp.getId();
    Exp findExp = expRepository.findOne(expId);

    // then
    Assertions.assertEquals(exp, findExp);
    Assertions.assertEquals(exp.getId(), findExp.getId());
    log.info("exp id : {}", exp.getId());
  }

  @Test
  @Order(2)
  @Transactional // 롤백
  public void 멤버의_경험치_조회() throws Exception {
    // given
    Member member = Member.createMember("test1", "test1");
    Member member2 = Member.createMember("test2", "test2");
    Exp exp = Exp.createExp(0L, 0L, 1L);
    em.persist(exp); // id 위해
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    log.info("persist 이전 character id : {}", character.getId()); // null
    em.persist(character); // id 위해 -> FK 오류 피하기 위함
    log.info("persist 이후 character id : {}", character.getId());
    member2.setCharacter(character);
    em.persist(member);
    em.persist(member2);
//        em.flush(); // findOneWithMember 는 DB 에서 검색하므로 강제 flush() // findOneWithMember 에서 쌓인 쿼리들 순서대로 커밋될거라 여기서 flush 안해도 됨

    // when
    log.info("flush 이전");
    Exp findExp = expRepository.findOneWithMember(member.getId());
    log.info("flush 이전1");
    Exp findExp2 = expRepository.findOneWithMember(member2.getId());
    log.info("flush 이후");

    // then
    Assertions.assertEquals(findExp, null); // findMember 는 null 이여야 정상
    Assertions.assertEquals(findExp2.getId(), exp.getId());
  }

  @Test
  @Order(3)
  @Transactional
  public void 전체_경험치_일일제한_초기화() throws Exception {
    // given
    log.info("flush 이전");
    Exp findExp = expRepository.findOne(expId);
    log.info("flush 이후");
    findExp.updateExp(5L, 5L);
//        em.flush(); // dirty checking -> 변경내용 DB로 insert 쿼리 // 아래에서 flush 할거라 여기서 안해도됨
    log.info("findExp.getPointTodayTask() : {}, findExp.getPointTodayTimer() : {}",
        findExp.getPointTodayTask(), findExp.getPointTodayTimer());

    // when
    log.info("flush 이전");
    expRepository.initPointToday(); // flush
    log.info("flush 이후");
    em.clear(); // 캐시 제거 (flush 로는 안지워짐)
    findExp = expRepository.findOne(expId); // 다시 조회 select 쿼리

    // then
    Assertions.assertEquals(findExp.getPointTodayTask(), 0L);
    Assertions.assertEquals(findExp.getPointTodayTimer(), 0L);
    log.info("findExp.getPointTodayTask() : {}, findExp.getPointTodayTimer() : {}",
        findExp.getPointTodayTask(), findExp.getPointTodayTimer());
  }

  @Test
  @Order(4)
  @Transactional
  public void 경험치_제거() throws Exception {
    // given
    Exp findExp = expRepository.findOne(expId);

    // when
    expRepository.remove(findExp); // 사실 경험치 제거는 사용할 일이 없긴함
    findExp = expRepository.findOne(expId);

    // then
    Assertions.assertEquals(findExp, null);
  }
}
