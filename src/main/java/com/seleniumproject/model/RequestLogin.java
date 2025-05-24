package com.seleniumproject.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class RequestLogin {
    @JsonProperty("email")
        public String email;

        @JsonProperty("password")
        public String password;
        
        @JsonProperty("reg_id")
        public Integer reg_id;

        public RequestLogin() {
        }

        public RequestLogin(String email, String password, Integer reg_id) {
            this.email = email;
            this.password = password;
            this.reg_id = reg_id;
        }
}
