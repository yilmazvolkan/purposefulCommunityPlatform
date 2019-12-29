package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.resource.DataInstanceResource;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.CSInstanceService;
import com.evteam.purposefulcommunitycloud.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Ara 2019
 */

@RestController
@RequestMapping(value = {"/search"})
@Api(value = "Search", tags = {"Search Operations"})
public class SearchController {


    @Autowired
    private JwtResolver jwtResolver;

    @Autowired
    private SearchService service;


    @ApiOperation(value = "Search data instances with field name,value and token ",response = DataInstanceResource.class, responseContainer = "List")
    @GetMapping("/search/name-value")
    public ResponseEntity<List<DataInstanceResource>> searchByFieldsNameAndValue(@RequestHeader String token, @RequestParam String name, @RequestParam String value) {
        return ResponseEntity.ok(service.searchByFieldNameAndValue(name, value, jwtResolver.getIdFromToken(token)));
    }

    @ApiOperation(value = "Search data instances with field name,value and token ",response = DataInstanceResource.class, responseContainer = "List")
    @PostMapping("/search/in-templates")
    public ResponseEntity<List<DataInstanceResource>> searchInTemplatesByKeyValueSet(@RequestHeader String token,
                                                                                     @RequestParam UUID templateId,
                                                                                     @RequestParam Boolean or,
                                                                                     @RequestBody Map<String,?> searchingValues) {
        return ResponseEntity.ok(service.searchByTemplate(templateId, searchingValues, or, jwtResolver.getIdFromToken(token)));
    }





}
