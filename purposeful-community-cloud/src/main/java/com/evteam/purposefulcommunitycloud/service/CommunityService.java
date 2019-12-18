package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.CommunityMapper;
import com.evteam.purposefulcommunitycloud.mapper.RegisterMapper;
import com.evteam.purposefulcommunitycloud.mapper.SmallSizeCommunityMapper;
import com.evteam.purposefulcommunitycloud.model.dto.CommunityDto;
import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.CommunityResource;
import com.evteam.purposefulcommunitycloud.model.resource.SmallSizeCommunityResource;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import com.evteam.purposefulcommunitycloud.repository.CommunityRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static com.evteam.purposefulcommunitycloud.constant.ErrorConstants.*;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@Service
public class CommunityService {

    @Autowired
    private CommunityMapper mapper;

    @Autowired
    private SmallSizeCommunityMapper smallMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private CommunityRepository repository;

    @Transactional
    public CommunityResource createCommunity(CommunityDto dto, UUID userId) {
        Community community = mapper.toEntity(dto);
        User creator = userRepository.findUserById(userId);
        community.setCreator(creator);
        community=repository.saveAndFlush(community);
        followCommunity(community.getId(),userId);
        return mapper.toResource(community);
    }

    public CommunityResource getCommunity(UUID communityId, UUID userId) throws IllegalAccessException {
        Community community = repository.findCommunityById(communityId);
        if (community.getIsPrivate() &&
                !(community.getFollowers().contains(userRepository.findUserById(userId))||(community.getCreator().getId().equals(userId)))) {
            throw new IllegalAccessException(COMMUNITY_IS_PRIVATE);
        }
        return mapper.toResource(community);
    }

    public List<SmallSizeCommunityResource> getAllCommunities(UUID userId) {
        List<Community> communities = repository.findAll();
        return smallMapper.toResource(communities);
    }

    public CommunityResource addBuilders(UUID communityId,List<UUID> idsOfBuilders,UUID creatorId){
        Community community=repository.getOne(communityId);
        if(!community.getCreator().getId().equals(creatorId)){
            throw new RuntimeException(ONLY_CREATOR_CAN_EDIT_BUILDER);
        }
        List<User> builders=community.getBuilders();
        for(UUID builderId: CollectionUtils.emptyIfNull(idsOfBuilders)){
            User user=userRepository.findUserById(builderId);
            builders.add(user);
        }
        community.setBuilders(builders);
        repository.save(community);
        return mapper.toResource(community);
    }

    @Modifying
    @Transactional
    public CommunityResource deleteBuilders(UUID communityId, UUID builderId, UUID creatorId) {
        Community community=repository.getOne(communityId);
        if(!community.getCreator().getId().equals(creatorId)){
            throw new RuntimeException(ONLY_CREATOR_CAN_EDIT_BUILDER);
        }
        community.getBuilders().remove(userRepository.findUserById(builderId));
        return mapper.toResource(community);
    }

    public CommunityResource followCommunity(UUID communityId, UUID userId){
        Community community=repository.getOne(communityId);
        User user=userRepository.findUserById(userId);
        if(community.getFollowers().contains(user)){
            throw new  RuntimeException(USER_ALREADY_FOLLOWED_THIS_COMMUNITY);
        }
        List<User> followers=community.getFollowers();
        followers.add(user);
        community.setFollowers(followers);
        repository.save(community);
        return mapper.toResource(community);
    }

    public List<UserResource> getBuilders(UUID communityId, UUID userId) {
        Community community=repository.findCommunityById(communityId);
        if(!community.getCreator().getId().equals(userId)){
            throw new RuntimeException(ONLY_CREATOR_CAN_SEE_BUILDER);
        }
        return registerMapper.toResource(community.getBuilders());
    }


    public List<UserResource> getFollowers(UUID communityId, UUID userId) {
        Community community=repository.findCommunityById(communityId);
        if (community.getIsPrivate() && !community.getFollowers().contains(userRepository.findUserById(userId))) {
            throw new RuntimeException(COMMUNITY_IS_PRIVATE);
        }
        return registerMapper.toResource(community.getFollowers());
    }

    public List<CommunityResource> getSelfFollowings(UUID userId){
        List<Community> communities=repository.findCommunitiesByFollowers(userRepository.findUserById(userId));
        return mapper.toResource(communities);
    }

    @Transactional
    @Modifying
    public SmallSizeCommunityResource unfollowCommunity(UUID communityId, UUID userId) throws IllegalAccessException {
        Community community=repository.getOne(communityId);
        User user=userRepository.findUserById(userId);
        if(!community.getFollowers().contains(user)){
            throw new  IllegalAccessException(USER_ALREADY_UNFOLLOWED_THIS_COMMUNITY);
        }
        List<User> followers=community.getFollowers();
        followers.remove(user);
        community.setFollowers(followers);
        repository.save(community);
        return smallMapper.toResource(community);
    }
}
