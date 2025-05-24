package com.seleniumproject.model.modelTugas_e2e;

import com.fasterxml.jackson.annotation.JsonProperty;

public class reqLogin {
    @JsonProperty("email")
    public String email;

    @JsonProperty("password")
    public String password;

     public reqLogin() {
        }

        public reqLogin(String email, String password) {
            this.email = email;
            this.password = password;
        }
}