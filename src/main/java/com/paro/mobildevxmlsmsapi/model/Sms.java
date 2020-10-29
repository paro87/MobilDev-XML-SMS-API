package com.paro.mobildevxmlsmsapi.model;

import lombok.Data;

@Data
public class Sms {
    private String message;
    private String numbers;

    //In case if we will be receiving multiple separate String objects - numbers, inserted to the separated fields
    //Make sure that you uncommented related section in the SmsApiService - postSmsToMany
    //private List<String> numbers;
}
