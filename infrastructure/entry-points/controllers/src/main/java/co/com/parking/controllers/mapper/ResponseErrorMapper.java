package co.com.parking.controllers.mapper;

import co.com.parking.controllers.dto.response.ResponseErrorDto;
import co.com.parking.model.parking.config.ErrorDictionary;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseErrorMapper {

    public static ResponseErrorDto toResponseErrorDto(ErrorDictionary errorDictionary) {
        return ResponseErrorDto.builder()
                .code(errorDictionary.getId())
                .messageEs(errorDictionary.getMessageEs())
                .messageEn(errorDictionary.getMessageEn())
                .build();
    }
}
