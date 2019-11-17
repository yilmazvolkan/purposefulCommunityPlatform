package com.evteam.purposefulcommunitycloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private String name;
    private String description;
    private Integer size;
    private Boolean isPrivate;
}
