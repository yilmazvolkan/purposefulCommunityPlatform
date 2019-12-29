package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.DataInstanceDto;
import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceResource;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.CSInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 18 Ara 2019
 */

@RestController
@RequestMapping(value = {"/data-instance"})
@Api(value = "Community Specific Data Instance", tags = {"Community Specific Data Instance Operations"})
public class CSInstanceController {

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private CSInstanceService service;

    @ApiOperation(value = "Create community specific data instance with the needed information",
            response = DataInstanceResource.class)
    @PostMapping("/create/instance")
    public ResponseEntity<DataInstanceResource> createCSDInstance(@RequestHeader String token, @RequestBody DataInstanceDto dto) {
        return ResponseEntity.ok(service.createCSDInstance(dto, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get data instances of a community with id of community ",response = DataInstanceResource.class,responseContainer = "List")
    @GetMapping("/get/community-instances/{community-id}")
    public ResponseEntity getInstancesOfCommunity(@RequestHeader String token, @PathVariable("community-id") UUID communityId) {
        return ResponseEntity.ok(service.getInstancesOfCommunity(communityId, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get data instances of a template with id of template ",response = DataInstanceResource.class,responseContainer = "List")
    @GetMapping("/get/template-instances/{template-id}")
    public ResponseEntity getInstancesOfTemplate(@RequestHeader String token, @PathVariable("template-id") UUID templateId) {
        return ResponseEntity.ok(service.getInstancesOfTemplate(templateId, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get data instances of created by a user with token",response = DataInstanceResource.class,responseContainer = "List")
    @GetMapping("/get/self-instances")
    public ResponseEntity getSelfInstances(@RequestHeader String token){
        return ResponseEntity.ok(service.getSelfInstances(jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get data instances of created by a user with token",response = DataInstanceResource.class)
    @GetMapping("/get/instance/{instance-id}")
    public ResponseEntity getInstance(@RequestHeader String token, @PathVariable("instance-id") UUID instanceId){
        return ResponseEntity.ok(service.getInstance(instanceId,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Delete data instances with field name,value and token ",response = String.class)
    @DeleteMapping("/delete/instance/{instance-id}")
    public ResponseEntity deleteInstance(@RequestHeader String token, @PathVariable("instance-id") UUID instanceId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.deleteInstance(instanceId, jwtResolver.getIdFromToken(token)));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
