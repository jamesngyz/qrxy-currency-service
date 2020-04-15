package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyController_Update_IT {
	
	private final TestRestTemplate restTemplate;
	private final CurrencyRepository repository;
	
	@Autowired
	public CurrencyController_Update_IT(
			TestRestTemplate restTemplate,
			CurrencyRepository repository) {
		this.restTemplate = restTemplate;
		this.repository = repository;
	}
	
	@BeforeEach
	void deleteAllCurrencies() {
		repository.deleteAll();
	}
	
	@Test
	void updateCurrency_AllOk_Status200WithUpdatedBody() {
		CurrencyResponse createResponse = callPostCurrencyEndpoint();
		UUID id = createResponse.getId();
		
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.build();
		HttpEntity<UpdateCurrencyRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
		
		ResponseEntity<CurrencyResponse> result = restTemplate
				.exchange(
						"/v1/currencies/" + id.toString(),
						HttpMethod.PATCH,
						updateRequestEntity,
						CurrencyResponse.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody()).matches(codeAndNameEqualTo(updateRequest));
		assertThat(result.getBody()).matches(statusAndCreatedAtAndCreatedByEqualTo(createResponse));
		assertThat(result.getBody().getVersion()).isEqualTo(createResponse.getVersion() + 1);
	}
	
	private Predicate<CurrencyResponse> codeAndNameEqualTo(UpdateCurrencyRequest updateRequest) {
		return response -> response.getCode().equals(updateRequest.getCode())
				&& response.getName().equals(updateRequest.getName());
	}
	
	private Predicate<CurrencyResponse> statusAndCreatedAtAndCreatedByEqualTo(CurrencyResponse createResponse) {
		return response -> response.getStatus().equals(createResponse.getStatus())
				&& response.getCreatedAt().equals(createResponse.getCreatedAt())
				&& response.getCreatedBy().equals(createResponse.getCreatedBy());
	}
	
	@Test
	void updateCurrency_CodeOnly_Status200WithUpdatedBody() {
		CurrencyResponse createResponse = callPostCurrencyEndpoint();
		UUID id = createResponse.getId();
		
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withCodeOnly();
		HttpEntity<UpdateCurrencyRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
		
		ResponseEntity<CurrencyResponse> result = restTemplate.exchange(
				"/v1/currencies/" + id.toString(),
				HttpMethod.PATCH,
				updateRequestEntity,
				CurrencyResponse.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody().getCode()).isEqualTo(updateRequest.getCode());
		assertThat(result.getBody().getName()).isEqualTo(createResponse.getName());
		assertThat(result.getBody()).matches(statusAndCreatedAtAndCreatedByEqualTo(createResponse));
		assertThat(result.getBody().getVersion()).isEqualTo(createResponse.getVersion() + 1);
	}
	
	@Test
	void updateCurrency_NameOnly_Status200WithUpdatedBody() {
		CurrencyResponse createResponse = callPostCurrencyEndpoint();
		UUID id = createResponse.getId();
		
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withNameOnly();
		HttpEntity<UpdateCurrencyRequest> updateRequestEntity = new HttpEntity<>(updateRequest);
		
		ResponseEntity<CurrencyResponse> result = restTemplate.exchange(
				"/v1/currencies/" + id.toString(),
				HttpMethod.PATCH,
				updateRequestEntity,
				CurrencyResponse.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(id);
		assertThat(result.getBody().getCode()).isEqualTo(createResponse.getCode());
		assertThat(result.getBody().getName()).isEqualTo(updateRequest.getName());
		assertThat(result.getBody()).matches(statusAndCreatedAtAndCreatedByEqualTo(createResponse));
		assertThat(result.getBody().getVersion()).isEqualTo(createResponse.getVersion() + 1);
	}
	
	@Test
	void updateCurrency_CurrencyNotFound_Status404() {
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.build();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, request);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void updateCurrency_CodeShorterThan3_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withCodeShorterThan3();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_CodeLonger3_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withCodeLongerThan3();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_CodeNonAlphabetic_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withCodeNonAlphabetic();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_CodeNotUpperCase_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withCodeNotUpperCase();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_NameShorterThan1_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withNameShorterThan1();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_NameLongerThan80_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withNameLongerThan80();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void updateCurrency_NameWhitespaceOnly_Status400() {
		UUID id = createCurrencyAndGetId();
		UpdateCurrencyRequest updateRequest = FakeCurrency.UpdateRequest.withNameWhitespaceOnly();
		
		ResponseEntity<?> result = callPatchCurrencyEndpoint(id, updateRequest);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	private UUID createCurrencyAndGetId() {
		CurrencyResponse createResponse = callPostCurrencyEndpoint();
		return createResponse.getId();
	}
	
	private CurrencyResponse callPostCurrencyEndpoint() {
		CreateCurrencyRequest createRequest = FakeCurrency.CreateRequest.build();
		CurrencyResponse createResponse = restTemplate
				.postForEntity("/v1/currencies", createRequest, CurrencyResponse.class)
				.getBody();
		Objects.requireNonNull(createResponse);
		return createResponse;
	}
	
	private ResponseEntity<?> callPatchCurrencyEndpoint(UUID id, UpdateCurrencyRequest updateRequest) {
		return restTemplate.exchange(
				"/v1/currencies/" + id.toString(),
				HttpMethod.PATCH,
				new HttpEntity<>(updateRequest),
				Object.class);
	}
	
}
