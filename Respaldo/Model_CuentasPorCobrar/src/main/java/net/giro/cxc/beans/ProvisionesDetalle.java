package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ProvisionesDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idProvision;
	private Long idFactura;
	private Long idTipoGasto;
	private double monto;
	private double montoRevisado;
	private double montoOriginal;
	private Long idBeneficiario;
	private String tipoBeneficiario;
	private Long idOrdenCompra;
	private String estatus;
	private Date fechaCreacion;
	private Long creadoPor;
	private Date fechaModificacion;
	private Long modificadoPor;
	
	
	public ProvisionesDetalle() {
		this.tipoBeneficiario = "";
		this.estatus = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ProvisionesDetalle(Long id) {
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

	public Long getIdProvision() {
		return idProvision;
	}

	public void setIdProvision(Long idProvision) {
		this.idProvision = idProvision;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public Long getIdTipoGasto() {
		return idTipoGasto;
	}

	public void setIdTipoGasto(Long idTipoGasto) {
		this.idTipoGasto = idTipoGasto;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getMontoRevisado() {
		return montoRevisado;
	}

	public void setMontoRevisado(double montoRevisado) {
		this.montoRevisado = montoRevisado;
	}

	public double getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(double montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public Long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(Long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
}
