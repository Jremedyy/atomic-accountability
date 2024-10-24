package com.atomicaccountability.atomic_accountability.service;

import com.atomicaccountability.atomic_accountability.repository.UserRepository;
import com.atomicaccountability.atomic_accountability.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository){
        this.repository = repository;
    }
    public Page<User> getUsers(final int pageNumber, final int pageSize){
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }


}
