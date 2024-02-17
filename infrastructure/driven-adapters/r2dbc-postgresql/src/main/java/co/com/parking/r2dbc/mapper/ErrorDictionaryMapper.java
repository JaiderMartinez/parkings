package co.com.parking.r2dbc.mapper;

import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.r2dbc.entities.ErrorDictionaryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorDictionaryMapper {

    public static ErrorDictionary toModel(ErrorDictionaryEntity errorDictionaryEntity) {
        return ErrorDictionary.builder()
                .id(errorDictionaryEntity.getId())
                .statusCode(errorDictionaryEntity.getHttpStatus())
                .message(errorDictionaryEntity.getMessage())
                .messageEn(errorDictionaryEntity.getMessageEn())
                .messageEs(errorDictionaryEntity.getMessageEs())
                .build();
    }
}
