package com.shown.springrabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("rabbitmq接口文档")
                .description("本文本描述了rabbitmq微服务接口定义")
                .version("1.0")
                .contact(new Contact("shown","https://mail.163.com/js6/main.jsp?sid=DAVFLkyLjRRFOxwupULLGqMSVBdnJkJQ&df=mail163_letter#module=welcome.WelcomeModule%7C%7B%7D","shownzr@163.com"))
                .build();
    }

}
