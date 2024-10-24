package com.atomicaccountability.atomic_accountability.repository;

import com.atomicaccountability.atomic_accountability.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
