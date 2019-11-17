package com.evteam.purposefulcommunitycloud.model.resource;

import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@Resource
public class DataFieldResource {

    UUID id;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private String name;

    private Set<DataTemplate> templates;

}
