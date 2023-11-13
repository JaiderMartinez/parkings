package co.com.parking.r2dbc;

import co.com.parking.model.parking.config.ErrorDictionary;
import co.com.parking.model.parking.gateways.ErrorDictionaryGateway;
import co.com.parking.r2dbc.dao.ErrorDictionaryDao;
import co.com.parking.r2dbc.mapper.ErrorDictionaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ErrorDictionaryRepositoryImpl implements ErrorDictionaryGateway {

    private final ErrorDictionaryDao errorDictionaryDao;

    @Override
    public Mono<ErrorDictionary> findById(String id) {
        return errorDictionaryDao.findById(id)
                .flatMap(errorDictionaryEntity -> {
                    log.info(errorDictionaryEntity.getId());
                    return Mono.just(ErrorDictionaryMapper.toModel(errorDictionaryEntity));
                })
                .retry(3)
                .onErrorResume(throwable -> {
                    log.error("Error during findById: {}", throwable.getMessage());
                    return Mono.empty();  // Puedes ajustar esto según tus necesidades
                });
    }
}
