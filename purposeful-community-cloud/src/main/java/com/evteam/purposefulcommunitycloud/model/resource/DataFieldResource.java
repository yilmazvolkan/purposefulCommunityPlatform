package com.evteam.purposefulcommunitycloud.model.resource;

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

    UUID id;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

    private String name;

}
