package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.repository.UserRepository;
import com.atomicaccountability.atomic_accountability.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<User> getUsers(final int pageNumber, final int pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Transactional(readOnly = true)
    public User getUserById(final UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public User save(final User user) {
        return repository.save(user);
    }

    @Transactional
    public void delete(final UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}
