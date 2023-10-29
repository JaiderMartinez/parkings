package co.com.parking.controllers.configuration.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Recurso no encontrado");
    }
}
