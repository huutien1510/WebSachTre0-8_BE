package ute.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // field khong xac dinh cu the se mac dinh la private
@JsonInclude(JsonInclude.Include.NON_NULL) // neu null thi ko kem vao json ->message
public class ApiResponse <T>{
    int code = 1000;
    String message;
    T data;
}
