package co.com.parking.utils;

import co.com.parking.config.security.User;
import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ParkingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class JwtProviderUtil {

    public static User getPayload(String token) {
        String payload = token.split("\\.")[1];
        byte[] decodedPayloadBytes = Base64.getDecoder().decode(payload);
        String decodedPayload = new String(decodedPayloadBytes);
        try {
            return new ObjectMapper().readValue(decodedPayload, User.class);
        } catch (JsonProcessingException e) {
            throw new ParkingException(ErrorCode.B401000);
        }
    }
}
