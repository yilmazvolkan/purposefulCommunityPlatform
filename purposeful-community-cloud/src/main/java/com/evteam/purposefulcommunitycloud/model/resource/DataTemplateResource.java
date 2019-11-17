package com.evteam.purposefulcommunitycloud.model.resource;

import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Resource
@Setter
@Getter
public class DataTemplateResource {

    private UUID id;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private String name;

    private Set<DataField> fields;

}
