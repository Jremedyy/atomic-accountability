package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HabitService {

    private final HabitRepository repository;

    @Autowired
    public HabitService(HabitRepository repository) {
        this.repository = repository;
    }

    public Habit getHabitById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Habit with id " + id + " not found"));
    }

    public List<Habit> getHabitsByUser(UUID userId) {
        return repository.findByUserId(userId);
    }

    public Habit saveHabit(Habit habit) {
        return repository.save(habit);
    }

    @Transactional
    public void deleteHabit(UUID id) {
        Habit habit = getHabitById(id); // Ensures habit exists, otherwise throws an exception
        repository.delete(habit);
    }

    public Habit updateHabit(UUID id, Habit updatedHabit) {
        Habit existingHabit = getHabitById(id);
        existingHabit.setName(updatedHabit.getName());
        existingHabit.setDescription(updatedHabit.getDescription());
        existingHabit.setFrequency(updatedHabit.getFrequency());
        existingHabit.setStartDate(updatedHabit.getStartDate());
        return repository.save(existingHabit);
    }

    @Transactional
    public Habit completeHabit(UUID habitId) {
        Habit habit = getHabitById(habitId);
        habit.setLastCompleted(LocalDateTime.now());

        if (habit.getCurrentStreak() == habit.getMaxStreak()) {
            habit.setMaxStreak(habit.getCurrentStreak() + 1);
        }

        habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        habit.setStreakCount(habit.getStreakCount() + 1);

        return repository.save(habit);
    }

    @Transactional
    public void resetHabitStreak(UUID habitId) {
        Habit habit = getHabitById(habitId);
        habit.setCurrentStreak(0);
        repository.save(habit);
    }
}
