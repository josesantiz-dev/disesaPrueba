package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.FormasPagos;

public class FacturaPagosExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private FacturaExt idFactura;
	private Date fecha;
	private FormasPagos idFormaPago; 
	private String referenciaFormaPago;
	private CatBancos idBancoOrigen; // institucion bancaria
	private BigDecimal monto;
	private CtasBanco idCuentaDeposito; // cuenta bancaria
	private String observaciones;
	private int estatus;
	private Date fechaCreacion;
	private long creadoPor;
	private Date fechaModificacion;
	private long modificadoPor;
	private Long idBeneficiario;
	private String beneficiario;
	private String tipoBeneficiario;
	
	
	public FacturaPagosExt() {
		this.monto = BigDecimal.ZERO;
		this.beneficiario = "";
		this.tipoBeneficiario = "";
		this.referenciaFormaPago = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public FacturaPagosExt(Long id) {
		this();
		this.id = id;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public FormasPagos getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(FormasPagos idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public CatBancos getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(CatBancos idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public CtasBanco getIdCuentaDeposito() {
		return idCuentaDeposito;
	}

	public void setIdCuentaDeposito(CtasBanco idCuentaDeposito) {
		this.idCuentaDeposito = idCuentaDeposito;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	// -------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------

	public String getFacturaDescripcion() {
		if (this.idFactura != null && this.idFactura.getId() != null && this.idFactura.getIdObra() != null)
			return this.idFactura.getFolioFactura() + " - " + this.idFactura.getIdObra().getClienteNombre();
		return "";
	}
	
	public void setFacturaDescripcion(String value) { }

	// -------------------------------------------------------------------------------------------------
	// COPIA
	// -------------------------------------------------------------------------------------------------
	
	public FacturaPagosExt getCopia() {
		return this.Copia();
	}
	
	public FacturaPagosExt Copia() {
		FacturaPagosExt dest = new FacturaPagosExt();
		
		dest.setId(this.id);
		dest.setIdFactura(this.idFactura);
		dest.setFecha(this.fecha);
		dest.setIdFormaPago(this.idFormaPago);
		dest.setReferenciaFormaPago(this.referenciaFormaPago);
		dest.setIdBancoOrigen(this.idBancoOrigen);
		dest.setMonto(this.monto);
		dest.setIdCuentaDeposito(this.idCuentaDeposito);
		dest.setObservaciones(this.observaciones);
		dest.setEstatus(this.estatus);
		dest.setFechaCreacion(this.fechaCreacion);
		dest.setCreadoPor(this.creadoPor);
		dest.setModificadoPor(this.modificadoPor);
		dest.setFechaModificacion(this.fechaModificacion);
		dest.setIdBeneficiario(this.idBeneficiario);
		dest.setBeneficiario(this.beneficiario);
		dest.setTipoBeneficiario(this.tipoBeneficiario);
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */