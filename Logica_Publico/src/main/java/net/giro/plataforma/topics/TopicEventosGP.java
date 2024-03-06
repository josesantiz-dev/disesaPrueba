package net.giro.plataforma.topics;

public enum TopicEventosGP {
	/** Alta de Obra
	 * @value OB_AL
	 * @param target idObra
	 * @category BackOffice
	 */
	OBRA_ALTA { public String toString() { return "OB_AL"; }},
	
	/** Baja de Obra
	 * @value OB_BA
	 * @param target idObra
	 * @category BackOffice
	 */
	OBRA_BAJA { public String toString() { return "OB_BA"; }},
	
	/** Quitar Empleado de Obra(s)
	 * @value OB_EM
	 * @param target idEmpleado
	 * @param referencia idObra
	 * @category BackOffice
	 */
	OBRA_QUITAR_EMPLEADO { public String toString() { return "OB_EM"; }},
	
	/** Asignar Almacenes a Obra
	 * @value OBRA_ALMACENES
	 * @param target idObra
	 * @param referencia idAlmacenPrincipal
	 * @param atributos listado de almacenes (id)
	 * @category BackOffice
	 */
	OBRA_ASIGNAR_ALMACENES { public String toString() { return "OBRA_ALMACENES"; }},

	/** Actualizacion de montos en Cobranza
	 * @value COBRANZA_ACTUALIZAR
	 * @param target idObra
	 * @param atributos listado de items (ObraCobranza)
	 * @category BackOffice
	 */
	COBRANZA_ACTUALIZAR { public String toString() { return "COBRANZA_ACTUALIZAR"; }},
	
	/** Actualizacion de ubicacion de Facturas en Cobranza
	 * @value COBRANZA_UBICACION_PREVIA
	 * @param target idFactura
	 * @param referencia idObraActual
	 * @category BackOffice
	 */
	COBRANZA_UBICACION_PREVIA { public String toString() { return "COBRANZA_UBICACION_PREVIA"; }},
	
	/** Actualizaciones de datos posterior al cambio de estatus de la Explosion de Insumos
	 * @value INSUMOS_ESTATUS
	 * @param target idExplosionInsumos
	 * @category BackOffice
	 */
	INSUMOS_ESTATUS { public String toString() { return "INSUMOS_ESTATUS"; }},
	
	/** Actualizacion de Suministro en Explosion de Insumos
	 * @value EI_SU
	 * @param target idObra
	 * @param atributos suministros (lista Long:Double)
	 * @category BackOffice
	 */
	INSUMOS_SUMINISTRO { public String toString() { return "EI_SU"; }},

	/** Traspasar Suministrados de una Explosion de Insumos a otra
	 * @value EI_AS
	 * @param target idInsumosActual
	 * @param referencia idInsumosPrevia
	 * @category BackOffice
	 */
	INSUMOS_TRASPASAR_SUMINISTROS { public String toString() { return "EI_AS"; }};
	
	public static TopicEventosGP fromString(String value) {
		for (TopicEventosGP item : TopicEventosGP.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosGP fromOrdinal(int value) {
		for (TopicEventosGP item : TopicEventosGP.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
