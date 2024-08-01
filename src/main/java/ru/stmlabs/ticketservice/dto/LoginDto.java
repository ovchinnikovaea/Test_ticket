package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDto {

    @JsonProperty("login")
    private String username;

    @JsonProperty("password")
    private String password;



}
