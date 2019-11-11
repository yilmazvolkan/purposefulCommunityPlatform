package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import org.mapstruct.Mapper;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */


@Mapper(componentModel = "spring")
public interface RegisterMapper extends Converter<RegisterDto, User, UserResource> {
}
