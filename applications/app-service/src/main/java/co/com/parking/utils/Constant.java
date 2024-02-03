package co.com.parking.utils;

public class Constant {

    private Constant() {}

    public static final String[] PATH_SWAGGER = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v3/api-docs/**"};
    public static final String KEY_TOKEN = "token";
    public static final String PREFIX_TOKEN = "Bearer";
    public static final String LOG_ERROR_AUTHENTICATE_USER = "Error al autenticarse el usuario {}";
}
