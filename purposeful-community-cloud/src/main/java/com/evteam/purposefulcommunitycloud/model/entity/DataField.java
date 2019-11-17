package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import com.evteam.purposefulcommunitycloud.constant.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_field")
public class DataField extends AbstractEntity {

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

//    @ManyToMany(mappedBy = "fields")
//    Set<DataTemplate> templates;
}
