package com.evteam.purposefulcommunitycloud.repository;

import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {

    Image findImageByCommunity(Community community);

    Image getImageByCommunity(Community community);

    Boolean existsImageByCommunity(Community community);
}
