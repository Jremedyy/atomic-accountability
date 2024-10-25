package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.service.HabitService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habit> getHabitById(@PathVariable UUID id) {
        try {
            Habit habit = habitService.getHabitById(id);
            return ResponseEntity.ok(habit);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Habit>> getHabitsByUser(@PathVariable UUID userId) {
        List<Habit> habits = habitService.getHabitsByUser(userId);
        return ResponseEntity.ok(habits);
    }

    @PostMapping
    public ResponseEntity<Habit> createHabit(@Valid @RequestBody Habit habit) {
        Habit savedHabit = habitService.saveHabit(habit);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHabit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habit> updateHabit(@PathVariable UUID id, @Valid @RequestBody Habit updatedHabit) {
        Habit habit = habitService.updateHabit(id, updatedHabit);
        return ResponseEntity.ok(habit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable UUID id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Habit> completeHabit(@PathVariable UUID id) {
        Habit completedHabit = habitService.completeHabit(id);
        return ResponseEntity.ok(completedHabit);
    }

    @PostMapping("/{id}/reset-streak")
    public ResponseEntity<Void> resetHabitStreak(@PathVariable UUID id) {
        habitService.resetHabitStreak(id);
        return ResponseEntity.noContent().build();
    }
}
