package com.evteam.purposefulcommunitycloud.service;

import com.evteam.purposefulcommunitycloud.model.entity.Community;
import com.evteam.purposefulcommunitycloud.model.entity.Image;
import com.evteam.purposefulcommunitycloud.repository.CommunityRepository;
import com.evteam.purposefulcommunitycloud.repository.ImageRepository;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

import static com.evteam.purposefulcommunitycloud.constant.ErrorConstants.*;

/**
 * Created by Emir Gökdemir
 * on 2 Ara 2019
 */
@Service
public class ImageService {

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private ImageRepository repository;

    public String uploadImage(MultipartFile file, UUID communityId, UUID userId) throws IllegalAccessException {
        Community community = communityRepository.findCommunityById(communityId);
        if (community == null) {
            throw new IllegalArgumentException(THERE_IS_NO_COMMUNITY_WITH_THIS_ID);
        } else if (!community.getCreator().getId().equals(userId)) {
            throw new IllegalAccessException(COMMUNITY_IMAGE_CAN_BE_CHANGED_BY_CREATOR);
        }
        Image img = new Image();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        img.setName(fileName);
        img.setCommunity(community);
        try {
            img.setFile(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        repository.save(img);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/download/")
                .path(fileName).path("/db")
                .toUriString();
        return fileDownloadUri;
    }

    @Transactional
    public Image getCommunityImage(UUID communityId) {
        Community community = communityRepository.findCommunityById(communityId);
        Image img = repository.findImageByCommunity(community);
        if (img == null) {
            throw new IllegalArgumentException(THERE_IS_NO_IMAGE_WITH_THIS_ID);
        }
        return img;
    }

}
