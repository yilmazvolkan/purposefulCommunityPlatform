package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community")
public class Community extends AbstractEntity {

    @NotNull
    @Column(name = "name",unique = true)
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "size")
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User creator;

    @Column(name = "is_private")
    private Boolean isPrivate;
}
