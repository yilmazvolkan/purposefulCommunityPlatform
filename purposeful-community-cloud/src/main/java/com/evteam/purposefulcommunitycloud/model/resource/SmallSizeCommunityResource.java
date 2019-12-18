package com.evteam.purposefulcommunitycloud.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 1 Ara 2019
 */

@Resource
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SmallSizeCommunityResource {

    private UUID id;

    private String createdDate;

    private String name;

    private String description;

    private Boolean isPrivate;

    private Integer size;
}
