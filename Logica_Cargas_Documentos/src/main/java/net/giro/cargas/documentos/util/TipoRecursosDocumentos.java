package net.giro.cargas.documentos.util;

public enum TipoRecursosDocumentos {
	Todos    { public String toString() { return "DOC"; }},
	Procesos { public String toString() { return "PR"; }},
	Formatos { public String toString() { return "FO"; }},
	DPs      { public String toString() { return "DP"; }};
	
	public static TipoRecursosDocumentos fromString(String value) {
		for (TipoRecursosDocumentos item : TipoRecursosDocumentos.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TipoRecursosDocumentos fromOrdinal(int value) {
		for (TipoRecursosDocumentos item : TipoRecursosDocumentos.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
