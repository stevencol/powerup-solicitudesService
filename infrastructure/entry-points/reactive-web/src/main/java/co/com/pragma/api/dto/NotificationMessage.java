package co.com.pragma.api.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationMessage {

    private String solicitudId;
    private String estado;
    private String email;
    private String nombre;
    private double valorCredito;

}
