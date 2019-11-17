package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.DataTemplateDto;
import com.evteam.purposefulcommunitycloud.model.entity.DataTemplate;
import com.evteam.purposefulcommunitycloud.model.resource.DataFieldResource;
import com.evteam.purposefulcommunitycloud.model.resource.DataTemplateResource;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.CSDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    private JwtResolver jwtResolver;

    @ApiOperation(value = "Create community specific data template with the needed information. Field types are TEXT,NUMBER, DECIMAL,DATE,LOCATION,TIME",
            response = DataTemplateResource.class)
    @PostMapping("/create-csd")
    public ResponseEntity<DataTemplateResource> createCSDTemplate(@RequestHeader String token, @RequestBody DataTemplateDto templateDto) {
        return ResponseEntity.ok(service.createCSDTemplate(templateDto, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get community specific data template with id", response = DataTemplateResource.class)
    @GetMapping("/{id}")
    public ResponseEntity<DataTemplateResource> getCSDTemplate(@RequestHeader String token, @PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getCSDTemplate(id, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get fields of community specific data template with id of data template", response = DataFieldResource.class)
    @GetMapping("/fields/{id}")
    public ResponseEntity<Set<DataFieldResource>> getFieldsOfCSDTemplate(@RequestHeader String token, @PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getFieldsOfCSDTemplate(id, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get data templates of a community with id of community", response = DataTemplateResource.class)
    @GetMapping("/get-community-templates/{community-id}")
    public ResponseEntity<List<DataTemplateResource>> getCSDTemplateOfCommunity(@RequestHeader String token, @PathVariable("community-id") UUID id) {
        return ResponseEntity.ok(service.getCommunityTemplates(id, jwtResolver.getIdFromToken(token)));
    }

}
