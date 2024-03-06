package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * operaciones_integradas_conceptos_sql
 * @author javaz
 *
 */
public class OperacionesIntegradasConceptosSql implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private OperacionesIntegradasTransacciones idOperacionIntegradaTransaccion;
	private Conceptos idConcepto;
	private String sql;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public OperacionesIntegradasConceptosSql() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public OperacionesIntegradasConceptosSql(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public OperacionesIntegradasConceptosSql(Long id, OperacionesIntegradasTransacciones idOperacionIntegradaTransaccion, 
			Conceptos idConcepto, String sql,
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idOperacionIntegradaTransaccion = idOperacionIntegradaTransaccion;
		this.idConcepto = idConcepto;
		this.sql = sql;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OperacionesIntegradasTransacciones getIdOperacionIntegradaTransaccion() {
		return idOperacionIntegradaTransaccion;
	}

	public void setIdOperacionIntegradaTransaccion(OperacionesIntegradasTransacciones idOperacionIntegradaTransaccion) {
		this.idOperacionIntegradaTransaccion = idOperacionIntegradaTransaccion;
	}

	public Conceptos getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Conceptos idConcepto) {
		this.idConcepto = idConcepto;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSql
 *  2.2	| 03/06/2016 | Javier Tirado	| Cambiamos la propiedad idOperacionIntegrada por idOperacionIntegradaTransaccion
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */