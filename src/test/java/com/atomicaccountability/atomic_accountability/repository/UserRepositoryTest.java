package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    public void save_StoresRecord_WhenRecordIsValid() {

        final User expected = TestEntityFactory.createUser();


        final User saved = repository.save(expected);
        final User actual = entityManager.find(User.class, saved.getId());

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void delete_DeletesRecord_WhenRecordIsValid() {
        final User expected = TestEntityFactory.createUser();

        repository.save(expected);
        repository.delete(expected);
        final Reward actual = entityManager.find(Reward.class, expected.getId());
        assertThat(actual).isNull();

    }

    @Test
    void findById_ReturnsRecord_WhenRecordExists() {

        final User expected = TestEntityFactory.createUser();


        repository.save(expected);

        User actual = repository.findById(expected.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
    }

    @Test
    void findAll_ReturnsAllRecords() {
        final User user1 = TestEntityFactory.createUser();

        user1.setFirstName("test1");
        repository.save(user1);

        final User user2 = TestEntityFactory.createUser();

        user2.setFirstName("test2");
        repository.save(user2);

        List<User> actual = repository.findAll();


        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void update_UpdatesRecord_WhenRecordIsValid() {

        final User user = TestEntityFactory.createUser();

        user.setFirstName("initial");
        repository.save(user);

        user.setFirstName("updated");
        repository.save(user);
        User actual = repository.findById(user.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getFirstName()).isEqualTo("updated");
    }

    @Test
    void delete_DoesNotThrow_WhenRecordDoesNotExist() {

        repository.deleteById(UUID.randomUUID()); // Use an ID that doesn't exist

        // Assert: No exception is thrown
        // This test can be validated by checking the state of the database or using @Transactional rollback
    }

    @Test
    void count_ReturnsCorrectCount() {

        repository.save(TestEntityFactory.createUser());
        repository.save(TestEntityFactory.createUser());

        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }
}
