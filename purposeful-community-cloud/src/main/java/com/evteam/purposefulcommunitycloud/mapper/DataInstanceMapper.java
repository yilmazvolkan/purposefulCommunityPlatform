package com.evteam.purposefulcommunitycloud.mapper;

import com.evteam.purposefulcommunitycloud.common.Converter;
import com.evteam.purposefulcommunitycloud.model.dto.DataInstanceDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataInstance;
import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE,uses = {DataTemplateMapper.class})
public interface DataInstanceMapper extends Converter<DataInstanceDto, DataInstance, DataInstanceResource> {

    List<DataInstanceResource> toResource(List<DataInstance> dataInstances);


}
