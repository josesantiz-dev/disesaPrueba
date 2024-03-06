package net.giro.plataforma.seguridad;

import java.io.Serializable;

public enum TipoPermiso implements Serializable {
	Ninguno(0),
	Leer(1),
	Escribir(3),
	Borrar(5),
	Todos(7); 
	
	private final int constValue;
	private TipoPermiso(int value) {
		this.constValue = value;
	}
	
	public int value() {
		return constValue;
	}

	public static TipoPermiso fromValue(int value) {
		for (TipoPermiso item : TipoPermiso.values()) {
			if (value == item.value())
				return item;
		}
		return null;
	}

	public static TipoPermiso fromOrdinal(int value) {
		for (TipoPermiso item : TipoPermiso.values()) {
			if (value == item.ordinal())
				return item;
		}
		return null;
	}

	public static TipoPermiso fromName(String value) {
		for (TipoPermiso item : TipoPermiso.values()) {
			if (item.name().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
}
