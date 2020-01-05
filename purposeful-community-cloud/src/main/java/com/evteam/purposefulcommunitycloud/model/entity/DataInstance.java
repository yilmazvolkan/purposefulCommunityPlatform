package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import com.sun.istack.NotNull;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.simple.JSONObject;

import javax.persistence.*;

/**
 * Created by Emir Gökdemir
 * on 17 Ara 2019
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "data_instance")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DataInstance extends AbstractEntity {

    @ManyToOne
    private User creator;

    @ManyToOne
    private DataTemplate template;

    @NotNull
    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JSONObject instanceFields;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JSONObject fieldNameValueTypes;
}
