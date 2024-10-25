package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.Notification;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class NotificationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository repository;
    private User user;
    private Habit habit;

    @BeforeEach
    public void setUp() {
        user = TestEntityFactory.createUser();
        entityManager.persist(user);
        habit = TestEntityFactory.createHabit(user);
        entityManager.persist(habit);

    }

    @Test
    public void save_StoresRecord_WhenRecordIsValid() {
        final Notification expected = TestEntityFactory.createNotification(user, habit);


        final Notification saved = repository.save(expected);
        final Notification actual = entityManager.find(Notification.class, saved.getId());

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void delete_DeletesRecord_WhenRecordIsValid() {
        final Notification expected = TestEntityFactory.createNotification(user, habit);

        repository.save(expected);
        repository.delete(expected);
        final Habit actual = entityManager.find(Habit.class, expected.getId());
        assertThat(actual).isNull();

    }

    @Test
    void findById_ReturnsRecord_WhenRecordExists() {

        final Notification expected = TestEntityFactory.createNotification(user, habit);


        repository.save(expected);

        Notification actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void findAll_ReturnsAllRecords() {
        final Notification expected1 = TestEntityFactory.createNotification(user, habit);


        expected1.setMessage("test1");
        repository.save(expected1);

        final Notification expected2 = TestEntityFactory.createNotification(user, habit);


        expected2.setMessage("test2");
        repository.save(expected2);

        List<Notification> actual = repository.findAll();


        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(expected1, expected2);
    }

    @Test
    void update_UpdatesRecord_WhenRecordIsValid() {

        final Notification expected = TestEntityFactory.createNotification(user, habit);


        expected.setMessage("initial");
        repository.save(expected);

        expected.setMessage("updated");
        repository.save(expected);
        Notification actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getMessage()).isEqualTo("updated");
    }

    @Test
    void delete_DoesNotThrow_WhenRecordDoesNotExist() {

        repository.deleteById(UUID.randomUUID()); // Use an ID that doesn't exist

        // Assert: No exception is thrown
        // This test can be validated by checking the state of the database or using @Transactional rollback
    }

    @Test
    void count_ReturnsCorrectCount() {

        repository.save(TestEntityFactory.createNotification(user, habit));
        repository.save(TestEntityFactory.createNotification(user, habit));

        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }
}
