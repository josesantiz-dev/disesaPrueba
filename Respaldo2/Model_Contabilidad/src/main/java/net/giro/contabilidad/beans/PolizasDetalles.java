package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * polizas_detalles
 * @author javaz
 *
 */
public class PolizasDetalles implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private Long idInterfaz; // NUMERIC
	private String tipoPoliza; // VARCHAR
	private Long folioPoliza; // NUMERIC
	private String cuentaContable; // VARCHAR
	private BigDecimal cargo; // NUMERIC
	private BigDecimal abono; // NUMERIC
	private String estatus; // CHAR
	private Date fecha; // DATE
	private Long segmentoNegocio; // NUMERIC
	private String concepto; // VARCHAR
	private String referencia; // VARCHAR
	private Long idEmpresa; // NUMERIC
	private Long ejercicio; // NUMERIC
	private String periodo; // VARCHAR
	private Long noLinea; // NUMERIC
	private String encabezado; // VARCHAR
	private Long idPagosGastos; // BIGINT
	private String tipo; // CHAR
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public PolizasDetalles() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public PolizasDetalles(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public PolizasDetalles(Long id, Long idInterfaz, String tipoPoliza, Long folioPoliza, String cuentaContable, BigDecimal cargo,
			BigDecimal abono, String estatus, Date fecha, Long segmentoNegocio, String concepto, String referencia,
			Long idEmpresa, Long ejercicio, String periodo, Long noLinea, String encabezado, Long idPagosGastos, String tipo, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idInterfaz = idInterfaz;
		this.tipoPoliza = tipoPoliza;
		this.folioPoliza = folioPoliza;
		this.cuentaContable = cuentaContable;
		this.cargo = cargo;
		this.abono = abono;
		this.estatus = estatus;
		this.fecha = fecha;
		this.segmentoNegocio = segmentoNegocio;
		this.concepto = concepto;
		this.referencia = referencia;
		this.idEmpresa = idEmpresa;
		this.ejercicio = ejercicio;
		this.periodo = periodo;
		this.noLinea = noLinea;
		this.encabezado = encabezado;
		this.idPagosGastos = idPagosGastos;
		this.tipo = tipo;
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
	
	public Long getIdInterfaz() {
		return idInterfaz;
	}

	public void setIdInterfaz(Long idInterfaz) {
		this.idInterfaz = idInterfaz;
	}

	public String getTipoPoliza() {
		return tipoPoliza;
	}

	public void setTipoPoliza(String tipoPoliza) {
		this.tipoPoliza = tipoPoliza;
	}

	public Long getFolioPoliza() {
		return folioPoliza;
	}

	public void setFolioPoliza(Long folioPoliza) {
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

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getSegmentoNegocio() {
		return segmentoNegocio;
	}

	public void setSegmentoNegocio(Long segmentoNegocio) {
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

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Long ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Long getNoLinea() {
		return noLinea;
	}

	public void setNoLinea(Long noLinea) {
		this.noLinea = noLinea;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public Long getIdPagosGastos() {
		return idPagosGastos;
	}

	public void setIdPagosGastos(Long idPagosGastos) {
		this.idPagosGastos = idPagosGastos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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