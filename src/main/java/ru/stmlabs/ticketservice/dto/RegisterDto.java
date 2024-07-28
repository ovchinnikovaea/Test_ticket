package ru.stmlabs.ticketservice.dto;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegisterDto {
    private String username;
    private String password;
    private String fullName;
}