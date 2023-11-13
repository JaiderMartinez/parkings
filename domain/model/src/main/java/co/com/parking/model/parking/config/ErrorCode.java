package co.com.parking.model.parking.config;

import lombok.Getter;

@Getter
public enum ErrorCode {
    B400000("B400-000", "Bad Request");

    private final String code;
    private final String log;

    ErrorCode(String code, String log) {
        this.code = code;
        this.log = log;
    }
}
