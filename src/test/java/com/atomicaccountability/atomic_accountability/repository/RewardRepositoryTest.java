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
public class RewardRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RewardRepository repository;
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

        final Reward expected = TestEntityFactory.createReward(user, habit);
        final Reward saved = repository.save(expected);
        final Reward actual = entityManager.find(Reward.class, saved.getId());

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void delete_DeletesRecord_WhenRecordIsValid() {
        final Reward expected = TestEntityFactory.createReward(user, habit);

        repository.save(expected);
        repository.delete(expected);
        final Reward actual = entityManager.find(Reward.class, expected.getId());
        assertThat(actual).isNull();

    }

    @Test
    void findById_ReturnsRecord_WhenRecordExists() {

        final Reward expected = TestEntityFactory.createReward(user, habit);

        repository.save(expected);

        Reward actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void findAll_ReturnsAllRecords() {
        final Reward reward1 = TestEntityFactory.createReward(user, habit);
        reward1.setName("test1");
        repository.save(reward1);
        final Reward reward2 = TestEntityFactory.createReward(user, habit);
        reward2.setName("test2");
        repository.save(reward2);

        List<Reward> actual = repository.findAll();


        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(reward1, reward2);
    }

    @Test
    void update_UpdatesRecord_WhenRecordIsValid() {

        final Reward reward = TestEntityFactory.createReward(user, habit);
        reward.setName("initial");
        repository.save(reward);

        reward.setName("updated");
        repository.save(reward);
        Reward actual = repository.findById(reward.getId()).orElse(null);

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

        repository.save(TestEntityFactory.createReward(user, habit));
        repository.save(TestEntityFactory.createReward(user, habit));

        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }
}
