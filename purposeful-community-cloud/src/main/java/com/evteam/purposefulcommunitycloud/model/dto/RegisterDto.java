package com.evteam.purposefulcommunitycloud.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@Getter
@Setter
public class RegisterDto {

    private String email;

    private String password;

    private String name;

    private String surname;
}
