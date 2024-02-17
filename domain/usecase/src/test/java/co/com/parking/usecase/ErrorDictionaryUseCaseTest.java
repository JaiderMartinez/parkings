package co.com.parking.usecase;

import co.com.parking.model.parking.config.ErrorCode;
import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.model.parking.gateways.ErrorDictionaryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorDictionaryUseCaseTest {

    @Mock
    private ErrorDictionaryGateway errorDictionaryGateway;
    @InjectMocks
    private ErrorDictionaryUseCase errorDictionaryUseCase;

    @Test
    void shouldFindByIdSuccessTest() {
        String codeError = ErrorCode.B400000.getCode();
        ErrorDictionary errorDictionaryExpected = ErrorDictionary.builder()
                .id(codeError)
                .statusCode(400)
                .messageEn("Bad request")
                .messageEs("Mala peticion")
                .message(ErrorCode.B400000.getLog())
                .build();
        when(errorDictionaryGateway.findById(codeError)).thenReturn(Mono.just(errorDictionaryExpected));
        StepVerifier.create(errorDictionaryUseCase.findById(codeError))
                .expectSubscription()
                .expectNext(errorDictionaryExpected)
                .verifyComplete();
    }
}