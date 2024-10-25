package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Habit;
import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.service.HabitService;
import com.atomicaccountability.atomic_accountability.util.TestEntityFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class HabitControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private HabitService habitService;

    private HabitController habitController;

    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        habitController = new HabitController(habitService);
        user = TestEntityFactory.createUser();
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(habitController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getHabitById_ShouldReturnHabit_WhenHabitExists() throws Exception {
        UUID id = UUID.randomUUID();
        Habit habit = new Habit(); // Populate with necessary fields
        habit.setId(id);

        given(habitService.getHabitById(id)).willReturn(habit);

        mockMvc.perform(get("/api/habits/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));

        then(habitService).should().getHabitById(id);
    }

    @Test
    void getHabitById_ShouldReturn404_WhenHabitDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(habitService.getHabitById(id)).willThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/habits/{id}", id))
                .andExpect(status().isNotFound());

        then(habitService).should().getHabitById(id);
    }

    @Test
    void getHabitsByUser_ShouldReturnListOfHabits() throws Exception {
        UUID userId = UUID.randomUUID();
        Habit habit1 = TestEntityFactory.createHabit(user);
        Habit habit2 = TestEntityFactory.createHabit(user);
        given(habitService.getHabitsByUser(userId)).willReturn(List.of(habit1, habit2));

        mockMvc.perform(get("/api/habits/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        then(habitService).should().getHabitsByUser(userId);
    }

    @Test
    void createHabit_ShouldReturnCreatedHabit() throws Exception {
        Habit habit = new Habit(); // Populate with necessary fields
        given(habitService.saveHabit(habit)).willReturn(habit);

        mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habit)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(habitService).should().saveHabit(habit);
    }

    @Test
    void updateHabit_ShouldReturnUpdatedHabit() throws Exception {
        UUID id = UUID.randomUUID();
        Habit updatedHabit = new Habit(); // Populate with necessary fields
        given(habitService.updateHabit(id, updatedHabit)).willReturn(updatedHabit);

        mockMvc.perform(put("/api/habits/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedHabit)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(habitService).should().updateHabit(id, updatedHabit);
    }

    @Test
    void deleteHabit_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/habits/{id}", id))
                .andExpect(status().isNoContent());

        then(habitService).should().deleteHabit(id);
    }

    @Test
    void completeHabit_ShouldReturnCompletedHabit() throws Exception {
        UUID id = UUID.randomUUID();
        Habit completedHabit = new Habit(); // Populate with necessary fields
        given(habitService.completeHabit(id)).willReturn(completedHabit);

        mockMvc.perform(post("/api/habits/{id}/complete", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(habitService).should().completeHabit(id);
    }

    @Test
    void resetHabitStreak_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/api/habits/{id}/reset-streak", id))
                .andExpect(status().isNoContent());

        then(habitService).should().resetHabitStreak(id);
    }
}
