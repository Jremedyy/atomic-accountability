package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HabitLogRepository extends JpaRepository<HabitLog, UUID> {
}
