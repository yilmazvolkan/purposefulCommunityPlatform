package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.LoginDto;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.resource.LoginResource;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import com.evteam.purposefulcommunitycloud.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@RestController
@RequestMapping(value = {"/user"})
@Api(value = "User", tags = {"User Operations"})
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "Register an user with the needed information", response = UserResource.class)
    @PostMapping("/register")
    public ResponseEntity<UserResource> register(@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok(service.register(registerDto));
    }

    @ApiOperation(value = "Login an user with the needed information", response = LoginResource.class)
    @PostMapping("/login")
    public ResponseEntity<UserResource> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(service.login(loginDto));
    }

}
