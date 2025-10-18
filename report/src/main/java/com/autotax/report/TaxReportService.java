package com.autotax.report;

import com.autotax.application.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxReportService {

    private final TaxService taxService;

    public String generateTaxpayerSummaryReport(String taxpayerId) {
        return "Taxpayer Summary Report for: " + taxpayerId + "\n" +
                "--------------------------------------------------\n" +
                "--------------------------------------------------\n";
    }
}
