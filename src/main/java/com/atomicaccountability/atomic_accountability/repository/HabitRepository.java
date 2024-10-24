package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HabitRepository extends JpaRepository<Habit, UUID> {
}
