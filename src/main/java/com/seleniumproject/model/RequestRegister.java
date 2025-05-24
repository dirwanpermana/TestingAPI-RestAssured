package com.seleniumproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestRegister {
    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;
        
    @JsonProperty("password")
    public String password;

    @JsonProperty("password_confirmation")
    public String password_confirmation;
    
    @JsonProperty("phone")
    public String phone;

        public RequestRegister() {
        }

        public RequestRegister(String name, String email, String password, String password_confirmation, String phone) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.password_confirmation = password_confirmation;
            this.phone = phone;
        }
}
