package com.evteam.purposefulcommunitycloud.model.entity;

import com.evteam.purposefulcommunitycloud.common.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Emir Gökdemir
 * on 2 Ara 2019
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image extends AbstractEntity {

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    @Column
    @Lob
    private byte[] file;

}
