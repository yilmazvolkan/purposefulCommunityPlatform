package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "data_template")
public class DataTemplate extends AbstractEntity {
    @NotNull
    String name;

    // TODO: 16 Kas 2019  community will be added

    @ManyToOne
    @JoinColumn(name = "user_id")
    User creator;

    @ManyToMany
            @JoinTable(name="template_include_fields",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id"))
    Set<DataField> fields;
}
