package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.mapper.DataInstanceMapper;
import com.evteam.purposefulcommunitycloud.model.entity.DataInstance;
import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceResource;
import com.evteam.purposefulcommunitycloud.repository.DataInstanceRepository;
import com.evteam.purposefulcommunitycloud.repository.DataTemplateRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Ara 2019
 */
@Service
public class SearchService {

    @Autowired
    private DataTemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataInstanceRepository instanceRepository;

    @Autowired
    private DataInstanceMapper instanceMapper;

    public List<DataInstanceResource> searchByFieldNameAndValue(String fieldName, String value, UUID userId) {
        List<DataInstance> instances = instanceRepository.searchByFieldNameAndValue(fieldName, value);
//        List<DataInstance> instances=entityManager.createNativeQuery("SELECT * FROM data_instance WHERE instance_fields ->> ':fieldName' like '%:value%'")
//                .setParameter("fieldName",fieldName)
//                .setParameter("value",value)
//                .getResultList();
//        for(DataInstance instance:instances){
//            instance.getTemplate().getCommunity().getFollowers().contains(userRepository.findUserById(userId));
//        }
        // TODO: 17 Ara 2019 private community'ler gelmeyecek
        return instanceMapper.toResource(instances);
    }


    public List<DataInstanceResource> searchByTemplate(UUID templateId, Map<String, ?> searchValues, Boolean or, UUID userId) {
        List<DataInstance> allInstancesOfTemplate = instanceRepository.findDataInstancesByTemplate(templateRepository.findDataTemplateById(templateId));
        boolean found;
        List<DataInstanceResource> resources = new ArrayList<>();
        for (DataInstance instance : allInstancesOfTemplate) {
            found = true;
            JSONObject fields = instance.getInstanceFields();
            if (or) {
                for (Map.Entry<String, ?> entry : searchValues.entrySet()) {
                    if (fields.containsKey(entry.getKey())&&fields.get(entry.getKey()).equals(entry.getValue())){
                        resources.add(instanceMapper.toResource(instance));
                        break;
                    }
                }
            } else {
                for (Map.Entry<String, ?> entry : searchValues.entrySet()) {
                    if (fields.containsKey(entry.getKey())&&!fields.get(entry.getKey()).equals(entry.getValue())) {
                        found = false;
                        break;
                    }
                }
                if (found)
                    resources.add(instanceMapper.toResource(instance));

            }
        }
        return resources;
    }

}
