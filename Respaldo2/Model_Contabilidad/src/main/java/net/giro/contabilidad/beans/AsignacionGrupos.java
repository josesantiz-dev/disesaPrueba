package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * asignacion_grupos
 * @author javaz
 *
 */
public class AsignacionGrupos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private Transacciones idTransaccion; // NUMERIC
	private Grupos idGrupoDebito; // NUMERIC
	private Grupos idGrupoCredito; // NUMERIC
	private Conceptos idConcepto;
	private Long idFormaPago; // NUMERIC
	private Long tipoPoliza;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public AsignacionGrupos() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public AsignacionGrupos(Long id) {
		this();
		this.id = id;
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

	public Transacciones getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Transacciones idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Grupos getIdGrupoDebito() {
		return idGrupoDebito;
	}

	public void setIdGrupoDebito(Grupos idGrupoDebito) {
		this.idGrupoDebito = idGrupoDebito;
	}

	public Grupos getIdGrupoCredito() {
		return idGrupoCredito;
	}

	public void setIdGrupoCredito(Grupos idGrupoCredito) {
		this.idGrupoCredito = idGrupoCredito;
	}

	public Conceptos getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Conceptos idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public Long getTipoPoliza() {
		return tipoPoliza;
	}

	public void setTipoPoliza(Long tipoPoliza) {
		this.tipoPoliza = tipoPoliza;
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