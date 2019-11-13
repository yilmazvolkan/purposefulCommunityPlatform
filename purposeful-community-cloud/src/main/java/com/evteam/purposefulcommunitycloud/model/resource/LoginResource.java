package com.evteam.purposefulcommunitycloud.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Resource;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@Setter
@Getter
@Resource
@AllArgsConstructor
@NoArgsConstructor
public class LoginResource extends UserResource{
    private String token;
}
