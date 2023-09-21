package com.lepl.Repository.character;

import com.lepl.domain.character.Notification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final EntityManager em;

    /**
     * save, findOne, findAll, remove
     */
    public void save(Notification notification) {
        if(notification.getId() == null) {
            em.persist(notification);
        }
    }

    public Notification findOne(Long id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> findAll() {
        return em.createQuery("select n from Notification n", Notification.class)
                .getResultList();
    }

    public void remove(Notification notification) { em.remove(notification);}
}
