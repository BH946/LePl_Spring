package com.lepl.Repository.character;

import com.lepl.domain.character.Notification;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

  private final EntityManager em;

  /**
   * save, findOne, findAll, remove, findAllWithCharacter
   */
  public void save(Notification notification) {
    if (notification.getId() == null) {
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

  public void remove(Notification notification) {
    em.remove(notification);
  }

  public List<Notification> findAllWithCharacter(Long characterId) {
    return em.createQuery(
            "select n from Notification n" +
                " where n.character.id = :characterId", Notification.class)
        .setParameter("characterId", characterId)
        .getResultList();
  }
}
