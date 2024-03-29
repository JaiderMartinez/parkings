package co.com.parking.model.parking.config;

import lombok.Getter;

@Getter
public enum ErrorCode {
    S204000("S204-000", "Not content"),
    B400000("B400-000", "Bad Request"),
    B401000("B401-000", "Unauthorized"),
    F404000("F404-000", "Not found"),
    C409000("C409-000", "Conflict"),
    B500000("B500-000", "Internal Server");

    private final String code;
    private final String log;

    ErrorCode(String code, String log) {
        this.code = code;
        this.log = log;
    }
}
