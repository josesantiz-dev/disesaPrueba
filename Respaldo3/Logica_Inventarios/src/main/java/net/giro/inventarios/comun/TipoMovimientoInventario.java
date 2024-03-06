package net.giro.inventarios.comun;

public enum TipoMovimientoInventario {
	ENTRADA,
	SALIDA;

	public static TipoMovimientoInventario fromOrdinal(int value) {
		for (TipoMovimientoInventario item : TipoMovimientoInventario.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
