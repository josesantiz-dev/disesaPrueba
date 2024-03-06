package net.izel.framework.util.swt;

import java.util.HashMap;

public class Errores 
{	
	/*TRANSACCION EXITOSA*/
	public static final long OK = 0;
	public static final long CAMPOS_REQUERIDOS= 1;
	public static final long CAMPOS_REQUERIDOS_IDE = 2;
	public static final long CONSULTA_SIN_RESULTADOS = 3;
	
	public static final long ERROR_CONSULTAR_DATOS_PRESTAMO = 113;
	public static final long ERROR_PAGO_PRESTAMO = 114;
	public static final long ERROR_OBTENER_CONCEP_OPERACION_XGRPOCLAVE= 115; //falta
	public static final long ERROR_BUSCAR_CUENTA= 116; //falta
	public static final long ERROR_CONSULTA_MOVIMIENTO= 117;
	public static final long ERROR_CONSULTA_SALDO= 118;
	public static final long ERROR_NOTA_CARGO_GEN= 119; //falta
	public static final long ERROR_NOTA_ABONO_GEN= 120; //falta
	public static final long ERROR_TRANSF_PROPIAS_GEN= 121; //falta	
	public static final long ERROR_CONSULTA_CARTA_INSTRUCTIVA= 122; //falta
	public static final long ERROR_TRANSACCION_CARTA_INSTRUCTIVA= 123; //falta
	public static final long ERROR_GRUPO_VALOR= 124; //falta
	public static final long ERROR_GRUPO_VALOR_ESTATUS= 125; //falta
	
	public static final long ERROR_OBTENER_CONTEXTO = 100001;
	public static final long ERROR_OBTENER_EJB = 100002;
	public static final long ERROR_PROCESO = 100003;
	public static final long ERROR_EXCEPTION = 100004;
	
	
	
		
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	
	/** Constructors */
	public Errores() 
	{	
	}
	
	
	static {
		descError.put(OK,  "TRANSACCION EXITOSA");
		descError.put(CAMPOS_REQUERIDOS,  "Campo requerido %s");
		descError.put(CAMPOS_REQUERIDOS_IDE,  "Campo requerido identificador de %s");
		descError.put(CONSULTA_SIN_RESULTADOS,  "Busqueda sin resultados");
		
		descError.put(ERROR_CONSULTAR_DATOS_PRESTAMO, "Error al consultar datos prestamo");
		descError.put(ERROR_PAGO_PRESTAMO, "Error al procesar pago de prestamo");
		descError.put(ERROR_OBTENER_CONCEP_OPERACION_XGRPOCLAVE,"Error al obtener Conceptos de Operacion por grupo concepto clave");
		descError.put(ERROR_BUSCAR_CUENTA, "Error al consultar cuenta por Id");
		descError.put(ERROR_CONSULTA_MOVIMIENTO, "Error al consultar movimiemtos de cuenta");
		descError.put(ERROR_CONSULTA_SALDO,  "Error al consultar saldo");
		descError.put(ERROR_NOTA_CARGO_GEN, "Error al procesar nota de cargo");
		descError.put(ERROR_NOTA_ABONO_GEN, "Error al procesar nota de abono");
		descError.put(ERROR_TRANSF_PROPIAS_GEN,"Error al procesar transferencia cuentas propias");
		descError.put(ERROR_CONSULTA_CARTA_INSTRUCTIVA,"Error al consultar carta instructiva");
		descError.put(ERROR_TRANSACCION_CARTA_INSTRUCTIVA,"Error al procesar carta instructiva");
		
		descError.put(ERROR_GRUPO_VALOR,"Error al obtener grupo valor");
		descError.put(ERROR_GRUPO_VALOR_ESTATUS, "Error al obtener grupo valor estatus");
		
		
		descError.put(ERROR_OBTENER_CONTEXTO,  "Error al obtene el Contexto, en la clase {%s}");
		descError.put(ERROR_OBTENER_EJB,  "Error al obtener EJB, en la clase {%s} ");		
		descError.put(ERROR_PROCESO,  "Error en proceso {%s},{Clase: %s},{Exception: %s}");
		descError.put(ERROR_EXCEPTION,  "Error en critico {%s},{Clase: %s},{Exception: %s}");
		
	}
		
}


/**  !Errores.java */