package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.enums.NotificationPref;
import com.atomicaccountability.atomic_accountability.repository.UserRepository;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository repository;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(repository);

    }


    @Test
    void getUsers_ShouldReturnPagedUsers_WhenUsersExist() {

        int pageNumber = 0;
        int pageSize = 2;
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        List<User> users = List.of(user1, user2);
        Page<User> page = new PageImpl<>(users, PageRequest.of(pageNumber, pageSize), users.size());
        given(repository.findAll(PageRequest.of(pageNumber, pageSize))).willReturn(page);

        Page<User> result = service.getUsers(pageNumber, pageSize);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactly(user1, user2);
        assertThat(result.getNumber()).isEqualTo(pageNumber);
        assertThat(result.getSize()).isEqualTo(pageSize);
        verify(repository).findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {

        UUID existingId = UUID.randomUUID();
        User user = new User();
        user.setId(existingId);
        given(repository.findById(existingId)).willReturn(Optional.of(user));

        User result = service.getUserById(existingId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {

        UUID nonExistentId = UUID.randomUUID();
        given(repository.findById(nonExistentId)).willReturn(Optional.empty());

        User result = service.getUserById(nonExistentId);

        assertThat(result).isNull();
        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    public void save_ReturnSaved_WhenUserRecordIsCreated() {

        final User expected = TestEntityFactory.createUser();


        given(repository.save(expected)).willReturn(expected);

        final User actual = service.save(expected);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().save(expected);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete_ShouldDeleteUser_GivenUserExistsInRepository() {

        final User user = TestEntityFactory.createUser();
        given(repository.findById(user.getId())).willReturn(Optional.of(user));

        service.delete(user.getId());

        then(repository).should(times(1)).findById(user.getId()); // Verify the findById call
        then(repository).should(times(1)).deleteById(user.getId()); // Verify the deleteById call
    }

    @Test
    void delete_ShouldThrowException_GivenNoUserExistsInRepository() {

        UUID nonExistentId = UUID.randomUUID();
        given(repository.findById(nonExistentId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.delete(nonExistentId));

        then(repository).should(times(1)).findById(nonExistentId);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        UUID existingId = UUID.randomUUID();
        User existingUser = TestEntityFactory.createUser();
        existingUser.setId(existingId);

        User updatedUser = TestEntityFactory.createUser();
        updatedUser.setFirstName("UpdatedFirstName");
        updatedUser.setLastName("UpdatedLastName");
        updatedUser.setUsername("UpdatedUsername");
        updatedUser.setNotificationPref(NotificationPref.EMAIL);

        given(repository.findById(existingId)).willReturn(Optional.of(existingUser));
        given(repository.save(existingUser)).willReturn(existingUser);

        User result = service.updateUser(existingId, updatedUser);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(result.getLastName()).isEqualTo("UpdatedLastName");
        assertThat(result.getUsername()).isEqualTo("UpdatedUsername");
        assertThat(result.getNotificationPref()).isEqualTo(updatedUser.getNotificationPref());

        verify(repository, times(1)).findById(existingId);
        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        User updatedUser = TestEntityFactory.createUser();

        given(repository.findById(nonExistentId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateUser(nonExistentId, updatedUser));

        verify(repository, times(1)).findById(nonExistentId);
    }
}
