package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.repository.RewardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RewardService {

    private final RewardRepository repository;

    @Autowired
    public RewardService(RewardRepository repository) {
        this.repository = repository;
    }

    public Reward getRewardById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Reward with id " + id + " not found"));
    }

    public List<Reward> getRewardsByUser(UUID userId) {
        return repository.findByUserId(userId);
    }

    public Reward saveReward(Reward reward) {
        return repository.save(reward);
    }

    public void deleteReward(UUID id) {
        Reward reward = getRewardById(id); // Ensures reward exists, otherwise throws an exception
        repository.delete(reward);
    }

    public Reward updateReward(UUID id, Reward updatedReward) {
        Reward existingReward = getRewardById(id);
        existingReward.setName(updatedReward.getName());
        existingReward.setDescription(updatedReward.getDescription());
        existingReward.setEarnedDate(updatedReward.getEarnedDate());
        return repository.save(existingReward);
    }
}
