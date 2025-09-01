package co.com.pragma.model.solicitudes.enums;

public enum IdentifierType {

    ID("id"),
    DOCUMENT_NUMBER("documentNumber"),
    EMAIL("email"),
    TELEPHONE("telephone"),
    USERNAME("username");

    private final String value;

    IdentifierType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
