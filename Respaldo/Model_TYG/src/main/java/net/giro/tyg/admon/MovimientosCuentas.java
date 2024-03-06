package net.giro.tyg.admon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovimientosCuentas implements Serializable { 

	private static final long serialVersionUID = 1L;

	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Long idCuentaOrigen;
	private Date fecha;
	private String tipo;
	private String estatus;
	private Long noCheque;
	private BigDecimal monto;
	private Long idSucursal;
	private Long idBeneficiario;
	private String operacion;
	private String tipoBeneficiario; //Generalmente vacio en lo que he visto...
	private Long idCuentaDestino;
	private Long idCuentaDestinoTerceros; //Generalmente vacio en lo que he visto
	private Long idPagosGastosRef; //Generalmente vacio en lo que he visto
	private Long idTiposMovimiento; //Generalmente vacio
	private Long idPagoMultiple; //Generalmente vacio
	private String nota; //Generalmente vacio
	private String concepto;
	private String folioAutorizacion;
	private Long idCuentaOrigenTerceros; //id de persona, generalmente vacio
	private Double tipoCambio; //Generalmente vacio
	private Long segmento; //Generalmente en 0
	
	public MovimientosCuentas() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getIdCuentaOrigen() {
		return idCuentaOrigen;
	}

	public void setIdCuentaOrigen(Long idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getNoCheque() {
		return noCheque;
	}

	public void setNoCheque(Long noCheque) {
		this.noCheque = noCheque;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiaro) {
		this.tipoBeneficiario = tipoBeneficiaro;
	}

	public Long getIdCuentaDestino() {
		return idCuentaDestino;
	}

	public void setIdCuentaDestino(Long idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}

	public Long getIdCuentaDestinoTerceros() {
		return idCuentaDestinoTerceros;
	}

	public void setIdCuentaDestinoTerceros(
			Long idCuentaDestinoTerceros) {
		this.idCuentaDestinoTerceros = idCuentaDestinoTerceros;
	}

	public Long getIdPagosGastosRef() {
		return idPagosGastosRef;
	}

	public void setIdPagosGastosRef(Long idPagosGastosRef) {
		this.idPagosGastosRef = idPagosGastosRef;
	}

	public Long getIdTiposMovimiento() {
		return idTiposMovimiento;
	}

	public void setIdTiposMovimiento(Long idTiposMovimiento) {
		this.idTiposMovimiento = idTiposMovimiento;
	}

	public Long getIdPagoMultiple() {
		return idPagoMultiple;
	}

	public void setIdPagoMultiple(Long idPagoMultiple) {
		this.idPagoMultiple = idPagoMultiple;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFolioAutorizacion() {
		return folioAutorizacion;
	}

	public void setFolioAutorizacion(String folioAutorizacion) {
		this.folioAutorizacion = folioAutorizacion;
	}

	public Long getIdCuentaOrigenTerceros() {
		return idCuentaOrigenTerceros;
	}

	public void setIdCuentaOrigenTerceros(Long idCuentaOrigenTerceros) {
		this.idCuentaOrigenTerceros = idCuentaOrigenTerceros;
	}

	public Double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public Long getSegmento() {
		return segmento;
	}

	public void setSegmento(Long segmento) {
		this.segmento = segmento;
	}
}