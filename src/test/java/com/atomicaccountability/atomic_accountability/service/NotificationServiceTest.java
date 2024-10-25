package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.Notification;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.repository.NotificationRepository;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
class NotificationServiceTest {

    @MockBean
    private NotificationRepository repository;

    private NotificationService service;

    private Notification notification;
    private UUID notificationId;
    private User user;
    private Habit habit;

    @BeforeEach
    void setUp() {
        service = new NotificationService(repository);
        user = TestEntityFactory.createUser();
        habit = TestEntityFactory.createHabit(user);
        notification = TestEntityFactory.createNotification(user, habit);
        notificationId = notification.getId();
    }

    @Test
    void getNotificationById_ShouldReturnNotification_WhenExists() {
        given(repository.findById(notificationId)).willReturn(Optional.of(notification));

        Notification result = service.getNotificationById(notificationId);

        assertThat(result).isEqualTo(notification);
        then(repository).should(times(1)).findById(notificationId);
    }

    @Test
    void getNotificationById_ShouldThrowException_WhenDoesNotExist() {
        given(repository.findById(notificationId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getNotificationById(notificationId));

        then(repository).should(times(1)).findById(notificationId);
    }

    @Test
    void getNotificationsByUser_ShouldReturnListOfNotifications_ForGivenUserId() {
        UUID userId = UUID.randomUUID();
        List<Notification> notifications = List.of(notification);
        given(repository.findByUserId(userId)).willReturn(notifications);

        List<Notification> result = service.getNotificationsByUser(userId);

        assertThat(result).isEqualTo(notifications);
        then(repository).should(times(1)).findByUserId(userId);
    }

    @Test
    void saveNotification_ShouldReturnSavedNotification() {
        given(repository.save(notification)).willReturn(notification);

        Notification result = service.saveNotification(notification);

        assertThat(result).isEqualTo(notification);
        then(repository).should(times(1)).save(notification);
    }

    @Test
    void deleteNotification_ShouldDeleteNotification_WhenExists() {
        given(repository.findById(notificationId)).willReturn(Optional.of(notification));

        service.deleteNotification(notificationId);

        then(repository).should(times(1)).findById(notificationId);
        then(repository).should(times(1)).delete(notification);
    }

    @Test
    void deleteNotification_ShouldThrowException_WhenDoesNotExist() {
        given(repository.findById(notificationId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteNotification(notificationId));

        then(repository).should(times(1)).findById(notificationId);
    }

    @Test
    void updateNotification_ShouldReturnUpdatedNotification() {
        Notification updatedNotification = TestEntityFactory.createNotification(user, habit);
        updatedNotification.setMessage("Updated Message");

        given(repository.findById(notificationId)).willReturn(Optional.of(notification));
        given(repository.save(notification)).willReturn(notification);

        Notification result = service.updateNotification(notificationId, updatedNotification);

        assertThat(result.getMessage()).isEqualTo("Updated Message");
        then(repository).should(times(1)).findById(notificationId);
        then(repository).should(times(1)).save(notification);
    }
}
