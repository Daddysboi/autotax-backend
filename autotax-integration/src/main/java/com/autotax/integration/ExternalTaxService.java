package com.autotax.integration;

import com.autotax.domain.Tax;

import java.util.List;

public interface ExternalTaxService {
    List<Tax> fetchExternalTaxes(String taxpayerId);
}
