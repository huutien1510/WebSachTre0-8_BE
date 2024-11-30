package ute.dto.response;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AccountResponse {
    private String email;
    private String password;
    private String username;
    private String phone;
    private String sex;
    private Date birthday;

}
