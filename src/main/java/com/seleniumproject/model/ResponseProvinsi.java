package com.seleniumproject.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseProvinsi {
    @JsonProperty("status")
    public boolean status;

    @JsonProperty("message")
    public String message;

    @JsonProperty("data")
    public List<Province> data;

    public static class Province {
        @JsonProperty("id")
        public int id;

        @JsonProperty("name")
        public String name;
}
}
