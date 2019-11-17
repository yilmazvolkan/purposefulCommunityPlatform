package com.evteam.purposefulcommunitycloud.mapper;


import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.DataFieldDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.resource.DataFieldResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DataFieldMapper extends Converter<DataFieldDto, DataField, DataFieldResource> {

    Set<DataFieldResource> toResource(Set<DataField> fields);

}
