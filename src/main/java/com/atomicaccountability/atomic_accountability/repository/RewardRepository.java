package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RewardRepository extends JpaRepository<Reward, UUID> {
    List<Reward> findByUserId(UUID userId);
}
