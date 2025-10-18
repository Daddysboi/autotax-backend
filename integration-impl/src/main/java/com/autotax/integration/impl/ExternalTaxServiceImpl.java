package com.autotax.integration.impl;

import com.autotax.domain.Tax;
import com.autotax.integration.ExternalTaxService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ExternalTaxServiceImpl implements ExternalTaxService {

    @Override
    public List<Tax> fetchExternalTaxes(String taxpayerId) {
        // Placeholder for actual external API call
        // In a real scenario, this would call an external service to fetch tax data
        System.out.println("Fetching external taxes for taxpayer: " + taxpayerId);
        return Arrays.asList(
                new Tax(null, taxpayerId, "VAT", new BigDecimal("100.00"), LocalDate.now().minusMonths(1), "PAID"),
                new Tax(null, taxpayerId, "Income Tax", new BigDecimal("500.00"), LocalDate.now().minusMonths(2), "PENDING")
        );
    }
}
