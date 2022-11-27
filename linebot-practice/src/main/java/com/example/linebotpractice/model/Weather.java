package com.example.linebotpractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    private String success;
    private String result;
    private String records;
    private String location;

}
