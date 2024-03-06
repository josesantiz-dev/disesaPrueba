package net.giro.plataforma.topics;

public enum TopicEventosRH {
	/** Alta de Empleado
	 * @value EMPLEADO_ALTA
	 * @param target idEmpleado
	 * @param referencia idContrato
	 * @category BackOffice
	 */
	EMPLEADO_ALTA { public String toString() { return "EMPLEADO_ALTA";  }},
	
	/** Alta Retroactiva de Empleado en Asistencia
	 * @value ALTA_RETROACTIVA
	 * @param target idEmpleado
	 * @param referencia idObra
	 * @param fecha fechaBase
	 * @category BackOffice
	 */
	EMPLEADO_ALTA_RETROACTIVA { public String toString() { return "ALTA_RETROACTIVA"; }},
	
	/** Baja de Empleado
	 * @value EMPLEADO_BAJA
	 * @param target idEmpleado
	 * @param referencia idSolicitado
	 * @category BackOffice
	 */
	EMPLEADO_BAJA { public String toString() { return "EMPLEADO_BAJA";  }},
	
	/** Finiquito
	 * @value FINIQUITO
	 * @param target idEmpleado
	 * @param referencia idFiniquito
	 * @category BackOffice
	 */
	FINIQUITO { public String toString() { return "FINIQUITO"; }},
	
	/** Generar Nomina Semanal
	 * @value NOMINA07
	 * @param target idNominaEstatus
	 * @param referencia nominaPreliminar
	 * @category BackOffice
	 */
	NOMINA_SEMANAL { public String toString() { return "NOMINA07"; }},
	
	/** Generar Nomina Quincenal
	 * @value NOMINA15
	 * @param target idNominaEstatus
	 * @param referencia nominaPreliminar
	 * @category BackOffice
	 */
	NOMINA_QUINCENAL { public String toString() { return "NOMINA15"; }},
	
	/** Cancelar Contratos vencidos a la fecha
	 * @value CONTRATOS_VENCIDOS
	 * @param target fecha
	 * @category BackOffice
	 */
	CONTRATOS_VENCIDOS { public String toString() { return "CONTRATOS_VENCIDOS"; }},
	
	/** Actualiza datos de Contratos anteriores
	 * @value CONTRATOS_NUEVO
	 * @param target idEmpleado
	 * @param referencia idContrato
	 * @category BackOffice
	 */
	CONTRATOS_NUEVO { public String toString() { return "CONTRATOS_NUEVO"; }},
	
	/** Actualiza el estatus de Empleado a Incapacitado
	 * @value EMPLEADOS_INCAPACIDAD
	 * @param target idIncapacidad
	 * @category BackOffice
	 */
	EMPLEADOS_INCAPACIDAD { public String toString() { return "EMPLEADOS_INCAPACIDAD"; }},
	
	/** Deshabilita/Cancela incapacidades de Empleados
	 * @value EMPLEADOS_CANCELAR_INCAPACIDAD
	 * @param target idIncapacidad
	 * @category BackOffice
	 */
	EMPLEADOS_CANCELAR_INCAPACIDAD { public String toString() { return "EMPLEADOS_CANCELAR_INCAPACIDAD"; }},
	
	/** Deshabilita/Cancela incapacidades de Empleados
	 * @value EMPLEADOS_CANCELAR_INCAPACIDADES
	 * @param target fecha
	 * @category BackOffice
	 */
	EMPLEADOS_CANCELAR_INCAPACIDADES { public String toString() { return "EMPLEADOS_CANCELAR_INCAPACIDADES"; }},
	
	/** Genera Asistencia en dia festivo indicado
	 * @value ASISTENCIA_DIA_FESTIVO
	 * @param target fecha
	 * @category BackOffice
	 */
	ASISTENCIA_DIA_FESTIVO { public String toString() { return "ASISTENCIA_DIA_FESTIVO"; }},
	
	/** Cancela Negociaciones de dias festivos
	 * @value CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS
	 * @param target fecha
	 * @param referencia idObra
	 * @category BackOffice
	 */
	CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS { public String toString() { return "CANCELAR_NEGOCIACIONES_DIAS_FESTIVOS"; }};
	
	public static TopicEventosRH fromString(String value) {
		for (TopicEventosRH item : TopicEventosRH.values()) {
			if (item.toString().equalsIgnoreCase(value))
				return item;
		}
		return null;
	}
	
	public static TopicEventosRH fromOrdinal(int value) {
		for (TopicEventosRH item : TopicEventosRH.values()) {
			if (item.ordinal() == value)
				return item;
		}
		return null;
	}
}
