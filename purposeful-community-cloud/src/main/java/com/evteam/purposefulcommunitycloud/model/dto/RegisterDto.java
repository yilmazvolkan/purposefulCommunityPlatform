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

    private String pword;

    private String studentId;

    private String name;

    private String surname;

    @Length(min=10,max=13)
    private String phone;
}
