package com.evteam.purposefulcommunitycloud.controller;

import com.evteam.purposefulcommunitycloud.model.entity.Image;
import com.evteam.purposefulcommunitycloud.security.JwtResolver;
import com.evteam.purposefulcommunitycloud.service.ImageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 1 Ara 2019
 */

@RestController
@RequestMapping(value = {"/image"})
@Api(value = "Image", tags = {"Image Operations"})
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private JwtResolver jwtResolver;

    @PostMapping("/upload-community-image")
    public ResponseEntity<String> uploadCommunityImage(@RequestParam("file") MultipartFile file, @RequestParam UUID communityId, @RequestHeader String token) throws IllegalAccessException {
        return ResponseEntity.ok(imageService.uploadImage(file, communityId, jwtResolver.getIdFromToken(token)));
    }

    @GetMapping(value = "/get-community-image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCommunityImage(@RequestParam UUID communityId){
        Image image=imageService.getCommunityImage(communityId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getName()+"\"")
                .body(image.getFile());
    }
}
