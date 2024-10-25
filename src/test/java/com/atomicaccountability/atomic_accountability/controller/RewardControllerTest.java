package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Reward;
import com.atomicaccountability.atomic_accountability.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class RewardControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    private RewardController rewardController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        rewardController = new RewardController(rewardService);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getRewardById_ShouldReturnReward_WhenRewardExists() throws Exception {
        UUID id = UUID.randomUUID();
        Reward reward = new Reward();
        reward.setId(id);

        given(rewardService.getRewardById(id)).willReturn(reward);

        mockMvc.perform(get("/api/rewards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));

        then(rewardService).should().getRewardById(id);
    }

    @Test
    void getRewardById_ShouldReturn404_WhenRewardDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(rewardService.getRewardById(id)).willThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/rewards/{id}", id))
                .andExpect(status().isNotFound());

        then(rewardService).should().getRewardById(id);
    }

    @Test
    void getRewardsByUser_ShouldReturnListOfRewards() throws Exception {
        UUID userId = UUID.randomUUID();
        Reward reward1 = new Reward(); // Populate with necessary fields
        Reward reward2 = new Reward(); // Populate with necessary fields
        given(rewardService.getRewardsByUser(userId)).willReturn(List.of(reward1, reward2));

        mockMvc.perform(get("/api/rewards/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        then(rewardService).should().getRewardsByUser(userId);
    }

    @Test
    void createReward_ShouldReturnCreatedReward() throws Exception {
        Reward reward = new Reward(); // Populate with necessary fields
        given(rewardService.saveReward(reward)).willReturn(reward);

        mockMvc.perform(post("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reward)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(rewardService).should().saveReward(reward);
    }

    @Test
    void updateReward_ShouldReturnUpdatedReward() throws Exception {
        UUID id = UUID.randomUUID();
        Reward updatedReward = new Reward(); // Populate with necessary fields
        given(rewardService.updateReward(id, updatedReward)).willReturn(updatedReward);

        mockMvc.perform(put("/api/rewards/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReward)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(rewardService).should().updateReward(id, updatedReward);
    }

    @Test
    void deleteReward_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/rewards/{id}", id))
                .andExpect(status().isNoContent());

        then(rewardService).should().deleteReward(id);
    }
}
