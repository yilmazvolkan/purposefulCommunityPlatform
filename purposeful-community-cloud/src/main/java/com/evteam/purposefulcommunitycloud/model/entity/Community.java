package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @ManyToMany
    @JoinColumn(name = "builders")
    List<User> builders;

    @ManyToMany
    @JoinTable(name = "followers_community", joinColumns = @JoinColumn(name = "community_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> followers;
//
//    @OneToOne(mappedBy = "community")
//    @Lob
//    private Image image;
}
