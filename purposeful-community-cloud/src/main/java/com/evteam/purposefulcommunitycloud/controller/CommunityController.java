package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.CommunityDto;
import com.evteam.purposefulcommunitycloud.model.resource.CommunityResource;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Kas 2019
 */
@RestController
@RequestMapping(value = {"/community"})
@Api(value = "Community", tags = {"Community Operations"})
public class CommunityController {

    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private CommunityService service;

    @ApiOperation(value = "Create community with the needed information", response = CommunityResource.class)
    @PostMapping("/create")
    public ResponseEntity<CommunityResource> create(@RequestBody CommunityDto dto,
                                                    @RequestHeader String token){
        return ResponseEntity.ok(service.createCommunity(dto,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get community with the token, for now private communities can be acccesed by only creator.", response = CommunityResource.class)
    @GetMapping("/get/{id}")
    public ResponseEntity<CommunityResource> get(@RequestHeader String token,@PathVariable("id") UUID communityId){
        return ResponseEntity.ok(service.getCommunity(communityId,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get all communities with the token", response = CommunityResource.class)
    @GetMapping("/get/all")
    public ResponseEntity<List<CommunityResource>> getAll(@RequestHeader String token){
        return ResponseEntity.ok(service.getAllCommunities(jwtResolver.getIdFromToken(token)));
    }




}
