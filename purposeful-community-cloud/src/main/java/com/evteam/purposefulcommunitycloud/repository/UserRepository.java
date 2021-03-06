package com.evteam.purposefulcommunitycloud.repository;


import com.evteam.purposefulcommunitycloud.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmailAndPassword(String email,String password);

    User findByEmail(String email);

    User findUserById(UUID id);

}
