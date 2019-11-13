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
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.evteam.purposefulcommunitycloud.constant.ErrorConstants.USER_NOT_EXIST;
import static com.evteam.purposefulcommunitycloud.constant.MailConstants.*;

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

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public UserResource register(RegisterDto registerDto) {
        User user = repository.saveAndFlush(registerMapper.toEntity(registerDto));
        sendActivationToken(registerDto.getEmail());
        return registerMapper.toResource(user);
    }

    public LoginResource login(LoginDto loginDto) {
        User user = repository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        if (user == null) {
            throw new RuntimeException(ErrorConstants.WRONG_EMAIL_OR_PASSWORD);
        }
        String token = jwtGenerator.generateToken(user.getId());
        LoginResource resource = loginMapper.toResource(user);
        resource.setToken(token);
        return resource;
    }

    public void sendActivationToken(String email) {
        String confirmationToken = jwtGenerator.generateTokenByEmail(email);
        SimpleMailMessage mailMessage = createMail(email);
        mailMessage.setTo(email);
        mailMessage.setSubject(CONFIRM_ACCOUNT_HEADER);
        mailMessage.setText(CONFIRM_ACCOUNT_BODY
                + this.environment.getProperty("spring.url")
                + CONFIRM_ACCOUNT_URL + confirmationToken);
        sendMail(mailMessage);
    }

    public void sendResetPasswordsToken(String email) {
        String confirmationToken = jwtGenerator.generateTokenByEmail(email);
        SimpleMailMessage mailMessage = createMail(email);
        mailMessage.setSubject(RESET_PASSWORD_HEADER);
        mailMessage.setText(RESET_PASSWORD_BODY
                + this.environment.getProperty("spring.url")
                + RESET_PASSWORD_URL + confirmationToken);
        sendMail(mailMessage);
    }


    public SimpleMailMessage createMail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        return mailMessage;
    }

    public void sendMail(SimpleMailMessage mailMessage) {
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException exception) {
            throw new RuntimeException(ErrorConstants.MAIL_SEND_FAILED);
        }
    }

    @Modifying
    @Transactional
    public void confirmRegister(String token) {
        UUID id = jwtResolver.getIdFromToken(token);
        User user = repository.getOne(id);
        if (user==null){
            throw new RuntimeException(USER_NOT_EXIST);
        }
        user.setConfirmed(true);
        repository.save(user);
    }


    public UserResource getUserByToken(String token) {
        UUID id = jwtResolver.getIdFromToken(token);
        User user = repository.getOne(id);
        if (user==null){
            throw new RuntimeException(USER_NOT_EXIST);
        }
        return registerMapper.toResource(user);
    }

    public void resetPassword(String token, String password) {
        UUID id = jwtResolver.getIdFromToken(token);
        User user = repository.getOne(id);
        if (user==null){
            throw new RuntimeException(USER_NOT_EXIST);
        }
        user.setPassword(password);
        repository.save(user);
    }
}
