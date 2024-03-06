package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ComprobacionNomina implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private int estatus;
	private String descripcion;
	private String expresionImpresa;
	private String serie;
	private String folio;
	private Date fecha;
	private Date fechaTimbrado;
	private String folioFiscal;
	// Emisor
	private String emisor;
	private String emisorRfc;
	private String emisorRegimen;
	// Receptor
	private String receptor;
	private String receptorRfc;
	private String receptorUsoCfdi;
	// Nomina
	private Date fechaPago;
	private Date fechaInicio;
	private Date fechaFinal;
	private int numDias;
	private BigDecimal totalPercepciones;
	private BigDecimal totalDeducciones;
	private BigDecimal totalOtrosPagos;
	private BigDecimal totalNomina;
	// Auditoria
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ComprobacionNomina() {
		this.descripcion = "";
		this.expresionImpresa = "";
		this.serie = "";
		this.folio = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaTimbrado = Calendar.getInstance().getTime();
		this.folioFiscal = "";
		// Emisor
		this.emisor = "";
		this.emisorRfc = "";
		this.emisorRegimen = "";
		// Receptor
		this.receptor = "";
		this.receptorRfc = "";
		this.receptorUsoCfdi = "";
		// Nomina
		this.fechaPago = Calendar.getInstance().getTime();
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaFinal = Calendar.getInstance().getTime();
		this.totalPercepciones = BigDecimal.ZERO;
		this.totalDeducciones = BigDecimal.ZERO;
		this.totalOtrosPagos = BigDecimal.ZERO;
		this.totalNomina = BigDecimal.ZERO;
		// Auditoria
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getExpresionImpresa() {
		return expresionImpresa;
	}

	public void setExpresionImpresa(String expresionImpresa) {
		this.expresionImpresa = expresionImpresa;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(Date fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getFolioFiscal() {
		return folioFiscal;
	}

	public void setFolioFiscal(String folioFiscal) {
		this.folioFiscal = folioFiscal;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getEmisorRfc() {
		return emisorRfc;
	}

	public void setEmisorRfc(String emisorRfc) {
		this.emisorRfc = emisorRfc;
	}

	public String getEmisorRegimen() {
		return emisorRegimen;
	}

	public void setEmisorRegimen(String emisorRegimen) {
		this.emisorRegimen = emisorRegimen;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

	public String getReceptorRfc() {
		return receptorRfc;
	}

	public void setReceptorRfc(String receptorRfc) {
		this.receptorRfc = receptorRfc;
	}

	public String getReceptorUsoCfdi() {
		return receptorUsoCfdi;
	}

	public void setReceptorUsoCfdi(String receptorUsoCfdi) {
		this.receptorUsoCfdi = receptorUsoCfdi;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public int getNumDias() {
		return numDias;
	}

	public void setNumDias(int numDias) {
		this.numDias = numDias;
	}

	public BigDecimal getTotalPercepciones() {
		return totalPercepciones;
	}

	public void setTotalPercepciones(BigDecimal totalPercepciones) {
		this.totalPercepciones = totalPercepciones;
	}

	public BigDecimal getTotalDeducciones() {
		return totalDeducciones;
	}

	public void setTotalDeducciones(BigDecimal totalDeducciones) {
		this.totalDeducciones = totalDeducciones;
	}

	public BigDecimal getTotalOtrosPagos() {
		return totalOtrosPagos;
	}

	public void setTotalOtrosPagos(BigDecimal totalOtrosPagos) {
		this.totalOtrosPagos = totalOtrosPagos;
	}

	public BigDecimal getTotalNomina() {
		return totalNomina;
	}

	public void setTotalNomina(BigDecimal totalNomina) {
		this.totalNomina = totalNomina;
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
