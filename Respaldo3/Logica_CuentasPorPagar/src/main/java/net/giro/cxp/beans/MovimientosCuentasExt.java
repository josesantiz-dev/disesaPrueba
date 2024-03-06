package net.giro.cxp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class MovimientosCuentasExt implements Serializable { 

	private static final long serialVersionUID = 1L;

	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Date fecha;
	private String tipo;
	private String estatus;
	private Long noCheque;
	private BigDecimal monto;
	private String operacion;
	private String tipoBeneficiario;
	private Long idPagosGastosRef;
	private Long idPagoMultiple;
	private String nota;
	private String concepto;
	private String folioAutorizacion;
	private Double tipoCambio;
	private Long segmento;
	private Long pagosGastosId;
	private String beneficiario;
	private CtasBancoExt idCuentaOrigen; //id de un objeto perteneciente ctasBanco
	private CtasBancoExt idCuentaDestino; 
	private PersonaExt idBeneficiario;
	private PersonaExt idCuentaOrigenTerceros;
	private PersonaExt idCuentaDestinoTerceros;
	private SucursalExt idSucursal;
	private ConValores idTiposMovimiento;


	public Long getPagosGastosId() {
		return pagosGastosId;
	}

	public void setIdPagosGastos(Long pagosGastosId){
		this.pagosGastosId = pagosGastosId;	
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario){
		this.beneficiario = beneficiario;
	}
	
	public MovimientosCuentasExt() {}

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

	public CtasBancoExt getIdCuentaOrigen() {
		return idCuentaOrigen;
	}

	public void setIdCuentaOrigen(CtasBancoExt idCuentaOrigen) {
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

	public SucursalExt getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(SucursalExt idSucursal) {
		this.idSucursal = idSucursal;
	}

	public PersonaExt getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(PersonaExt idBeneficiario) {
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

	public CtasBancoExt getIdCuentaDestino() {
		return idCuentaDestino;
	}

	public void setIdCuentaDestino(CtasBancoExt idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}

	public PersonaExt getIdCuentaDestinoTerceros() {
		return idCuentaDestinoTerceros;
	}

	public void setIdCuentaDestinoTerceros(
			PersonaExt idCuentaDestinoTerceros) {
		this.idCuentaDestinoTerceros = idCuentaDestinoTerceros;
	}

	public Long getIdPagosGastosRef() {
		return idPagosGastosRef;
	}

	public void setIdPagosGastosRef(Long idPagosGastosRef) {
		this.idPagosGastosRef = idPagosGastosRef;
	}

	public ConValores getIdTiposMovimiento() {
		return idTiposMovimiento;
	}

	public void setIdTiposMovimiento(ConValores idTiposMovimiento) {
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

	public PersonaExt getIdCuentaOrigenTerceros() {
		return idCuentaOrigenTerceros;
	}

	public void setIdCuentaOrigenTerceros(PersonaExt idCuentaOrigenTerceros) {
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