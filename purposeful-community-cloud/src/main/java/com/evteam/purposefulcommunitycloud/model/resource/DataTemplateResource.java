package com.evteam.purposefulcommunitycloud.model.resource;

import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

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

    private ZonedDateTime lastModifiedDate;

    private String name;

    private Set<DataFieldResource> fieldResources;

    private JSONObject instanceContext;
}
