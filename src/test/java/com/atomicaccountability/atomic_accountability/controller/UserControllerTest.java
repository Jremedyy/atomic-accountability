package com.atomicaccountability.atomic_accountability.controller;

import com.atomicaccountability.atomic_accountability.entity.User;
import com.atomicaccountability.atomic_accountability.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUsers_ShouldReturnPageOfUsers() throws Exception {
        User user1 = new User(); // Populate fields as necessary
        User user2 = new User(); // Populate fields as necessary
        Page<User> usersPage = new PageImpl<>(List.of(user1, user2), PageRequest.of(0, 10), 2);
        given(userService.getUsers(0, 10)).willReturn(usersPage);

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2));

        then(userService).should().getUsers(0, 10);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        given(userService.getUserById(id)).willReturn(user);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));

        then(userService).should().getUserById(id);
    }

    @Test
    void getUserById_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        given(userService.getUserById(id)).willThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());

        then(userService).should().getUserById(id);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        User user = new User(); // Populate with necessary fields
        given(userService.save(user)).willReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(userService).should().save(user);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UUID id = UUID.randomUUID();
        User updatedUser = new User(); // Populate with necessary fields
        given(userService.updateUser(id, updatedUser)).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(userService).should().updateUser(id, updatedUser);
    }

    @Test
    void deleteUser_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        then(userService).should().delete(id);
    }
}
