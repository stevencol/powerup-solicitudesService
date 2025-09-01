package co.com.pragma.model.solicitudes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    Long id;
    String firstName;
    String middleName;
    String lastName;
    String secondLastName;
    String otherLastName;
    LocalDate birthDate;
    String address;
    String phoneNumber;
    String email;
    BigDecimal baseSalary;
    String documentNumber;
}
