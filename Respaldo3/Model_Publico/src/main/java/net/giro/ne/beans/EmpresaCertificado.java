package net.giro.ne.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * empresa_certificado
 * @author javaz
 *
 */
public class EmpresaCertificado implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Empresa idEmpresa;
	private String certificado;
	private String noCertificado;
	private String estatus;
	private String llave;
	private Date al;
	private Date del;
	private String asunto;
	private byte[] certificadoData;
	private byte[] llaveData;
	private String palabra;
	private byte[] certificadoDataPem;
	private byte[] llaveDataPem;
	private byte[] llaveDataEnc;
	private String usuarioTimbre;
	private String claveTimbre;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public EmpresaCertificado() {
		this.al = Calendar.getInstance().getTime();
		this.del = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpresaCertificado(Long id) {
		super();
		this.id = id;
		this.al = Calendar.getInstance().getTime();
		this.del = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public EmpresaCertificado(Long id, Empresa idEmpresa, String certificado, String noCertificado, String estatus,
			String llave, Date al, Date del, String asunto, byte[] certificadoData, byte[] llaveData, String palabra,
			byte[] certificadoDataPem, byte[] llaveDataPem, byte[] llaveDataEnc, String usuarioTimbre,
			String claveTimbre, long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idEmpresa = idEmpresa;
		this.certificado = certificado;
		this.noCertificado = noCertificado;
		this.estatus = estatus;
		this.llave = llave;
		this.al = al;
		this.del = del;
		this.asunto = asunto;
		this.certificadoData = certificadoData;
		this.llaveData = llaveData;
		this.palabra = palabra;
		this.certificadoDataPem = certificadoDataPem;
		this.llaveDataPem = llaveDataPem;
		this.llaveDataEnc = llaveDataEnc;
		this.usuarioTimbre = usuarioTimbre;
		this.claveTimbre = claveTimbre;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
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

	public Empresa getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Empresa idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public String getNoCertificado() {
		return noCertificado;
	}

	public void setNoCertificado(String noCertificado) {
		this.noCertificado = noCertificado;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getLlave() {
		return llave;
	}

	public void setLlave(String llave) {
		this.llave = llave;
	}

	public Date getAl() {
		return al;
	}

	public void setAl(Date al) {
		this.al = al;
	}

	public Date getDel() {
		return del;
	}

	public void setDel(Date del) {
		this.del = del;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public byte[] getCertificadoData() {
		return certificadoData;
	}

	public void setCertificadoData(byte[] certificadoData) {
		this.certificadoData = certificadoData;
	}

	public byte[] getLlaveData() {
		return llaveData;
	}

	public void setLlaveData(byte[] llaveData) {
		this.llaveData = llaveData;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	public byte[] getCertificadoDataPem() {
		return certificadoDataPem;
	}

	public void setCertificadoDataPem(byte[] certificadoDataPem) {
		this.certificadoDataPem = certificadoDataPem;
	}

	public byte[] getLlaveDataPem() {
		return llaveDataPem;
	}

	public void setLlaveDataPem(byte[] llaveDataPem) {
		this.llaveDataPem = llaveDataPem;
	}

	public byte[] getLlaveDataEnc() {
		return llaveDataEnc;
	}

	public void setLlaveDataEnc(byte[] llaveDataEnc) {
		this.llaveDataEnc = llaveDataEnc;
	}

	public String getUsuarioTimbre() {
		return usuarioTimbre;
	}

	public void setUsuarioTimbre(String usuarioTimbre) {
		this.usuarioTimbre = usuarioTimbre;
	}

	public String getClaveTimbre() {
		return claveTimbre;
	}

	public void setClaveTimbre(String claveTimbre) {
		this.claveTimbre = claveTimbre;
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

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */