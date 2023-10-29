package co.com.parking.controllers.configuration.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    NOT_FOUND("404");

    private final String code;
}
