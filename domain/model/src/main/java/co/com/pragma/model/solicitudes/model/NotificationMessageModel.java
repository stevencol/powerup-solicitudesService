package co.com.pragma.model.solicitudes.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NotificationMessageModel {

    private Long solicitudId;
    private String estado;
    private String email;
    private String nombre;
    private BigDecimal valorCredito;

}
