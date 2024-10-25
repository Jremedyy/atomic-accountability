package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import com.atomicaccountability.atomic_accountability.repository.HabitLogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HabitLogService {

    private final HabitLogRepository repository;

    @Autowired
    public HabitLogService(HabitLogRepository repository) {
        this.repository = repository;
    }

    // Get a HabitLog by ID
    public HabitLog getHabitLogById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("HabitLog with id " + id + " not found"));
    }

    // Get all HabitLogs for a specific Habit
    public List<HabitLog> getHabitLogsByHabit(UUID habitId) {
        return repository.findByHabitId(habitId);
    }

    // Save a new HabitLog
    public HabitLog saveHabitLog(HabitLog habitLog) {
        return repository.save(habitLog);
    }

    // Delete a HabitLog by ID
    public void deleteHabitLog(UUID id) {
        HabitLog habitLog = getHabitLogById(id);
        repository.delete(habitLog);
    }

    // Update a HabitLog
    public HabitLog updateHabitLog(UUID id, HabitLog updatedHabitLog) {
        HabitLog existingHabitLog = getHabitLogById(id);
        existingHabitLog.setCompletionDate(updatedHabitLog.getCompletionDate());
        existingHabitLog.setStatus(updatedHabitLog.getStatus());
        existingHabitLog.setNotes(updatedHabitLog.getNotes());
        return repository.save(existingHabitLog);
    }
}
