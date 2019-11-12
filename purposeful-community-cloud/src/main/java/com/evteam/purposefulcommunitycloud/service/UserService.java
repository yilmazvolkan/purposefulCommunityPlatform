package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.constant.ErrorConstants;
import com.evteam.purposefulcommunitycloud.mapper.LoginMapper;
import com.evteam.purposefulcommunitycloud.mapper.RegisterMapper;
import com.evteam.purposefulcommunitycloud.model.dto.LoginDto;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.LoginResource;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import com.evteam.purposefulcommunitycloud.security.JwtGenerator;
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
    private RegisterMapper registerMapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Transactional
    public UserResource register(RegisterDto registerDto){
        return registerMapper.toResource(repository.saveAndFlush(registerMapper.toEntity(registerDto)));
    }

    public LoginResource login(LoginDto loginDto){
        User user=repository.findByEmailAndPassword(loginDto.getEmail(),loginDto.getPassword());
        if(user == null){
            throw new RuntimeException(ErrorConstants.WRONG_EMAIL_OR_PASSWORD);
        }
        String token =jwtGenerator.generateToken(user.getId());
        LoginResource resource=loginMapper.toResource(user);
        resource.setToken(token);
        return resource;
    }
}
