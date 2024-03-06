package net.giro.cxc.realvirtual.beans;

public class AcuseSchema {
	private int state; 
	private String descripcion;
	private String cfdi;
	private String rfcEmisor;
	private String rfcReceptor;
	private String version;
	private String serie;
	private String folio;
	private String fechaExpedicion;
	private String montoOperacion;
	private String montoImpuesto;
	private String tipoComprobante;
	private String cadena;
	private String firma;
	private String serieCertificado;
	private String timbre;
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCfdi() {
		return cfdi;
	}

	public void setCfdi(String cfdi) {
		this.cfdi = cfdi;
	}

	public String getRfcEmisor() {
		return rfcEmisor;
	}

	public void setRfcEmisor(String rfcEmisor) {
		this.rfcEmisor = rfcEmisor;
	}

	public String getRfcReceptor() {
		return rfcReceptor;
	}

	public void setRfcReceptor(String rfcReceptor) {
		this.rfcReceptor = rfcReceptor;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFechaExpedicion() {
		return fechaExpedicion;
	}

	public void setFechaExpedicion(String fechaExpedicion) {
		this.fechaExpedicion = fechaExpedicion;
	}

	public String getMontoOperacion() {
		return montoOperacion;
	}

	public void setMontoOperacion(String montoOperacion) {
		this.montoOperacion = montoOperacion;
	}

	public String getMontoImpuesto() {
		return montoImpuesto;
	}

	public void setMontoImpuesto(String montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getCadena() {
		return cadena;
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getSerieCertificado() {
		return serieCertificado;
	}

	public void setSerieCertificado(String serieCertificado) {
		this.serieCertificado = serieCertificado;
	}

	public String getTimbre() {
		return timbre;
	}

	public void setTimbre(String timbre) {
		this.timbre = timbre;
	}
	
	@Override
	public String toString() {
		return this.state + " - " + this.descripcion;
	}
}
