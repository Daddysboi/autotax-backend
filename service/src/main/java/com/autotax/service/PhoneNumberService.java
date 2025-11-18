package com.autotax.service;

public interface PhoneNumberService {

    String formatPhoneNumber(String phoneNumberWithIntlDialingCode);

    String formatPhoneNumberWithoutIntlDialingCode(String phoneNumber, String countryAlpha2);

    boolean isValid(String value, String region);
}
