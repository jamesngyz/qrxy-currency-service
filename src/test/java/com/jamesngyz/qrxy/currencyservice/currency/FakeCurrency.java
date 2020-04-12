package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.javafaker.Faker;

class FakeCurrency {
	
	private static final Faker faker = new Faker();
	
	static Currency build() {
		String code = generateCurrencyCode();
		String name = generateCurrencyName();
		
		Currency currency = new Currency();
		currency.setCode(code);
		currency.setName(name);
		currency = generatePersistenceFields(currency);
		return currency;
	}
	
	private static Currency generatePersistenceFields(Currency currency) {
		currency.setCreatedAt(faker.date().birthday());
		currency.setCreatedBy(faker.name().firstName());
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		currency.setUpdatedAt(currency.getCreatedAt());
		currency.setUpdatedBy(currency.getCreatedBy());
		currency.setVersion(0);
		return currency;
	}
	
	static class Request {
		static CreateCurrencyRequest build() {
			String code = generateCurrencyCode();
			String name = generateCurrencyName();
			
			CreateCurrencyRequest request = new CreateCurrencyRequest();
			request.setCode(code);
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withCodeShorterThan3() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String code = RandomStringUtils.randomAlphabetic(0, 3).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeLongerThan3() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String code = RandomStringUtils.randomAlphabetic(4, 21).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNonAlphabetic() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String code = RandomStringUtils.randomNumeric(3);
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNotUpperCase() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String code = RandomStringUtils.randomAlphabetic(3).toLowerCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNull() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			request.setCode(null);
			return request;
		}
		
		static CreateCurrencyRequest withNameShorterThan1() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String name = RandomStringUtils.randomAlphabetic(0, 1).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameLongerThan80() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			String name = RandomStringUtils.randomAlphabetic(81, 201).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameWhitespaceOnly() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			int length = faker.number().numberBetween(1, 80);
			String name = RandomStringUtils.random(length, " ");
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameNull() {
			CreateCurrencyRequest request = FakeCurrency.Request.build();
			request.setName(null);
			return request;
		}
	}
	
	static class UpdateRequest {
		static UpdateCurrencyRequest build() {
			String code = generateCurrencyCode();
			String name = generateCurrencyName();
			
			UpdateCurrencyRequest request = new UpdateCurrencyRequest();
			request.setCode(code);
			request.setName(name);
			return request;
		}
	}
	
	private static String generateCurrencyCode() {
		return RandomStringUtils.randomAlphabetic(3).toUpperCase();
	}
	
	private static String generateCurrencyName() {
		int wordCount = faker.number().numberBetween(1, 20);
		int maxLength = faker.number().numberBetween(1, 80);
		String sentence = faker.lorem().sentence(wordCount);
		return sentence.substring(0, Math.min(maxLength - 1, sentence.length() - 1));
	}
	
	static Currency fromRequest(CreateCurrencyRequest request) {
		Currency currency = new Currency();
		currency.setCode(request.getCode());
		currency.setName(request.getName());
		return currency;
	}
	
	static Currency fromRequest(UpdateCurrencyRequest request) {
		Currency currency = new Currency();
		currency.setCode(request.getCode());
		currency.setName(request.getName());
		return currency;
	}
	
	static Currency fromRequestAndGeneratePersistenceFields(UpdateCurrencyRequest request) {
		Currency currency = fromRequest(request);
		currency = generatePersistenceFields(currency);
		return currency;
	}
	
	static CurrencyResponse toResponse(Currency currency) {
		CurrencyResponse response = new CurrencyResponse();
		response.setCode(currency.getCode());
		response.setName(currency.getName());
		response.setId(currency.getId());
		response.setStatus(currency.getStatus());
		response.setCreatedAt(currency.getCreatedAt());
		response.setCreatedBy(currency.getCreatedBy());
		response.setUpdatedAt(currency.getUpdatedAt());
		response.setUpdatedBy(currency.getUpdatedBy());
		response.setVersion(currency.getVersion());
		return response;
	}
	
}
