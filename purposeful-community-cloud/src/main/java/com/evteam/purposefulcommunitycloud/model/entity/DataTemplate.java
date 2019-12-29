package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.simple.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "data_template")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class DataTemplate extends AbstractEntity {
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "template")
    private List<DataField> fields;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JSONObject instanceContext;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JSONObject templatesNameId;
}
