package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.repository.HabitRepository;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository repository;

    @InjectMocks
    private HabitService service;

    private Habit habit;
    private UUID habitId;
    private User user;

    @BeforeEach
    void setUp() {
        user = TestEntityFactory.createUser();
        habit = TestEntityFactory.createHabit(user);
        habitId = habit.getId();
    }

    @Test
    void getHabitById_ShouldReturnHabit_WhenHabitExists() {
        given(repository.findById(habitId)).willReturn(Optional.of(habit));

        Habit result = service.getHabitById(habitId);

        assertThat(result).isEqualTo(habit);
        then(repository).should(times(1)).findById(habitId);
    }

    @Test
    void getHabitById_ShouldThrowException_WhenHabitDoesNotExist() {
        given(repository.findById(habitId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getHabitById(habitId));

        then(repository).should(times(1)).findById(habitId);
    }

    @Test
    void getHabitsByUser_ShouldReturnListOfHabits_ForGivenUserId() {
        UUID userId = UUID.randomUUID();
        List<Habit> habits = List.of(habit);
        given(repository.findByUserId(userId)).willReturn(habits);

        List<Habit> result = service.getHabitsByUser(userId);

        assertThat(result).isEqualTo(habits);
        then(repository).should(times(1)).findByUserId(userId);
    }

    @Test
    void saveHabit_ShouldReturnSavedHabit() {
        given(repository.save(habit)).willReturn(habit);

        Habit result = service.saveHabit(habit);

        assertThat(result).isEqualTo(habit);
        then(repository).should(times(1)).save(habit);
    }

    @Test
    void deleteHabit_ShouldDeleteHabit_WhenHabitExists() {
        given(repository.findById(habitId)).willReturn(Optional.of(habit));

        service.deleteHabit(habitId);

        then(repository).should(times(1)).findById(habitId);
        then(repository).should(times(1)).delete(habit);
    }

    @Test
    void deleteHabit_ShouldThrowException_WhenHabitDoesNotExist() {
        given(repository.findById(habitId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteHabit(habitId));

        then(repository).should(times(1)).findById(habitId);
    }

    @Test
    void updateHabit_ShouldReturnUpdatedHabit() {
        Habit updatedHabit = TestEntityFactory.createHabit(user);
        updatedHabit.setName("Updated Name");
        given(repository.findById(habitId)).willReturn(Optional.of(habit));
        given(repository.save(habit)).willReturn(habit);

        Habit result = service.updateHabit(habitId, updatedHabit);

        assertThat(result.getName()).isEqualTo("Updated Name");
        then(repository).should(times(1)).findById(habitId);
        then(repository).should(times(1)).save(habit);
    }

    @Test
    void completeHabit_ShouldUpdateStreaksAndReturnHabit() {

        habit.setCurrentStreak(0);
        habit.setStreakCount(0);
        habit.setMaxStreak(0);

        given(repository.findById(habitId)).willReturn(Optional.of(habit));
        given(repository.save(habit)).willReturn(habit);

        Habit result = service.completeHabit(habitId);

        assertThat(result.getCurrentStreak()).isEqualTo(1);
        assertThat(result.getStreakCount()).isEqualTo(1);
        assertThat(result.getLastCompleted()).isNotNull();

        then(repository).should(times(1)).findById(habitId);
        then(repository).should(times(1)).save(habit);
    }

    @Test
    void resetHabitStreak_ShouldSetCurrentStreakToZero() {
        given(repository.findById(habitId)).willReturn(Optional.of(habit));

        service.resetHabitStreak(habitId);

        assertThat(habit.getCurrentStreak()).isZero();
        then(repository).should(times(1)).findById(habitId);
        then(repository).should(times(1)).save(habit);
    }
}
