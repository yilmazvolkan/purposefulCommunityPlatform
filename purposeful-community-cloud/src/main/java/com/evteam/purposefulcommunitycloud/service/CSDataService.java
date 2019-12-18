package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.DataTemplateMapper;
import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataFieldResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.repository.*;
import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


/**
 * Created by Emir Gökdemir
 * on 16 Kas 2019
 */
@Service
public class CSDataService {

    @Autowired
    private DataTemplateMapper templateMapper;

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
        JSONObject contextOfFields=new JSONObject();
        DataTemplate dataTemplate = templateMapper.toEntity(templateDto);
        dataTemplate.setCreator(userRepository.findUserById(templateDto.getUserId()));
        dataTemplate.setCommunity(communityRepository.findCommunityById(templateDto.getCommunityId()));
        contextOfFields.put("xsd","http://www.w3.org/2001/XMLSchema#");
        for (DataField field : CollectionUtils.emptyIfNull(dataTemplate.getFields())) {
            field.setTemplate(dataTemplate);
            fieldRepository.save(field);
            contextOfFields.put(field.getName(),"xsd:"+field.getFieldType().toLowerCaseString());
        }
        dataTemplate.setInstanceContext(contextOfFields);
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
