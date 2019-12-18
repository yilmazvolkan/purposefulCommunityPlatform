package com.evteam.purposefulcommunitycloud.mapper;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Emir Gökdemir
 * on 18 Ara 2019
 */
@Component
public class DateMapper {

    public String toResource(ZonedDateTime createdDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy");
        return createdDate.format(formatter);
    }
}
