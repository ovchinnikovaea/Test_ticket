package ru.stmlabs.ticketservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import ru.stmlabs.ticketservice.entity.Role;

@Data
@Getter
public class RegisterDto {
    @Size(min = 4, max = 32)
    @JsonProperty("username")
    private String username;
    @Size(min = 8, max = 16)
    @JsonProperty("password")
    private String password;
    @Size(min = 2, max = 50)
    @JsonProperty("firstName")
    private String fullName;
    @JsonProperty("role")
    private Role role;
}