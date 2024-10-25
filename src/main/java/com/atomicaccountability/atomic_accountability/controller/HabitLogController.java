package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import com.atomicaccountability.atomic_accountability.service.HabitLogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/habitlogs")
public class HabitLogController {

    private final HabitLogService habitLogService;

    @Autowired
    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    // Get a HabitLog by ID
    @GetMapping("/{id}")
    public ResponseEntity<HabitLog> getHabitLogById(@PathVariable UUID id) {
        try {
            HabitLog habitLog = habitLogService.getHabitLogById(id);
            return ResponseEntity.ok(habitLog);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get all HabitLogs for a specific Habit
    @GetMapping("/habit/{habitId}")
    public ResponseEntity<List<HabitLog>> getHabitLogsByHabit(@PathVariable UUID habitId) {
        List<HabitLog> habitLogs = habitLogService.getHabitLogsByHabit(habitId);
        return ResponseEntity.ok(habitLogs);
    }

    // Save a new HabitLog
    @PostMapping
    public ResponseEntity<HabitLog> createHabitLog(@Valid @RequestBody HabitLog habitLog) {
        HabitLog savedHabitLog = habitLogService.saveHabitLog(habitLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHabitLog);
    }

    // Update a HabitLog
    @PutMapping("/{id}")
    public ResponseEntity<HabitLog> updateHabitLog(@PathVariable UUID id, @Valid @RequestBody HabitLog updatedHabitLog) {
        HabitLog habitLog = habitLogService.updateHabitLog(id, updatedHabitLog);
        return ResponseEntity.ok(habitLog);
    }

    // Delete a HabitLog by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitLog(@PathVariable UUID id) {
        habitLogService.deleteHabitLog(id);
        return ResponseEntity.noContent().build();
    }
}
