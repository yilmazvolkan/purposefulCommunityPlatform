package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.RegisterDto;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper extends Converter<RegisterDto, User, UserResource> {

    List<UserResource> toResource(List<User> users);

    Set<UserResource> toResource(Set<User> users);

}
