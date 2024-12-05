package ute.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest {
    private String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    private String username;
    private String name;
    private String phone;
    private String sex;
    private Date birthday;
    private Boolean is_active = false;
    private Boolean is_admin = false;
    private Set<String> roles;
    private Integer bonusPoint = 0;
    private String avatar = "https://1.bp.blogspot.com/-CV8fOXMMw60/YZ-UJ4X9sAI/AAAAAAAACMc/2Svet97exjgNdJ9CeTKUU3OuA-mnCQEzwCLcBGAsYHQ/s595/3a.jpg";
}
