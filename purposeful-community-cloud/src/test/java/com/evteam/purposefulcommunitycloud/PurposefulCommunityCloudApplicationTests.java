package com.evteam.purposefulcommunitycloud;

import com.evteam.purposefulcommunitycloud.mapper.DateMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@SpringBootTest
class PurposefulCommunityCloudApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private DateMapper dateMapper;
	@Test
	void dateMapperTest(){
		ZonedDateTime time=ZonedDateTime.now();
		String sdate=dateMapper.toResource(time);
		System.out.println(sdate);

	}

}
