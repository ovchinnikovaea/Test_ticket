package ru.stmlabs.ticketservice.dto;

/* Здесь должна быть хорошая авторизация. И будет. Наверно.*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDto {
    // Dto для авторизации пользователя

    @JsonProperty("login") // имя пользователя
    private String username;

    @JsonProperty("password") // пароль

    private String password;



}
