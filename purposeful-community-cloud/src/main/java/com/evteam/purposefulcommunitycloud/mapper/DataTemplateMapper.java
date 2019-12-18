package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {DataFieldMapper.class, DateMapper.class})
public interface DataTemplateMapper extends Converter<DataTemplateDto, DataTemplate, DataTemplateResource> {

    @Mapping(source = "fields", target = "fieldResources")
    DataTemplateResource toResource(DataTemplate dataTemplate);

    List<DataTemplateResource> toResource(List<DataTemplate> templates);

}
