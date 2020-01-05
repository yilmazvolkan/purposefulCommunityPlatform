package com.evteam.purposefulcommunitycloud.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Ara 2019
 */
@Resource
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DataInstanceResource {
    private String createdDate;
    private UUID id;
    private UserResource creator;
    private JSONObject instanceFields;
    private DataTemplateResource template;
    private String name;
}
