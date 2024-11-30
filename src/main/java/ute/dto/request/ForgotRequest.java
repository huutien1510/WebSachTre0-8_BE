package ute.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ForgotRequest {
    String email;
}
