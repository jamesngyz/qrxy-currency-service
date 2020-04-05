package com.jamesngyz.qrxy.currencyservice;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {
	
	@Bean
	public AuditorAware auditorProvider() {
		return new EmptyAuditorProvider();
	}
	
	public static class EmptyAuditorProvider implements AuditorAware<String> {
		@Override
		public Optional<String> getCurrentAuditor() {
			// TODO
			return Optional.of("");
		}
	}
}
