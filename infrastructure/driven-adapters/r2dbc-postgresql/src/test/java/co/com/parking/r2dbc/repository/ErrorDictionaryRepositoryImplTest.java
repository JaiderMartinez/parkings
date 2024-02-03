package co.com.parking.r2dbc.repository;

import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.r2dbc.dao.ErrorDictionaryDao;
import co.com.parking.r2dbc.entities.ErrorDictionaryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ErrorDictionaryRepositoryImplTest {

    @Mock
    private ErrorDictionaryDao errorDictionaryDao;
    @InjectMocks
    private ErrorDictionaryRepositoryImpl errorDictionaryRepository;

    @Test
    void shouldFindByIdSuccessTest() {
        String codeError = ErrorCode.S204000.getCode();
        ErrorDictionaryEntity errorDictionary = new ErrorDictionaryEntity(codeError, "Not content", "Sin contenido", 204, ErrorCode.S204000.getLog());
        Mockito.when(errorDictionaryDao.findById(codeError)).thenReturn(Mono.just(errorDictionary));
        StepVerifier.create(errorDictionaryRepository.findById(codeError))
                .expectNextMatches(errorDictionaryResult -> errorDictionaryResult.getId().equals(codeError) &&
                        errorDictionaryResult.getMessage().equals(errorDictionary.getMessage()) &&
                        errorDictionaryResult.getHttpStatus().equals(errorDictionary.getHttpStatus()) &&
                        errorDictionaryResult.getMessageEn().equals(errorDictionary.getMessageEn()) &&
                        errorDictionaryResult.getMessageEs().equals(errorDictionary.getMessageEs()))
                .expectComplete()
                .verify();
    }
}