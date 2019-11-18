package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.CommunityMapper;
import com.evteam.purposefulcommunitycloud.model.dto.CommunityDto;
import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.CommunityResource;
import com.evteam.purposefulcommunitycloud.repository.CommunityRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@Service
public class CommunityService {

    @Autowired
    private CommunityMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository repository;

    @Transactional
    public CommunityResource createCommunity(CommunityDto dto, UUID userId) {
        Community community = mapper.toEntity(dto);
        User creator = userRepository.findUserById(userId);
        community.setCreator(creator);
        return mapper.toResource(repository.saveAndFlush(community));
    }

    public CommunityResource getCommunity(UUID communityId, UUID userId) {
        Community community = repository.findCommunityById(communityId);
        if (community.getIsPrivate() && community.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Community is private");
            // TODO: 17 Kas 2019 following mechanism will be added
        }
        return mapper.toResource(community);
    }

    public List<CommunityResource> getAllCommunities(UUID userId) {
        List<Community> communities = repository.findAll();
        return mapper.toResource(communities);
    }
}
