package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.DataFieldMapper;
import com.evteam.purposefulcommunitycloud.mapper.DataTemplateMapper;
import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataFieldResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.repository.CommunityRepository;
import com.evteam.purposefulcommunitycloud.repository.DataFieldRepository;
import com.evteam.purposefulcommunitycloud.repository.DataTemplateRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Service
public class CSDataService {

    @Autowired
    private DataTemplateMapper templateMapper;

    @Autowired
    private DataFieldMapper fieldMapper;

    @Autowired
    private DataFieldRepository fieldRepository;

    @Autowired
    private DataTemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Transactional
    public DataTemplateResource createCSDTemplate(DataTemplateDto templateDto, UUID userId) {
        DataTemplate dataTemplate = templateMapper.toEntity(templateDto);
        dataTemplate.setCreator(userRepository.findUserById(templateDto.getUserId()));
        dataTemplate.setCommunity(communityRepository.findCommunityById(templateDto.getCommunityId()));
        for (DataField field : CollectionUtils.emptyIfNull(dataTemplate.getFields())) {
            fieldRepository.saveAndFlush(field);
        }
        templateRepository.save(dataTemplate);
        return templateMapper.toResource(dataTemplate);
    }

    public DataTemplateResource getCSDTemplate(UUID id, UUID userId) {
        return templateMapper.toResource(templateRepository.findDataTemplateById(id));
    }

    public Set<DataFieldResource> getFieldsOfCSDTemplate(UUID id, UUID userId) {
        return templateMapper.toResource(templateRepository.findDataTemplateById(id)).getFieldResources();
    }

    public List<DataTemplateResource> getCommunityTemplates(UUID communityId, UUID userId){
        return templateMapper.toResource(templateRepository.findDataTemplatesByCommunity(communityRepository.findCommunityById(communityId)));
    }
}