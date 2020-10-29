package com.paro.mobildevxmlsmsapi.model;

import lombok.Data;

import java.util.List;

@Data
public class MultiSenderWrapper {
    private List<SmsMultiSenders> multiSendersList;
}
