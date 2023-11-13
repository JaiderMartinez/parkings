package co.com.parking.model.parking.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDictionary {

    private String id;
    private String messageEn;
    private String messageEs;
    private Integer httpStatus;
    private String message;
}
