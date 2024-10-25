package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.enums.Status;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HabitLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private HabitLogRepository repository;

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

        final HabitLog expected = TestEntityFactory.createHabitLog(habit);


        final HabitLog saved = repository.save(expected);
        final HabitLog actual = entityManager.find(HabitLog.class, saved.getId());

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void delete_DeletesRecord_WhenRecordIsValid() {
        final HabitLog expected = TestEntityFactory.createHabitLog(habit);

        repository.save(expected);
        repository.delete(expected);
        final Reward actual = entityManager.find(Reward.class, expected.getId());
        assertThat(actual).isNull();

    }

    @Test
    void findById_ReturnsRecord_WhenRecordExists() {

        final HabitLog expected = TestEntityFactory.createHabitLog(habit);

        repository.save(expected);

        HabitLog actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void findAll_ReturnsAllRecords() {
        final HabitLog habitLog1 = TestEntityFactory.createHabitLog(habit);
        habitLog1.setNotes("test1");
        repository.save(habitLog1);
        HabitLog habitLog2 = TestEntityFactory.createHabitLog(habit);

        habitLog2.setNotes("test2");
        repository.save(habitLog2);

        List<HabitLog> actual = repository.findAll();


        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(habitLog1, habitLog2);
    }

    @Test
    void update_UpdatesRecord_WhenRecordIsValid() {

        final HabitLog habitLog = TestEntityFactory.createHabitLog(habit);

        habitLog.setNotes("initial");
        repository.save(habitLog);

        habitLog.setNotes("updated");
        repository.save(habitLog);
        HabitLog actual = repository.findById(habitLog.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getNotes()).isEqualTo("updated");
    }

    @Test
    void delete_DoesNotThrow_WhenRecordDoesNotExist() {

        repository.deleteById(UUID.randomUUID()); // Use an ID that doesn't exist

        // Assert: No exception is thrown
        // This test can be validated by checking the state of the database or using @Transactional rollback
    }

    @Test
    void count_ReturnsCorrectCount() {

        repository.save(TestEntityFactory.createHabitLog(habit));
        repository.save(TestEntityFactory.createHabitLog(habit));

        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }
}
