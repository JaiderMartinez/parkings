package co.com.parking.model.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String document;
    private String email;
    private String phone;
    private String password;
    private String locationX;
    private String locationY;
}
