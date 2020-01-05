package com.evteam.purposefulcommunitycloud.model.dto;

import lombok.*;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Ara 2019
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DataInstanceDto {

    private UUID templateId;

    private JSONObject instanceFields;

    private String name;

}
