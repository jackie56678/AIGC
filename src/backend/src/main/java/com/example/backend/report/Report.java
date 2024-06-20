package com.example.backend.report;

import lombok.Data;

import java.util.Date;
@Data
public class Report {
    private Integer hid;
    private Integer uid;
    private Date date;
    private String url;
    private String result;
    private String suggestion;

}
