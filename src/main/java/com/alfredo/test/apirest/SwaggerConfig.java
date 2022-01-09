package com.alfredo.test.apirest;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.alfredo.test.apirest.controllers"))
                // .paths(PathSelectors.ant("/Examen"))
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo("TEST API",
                "Los servicios de esta API son para el TEST A", "1.0",
                "http://codmind.com/terms", new Contact("Codmind", "https://codmind.com", "apis@codmind.com"),
                "LICENSE", "LICENSE URL", Collections.emptyList());
    }
}
