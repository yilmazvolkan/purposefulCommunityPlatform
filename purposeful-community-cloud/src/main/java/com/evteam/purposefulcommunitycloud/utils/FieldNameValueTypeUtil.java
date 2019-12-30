package com.evteam.purposefulcommunitycloud.utils;

import com.evteam.purposefulcommunitycloud.model.FieldNameValueType;
import com.evteam.purposefulcommunitycloud.model.entity.DataField;
import com.evteam.purposefulcommunitycloud.model.entity.DataInstance;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.repository.DataTemplateRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Emir Gökdemir
 * on 29 Ara 2019
 */
@Component
public class FieldNameValueTypeUtil {
    @Autowired
    private DataTemplateRepository templateRepository;

    public List<FieldNameValueType> getFieldNameValueType(DataInstance resource){
        JSONObject instanceFields=new JSONObject(resource.getInstanceFields());
        DataTemplate template=resource.getTemplate();
        List<FieldNameValueType> list=helper(template,instanceFields);
        Map<String,String> map=(Map)template.getTemplatesNameId();
        for (Map.Entry<String,String> entry:map.entrySet()){
            list.addAll(helper(templateRepository.findDataTemplateById(UUID.fromString(entry.getValue())),(JSONObject)instanceFields.get(entry.getKey())));
        }
        return list;
    }

    private List<FieldNameValueType> helper(DataTemplate template,JSONObject instanceFields){
        List<DataField> fields=template.getFields();
        List<FieldNameValueType> list=new ArrayList<>();
        for(DataField field: CollectionUtils.emptyIfNull(fields)){
            FieldNameValueType fieldNameValueType=new FieldNameValueType();
            fieldNameValueType.setType(field.getFieldType());
            fieldNameValueType.setName(field.getName());
            fieldNameValueType.setValue((String) instanceFields.get(field.getName()));
            list.add(fieldNameValueType);
        }
        return list;
    }
}
