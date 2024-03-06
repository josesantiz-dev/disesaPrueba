package net.giro.plataforma.topics;

import java.io.Serializable;

public enum Meses implements Serializable {
	ENERO { public String toString() { return "Enero"; }},
	FEBRERO { public String toString() { return "Febrero"; }},
	MARZO { public String toString() { return "Marzo"; }},
	ABRIL { public String toString() { return "Abril"; }},
	MAYO { public String toString() { return "Mayo"; }},
	JUNIO { public String toString() { return "Junio"; }},
	JULIO { public String toString() { return "Julio"; }},
	AGOSTO { public String toString() { return "Agosto"; }},
	SEPTIEMBRE { public String toString() { return "Septiembre"; }},
	OCTUBRE { public String toString() { return "Octubre"; }},
	NOVIEMBRE { public String toString() { return "Noviembre"; }},
	DICIEMBRE { public String toString() { return "Diciembre"; }};
	
	// Recupera el valor del enumerados a traves del nombre
	public static Meses fromString(String value) {
		for (Meses item : Meses.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	// Recupera el valor del enumerador a traves del indice
	public static Meses fromOrdinal(int value) {
		for (Meses item : Meses.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
