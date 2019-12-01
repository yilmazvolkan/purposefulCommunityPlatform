package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.dto.CommunityDto;
import com.evteam.purposefulcommunitycloud.model.resource.CommunityResource;
import com.evteam.purposefulcommunitycloud.model.resource.UserResource;
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

    @ApiOperation(value = "Add builders to a community with builder emails and token", response = CommunityResource.class)
    @PostMapping("/builder/add")
    public ResponseEntity<CommunityResource> addBuilders(@RequestHeader String token,@RequestBody List<String> emailsOfBuilders,@RequestParam UUID communityId){
        return ResponseEntity.ok(service.addBuilders(communityId,emailsOfBuilders,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Follow a community with communityId and token", response = CommunityResource.class)
    @GetMapping("/follow-community")
    public ResponseEntity<CommunityResource> followCommunity(@RequestHeader String token,@RequestParam UUID communityId){
        return ResponseEntity.ok(service.followCommunity(communityId,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get all builders of community with communityId and token", response = UserResource.class)
    @GetMapping("/builder/get-all")
    public ResponseEntity<List<UserResource>> getBuilders(@RequestHeader String token,@RequestParam UUID communityId){
        return ResponseEntity.ok(service.getBuilders(communityId,jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Get all followers of community with communityId and token", response = UserResource.class)
    @GetMapping("/follower/get-all")
    public ResponseEntity<List<UserResource>> getFollowers(@RequestHeader String token,@RequestParam UUID communityId){
        return ResponseEntity.ok(service.getFollowers(communityId,jwtResolver.getIdFromToken(token)));
    }



}
