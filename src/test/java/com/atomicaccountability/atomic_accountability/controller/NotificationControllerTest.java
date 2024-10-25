package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.Notification;
import com.atomicaccountability.atomic_accountability.service.NotificationService;
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
class NotificationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private NotificationController notificationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        notificationController = new NotificationController(notificationService);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getNotificationById_ShouldReturnNotification_WhenExists() throws Exception {
        UUID id = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setId(id);

        given(notificationService.getNotificationById(id)).willReturn(notification);

        mockMvc.perform(get("/api/notifications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));

        then(notificationService).should().getNotificationById(id);
    }

    @Test
    void getNotificationById_ShouldReturn404_WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(notificationService.getNotificationById(id)).willThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/notifications/{id}", id))
                .andExpect(status().isNotFound());

        then(notificationService).should().getNotificationById(id);
    }

    @Test
    void getNotificationsByUser_ShouldReturnListOfNotifications() throws Exception {
        UUID userId = UUID.randomUUID();
        Notification notification1 = new Notification(); // Populate with necessary fields
        Notification notification2 = new Notification(); // Populate with necessary fields
        given(notificationService.getNotificationsByUser(userId)).willReturn(List.of(notification1, notification2));

        mockMvc.perform(get("/api/notifications/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        then(notificationService).should().getNotificationsByUser(userId);
    }

    @Test
    void createNotification_ShouldReturnCreatedNotification() throws Exception {
        Notification notification = new Notification(); // Populate with necessary fields
        given(notificationService.saveNotification(notification)).willReturn(notification);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(notificationService).should().saveNotification(notification);
    }

    @Test
    void updateNotification_ShouldReturnUpdatedNotification() throws Exception {
        UUID id = UUID.randomUUID();
        Notification updatedNotification = new Notification(); // Populate with necessary fields
        given(notificationService.updateNotification(id, updatedNotification)).willReturn(updatedNotification);

        mockMvc.perform(put("/api/notifications/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNotification)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(notificationService).should().updateNotification(id, updatedNotification);
    }

    @Test
    void deleteNotification_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/notifications/{id}", id))
                .andExpect(status().isNoContent());

        then(notificationService).should().deleteNotification(id);
    }
}
