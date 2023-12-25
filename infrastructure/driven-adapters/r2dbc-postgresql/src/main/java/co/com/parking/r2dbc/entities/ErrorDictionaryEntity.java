package co.com.parking.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "error_dictionary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDictionaryEntity {

    @Id
    private String id;
    private String messageEn;
    private String messageEs;
    private Integer httpStatus;
    private String message;
}
