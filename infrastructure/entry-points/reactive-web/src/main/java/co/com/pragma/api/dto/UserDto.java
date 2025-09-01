package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(Long id,
                      String firstName,
                      String middleName,
                      String lastName,
                      String secondLastName,
                      String otherLastName,
                      LocalDate birthDate,
                      String address,
                      String phoneNumber,
                      String email,
                      BigDecimal baseSalary,
                      String documentNumber

) {

}
