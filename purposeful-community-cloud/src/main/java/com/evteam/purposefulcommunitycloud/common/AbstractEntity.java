package com.evteam.purposefulcommunitycloud.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.evteam.purposefulcommunitycloud.constant.GeneralConstants.ID_LENGTH;

/**
 * Created by Emir Gökdemir
 * on 11 Kas 2019
 */
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", length = ID_LENGTH)
    protected UUID id;


    @CreatedDate
    @Column(name = "created_date", nullable = false)
    protected ZonedDateTime createdDate = ZonedDateTime.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    protected ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && Objects.equals(id, ((AbstractEntity) obj).id);
    }
}
