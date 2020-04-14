package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.Optional;
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
		generatePersistenceFields(currency);
		return currency;
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
	
	private static void generatePersistenceFields(Currency currency) {
		currency.setCreatedAt(faker.date().birthday());
		currency.setCreatedBy(faker.name().firstName());
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		currency.setUpdatedAt(currency.getCreatedAt());
		currency.setUpdatedBy(currency.getCreatedBy());
		currency.setVersion(0);
	}
	
	private static Currency fromRequest(CreateCurrencyRequest request) {
		Currency currency = new Currency();
		currency.setCode(request.getCode());
		currency.setName(request.getName());
		return currency;
	}
	
	static Currency fromRequestAndGeneratePersistenceFields(CreateCurrencyRequest request) {
		Currency currency = fromRequest(request);
		generatePersistenceFields(currency);
		return currency;
	}
	
	static Currency fromInitialThenUpdate(Currency initial, UpdateCurrencyRequest request) {
		Currency currency = new Currency();
		currency.setCode(initial.getCode());
		currency.setName(initial.getName());
		currency.setId(initial.getId());
		currency.setStatus(initial.getStatus());
		currency.setCreatedAt(initial.getCreatedAt());
		currency.setCreatedBy(initial.getCreatedBy());
		currency.setUpdatedAt(initial.getUpdatedAt());
		currency.setUpdatedBy(initial.getUpdatedBy());
		currency.setVersion(initial.getVersion());
		
		Optional.ofNullable(request.getCode()).ifPresent(currency::setCode);
		Optional.ofNullable(request.getName()).ifPresent(currency::setName);
		return currency;
	}
	
	static class CreateRequest {
		static CreateCurrencyRequest build() {
			String code = generateCurrencyCode();
			String name = generateCurrencyName();
			
			CreateCurrencyRequest request = new CreateCurrencyRequest();
			request.setCode(code);
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withCodeShorterThan3() {
			CreateCurrencyRequest request = CreateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(0, 3).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeLongerThan3() {
			CreateCurrencyRequest request = CreateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(4, 21).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNonAlphabetic() {
			CreateCurrencyRequest request = CreateRequest.build();
			String code = RandomStringUtils.randomNumeric(3);
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNotUpperCase() {
			CreateCurrencyRequest request = CreateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(3).toLowerCase();
			request.setCode(code);
			return request;
		}
		
		static CreateCurrencyRequest withCodeNull() {
			CreateCurrencyRequest request = CreateRequest.build();
			request.setCode(null);
			return request;
		}
		
		static CreateCurrencyRequest withNameShorterThan1() {
			CreateCurrencyRequest request = CreateRequest.build();
			String name = RandomStringUtils.randomAlphabetic(0, 1).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameLongerThan80() {
			CreateCurrencyRequest request = CreateRequest.build();
			String name = RandomStringUtils.randomAlphabetic(81, 201).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameWhitespaceOnly() {
			CreateCurrencyRequest request = CreateRequest.build();
			int length = faker.number().numberBetween(1, 80);
			String name = RandomStringUtils.random(length, " ");
			request.setName(name);
			return request;
		}
		
		static CreateCurrencyRequest withNameNull() {
			CreateCurrencyRequest request = CreateRequest.build();
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
		
		static UpdateCurrencyRequest withCodeOnly() {
			String code = generateCurrencyCode();
			UpdateCurrencyRequest request = new UpdateCurrencyRequest();
			request.setCode(code);
			return request;
		}
		
		static UpdateCurrencyRequest withNameOnly() {
			String name = generateCurrencyName();
			UpdateCurrencyRequest request = new UpdateCurrencyRequest();
			request.setName(name);
			return request;
		}
		
		static UpdateCurrencyRequest withCodeShorterThan3() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(0, 3).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static UpdateCurrencyRequest withCodeLongerThan3() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(4, 21).toUpperCase();
			request.setCode(code);
			return request;
		}
		
		static UpdateCurrencyRequest withCodeNonAlphabetic() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String code = RandomStringUtils.randomNumeric(3);
			request.setCode(code);
			return request;
		}
		
		static UpdateCurrencyRequest withCodeNotUpperCase() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String code = RandomStringUtils.randomAlphabetic(3).toLowerCase();
			request.setCode(code);
			return request;
		}
		
		static UpdateCurrencyRequest withNameShorterThan1() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String name = RandomStringUtils.randomAlphabetic(0, 1).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static UpdateCurrencyRequest withNameLongerThan80() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			String name = RandomStringUtils.randomAlphabetic(81, 201).toUpperCase();
			request.setName(name);
			return request;
		}
		
		static UpdateCurrencyRequest withNameWhitespaceOnly() {
			UpdateCurrencyRequest request = UpdateRequest.build();
			int length = faker.number().numberBetween(1, 80);
			String name = RandomStringUtils.random(length, " ");
			request.setName(name);
			return request;
		}
	}
	
	static class Response {
		static CurrencyResponse fromCurrency(Currency currency) {
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
	
}
