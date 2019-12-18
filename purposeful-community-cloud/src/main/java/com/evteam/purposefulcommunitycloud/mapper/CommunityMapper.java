package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.CommunityDto;
import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.resource.CommunityResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE,uses = {RegisterMapper.class, DateMapper.class})
public interface CommunityMapper extends Converter<CommunityDto, Community, CommunityResource> {

    @Mapping(source = "creator", target = "creatorUser")
    CommunityResource toResource(Community community);

    List<CommunityResource> toResource(List<Community> communities);
}
