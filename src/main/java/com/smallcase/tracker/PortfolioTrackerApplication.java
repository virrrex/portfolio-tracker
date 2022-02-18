package com.smallcase.tracker;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class PortfolioTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioTrackerApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.smallcase"))
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Portfolio Tracking API",
				"API Documentation for smallcase assignment portfilio tracking API",
				"1.0", 
				"smallcase Use",
				new springfox.documentation.service.Contact("Vishal", "https://github.com/virrrex/portfolio-tracker", "rex.vishal.3@gmail.com"),
				"API License",
				"http://apache.org/licenses",
				Collections.emptyList());
	}
}
