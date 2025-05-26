package com.seleniumproject.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class responseRegister {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("department")
    private String department;

    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("create_at")
    private String createAt;

    @JsonProperty("update_at")
    private String updateAt;
}
