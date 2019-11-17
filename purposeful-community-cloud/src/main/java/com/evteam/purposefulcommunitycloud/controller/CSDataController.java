package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.resource.DataFieldResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.service.CSDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@RestController
@RequestMapping(value = {"/data-template"})
@Api(value = "Community Specific Data", tags = {"Community Specific Data Operations"})
public class CSDataController {

    @Autowired
    private CSDataService service;

    @ApiOperation(value = "Create community specific data template with the needed information", response = DataTemplateResource.class)
    @PostMapping("/create-csd")
    public ResponseEntity<DataTemplateResource> createCSDTemplate(@RequestBody DataTemplateDto templateDto){
        return ResponseEntity.ok(service.createCSDTemplate(templateDto));
    }

    @ApiOperation(value = "Get community specific data template with id", response = DataTemplateResource.class)
    @GetMapping("/{id}")
    public ResponseEntity<DataTemplateResource> getCSDTemplate(@PathVariable("id")UUID id){
        return ResponseEntity.ok(service.getCSDTemplate(id));
    }

    @ApiOperation(value = "Get fields of community specific data template with id of data template", response = DataFieldResource.class)
    @GetMapping("/fields/{id}")
    public ResponseEntity<Set<DataFieldResource>> getFieldsOfCSDTemplate(@PathVariable("id")UUID id){
        return ResponseEntity.ok(service.getFieldsOfCSDTemplate(id));
    }

}
