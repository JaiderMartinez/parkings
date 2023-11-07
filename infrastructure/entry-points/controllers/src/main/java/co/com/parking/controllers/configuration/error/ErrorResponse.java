package co.com.parking.controllers.configuration.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ErrorResponse {

    private String exceptionName;
    private String controllerErrorOrigin;
    private String methodName;
    private String description;
    private String message;
    private Integer statusCode;
}
