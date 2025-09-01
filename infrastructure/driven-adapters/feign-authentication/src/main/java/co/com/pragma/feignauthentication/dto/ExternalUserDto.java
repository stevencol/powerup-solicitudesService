package co.com.pragma.feignauthentication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExternalUserDto(Long id,
                              @JsonProperty("firstName")
                              String firstName,
                              String middleName,

                              String lastName,
                              String secondLastName,
                              String otherLastName,
                              @JsonFormat(pattern = "yyyy-MM-dd")
                              LocalDate birthDate,
                              String address,
                              String phoneNumber,
                              String email,
                              BigDecimal baseSalary,
                              @JsonProperty("documentNumber")
                              String documentNumber

) {


}
