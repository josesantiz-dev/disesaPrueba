package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * c66ff17ccc
 * @author javaz
 *
 */
public class ComprobacionFactura implements Serializable { 
	private static final long serialVersionUID = 1L;

	private long id;
	private Date fechaComprobacion;
	private String expresionImpresa;
	private String facturaSerie;
	private String facturaFolio;
	private String facturaRazonSocial;
	private String facturaFolioFiscal;
	private String estado;
	private String codigoEstatus;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private int estatus; // O: Activo, 1: Eliminado
	private Long idEmpresa;

	
	public ComprobacionFactura() {
		this.estado = "";
		this.codigoEstatus = "";
		this.facturaSerie = "";
		this.facturaFolio = "";
		this.facturaRazonSocial = "";
		this.facturaFolioFiscal = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaComprobacion = Calendar.getInstance().getTime();
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExpresionImpresa() {
		return expresionImpresa;
	}

	public void setExpresionImpresa(String expresionImpresa) {
		this.expresionImpresa = expresionImpresa;
	}

	public String getFacturaSerie() {
		if (this.facturaSerie != null)
			return this.facturaSerie.trim();
		return "";
	}

	public void setFacturaSerie(String facturaSerie) {
		this.facturaSerie = facturaSerie;
	}

	public String getFacturaFolio() {
		if (this.facturaFolio != null)
			return this.facturaFolio.trim();
		return "";
	}

	public void setFacturaFolio(String facturaFolio) {
		this.facturaFolio = facturaFolio;
	}

	public String getFacturaRazonSocial() {
		return facturaRazonSocial;
	}

	public void setFacturaRazonSocial(String facturaRazonSocial) {
		this.facturaRazonSocial = facturaRazonSocial;
	}

	public String getFacturaFolioFiscal() {
		return facturaFolioFiscal;
	}

	public void setFacturaFolioFiscal(String facturaFolioFiscal) {
		this.facturaFolioFiscal = facturaFolioFiscal;
	}

	public Date getFechaComprobacion() {
		return fechaComprobacion;
	}

	public void setFechaComprobacion(Date fechaComprobacion) {
		this.fechaComprobacion = fechaComprobacion;
	}

	public String getCodigoEstatus() {
		return codigoEstatus;
	}

	public void setCodigoEstatus(String codigoEstatus) {
		this.codigoEstatus = codigoEstatus;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/***
	 * O: Activo, 1: Eliminado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/***
	 * O: Activo, 1: Eliminado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	// -----------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------
	
	public String getFactura() {
		String val = ""; 
		
		if (this.expresionImpresa == null || "".equals(this.expresionImpresa.trim()))
			return "";

		if (! "".equals(getFacturaSerie()))
			val = getFacturaSerie();
		
		if (! "".equals(getFacturaFolio()))
			val += (! "".equals(val.trim()) ? "-" : "") + getFacturaFolio();
		
		return val;
	}
	
	public void setFactura(String value) {}
	
	public String getRazonSocialORfc() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim())) {
			if (this.facturaRazonSocial == null || "".equals(this.facturaRazonSocial.trim()))
				return getRfcEmisor();
			return this.facturaRazonSocial;
		}
			
		return "";
	}
	
	public void setRazonSocialORfc(String value) {}
	
	public String getRfcEmisor() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("?re=") + 4, this.expresionImpresa.indexOf("&rr="));
		return "";
	}
	
	public void setRfcEmisor(String value) {}
	
	public String getRfcReceptor() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("&rr=") + 4, this.expresionImpresa.indexOf("&tt="));
		return "";
	}
	
	public void setRfcReceptor(String value) {}
	
	public String getTotal() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("&tt=") + 4, this.expresionImpresa.indexOf("&id="));
		return "0.0";
	}
	
	public void setTotal(String value) {}
	
	public String getUuid() {
		if (this.expresionImpresa != null && ! "".equals(this.expresionImpresa.trim()))
			return this.expresionImpresa.substring(this.expresionImpresa.indexOf("&id=") + 4);
		return "";
	}
	
	public void setUuid(String value) {}
}