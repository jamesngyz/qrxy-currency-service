package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
}
