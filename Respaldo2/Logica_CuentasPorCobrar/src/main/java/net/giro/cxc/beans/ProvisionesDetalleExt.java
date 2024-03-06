package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ProvisionesDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Provisiones idProvision;
	private FacturaExt idFactura;
	private Long idTipoGasto;
	private double monto;
	private double montoRevisado;
	private double montoOriginal;
	private Long idBeneficiario;
	private String nombreBeneficiario;
	private String tipoBeneficiario;
	private Long idOrdenCompra;
	private String folioOrdenCompra;
	private String estatus;
	private Date fechaCreacion;
	private Long creadoPor;
	private Date fechaModificacion;
	private Long modificadoPor;
	
	
	public ProvisionesDetalleExt() {
		this.nombreBeneficiario = "";
		this.tipoBeneficiario = "";
		this.folioOrdenCompra = "";
		this.estatus = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ProvisionesDetalleExt(Long id) {
		this();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Provisiones getIdProvision() {
		return idProvision;
	}

	public void setIdProvision(Provisiones idProvision) {
		this.idProvision = idProvision;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
		if (idFactura != null) {
			this.idBeneficiario = idFactura.getIdBeneficiario();
			this.nombreBeneficiario = idFactura.getBeneficiario();
			this.tipoBeneficiario = idFactura.getTipoBeneficiario();
		}
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

	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
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

	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
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
