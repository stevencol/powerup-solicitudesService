package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    private Long id;
    @Column("user_document_number")
    private String userDocumentNumber;
    @Column("mount")
    private BigDecimal mount;
    @Column("term_in_months")
    private Integer termInMonths;
    @Column("loan_type")
    private String loanType;
    @Column("status")
    private String status;

}
