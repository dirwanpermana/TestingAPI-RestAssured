package com.seleniumproject.model.modelTugas_e2e;

import com.fasterxml.jackson.annotation.JsonProperty;

public class reqRegist {
    @JsonProperty("email")
    public String email;
    
    @JsonProperty("full_name")
    public String full_name;
    
    @JsonProperty("password")
    public String password;
    
    @JsonProperty("department")
    public String department;
    
    @JsonProperty("phone_number")
    public String phone_number;

    public reqRegist() {
        }

        public reqRegist(String email, String full_name, String password, String department, String phone_number) {
            this.email = email;
            this.full_name = full_name;
            this.password = password;
            this.department = department;
            this.phone_number = phone_number;
        }
}
