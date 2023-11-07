package co.com.parking.usecase.exceptions;

import static co.com.parking.usecase.utils.ErrorMessagesUtil.PARKING_SPACE_NOT_FOUND;

public class ParkingSpaceNotFoundException extends RuntimeException {

    public ParkingSpaceNotFoundException() {
        super(PARKING_SPACE_NOT_FOUND);
    }
}
