package com.evteam.purposefulcommunitycloud.repository;

import com.evteam.purposefulcommunitycloud.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {

    Community findCommunityById(UUID id);
}
