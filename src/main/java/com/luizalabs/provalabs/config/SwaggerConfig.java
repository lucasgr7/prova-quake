package com.luizalabs.provalabs.config;

import java.util.Collections;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer{

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName("com.luizalabs").select()
				.apis(RequestHandlerSelectors.basePackage("com.luizalabs.provalabs")).paths(PathSelectors.ant("/**")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Prova Luiza Labs", "API dos dados do jogo QUAKE 3.", 
				getClass().getPackage().getImplementationVersion() == null ? "stage" : getClass().getPackage().getImplementationVersion(), "",
				new Contact("Lucas Garcia Ribeiro", "www.example.com", "lucas.ribeiro@luizalabs.com"), "Licença MIT",
				"", Collections.emptyList());
	}
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }
}
