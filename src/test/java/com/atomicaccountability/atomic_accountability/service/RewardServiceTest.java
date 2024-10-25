package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.repository.RewardRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class RewardServiceTest {

    @MockBean
    private RewardRepository repository;

    private RewardService service;

    private Reward reward;
    private UUID rewardId;
    private User user;
    private Habit habit;

    @BeforeEach
    void setUp() {
        service = new RewardService(repository);
        reward = TestEntityFactory.createReward(user, habit);
        rewardId = reward.getId();
    }

    @Test
    void getRewardById_ShouldReturnReward_WhenRewardExists() {
        given(repository.findById(rewardId)).willReturn(Optional.of(reward));

        Reward result = service.getRewardById(rewardId);

        assertThat(result).isEqualTo(reward);
        then(repository).should(times(1)).findById(rewardId);
    }

    @Test
    void getRewardById_ShouldThrowException_WhenRewardDoesNotExist() {
        given(repository.findById(rewardId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getRewardById(rewardId));

        then(repository).should(times(1)).findById(rewardId);
    }

    @Test
    void getRewardsByUser_ShouldReturnListOfRewards_ForGivenUserId() {
        UUID userId = UUID.randomUUID();
        List<Reward> rewards = List.of(reward);
        given(repository.findByUserId(userId)).willReturn(rewards);

        List<Reward> result = service.getRewardsByUser(userId);

        assertThat(result).isEqualTo(rewards);
        then(repository).should(times(1)).findByUserId(userId);
    }

    @Test
    void saveReward_ShouldReturnSavedReward() {
        given(repository.save(reward)).willReturn(reward);

        Reward result = service.saveReward(reward);

        assertThat(result).isEqualTo(reward);
        then(repository).should(times(1)).save(reward);
    }

    @Test
    void deleteReward_ShouldDeleteReward_WhenRewardExists() {
        given(repository.findById(rewardId)).willReturn(Optional.of(reward));

        service.deleteReward(rewardId);

        then(repository).should(times(1)).findById(rewardId);
        then(repository).should(times(1)).delete(reward);
    }

    @Test
    void deleteReward_ShouldThrowException_WhenRewardDoesNotExist() {
        given(repository.findById(rewardId)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteReward(rewardId));

        then(repository).should(times(1)).findById(rewardId);
    }

    @Test
    void updateReward_ShouldReturnUpdatedReward() {
        Reward updatedReward = TestEntityFactory.createReward(user, habit);
        updatedReward.setName("Updated Reward");
        given(repository.findById(rewardId)).willReturn(Optional.of(reward));
        given(repository.save(reward)).willReturn(reward);

        Reward result = service.updateReward(rewardId, updatedReward);

        assertThat(result.getName()).isEqualTo("Updated Reward");
        then(repository).should(times(1)).findById(rewardId);
        then(repository).should(times(1)).save(reward);
    }
}
