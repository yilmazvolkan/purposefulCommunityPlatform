package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DataTemplateMapper extends Converter<DataTemplateDto, DataTemplate, DataTemplateResource> {
}
