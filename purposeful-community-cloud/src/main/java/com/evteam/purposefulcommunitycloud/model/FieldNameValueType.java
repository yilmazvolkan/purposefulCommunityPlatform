package com.evteam.purposefulcommunitycloud.model;

import com.evteam.purposefulcommunitycloud.constant.FieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Emir Gökdemir
 * on 30 Ara 2019
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FieldNameValueType implements Serializable {
    private String name;
    private String value;
    private FieldType type;
}
