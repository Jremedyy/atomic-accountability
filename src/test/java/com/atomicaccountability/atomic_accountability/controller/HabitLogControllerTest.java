package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.HabitLog;
import com.atomicaccountability.atomic_accountability.service.HabitLogService;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class HabitLogControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private HabitLogService habitLogService;

    private HabitLogController habitLogController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        habitLogController = new HabitLogController(habitLogService);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(habitLogController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getHabitLogById_ShouldReturnHabitLog_WhenHabitLogExists() throws Exception {
        UUID id = UUID.randomUUID();
        HabitLog habitLog = new HabitLog();
        habitLog.setId(id);

        given(habitLogService.getHabitLogById(id)).willReturn(habitLog);

        mockMvc.perform(get("/api/habitlogs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));

        then(habitLogService).should().getHabitLogById(id);
    }

    @Test
    void getHabitLogById_ShouldReturn404_WhenHabitLogDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(habitLogService.getHabitLogById(id)).willThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/habitlogs/{id}", id))
                .andExpect(status().isNotFound());

        then(habitLogService).should().getHabitLogById(id);
    }

    @Test
    void getHabitLogsByHabit_ShouldReturnListOfHabitLogs() throws Exception {
        UUID habitId = UUID.randomUUID();
        HabitLog habitLog1 = new HabitLog(); // Populate with necessary fields
        HabitLog habitLog2 = new HabitLog(); // Populate with necessary fields
        given(habitLogService.getHabitLogsByHabit(habitId)).willReturn(List.of(habitLog1, habitLog2));

        mockMvc.perform(get("/api/habitlogs/habit/{habitId}", habitId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        then(habitLogService).should().getHabitLogsByHabit(habitId);
    }

    @Test
    void createHabitLog_ShouldReturnCreatedHabitLog() throws Exception {
        HabitLog habitLog = new HabitLog(); // Populate with necessary fields
        given(habitLogService.saveHabitLog(habitLog)).willReturn(habitLog);

        mockMvc.perform(post("/api/habitlogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habitLog)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(habitLogService).should().saveHabitLog(habitLog);
    }

    @Test
    void updateHabitLog_ShouldReturnUpdatedHabitLog() throws Exception {
        UUID id = UUID.randomUUID();
        HabitLog updatedHabitLog = new HabitLog(); // Populate with necessary fields
        given(habitLogService.updateHabitLog(id, updatedHabitLog)).willReturn(updatedHabitLog);

        mockMvc.perform(put("/api/habitlogs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedHabitLog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(habitLogService).should().updateHabitLog(id, updatedHabitLog);
    }

    @Test
    void deleteHabitLog_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/habitlogs/{id}", id))
                .andExpect(status().isNoContent());

        then(habitLogService).should().deleteHabitLog(id);
    }
}
