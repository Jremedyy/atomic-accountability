package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import com.atomicaccountability.atomic_accountability.repository.HabitLogRepository;
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
class HabitLogServiceTest {

    @MockBean
    private HabitLogRepository repository;

    private HabitLogService service;
    private HabitLog habitLog;
    private UUID habitLogId;
    private Habit habit;

    @BeforeEach
    void setUp() {
        service = new HabitLogService(repository);
        habitLog = TestEntityFactory.createHabitLog(habit);
        habitLogId = habitLog.getId();
    }

    @Test
    void getHabitLogById_ShouldReturnHabitLog_WhenHabitLogExists() {
        given(repository.findById(habitLogId)).willReturn(Optional.of(habitLog));

        HabitLog result = service.getHabitLogById(habitLogId);

        assertThat(result).isEqualTo(habitLog);
        then(repository).should(times(1)).findById(habitLogId);
    }

    @Test
    void getHabitLogById_ShouldThrowException_WhenHabitLogDoesNotExist() {
        given(repository.findById(habitLogId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getHabitLogById(habitLogId));

        then(repository).should(times(1)).findById(habitLogId);
    }

    @Test
    void getHabitLogsByHabit_ShouldReturnListOfHabitLogs_ForGivenHabitId() {
        UUID habitId = UUID.randomUUID();
        List<HabitLog> habitLogs = List.of(habitLog);
        given(repository.findByHabitId(habitId)).willReturn(habitLogs);

        List<HabitLog> result = service.getHabitLogsByHabit(habitId);

        assertThat(result).isEqualTo(habitLogs);
        then(repository).should(times(1)).findByHabitId(habitId);
    }

    @Test
    void saveHabitLog_ShouldReturnSavedHabitLog() {
        given(repository.save(habitLog)).willReturn(habitLog);

        HabitLog result = service.saveHabitLog(habitLog);

        assertThat(result).isEqualTo(habitLog);
        then(repository).should(times(1)).save(habitLog);
    }

    @Test
    void deleteHabitLog_ShouldDeleteHabitLog_WhenHabitLogExists() {
        given(repository.findById(habitLogId)).willReturn(Optional.of(habitLog));

        service.deleteHabitLog(habitLogId);

        then(repository).should(times(1)).findById(habitLogId);
        then(repository).should(times(1)).delete(habitLog);
    }

    @Test
    void deleteHabitLog_ShouldThrowException_WhenHabitLogDoesNotExist() {
        given(repository.findById(habitLogId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteHabitLog(habitLogId));

        then(repository).should(times(1)).findById(habitLogId);
    }

    @Test
    void updateHabitLog_ShouldReturnUpdatedHabitLog() {
        HabitLog updatedHabitLog = TestEntityFactory.createHabitLog(habit); // Create an updated HabitLog
        updatedHabitLog.setNotes("Updated Notes");
        given(repository.findById(habitLogId)).willReturn(Optional.of(habitLog));
        given(repository.save(habitLog)).willReturn(habitLog);

        HabitLog result = service.updateHabitLog(habitLogId, updatedHabitLog);

        assertThat(result.getNotes()).isEqualTo("Updated Notes");
        then(repository).should(times(1)).findById(habitLogId);
        then(repository).should(times(1)).save(habitLog);
    }
}
