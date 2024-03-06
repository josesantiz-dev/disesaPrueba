package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * polizas_interfaces
 * @author javaz
 *
 */
public class PolizasInterfaces implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // NUMERIC
	private long idMensajeTransaccion;
	private TransaccionesData idTransaccionesData;
	private long idInterfaz; // NUMERIC
	private String tipoPoliza; // VARCHAR
	private long folioPoliza; // NUMERIC
	private String cuentaContable; // VARCHAR
	private BigDecimal cargo; // NUMERIC
	private BigDecimal abono; // NUMERIC
	private Date fecha; // DATE
	private long segmentoNegocio; // NUMERIC
	private String concepto; // VARCHAR
	private String referencia; // VARCHAR
	private long idEmpresa; // NUMERIC
	private long ejercicio; // NUMERIC
	private String periodo; // VARCHAR
	private long noLinea; // NUMERIC
	private String encabezado; // VARCHAR
	private long idOperacion; // BIGINT
	private String tipo; // CHAR
	private long idGrupoCuenta;
	private String estatus; // CHAR: P:Pendiente, A:Abonado, L:Liquidada, X:Cancelada
	private String estatusMensaje;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public PolizasInterfaces() {
		this.tipoPoliza = "";
		this.cuentaContable = "";
		this.estatus = "";
		this.estatusMensaje = "";
		this.concepto = "";
		this.referencia = "";
		this.periodo = "";
		this.encabezado = "";
		this.tipo = "";
		this.cargo = BigDecimal.ZERO;
		this.abono = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public long getIdMensajeTransaccion() {
		return idMensajeTransaccion;
	}

	public void setIdMensajeTransaccion(long idMensajeTransaccion) {
		this.idMensajeTransaccion = idMensajeTransaccion;
	}

	public TransaccionesData getIdTransaccionesData() {
		return idTransaccionesData;
	}

	public void setIdTransaccionesData(TransaccionesData idTransaccionesData) {
		this.idTransaccionesData = idTransaccionesData;
	}
	
	public long getIdInterfaz() {
		return idInterfaz;
	}

	public void setIdInterfaz(long idInterfaz) {
		this.idInterfaz = idInterfaz;
	}

	public String getTipoPoliza() {
		return tipoPoliza;
	}

	public void setTipoPoliza(String tipoPoliza) {
		this.tipoPoliza = tipoPoliza;
	}

	public long getFolioPoliza() {
		return folioPoliza;
	}

	public void setFolioPoliza(long folioPoliza) {
		this.folioPoliza = folioPoliza;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public BigDecimal getCargo() {
		return cargo;
	}

	public void setCargo(BigDecimal cargo) {
		this.cargo = cargo;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	/**
	 * ESTATUS: P - Pendiente, A - Abonado, L - Liquidada, X - Cancelada
	 * @return
	 */
	public String getEstatus() {
		return estatus;
	}

	/**
	 * P - Pendiente, A - Abonado, L - Liquidada, X - Cancelada
	 * @param estatus
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public long getSegmentoNegocio() {
		return segmentoNegocio;
	}

	public void setSegmentoNegocio(long segmentoNegocio) {
		this.segmentoNegocio = segmentoNegocio;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public long getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(long ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public long getNoLinea() {
		return noLinea;
	}

	public void setNoLinea(long noLinea) {
		this.noLinea = noLinea;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public long getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(long idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getIdGrupoCuenta() {
		return idGrupoCuenta;
	}

	public void setIdGrupoCuenta(long idGrupoCuenta) {
		this.idGrupoCuenta = idGrupoCuenta;
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

	public String getEstatusMensaje() {
		return estatusMensaje;
	}

	public void setEstatusMensaje(String estatusMensaje) {
		this.estatusMensaje = estatusMensaje;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */