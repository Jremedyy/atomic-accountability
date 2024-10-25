package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.Reward;
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
public class HabitRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HabitRepository repository;

    private User user;


    @BeforeEach
    public void setUp() {
        user = TestEntityFactory.createUser();
        entityManager.persist(user);


    }

    @Test
    public void save_StoresRecord_WhenRecordIsValid() {
        entityManager.persist(user);

        final Habit expected = new Habit();
        expected.setUser(user);
        expected.setName("Test");
        expected.setDescription("Test");
        expected.setFrequency("2 days");


        final Habit saved = repository.save(expected);
        final Habit actual = entityManager.find(Habit.class, saved.getId());

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void delete_DeletesRecord_WhenRecordIsValid() {
        final Habit expected = TestEntityFactory.createHabit(user);

        repository.save(expected);
        repository.delete(expected);
        final Habit actual = entityManager.find(Habit.class, expected.getId());
        assertThat(actual).isNull();

    }

    @Test
    void findById_ReturnsRecord_WhenRecordExists() {

        final Habit expected = TestEntityFactory.createHabit(user);


        repository.save(expected);

        Habit actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void findAll_ReturnsAllRecords() {
        final Habit expected1 = TestEntityFactory.createHabit(user);


        expected1.setName("test1");
        repository.save(expected1);

        final Habit expected2 = TestEntityFactory.createHabit(user);


        expected2.setName("test2");
        repository.save(expected2);

        List<Habit> actual = repository.findAll();


        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(expected1, expected2);
    }

    @Test
    void update_UpdatesRecord_WhenRecordIsValid() {

        final Habit expected = TestEntityFactory.createHabit(user);


        expected.setName("initial");
        repository.save(expected);

        expected.setName("updated");
        repository.save(expected);
        Habit actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("updated");
    }

    @Test
    void delete_DoesNotThrow_WhenRecordDoesNotExist() {

        repository.deleteById(UUID.randomUUID()); // Use an ID that doesn't exist

        // Assert: No exception is thrown
        // This test can be validated by checking the state of the database or using @Transactional rollback
    }

    @Test
    void count_ReturnsCorrectCount() {

        repository.save(TestEntityFactory.createHabit(user));
        repository.save(TestEntityFactory.createHabit(user));

        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }
}
