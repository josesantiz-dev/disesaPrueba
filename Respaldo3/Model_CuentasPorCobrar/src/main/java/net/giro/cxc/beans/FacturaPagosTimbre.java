package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class FacturaPagosTimbre implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long codigo;
	private String mensaje;
	private int timbrado; // TIMBRADO: 0-Sin Timbrar, 1-Timbrado
	private long timbradoPor;
	private Date fecha;
	private int pruebas;
	// ------------------------------------
	private double monto;
	private String uuid;
	private String serie;
	private String folio;
	private String cadenaOriginal;
	private String sello;
	private String selloSat;
	private String certificadoSat;
	private String versionTimbre;
	private byte[] xmlPrevio;
	private byte[] xmlTimbrado;
	private String rfcEmisor;
	private String rfcReceptor;
	// ------------------------------------
	private int cancelado;
	private long canceladoPor;
	private Date fechaCancelacion;
	private long estadoCancelacion;
	private String mensajeCancelacion;
	private String rfcSolicitante;
	private String noSerieFirmante;
	private byte[] acuseCancelacion;
	private String motivoCancelacion;
	private int intentosCancelacion;
	private Date fechaSolicitudCancelacion;
	// ------------------------------------
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public FacturaPagosTimbre() {
		this.id = 0L;
		this.mensaje = "";
		this.uuid = "";
		this.serie = "";
		this.folio = "";
		this.cadenaOriginal = "";
		this.sello = "";
		this.selloSat = "";
		this.certificadoSat = "";
		this.versionTimbre = "";
		this.rfcEmisor = "";
		this.rfcReceptor = "";
		this.mensajeCancelacion = "";
		this.motivoCancelacion = "";
		this.rfcSolicitante = "";
		this.noSerieFirmante = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.idEmpresa = 0L;
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

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		if (mensaje == null)
			mensaje = "";
		this.mensaje = mensaje;
	}

	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado
	 * @return
	 */
	public int getTimbrado() {
		return timbrado;
	}

	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado
	 * @param timbrado
	 */
	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public long getTimbradoPor() {
		return timbradoPor;
	}

	public void setTimbradoPor(long timbradoPor) {
		this.timbradoPor = timbradoPor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getPruebas() {
		return pruebas;
	}

	public void setPruebas(int pruebas) {
		this.pruebas = pruebas;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		if (uuid == null)
			uuid = "";
		this.uuid = uuid;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		if (serie == null)
			serie = "";
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		if (folio == null)
			folio = "";
		this.folio = folio;
	}

	public String getCadenaOriginal() {
		return cadenaOriginal;
	}

	public void setCadenaOriginal(String cadenaOriginal) {
		if (cadenaOriginal == null)
			cadenaOriginal = "";
		this.cadenaOriginal = cadenaOriginal;
	}

	public String getSello() {
		return sello;
	}

	public void setSello(String sello) {
		if (sello == null)
			sello = "";
		this.sello = sello;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		if (selloSat == null)
			selloSat = "";
		this.selloSat = selloSat;
	}

	public String getCertificadoSat() {
		return certificadoSat;
	}

	public void setCertificadoSat(String certificadoSat) {
		if (certificadoSat == null)
			certificadoSat = "";
		this.certificadoSat = certificadoSat;
	}

	public String getVersionTimbre() {
		return versionTimbre;
	}

	public void setVersionTimbre(String versionTimbre) {
		if (versionTimbre == null)
			versionTimbre = "";
		this.versionTimbre = versionTimbre;
	}

	public byte[] getXmlPrevio() {
		return xmlPrevio;
	}

	public void setXmlPrevio(byte[] xmlPrevio) {
		this.xmlPrevio = xmlPrevio;
	}

	public byte[] getXmlTimbrado() {
		return xmlTimbrado;
	}

	public void setXmlTimbrado(byte[] xmlTimbrado) {
		this.xmlTimbrado = xmlTimbrado;
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

	public int getCancelado() {
		return cancelado;
	}

	public void setCancelado(int cancelado) {
		this.cancelado = cancelado;
	}

	public long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public long getEstadoCancelacion() {
		return estadoCancelacion;
	}

	public void setEstadoCancelacion(long estadoCancelacion) {
		this.estadoCancelacion = estadoCancelacion;
	}

	public String getMensajeCancelacion() {
		return mensajeCancelacion;
	}

	public void setMensajeCancelacion(String mensajeCancelacion) {
		if (mensajeCancelacion == null)
			mensajeCancelacion = "";
		this.mensajeCancelacion = mensajeCancelacion;
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

	public byte[] getAcuseCancelacion() {
		return acuseCancelacion;
	}

	public void setAcuseCancelacion(byte[] acuseCancelacion) {
		this.acuseCancelacion = acuseCancelacion;
	}

	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	public void setMotivoCancelacion(String motivoCancelacion) {
		if (motivoCancelacion == null)
			motivoCancelacion = "";
		this.motivoCancelacion = motivoCancelacion;
	}

	public int getIntentosCancelacion() {
		return intentosCancelacion;
	}

	public void setIntentosCancelacion(int intentosCancelacion) {
		this.intentosCancelacion = intentosCancelacion;
	}

	public Date getFechaSolicitudCancelacion() {
		return fechaSolicitudCancelacion;
	}

	public void setFechaSolicitudCancelacion(Date fechaSolicitudCancelacion) {
		this.fechaSolicitudCancelacion = fechaSolicitudCancelacion;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
