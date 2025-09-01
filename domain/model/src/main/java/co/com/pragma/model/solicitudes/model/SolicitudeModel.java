package co.com.pragma.model.solicitudes.model;

import co.com.pragma.model.solicitudes.enums.LoanType;
import co.com.pragma.model.solicitudes.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudeModel {

    private Long id;
    private String userDocumentNumber;
    private BigDecimal mount;
    private Integer termInMonths;
    private LoanType loanType;
    private Status status;

}
