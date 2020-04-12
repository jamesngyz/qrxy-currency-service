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
		CreateCurrencyRequest createRequest = FakeCurrency.CreateRequest.build();
		ResponseEntity<CurrencyResponse> createResponse = restTemplate
				.postForEntity("/v1/currencies", createRequest, CurrencyResponse.class);
		
		Objects.requireNonNull(createResponse.getBody());
		UUID id = createResponse.getBody().getId();
		
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
		assertThat(result.getBody()).matches(idAndCodeAndNameEqualTo(id, updateRequest));
	}
	
	private Predicate<CurrencyResponse> idAndCodeAndNameEqualTo(UUID id, UpdateCurrencyRequest updateRequest) {
		return response -> response.getId().equals(id)
				&& response.getCode().equals(updateRequest.getCode())
				&& response.getName().equals(updateRequest.getName());
	}
	
}
