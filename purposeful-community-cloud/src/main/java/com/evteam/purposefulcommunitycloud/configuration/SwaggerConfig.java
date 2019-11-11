package com.evteam.purposefulcommunitycloud.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.evteam.purposefulcommunitycloud.constant.ConfigConstants.PURPOSEFUL_COMMUNITY_MAIL_ADRESS;
import static com.evteam.purposefulcommunitycloud.constant.ConfigConstants.PURPOSEFUL_COMMUNITY_URL;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.evteam.purposefulcommunitycloud.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("EVteam")
                .description("Purposeful Community REST API")
                .contact(new Contact("PurposefulCommunity", PURPOSEFUL_COMMUNITY_URL,PURPOSEFUL_COMMUNITY_MAIL_ADRESS ))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
