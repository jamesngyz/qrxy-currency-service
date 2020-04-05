package com.jamesngyz.qrxy.currencyservice.currency;

import org.mapstruct.Mapper;

@Mapper
public interface CurrencyDtoMapper {
	
	Currency requestToCurrency(CurrencyRequest request);
	
	CurrencyResponse currencyToResponse(Currency currency);
	
}
