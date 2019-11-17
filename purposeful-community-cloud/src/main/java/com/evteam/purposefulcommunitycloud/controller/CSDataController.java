package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.service.CSDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "Save community specific data template with the needed information", response = DataTemplateResource.class)
    @PostMapping("/create-csd")
    public ResponseEntity<DataTemplateResource> createCSDTemplate(@RequestBody DataTemplateDto templateDto){
        return ResponseEntity.ok(service.createCSDTemplate(templateDto));
    }
}
