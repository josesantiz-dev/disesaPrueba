package net.giro.inventarios.comun;

public enum TipoTraspaso {
	/** 0 - Sin clasificacion */
	Ninguno,
	/** 1 - Traspaso normal: Almacen a Almacen/Bodega */
	TRASPASO,
	/** 2 - Devolucion: Bodega a Almacen */
	DEVOLUCION,
	/** 3- Traspaso especial de Solicitud a Bodega */
	SOLICITUD_BODEGA;

	public static TipoTraspaso fromOrdinal(int value) {
		for (TipoTraspaso item : TipoTraspaso.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
