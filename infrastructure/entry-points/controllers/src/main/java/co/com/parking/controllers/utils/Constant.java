package co.com.parking.controllers.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constant {

    public static final String BODY_DEFAULT = "error";
    public static final String LOG_CONTROLLER_ADVICE = "Error message : {}";
    public static final String KEY_HEADER_MESSAGE = "message";
    public static final List<String> HTTP_VERBS = List.of("POST", "GET", "PATCH", "PUT", "OPTIONS", "DELETE");
    public static final String MESSAGE_ES_ERROR_DEFAULT = "Ha ocurrido un error en el sistema, por favor contacte al administrador";
    public static final String MESSAGE_EN_ERROR_DEFAULT = "An error has occurred in the system, please contact the administrator";
    public static final String PATH_CONFIGURE_CORS_PERMIT = "/**";
    public static final String DELIMITER = ",";
}
