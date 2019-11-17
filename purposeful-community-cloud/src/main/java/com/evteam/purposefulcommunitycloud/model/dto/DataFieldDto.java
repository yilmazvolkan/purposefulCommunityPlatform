package com.evteam.purposefulcommunitycloud.model.dto;

import com.evteam.purposefulcommunitycloud.constant.FieldType;
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
public class DataFieldDto {
    private String name;
    private FieldType fieldType;
}
