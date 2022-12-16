package com.b.ignited.pidemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainResponseObject {

    private String k;
    private String status;
    private int p;

}
