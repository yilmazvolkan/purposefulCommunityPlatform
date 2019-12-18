package com.evteam.purposefulcommunitycloud.repository;

import com.evteam.purposefulcommunitycloud.model.entity.DataInstance;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataInstanceRepository extends JpaRepository<DataInstance, UUID>, JpaSpecificationExecutor<DataInstance> {

    DataInstance findDataInstanceById(UUID id);

    List<DataInstance> findDataInstancesByTemplate(DataTemplate template);

    List<DataInstance> findDataInstancesByCreator(User user);

    @Query (value = "SELECT * FROM data_instance WHERE instance_fields ->> :fieldName like :value ;",nativeQuery = true)
    List<DataInstance> searchByFieldNameAndValue(@Param("fieldName") String fieldName, @Param("value") String value);
}
