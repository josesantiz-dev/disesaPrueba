package net.giro.cargas.documentos.util;

import mx.gob.sat.cfdi.v33.catalogos.CTipoDeComprobante;

public enum TipoDeComprobante {
	INGRESO  { public String toString() { return CTipoDeComprobante.I.value(); }},
	EGRESO   { public String toString() { return CTipoDeComprobante.E.value(); }},
	TRASLADO { public String toString() { return CTipoDeComprobante.T.value(); }},
	NOMINA   { public String toString() { return CTipoDeComprobante.N.value(); }},
	PAGO     { public String toString() { return CTipoDeComprobante.P.value(); }};
	
    public String value() {
        return this.toString();
    }
    
	public static TipoDeComprobante fromString(String value) {
		for (TipoDeComprobante item : TipoDeComprobante.values()) {
			if (item.toString().equalsIgnoreCase(value) || item.name().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TipoDeComprobante fromOrdinal(int value) {
		for (TipoDeComprobante item : TipoDeComprobante.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
