package com.paro.mobildevxmlsmsapi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ReportDateRange {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date LDate;
}
