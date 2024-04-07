package in.cerpsoft.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponse {

    private String error;
    private String message;
    private HttpStatusCode status;
}
