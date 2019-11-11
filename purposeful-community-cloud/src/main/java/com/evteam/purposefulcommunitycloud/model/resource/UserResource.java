package com.evteam.purposefulcommunitycloud.model.resource;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Getter
@Setter
@Resource
public class UserResource {

    private String email;

    private String studentId;
    
    private String phone;

    private String name;

    private String surname;

}
