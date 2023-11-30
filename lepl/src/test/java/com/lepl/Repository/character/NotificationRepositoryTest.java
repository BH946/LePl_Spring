package com.lepl.Repository.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Notification;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest // 스프링 빈 사용하려면 통합테스트 사용 필수
@Slf4j
class NotificationRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    NotificationRepository notificationRepository;
    static Long notificationId; // 전역

    /**
     * save, findOne, findAll, remove, findAllWithCharacter
     */
    @Test
    @Order(1)
    @Transactional
    @Rollback(value = false)
    public void 알림_저장과조회() throws Exception {
        // given
        Exp exp = Exp.createExp(0L, 0L, 1L);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Notification notification = Notification.createNotification(character, "테스트 알림입니다.");
        em.persist(exp);
        em.persist(character);

        // when
        notificationRepository.save(notification);
        Notification findNotification = notificationRepository.findOne(notification.getId());
        List<Notification> notifications = notificationRepository.findAll();
        notificationId = notification.getId();

        // then
        Assertions.assertEquals(notification.getId(), findNotification.getId());
        for (Notification no : notifications) {
            Assertions.assertInstanceOf(Notification.class, no);
        }
    }

    @Test
    @Transactional
    public void 캐릭터의_알림전체조회() throws Exception {
        // given
        Exp exp = Exp.createExp(0L, 0L, 1L);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Notification notification = Notification.createNotification(character, "테스트 알림입니다.123123");
        em.persist(exp); // FK ID
        em.persist(character);
        em.persist(notification);

        // when
        List<Notification> notifications = notificationRepository.findAllWithCharacter(character.getId()); // flush

        // then
        for (Notification no : notifications) {
            Assertions.assertInstanceOf(Notification.class, no);
            log.info("no.id : {}", no.getId());
        }
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback(value = false)
    public void 알림_삭제() throws Exception {
        // given
        Notification findNotification = notificationRepository.findOne(notificationId);
        log.info("findNotification : {}", findNotification);

        // when
        notificationRepository.remove(findNotification);
        findNotification = notificationRepository.findOne(notificationId);

        // then
        Assertions.assertEquals(findNotification, null);
        log.info("findNotification : {}", findNotification);
    }

}