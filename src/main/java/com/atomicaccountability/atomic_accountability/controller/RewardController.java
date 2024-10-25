package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.service.RewardService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable UUID id) {
        try {
            Reward reward = rewardService.getRewardById(id);
            return ResponseEntity.ok(reward);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reward>> getRewardsByUser(@PathVariable UUID userId) {
        List<Reward> rewards = rewardService.getRewardsByUser(userId);
        return ResponseEntity.ok(rewards);
    }

    @PostMapping
    public ResponseEntity<Reward> createReward(@RequestBody Reward reward) {
        Reward savedReward = rewardService.saveReward(reward);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReward);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reward> updateReward(@PathVariable UUID id, @RequestBody Reward updatedReward) {
        Reward reward = rewardService.updateReward(id, updatedReward);
        return ResponseEntity.ok(reward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable UUID id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }
}
