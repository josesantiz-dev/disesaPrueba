package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class MensajeTransaccionExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idTransaccion; // Transacciones
	private Long idOperacion;
	private Long idMoneda;
	private String descripcionMoneda;
	private BigDecimal importe;
	private Long idPersonaReferencia;
	private String nombrePersonaReferencia;
	private String tipoPersona;
	private String referencia;
	private Long idFormaPago;
	private String referenciaFormaPago;
	private long idUsuarioCreacionRegistro;
	private Long idSucursal;
	private Long poliza;
	private Long lote;
	private long creadoPor;
	private String fechaCreacion;
	private long anuladoPor;
	private String fechaAnulacion;
	private int estatus; // 0 - Sin ejecucion, 1 - Por Contabilizar, 2 - Contabilizado
	private String estatusMensaje;
	private String fechaRegistro;
	private Long idEmpresa;
	
	public MensajeTransaccionExt() {
		this.importe = BigDecimal.ZERO;
		this.descripcionMoneda = "";
		this.nombrePersonaReferencia = "";
		this.tipoPersona = "";
		this.referencia = "";
		this.referenciaFormaPago = "";
		this.fechaCreacion = "";
		this.fechaAnulacion = "";
		this.fechaRegistro = "";
		this.estatusMensaje = "";
	}
	
	public MensajeTransaccionExt(Long id) {
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

	public Long getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public Long getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(Long idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getDescripcionMoneda() {
		return descripcionMoneda;
	}

	public void setDescripcionMoneda(String descripcionMoneda) {
		this.descripcionMoneda = descripcionMoneda;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public Long getIdPersonaReferencia() {
		return idPersonaReferencia;
	}

	public void setIdPersonaReferencia(Long idPersonaReferencia) {
		this.idPersonaReferencia = idPersonaReferencia;
	}

	public String getNombrePersonaReferencia() {
		return nombrePersonaReferencia;
	}

	public void setNombrePersonaReferencia(String nombrePersonaReferencia) {
		this.nombrePersonaReferencia = nombrePersonaReferencia;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public long getIdUsuarioCreacionRegistro() {
		return idUsuarioCreacionRegistro;
	}

	public void setIdUsuarioCreacionRegistro(long idUsuarioCreacionRegistro) {
		this.idUsuarioCreacionRegistro = idUsuarioCreacionRegistro;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getPoliza() {
		return poliza;
	}

	public void setPoliza(Long poliza) {
		this.poliza = poliza;
	}

	public Long getLote() {
		return lote;
	}

	public void setLote(Long lote) {
		this.lote = lote;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getAnuladoPor() {
		return anuladoPor;
	}

	public void setAnuladoPor(long anuladoPor) {
		this.anuladoPor = anuladoPor;
	}

	public String getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	/**
	 * ESTATUS: 0 - Sin ejecucion, 1 - Por Contabilizar, 2 - Contabilizado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Sin ejecucion, 1 - Por Contabilizar, 2 - Contabilizado
	 * @return
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */