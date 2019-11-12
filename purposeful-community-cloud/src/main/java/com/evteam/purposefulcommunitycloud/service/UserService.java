package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.RegisterMapper;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@Service
public class UserService {

    @Autowired
    RegisterMapper mapper;

    @Autowired
    UserRepository repository;


    @Transactional
    public UserResource register(RegisterDto registerDto){
        User user=mapper.toEntity(registerDto);
        repository.saveAndFlush(user);

        return mapper.toResource(user);
    }

}
