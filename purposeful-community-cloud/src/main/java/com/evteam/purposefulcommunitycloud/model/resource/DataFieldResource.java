package com.evteam.purposefulcommunitycloud.model.resource;

import com.evteam.purposefulcommunitycloud.constant.FieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@Resource
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DataFieldResource {

    private UUID id;

    private FieldType fieldType;

    private String name;

    private Boolean isRequired;

}
