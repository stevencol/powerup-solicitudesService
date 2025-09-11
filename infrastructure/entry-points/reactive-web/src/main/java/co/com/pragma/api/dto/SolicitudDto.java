package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SolicitudDto(

        Long id,
        @JsonProperty("user")
        UserDto userDto,
        @Positive
        @NotNull
        BigDecimal mount,
        @Positive
        Integer termInMonths,
        String loanType,
        String status,
        double interestRate,
        Boolean validationAutomatic

) {
}
