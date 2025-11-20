package com.burakmert.notification_query_service.repository;

import com.burakmert.notification_query_service.model.Notification;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    public List<Notification> findAllByRecipientIdOrderByCreatedAt(String RecipientId);

    public List<Notification> findAllByRecipientIdAndIsReadOrderByCreatedAt(String RecipientId, boolean isRead);

    @Query("{ 'recipientId' : ?0 }")
    @Update("{ '$set' : { 'isRead' : ?1 } }")
    public long updateIsReadByRecipientId(String RecipientId, boolean isRead);

}
