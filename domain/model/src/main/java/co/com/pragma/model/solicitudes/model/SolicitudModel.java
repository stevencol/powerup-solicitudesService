package co.com.pragma.model.solicitudes.model;

import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SolicitudModel {

    private Long id;
    private String userDocumentNumber;
    private BigDecimal mount;
    private Integer termInMonths;
    private LoanType loanType;
    private Status status;
    private double interestRate;
    private boolean validationAutomatic;


}
