package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@Data
@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User extends AbstractEntity {

    @NotNull
    @Column(name = "email")
    @Email(message = "Please provide acceptable mail address")
    private String email;


    @Column(name = "password")
    @Length(max = 30, message = "The password cannot be longer than 30")
    @Length(min = 8, message = "The password cannot be shorter than 8")
    @NotEmpty(message = "Please provide your password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "confirmed")
    private boolean confirmed=false;

}
