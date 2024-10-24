package com.atomicaccountability.atomic_accountability.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

//    @Test
//    public void save_StoresRecord_WhenRecordIsValid(){
//
//        final User expected = new User();
//        expected.setFirstName(UUID.randomUUID().toString());
//        expected.setLastName(UUID.randomUUID().toString());
//
//        final User saved = repository.save(expected);
//        final User actual = entityManager.find(User.class, saved.getUsername());
//
//        assertThat(actual).isEqualTo(expected);
//
//    }
}
