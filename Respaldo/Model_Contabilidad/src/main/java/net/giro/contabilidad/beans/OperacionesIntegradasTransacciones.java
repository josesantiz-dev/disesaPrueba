package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class OperacionesIntegradasTransacciones implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private OperacionesIntegradas idOperacionIntegrada; // NUMERIC
	private Transacciones idTransaccion; // NUMERIC
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public OperacionesIntegradasTransacciones() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public OperacionesIntegradasTransacciones(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public OperacionesIntegradasTransacciones(Long id, OperacionesIntegradas idOperacionIntegrada, Transacciones idTransaccion, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idOperacionIntegrada = idOperacionIntegrada;
		this.idTransaccion = idTransaccion;
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

	public OperacionesIntegradas getIdOperacionIntegrada() {
		return idOperacionIntegrada;
	}

	public void setIdOperacionIntegrada(OperacionesIntegradas idOperacionIntegrada) {
		this.idOperacionIntegrada = idOperacionIntegrada;
	}

	public Transacciones getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Transacciones idTransaccion) {
		this.idTransaccion = idTransaccion;
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
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */