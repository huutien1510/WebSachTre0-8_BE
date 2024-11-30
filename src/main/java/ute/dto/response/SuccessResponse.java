package ute.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class SuccessResponse<T> {
    private Boolean success = true;
    private int status = 200;
    private String message;
    private T data;

}
