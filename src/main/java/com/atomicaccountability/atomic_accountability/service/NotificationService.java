package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Notification;
import com.atomicaccountability.atomic_accountability.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification getNotificationById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Notification with id " + id + " not found"));
    }

    public List<Notification> getNotificationsByUser(UUID userId) {
        return repository.findByUserId(userId);
    }

    public Notification saveNotification(Notification notification) {
        return repository.save(notification);
    }

    public void deleteNotification(UUID id) {
        Notification notification = getNotificationById(id);
        repository.delete(notification);
    }

    public Notification updateNotification(UUID id, Notification updatedNotification) {
        Notification existingNotification = getNotificationById(id);
        existingNotification.setMessage(updatedNotification.getMessage());
        existingNotification.setNotificationType(updatedNotification.getNotificationType());
        existingNotification.setSentAt(updatedNotification.getSentAt());
        return repository.save(existingNotification);
    }
}
