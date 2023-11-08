package co.com.parking.usecase.exceptions;

import co.com.parking.usecase.utils.ErrorMessagesUtil;

public class ParkingNotFoundException extends RuntimeException {

    public ParkingNotFoundException() {
        super(ErrorMessagesUtil.PARKING_NOT_FOUND);
    }
}
