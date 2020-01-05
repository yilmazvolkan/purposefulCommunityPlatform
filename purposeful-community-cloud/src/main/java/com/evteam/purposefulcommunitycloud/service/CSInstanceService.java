package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.DataInstanceJsonMapper;
import com.evteam.purposefulcommunitycloud.mapper.DataInstanceMapper;
import com.evteam.purposefulcommunitycloud.model.FieldNameValueType;
import com.evteam.purposefulcommunitycloud.model.dto.DataInstanceDto;
import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.entity.DataInstance;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceJsonResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.repository.DataInstanceRepository;
import com.evteam.purposefulcommunitycloud.repository.DataTemplateRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import com.evteam.purposefulcommunitycloud.utils.FieldNameValueTypeUtil;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.evteam.purposefulcommunitycloud.constant.ErrorConstants.REQUIRED_FIELDS_CANNOT_BE_BLANK;
import static com.evteam.purposefulcommunitycloud.constant.ErrorConstants.YOU_CAN_NOT_DELETE_THIS_DATA_INSTANCE;

/**
 * Created by Emir Gökdemir
 * on 18 Ara 2019
 */
@Service
public class CSInstanceService {


    @Autowired
    private DataTemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CSDataService dataService;

    @Autowired
    private DataInstanceRepository instanceRepository;

    @Autowired
    private DataInstanceMapper instanceMapper;

    @Autowired
    private DataInstanceJsonMapper instanceJsonMapper;

    @Autowired
    private FieldNameValueTypeUtil fieldNameValueTypeUtil;


    public DataInstanceResource createCSDInstance(DataInstanceDto dto, UUID userId) {
        DataInstance instance = instanceMapper.toEntity(dto);
        instance.setCreator(userRepository.findUserById(userId));
        DataTemplate template = templateRepository.findDataTemplateById(dto.getTemplateId());
        instance.setTemplate(template);
        Map<String, Object> instanceFields = instance.getInstanceFields();
//        JsonLdProcessor.compact(instance.getInstanceFields(),template.getInstanceContext(),new JsonLdOptions());
        instance.getInstanceFields().put("@context", template.getInstanceContext());
        Set<String> keySet = instanceFields.keySet();
        for (DataField field : template.getFields()) {
            if (field.getIsRequired() && !keySet.contains(field.getName())) {
                throw new IllegalArgumentException(REQUIRED_FIELDS_CANNOT_BE_BLANK);
            }
        }
        List<FieldNameValueType> list=fieldNameValueTypeUtil.getFieldNameValueType(instance);
        instance.setFieldNameValueTypes(new JSONObject());
        instance.getFieldNameValueTypes().put("structure",list);
        return instanceMapper.toResource(instanceRepository.save(instance));
    }

    @Transactional
    @Modifying
    public String deleteInstance(UUID instanceId, UUID userId) throws IllegalAccessException {
        DataInstance instance = instanceRepository.getOne(instanceId);
        Community community = instance.getTemplate().getCommunity();
        if (!instance.getCreator().getId().equals(userId)
                && !community.getCreator().getId().equals(userId)
                && !community.getBuilders().contains(userRepository.findUserById(userId))) {
            throw new IllegalAccessException(YOU_CAN_NOT_DELETE_THIS_DATA_INSTANCE);
        }
        instanceRepository.delete(instance);
        return "Successfully Deleted!";
    }

    public List<?> getInstancesOfCommunity(UUID communityId, String format, UUID userId) {
        List<DataTemplateResource> templates = dataService.getCommunityTemplates(communityId, userId);
        if(format.toLowerCase().equals("json")){
            List<DataInstanceJsonResource> resources = new ArrayList<>();
            for (DataTemplateResource templateResource : CollectionUtils.emptyIfNull(templates)) {
                resources.addAll((List<DataInstanceJsonResource>)getInstancesOfTemplate(templateResource.getId(),format, userId));
            }
            return resources;
        }
        List<DataInstanceResource> resources = new ArrayList<>();
        for (DataTemplateResource templateResource : CollectionUtils.emptyIfNull(templates)) {
            resources.addAll((List<DataInstanceResource>)getInstancesOfTemplate(templateResource.getId(),format, userId));
        }
        return resources;
    }

    public List<?> getInstancesOfTemplate(UUID templateId, String format, UUID userId) {
        if(format.toLowerCase().equals("json")){
           return instanceJsonMapper.toResource(instanceRepository.findDataInstancesByTemplate(templateRepository.findDataTemplateById(templateId)));
        }
        return instanceMapper.toResource(instanceRepository.findDataInstancesByTemplate(templateRepository.findDataTemplateById(templateId)));
    }

    public List<?> getSelfInstances(String format,UUID userId) {
        if(format.toLowerCase().equals("json"))
            return instanceJsonMapper.toResource(instanceRepository.findDataInstancesByCreator(userRepository.findUserById(userId)));
        return instanceMapper.toResource(instanceRepository.findDataInstancesByCreator(userRepository.findUserById(userId)));
    }

    public Object getInstance(UUID instanceId, String format, UUID userId) {
        if(format.toLowerCase().equals("json"))
            return instanceJsonMapper.toResource(instanceRepository.findDataInstanceById(instanceId));
        return instanceMapper.toResource(instanceRepository.findDataInstanceById(instanceId));
    }

}
