package com.evteam.purposefulcommunitycloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataTemplateDto  {

    private String name;

    private UUID userId;

    private Set<DataFieldDto> fields;

    private UUID communityId;
}
