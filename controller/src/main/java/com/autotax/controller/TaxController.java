package com.autotax.controller;

import com.autotax.dao.TaxDto;
import com.autotax.service.TaxService;
import com.autotax.domain.Tax;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @GetMapping
    public List<TaxDto> getAllTaxes() {
        return taxService.getAllTaxes().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxDto> getTaxById(@PathVariable Long id) {
        return taxService.getTaxById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaxDto> createTax(@RequestBody TaxDto taxDto) {
        Tax tax = convertToEntity(taxDto);
        Tax savedTax = taxService.saveTax(tax);
        return new ResponseEntity<>(convertToDto(savedTax), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxDto> updateTax(@PathVariable Long id, @RequestBody TaxDto taxDto) {
        return taxService.getTaxById(id)
                .map(existingTax -> {
                    existingTax.setTaxpayerId(taxDto.getTaxpayerId());
                    existingTax.setTaxType(taxDto.getTaxType());
                    existingTax.setAmount(taxDto.getAmount());
                    existingTax.setTaxDate(taxDto.getTaxDate());
                    existingTax.setStatus(taxDto.getStatus());
                    Tax updatedTax = taxService.saveTax(existingTax);
                    return ResponseEntity.ok(convertToDto(updatedTax));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        taxService.deleteTax(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/taxpayer/{taxpayerId}")
//    public List<TaxDto> getTaxesByTaxpayerId(@PathVariable String taxpayerId) {
//        return taxService.getTaxesByTaxpayerId(taxpayerId).stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    private TaxDto convertToDto(Tax tax) {
        return new TaxDto(tax.getId(), tax.getTaxpayerId(), tax.getTaxType(), tax.getAmount(), tax.getTaxDate(), tax.getStatus());
    }

    private Tax convertToEntity(TaxDto taxDto) {
        return new Tax(taxDto.getId(), taxDto.getTaxpayerId(), taxDto.getTaxType(), taxDto.getAmount(), taxDto.getTaxDate(), taxDto.getStatus());
    }
}
