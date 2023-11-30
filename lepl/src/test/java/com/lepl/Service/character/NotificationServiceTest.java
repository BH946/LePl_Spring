package com.lepl.Service.character;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Notification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
public class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;
    @Autowired
    CharacterService characterService;
    @Autowired
    ExpService expService;
    static Long notificationId; // 전역

    /**
     * join, findOne, findAll, remove, findAllWithCharacter
     */
    @Test
    @Order(1)
    @Rollback(value = false)
    public void 알림_저장과조회() throws Exception {
        // given
        Exp exp = Exp.createExp(0L, 0L, 1L);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Notification notification = Notification.createNotification(character, "테스트 알림입니다.");
        expService.join(exp);
        characterService.join(character);

        // when
        notificationService.join(notification);
        Notification findNotification = notificationService.findOne(notification.getId());
        List<Notification> notifications = notificationService.findAll();
        notificationId = notification.getId();

        // then
        Assertions.assertEquals(notification.getId(), findNotification.getId());
        for (Notification no : notifications) {
            Assertions.assertInstanceOf(Notification.class, no);
        }

    }

    @Test
    @Order(2)
    public void 캐릭터의_알림전체조회() throws Exception {
        // given
        Exp exp = Exp.createExp(0L, 0L, 1L);
        Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Notification notification = Notification.createNotification(character, "테스트 알림입니다.@@@");
        expService.join(exp);
        characterService.join(character);
        notificationService.join(notification);

        // when
        List<Notification> notifications = notificationService.findAllWithCharacter(character.getId()); // flush

        // then
        for (Notification no : notifications) {
            Assertions.assertInstanceOf(Notification.class, no);
            log.info("no.id : {}", no.getId());
        }
    }

    @Test
    @Order(3)
    public void 알림_삭제() throws Exception {
        // given
        Notification findNotification = notificationService.findOne(notificationId);
        log.info("findNotification : {}", findNotification);

        // when
        notificationService.remove(findNotification);
        findNotification = notificationService.findOne(notificationId);

        // then
        Assertions.assertNull(findNotification);
        log.info("findNotification : {}", findNotification);
    }

}
