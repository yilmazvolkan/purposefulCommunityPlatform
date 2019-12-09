package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.LoginDto;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.resource.LoginResource;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.evteam.purposefulcommunitycloud.constant.ResponseConstants.MAIL_SENT_TO_THAT_ADDRESS;
import static com.evteam.purposefulcommunitycloud.constant.ResponseConstants.USER_IS_CONFIRMED;

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

    @Autowired
    private JwtResolver jwtResolver;

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

    @ApiOperation(value = "Get an user informations with the user id", response = UserResource.class)
    @PostMapping("/get/{id}")
    public ResponseEntity<UserResource> login(@PathVariable("id") UUID userId){
        return ResponseEntity.ok(service.getUserById(userId));
    }

    @ApiOperation(value = "Get an user informations with the email", response = UserResource.class)
    @PostMapping("/get-with-mail/{email}")
    public ResponseEntity<UserResource> login(@PathVariable("email") String email){
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @ApiOperation(value = "Update user's self profile with token and RegisterDto", response = UserResource.class)
    @PostMapping("/update-profile")
    public ResponseEntity<UserResource> login(@RequestHeader String token,@RequestBody RegisterDto userDto){
        return ResponseEntity.ok(service.updateUserProfile(userDto,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Reset password request for an user with email", response = String.class)
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam String email){
        service.sendResetPasswordsToken(email);
        return ResponseEntity.ok(MAIL_SENT_TO_THAT_ADDRESS+email);
    }


    @ApiOperation(value = "Reset password with token and new password", response = String.class)
    @PostMapping("/confirm-reset-password/{token}")
    public ResponseEntity<UserResource> resetPassword(@PathVariable String token){
        return ResponseEntity.ok(service.getUserByToken(token));
    }


    @ApiOperation(value = "Reset password with token and new password", response = String.class)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestHeader String token, String password){
        service.resetPassword(token,password);
        return ResponseEntity.ok("Password changed");
    }

    @ApiOperation(value = "Login an user with the needed information", response = LoginResource.class)
    @PostMapping("/confirm-register/{token}")
    public ResponseEntity<String> confirmRegister(@PathVariable("token") String token){
        service.confirmRegister(token);
        return ResponseEntity.ok(USER_IS_CONFIRMED);
    }

}
