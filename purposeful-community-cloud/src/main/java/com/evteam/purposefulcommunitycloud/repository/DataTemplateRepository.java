package com.evteam.purposefulcommunitycloud.repository;

import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataTemplateRepository extends JpaRepository<DataTemplate, UUID> {

}
