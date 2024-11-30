package ute.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ute.entity.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    Account account;
    String accessToken;
}
