package net.giro.comparativa;

public enum CTipoDeComprobante {
    I,
    E,
    T,
    N,
    P;

    public String value() {
        return name();
    }

    public static CTipoDeComprobante fromValue(String v) {
        return valueOf(v);
    }
}
