package co.com.parking.usecase.exceptions;

public class FailedTakeParkingSpaceException extends RuntimeException {

    public FailedTakeParkingSpaceException(String message) {
        super(message);
    }
}
