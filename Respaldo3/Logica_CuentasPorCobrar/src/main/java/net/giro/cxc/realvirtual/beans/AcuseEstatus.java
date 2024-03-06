package net.giro.cxc.realvirtual.beans;

public class AcuseEstatus {
	private int state; 
	private String mensaje;
	private String fecha;
	private String UUIDs;
	private String rfcSolicitante;
	private String noSerieFirmante;
	private String acuse;
	private String rutaDescargaAcuse;
	private String esCancelable;
	private String estatusCancelacion;
	private String estatusCFDI;
	private String descripcion;
	private String expresionImpresa;
	
	public AcuseEstatus() {
		this.mensaje = "";
		this.rfcSolicitante = "";
		this.fecha = "";
		this.UUIDs = "";
		this.noSerieFirmante = "";
		this.acuse = "";
		this.rutaDescargaAcuse = "";
		this.esCancelable = "";
		this.estatusCancelacion = "";
		this.estatusCFDI = "";
		this.descripcion = "";
		this.expresionImpresa = "";
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		if (mensaje == null)
			mensaje = "";
		this.mensaje = mensaje;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		if (fecha == null)
			fecha = "";
		this.fecha = fecha;
	}

	public String getUUIDs() {
		return UUIDs;
	}

	public void setUUIDs(String UUIDs) {
		if (UUIDs == null)
			UUIDs = "";
		this.UUIDs = UUIDs;
	}

	public String getRfcSolicitante() {
		return rfcSolicitante;
	}

	public void setRfcSolicitante(String rfcSolicitante) {
		if (rfcSolicitante == null)
			rfcSolicitante = "";
		this.rfcSolicitante = rfcSolicitante;
	}

	public String getNoSerieFirmante() {
		return noSerieFirmante;
	}

	public void setNoSerieFirmante(String noSerieFirmante) {
		if (noSerieFirmante == null)
			noSerieFirmante = "";
		this.noSerieFirmante = noSerieFirmante;
	}

	public String getAcuse() {
		return acuse;
	}

	public void setAcuse(String acuse) {
		if (acuse == null)
			acuse = "";
		this.acuse = acuse;
	}

	public String getRutaDescargaAcuse() {
		return rutaDescargaAcuse;
	}

	public void setRutaDescargaAcuse(String rutaDescargaAcuse) {
		if (rutaDescargaAcuse == null)
			rutaDescargaAcuse = "";
		this.rutaDescargaAcuse = rutaDescargaAcuse;
	}

	public String getEsCancelable() {
		return esCancelable;
	}

	public void setEsCancelable(String esCancelable) {
		if (esCancelable == null)
			esCancelable = "";
		this.esCancelable = esCancelable;
	}

	public String getEstatusCancelacion() {
		return estatusCancelacion;
	}

	public void setEstatusCancelacion(String estatusCancelacion) {
		if (estatusCancelacion == null)
			estatusCancelacion = "";
		this.estatusCancelacion = estatusCancelacion;
	}

	public String getEstatusCFDI() {
		return estatusCFDI;
	}

	public void setEstatusCFDI(String estatusCFDI) {
		if (estatusCFDI == null)
			estatusCFDI = "";
		this.estatusCFDI = estatusCFDI;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		if (descripcion == null)
			descripcion = "";
		this.descripcion = descripcion;
	}

	public String getExpresionImpresa() {
		return expresionImpresa;
	}

	public void setExpresionImpresa(String expresionImpresa) {
		if (expresionImpresa == null)
			expresionImpresa = "";
		this.expresionImpresa = expresionImpresa;
	}
	
	@Override
	public String toString() {
		return this.state + " - " + this.descripcion;
	}
}
