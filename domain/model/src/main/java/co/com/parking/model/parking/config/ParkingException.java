package co.com.parking.model.parking.config;

import lombok.Getter;

@Getter
public class ParkingException extends RuntimeException {

    private final ErrorCode error;

    public ParkingException(ErrorCode errorCode) {
        super(errorCode.getLog());
        this.error = errorCode;
    }
}
