package com.jamesngyz.qrxy.currencyservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@TestConfiguration
public class IntegrationTestConfig {
	
	@Bean
	RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder().requestFactory(HttpComponentsClientHttpRequestFactory.class);
	}
	
}
