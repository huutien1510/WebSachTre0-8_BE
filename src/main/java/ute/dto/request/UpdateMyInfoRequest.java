package ute.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateMyInfoRequest {
    String name;
    String sex;
    String phone;
    @JsonFormat(pattern = "dd/MM/yyyy")
    Date birthday;
    String avatar;
}
