package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Follow;
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
@SpringBootTest // 스프링 빈 사용하려면 통합테스트 사용 필수
@Slf4j
class FollowRepositoryTest {

  @Autowired
  EntityManager em;
  @Autowired
  FollowRepository followRepository;
  static Long followId; // 전역
  static Long characterId; // 전역
  static final Long CHARACTER_EX_ID = 10L;

  /**
   * save, findOne, findAll, findById, remove, findAllWithFollowing, findAllWithFollower
   */
  @Test
  @Order(1)
  @Transactional
  @Rollback(value = false)
  public void 팔로우_저장과조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    em.persist(exp);
    em.persist(character); // id 기록위함
    characterId = character.getId();
    Follow follow = Follow.createFollow(character,
        CHARACTER_EX_ID); // followerId 는 자동으로 자기자신 등록, followingId 는 임의로 10L인 캐릭터로 설정

    // when
    followRepository.save(follow);
    Follow findFollow = followRepository.findOne(follow.getId());
    List<Follow> follows = followRepository.findAll();
    Follow findFollow2 = followRepository.findById(follow.getFollowerId(), follow.getFollowingId());
    followId = follow.getId();

    // then
    Assertions.assertEquals(follow.getId(), findFollow.getId());
    Assertions.assertEquals(follow.getId(), findFollow2.getId());
    for (Follow f : follows) {
      Assertions.assertInstanceOf(Follow.class, f);
    }
  }

  @Test
  @Order(2)
  @Transactional
  @Rollback(value = false)
  public void 팔로잉_팔로워_조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character c = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>()); // characterId 인 캐릭터를 팔로우 하는 캐릭터를 임의로 생성 목적
    em.persist(exp);
    em.persist(c);
    Follow follow = Follow.createFollow(c, characterId);
    followRepository.save(follow);
    em.flush();

    List<Follow> followers;
    List<Follow> followings;

    // when
    followers = followRepository.findAllWithFollower(characterId);
    followings = followRepository.findAllWithFollowing(characterId);

    // then
    for (Follow f : followers) {
      // followers 크기 1이라 가정
      Assertions.assertEquals(f.getCharacter().getId(), c.getId());
      log.info("나를 팔로워 하는 캐릭터 id : {}", f.getCharacter().getId()); // c.getId() 이어야 정상
    }
    for (Follow f : followings) {
      // followings 크기 1이라 가정
      Assertions.assertEquals(f.getFollowingId(), CHARACTER_EX_ID);
      log.info("내가 팔로잉 하는 캐릭터 id : {}", f.getFollowingId()); // CHARACTER_EX_ID 이어야 정상
    }
  }

  @Test
  @Order(3)
  @Transactional
  @Rollback(value = false)
  public void 팔로우_제거() throws Exception {
    // given
    Follow findFollow = followRepository.findOne(followId);
    log.info("findFollow : {}", findFollow);

    // when
    followRepository.remove(findFollow);
    findFollow = followRepository.findOne(followId);

    // then
    Assertions.assertEquals(findFollow, null);
    log.info("findNotification : {}", findFollow);

  }

}