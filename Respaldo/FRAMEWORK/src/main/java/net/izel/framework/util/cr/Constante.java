package net.izel.framework.util.cr;

import java.util.HashMap;

public abstract class Constante {
	
	/*
	 * CONSTANTES DE TRASNFERENCIA EN LOS DESEMBOLSOS PROGRAMADOS
	 * 
	 */
	public static final Long DESEMBOLSO_PROGRAMADO_PENDIENTE = 0L;
	public static final Long DESEMBOLSO_PROGRAMADO_TRANSFERIDO = 1L;
	public static final Long DESEMBOLSO_PROGRAMADO_CANCELADO = 2L;
	
	
	
	// property constants
	public static final Long ALTA_ID_TARI = 1000000000273L;
	public static final Long ALTA_ID = 100000000004L;
	public static final Long BAJA_ID = 100000000005L;
	
	// PRODUCTOS
	
	public static final Long PROD_CREDIAGIL_FORMAL = 1000000000046L;
	public static final Long PROD_CREDIAGIL_INFORMAL =  1000000000049L;
	public static final Long PROD_NIVEL1_FORMAL = 1000000000043L;
	public static final Long PROD_NIVEL1_INFORMAL = 1000000000044L;
	public static final Long PROD_CREDICRECE_FORMAL = 1000000000039L;
	public static final Long PROD_CREDICRECE_INFORMAL = 1000000000040L;
	public static final Long PROD_EXPANSION_FORMAL = 1000000000058L;
	public static final Long PROD_EXPANSION_INFORMAL = 1000000000058L;
	public static final Long PROD_DESARROLLO_FORMAL = 1000000000060L;
	public static final Long PROD_DESARROLLO_INFORMAL = 1000000000060L;
	
	public static final Long PROD_AUTOPRODUCCION_ACTPROD = 1000000000071L;
	public static final Long PROD_AUTOPRODUCCION_ASALARIADO = 1000000000067L;
	
	public static final Long PROD_MEJORATUCASA_SUCURSAL = 1000000000012L;
	public static final Long PROD_MEJORATUCASA_VENTANILLA = 1000000000013L;
	
	public static final Long PROD_MEJORATUCASA_INTERNO = 1000000000025L;

	public static final Long PROD_RENUEVATE_SUCURSAL= 1000000000001L;
	public static final Long PROD_RENUEVATE_VENTANILLA= 1000000000002L;
	public static final Long PROD_RENUEVATE_DOM_SUCURSAL= 1000000000004L;
	public static final Long PROD_RENUEVATE_DOM_VENTANILLA= 1000000000006L;
	
	public static final Long PROD_DESARROLLO_SUCURSAL_SIN_IVA  =1000000000003L; 
	public static final Long PROD_DESARROLLO_VENTANILLA_SIN_IVA  =1000000000019l;
	public static final Long PROD_DESARROLLO_SUCURSAL_CON_IVA  =1000000000020L;
	public static final Long PROD_DESARROLLO_VENTANILLAL_CON_IVA  =1000000000021L;

	public static final Long PROD_EXPANSION_SUCURSAL_SIN_IVA= 1000000000010L;
	public static final Long PROD_EXPANSION_VENTANILLA_SIN_IVA= 1000000000014L;
	public static final Long PROD_EXPANSION_SUCURSAL_CON_IVA= 1000000000015L;
	public static final Long PROD_EXPANSION_VENTANILLA_CON_IVA= 1000000000016L;
	
	public static final Long PROD_CREDIAGIL_SUCURSAL_SIN_IVA=1000000000022L; 
	public static final Long PROD_CREDIAGIL_SUCURSAL_CON_IVA=1000000000023L; 
	public static final Long PROD_CREDIAGIL_VENTANILLA_CON_IVA=1000000000026L; 
	public static final Long PROD_CREDIAGIL_VENTANILLA_SIN_IVA=1000000000024L; 
	
	
	public static final Long PROD_NIVELUNO_SUCURSAL_FORMAL=1000000000027L; 
	public static final Long PROD_NIVELUNO_SUCURSAL_INFORMAL=1000000000028L; 
	public static final Long PROD_NIVELUNO_VENTANILLA_FORMAL=1000000000029L; 
	public static final Long PROD_NIVELUNO_VENTANILLA_INFORMAL=1000000000030L; 
	
	public static final Long PROD_CREDIMPULSA_SUCURSAL_FORMAL=1000000000031L; 
	public static final Long PROD_CREDIMPULSA_SUCURSAL_INFORMAL=1000000000032L; 
	public static final Long PROD_CREDIMPULSA_VENTANILLA_FORMAL=1000000000033L; 
	public static final Long PROD_CREDIMPULSA_VENTANILLA_INFORMAL=1000000000034L; 
	
	public static final Long PROD_CREDICRECE_SUCURSAL_FORMAL=1000000000035L; 
	public static final Long PROD_CREDICRECE_SUCURSAL_INFORMAL=1000000000037L; 
	public static final Long PROD_CREDICRECE_VENTANILLA_FORMAL=1000000000036L; 
	public static final Long PROD_CREDICRECE_VENTANILLA_INFORMAL=1000000000038L; 
	

	// ESTADO PRESTAMOS
	public static final Long ALTA      = 1000000000001L;
	public static final Long VALIDADO  = 1000000000002L;
	public static final Long ACTIVO    = 1000000000003L;
	public static final Long LIQUIDADO = 1000000000004L;
	
	
	// ------- ConstantesGral usadas en el calculo CAT
	public static final Long MONTO_COBERTURA_OPERACION = 1000000000016L;
	public static final Long MONTO_COBERTURA_GARANTIA  = 1000000000017L;
	public static final Long TIPO_CAMBIO               = 1000000000018L;
	
	// ----------- RECALCULO CONCEPTOS ---------- //
	public static final Long RECALCULO_IVA = 1000000000004L;
	
	// ANULAR OPERACIONES
	public static final Long MEDIO_PAGO_CUOTA = 1000000000011L;
	
	// APLICAR PAGO A CUENTA
	public static final Long USUARIO_SISTEMAS = 9L;
	public static final Long CLASE_CANAL_SUCURSAL= 100000000001L;
	public static final Long CANAL_ATENCION_PROBATCH = 1000000000021L;
	public static final Long PTO_ATEN_SER_OPE = 1000000000034L;
	public static final Long UBICACION_SERVIDOR =1000000000002L;
	public static final Long MATRIZ	= 1000000000002L;
	
	public static final Long MONEDA_PESOS_ID=1000000000005l;
	public static final Long TIPO_CANAL_VENTA_INTERNO_ID=1000000000341l;
	public static final Long CANAL_VENTA_SUCURSAL_ID = 1000000000066l;
	public static final Long ESTADO_PRESTAMO_ALTA_ID=1000000000001L;
	public static final Long ESTADO_PRESTAMO_ACTIVO_ID=1000000000003L;
	public static final Long ESTADO_PRESTAMO_VALIDADO_ID=1000000000002L;
	public static final Long ESTADO_PRESTAMO_LIQUIDADO_ID=1000000000004L;
	public static final Long CONDICION_PRESTAMO_NUEVO_ID= 1000000000068L;
	public static final Long CONDICION_PRESTAMO_RECURRENTE_ID=1000000000070L;
	public static final Long EMPRESA_OPCIONES_ID=1L;
	public static final Long ESQUEMA_GRUPAL_ID=1000000000061L;
	public static final Long ESQUEMA_INDIVIDUAL_ID=1000000000062L;
	public static final Long ESTADO_CONTABLE_NORMAL_ID=1000000000001L;
	public static final Long ESTADO_CONSTABLE_CASTIGO_ID=1000000000003l;
	public static final Long ESTADO_CONTABLE_REF_ID=1000000000002l;
	public static final Long ESTADO_CONTABLE_JUDICIAL_ID=1000000000004L;
	public static final Long TIPO_IDENTIFICACION_IFE=1000000000475L;
	public static final Long TIPO_SOLICITUCC_ID= 1000000000060l;
	public static final Long TIPO_SOLICITUD_NORMAL_ID= 1000000000059L;
	public static final Long TIPO_CAMBIO_CONDICION_REF_ID=1000000000056L;
	public static final Long TIPO_CAMBIO_CONDICION_REP_ID=1000000000057L;
	
	public static final String CALIFICACIONES_MALAS= "Nr,Pd";
	
	public static final Long MODALIDAD_CUOTA_NORMAL_ID = 1000000000359L;
	                                            
	
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	static {
		descEstatus.put(ALTA_ID_TARI,"C");
		descEstatus.put(ALTA_ID,"A");
		descEstatus.put(BAJA_ID,"B");
		descEstatus.put(ALTA,"ESTADO PRESTAMO ALTA");
		descEstatus.put(VALIDADO,"ESTADO PRESTAMO VALIDADO");
		descEstatus.put(ACTIVO,"ESTADO PRESTAMO ACTIVO");
		descEstatus.put(LIQUIDADO,"ESTADO PRESTAMO LIQUIDADO");
		descEstatus.put(PROD_CREDIAGIL_FORMAL,"Producto CrediAgil Formal");
		descEstatus.put(PROD_CREDIAGIL_INFORMAL,"Producto CrediAgil Informal ");
		descEstatus.put(PROD_NIVEL1_FORMAL,"Producto nivel 1 Formal");
		descEstatus.put(PROD_NIVEL1_INFORMAL,"Producto nivel 1 Informal");
		descEstatus.put(PROD_CREDICRECE_FORMAL,"Producto Credicrece Formal");
		descEstatus.put(PROD_CREDICRECE_INFORMAL,"Producto Credicrece Informal");
		descEstatus.put(PROD_MEJORATUCASA_SUCURSAL,"Producto Mejora tu Casa Sucursal");
		descEstatus.put(PROD_MEJORATUCASA_VENTANILLA,"Producto Mejora tu Casa Ventanilla");
		descEstatus.put(PROD_MEJORATUCASA_INTERNO,"Producto Mejora tu Casa Interno");
		descEstatus.put(PROD_EXPANSION_FORMAL, "Producto Expansion Formal");
		descEstatus.put(PROD_EXPANSION_INFORMAL, "Producto Expansion Informal");
		descEstatus.put(PROD_DESARROLLO_FORMAL, "Producto Desarrollo Formal");
		descEstatus.put(PROD_DESARROLLO_INFORMAL, "Producto Desarrollo Informal");
		
		descEstatus.put(PROD_AUTOPRODUCCION_ACTPROD, "Producto AutoProduccion Actividad Empresarial");
		descEstatus.put(PROD_AUTOPRODUCCION_ASALARIADO, "Producto AutoProduccion Pensionado y Jubilado");
		
		descEstatus.put(PROD_RENUEVATE_SUCURSAL, "Producto Renuevate Sucursal");
		descEstatus.put(PROD_RENUEVATE_VENTANILLA, "Producto Renuevate Ventanilla");
		
		descEstatus.put(PROD_RENUEVATE_DOM_SUCURSAL, "Producto Renuevate Domiciliacion Sucursal");
		descEstatus.put(PROD_RENUEVATE_DOM_VENTANILLA, "Producto Renuevate Domiciliacion Ventanilla");
		
		descEstatus.put(PROD_EXPANSION_SUCURSAL_SIN_IVA, "Producto Expansion Sucursal Sin Iva");
		descEstatus.put(PROD_EXPANSION_VENTANILLA_SIN_IVA, "Producto Expansion Ventanilla Sin Iva");
		descEstatus.put(PROD_EXPANSION_SUCURSAL_CON_IVA, "Producto Expansion Sucursal Con Iva");
		descEstatus.put(PROD_EXPANSION_VENTANILLA_CON_IVA, "Producto Expansion Ventanilla Con Iva");
		
		descEstatus.put(PROD_DESARROLLO_SUCURSAL_SIN_IVA, "Producto Desarrollo Sucursal Sin Iva");
		descEstatus.put(PROD_DESARROLLO_VENTANILLA_SIN_IVA, "Producto Desarrollo Ventanilla Sin Iva");
		descEstatus.put(PROD_DESARROLLO_SUCURSAL_CON_IVA, "Producto Desarrollo Sucursal Con Iva");
		descEstatus.put(PROD_DESARROLLO_VENTANILLAL_CON_IVA, "Producto Desarrollo Ventanilla Con Iva");
		
		descEstatus.put(PROD_CREDIAGIL_SUCURSAL_SIN_IVA, "Producto Crediagil Sucursal Sin Iva");
		descEstatus.put(PROD_CREDIAGIL_SUCURSAL_CON_IVA, "Producto Crediagil Sucursal Con Iva");
		descEstatus.put(PROD_CREDIAGIL_VENTANILLA_CON_IVA, "Producto Crediagil Ventanilla Con Iva");
		descEstatus.put(PROD_CREDIAGIL_VENTANILLA_SIN_IVA, "Producto Crediagil Ventanilla Sin Iva");
		
		descEstatus.put(PROD_NIVELUNO_SUCURSAL_FORMAL, "Producto NivelUno Sucursal Formal");
		descEstatus.put(PROD_NIVELUNO_SUCURSAL_INFORMAL, "Producto NivelUno Sucursal Informal");
		descEstatus.put(PROD_NIVELUNO_VENTANILLA_FORMAL, "Producto NivelUno Ventanilla Formal");
		descEstatus.put(PROD_NIVELUNO_VENTANILLA_INFORMAL, "Producto NivelUno Ventanilla Informal");
		
		descEstatus.put(PROD_CREDIMPULSA_SUCURSAL_FORMAL, "Producto Credimpulsa Sucursal Formal");
		descEstatus.put(PROD_CREDIMPULSA_SUCURSAL_INFORMAL, "Producto Credimpulsa Sucursal Informal");
		descEstatus.put(PROD_CREDIMPULSA_VENTANILLA_FORMAL, "Producto Credimpulsa Ventanilla Formal");
		descEstatus.put(PROD_CREDIMPULSA_VENTANILLA_INFORMAL, "Producto Credimpulsa Ventanilla Informal");
		
		descEstatus.put(MONTO_COBERTURA_OPERACION, "Monto Cobertura Operacion");
		descEstatus.put(MONTO_COBERTURA_GARANTIA, "Monto Cobertura Garantia");
		descEstatus.put(TIPO_CAMBIO , "Tipo Cambio");
		
		descEstatus.put(RECALCULO_IVA , "Recalculo IVA");
		
		descEstatus.put(MEDIO_PAGO_CUOTA, "Medio de pago cuota");
		
		descEstatus.put(MODALIDAD_CUOTA_NORMAL_ID, "Modalidad cuota normal");
		

		descEstatus.put(DESEMBOLSO_PROGRAMADO_TRANSFERIDO,"T");
		descEstatus.put(DESEMBOLSO_PROGRAMADO_PENDIENTE,"P");
		descEstatus.put(DESEMBOLSO_PROGRAMADO_CANCELADO,"X");
		
		
					
	}
}
