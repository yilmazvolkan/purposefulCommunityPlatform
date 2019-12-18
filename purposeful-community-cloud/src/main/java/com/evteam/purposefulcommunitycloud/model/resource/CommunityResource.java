package com.evteam.purposefulcommunitycloud.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.List;
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
public class CommunityResource {

    private UUID id;

    private String createdDate;

    private String lastModifiedDate;

    private String name;

    private String description;

    private Integer size;

    private Boolean isPrivate;

    private UserResource creatorUser;

    private List<UserResource> builders;

    private List<UserResource> followers;

}
