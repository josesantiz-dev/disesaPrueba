package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_satics
 * @author javaz
 *
 */
public class ObraSatics implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Obra idObra;
	private String nombreObra;
	private Long idContratoObra;
	private Long idSatics;
	private String nombreSatics;
	private String numero;
	private Date fecha;
	private Date fechaFinalizacion;
	private Long idSaticsAdendum;
	private String nombreSaticsAdendum;
	private int tipo;
	private BigDecimal importe;
	private String observaciones;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private int pdfCargado;

	
	public ObraSatics() {
		this.nombreObra = "";
		this.nombreSatics = "";
		this.numero = "";
		this.nombreSaticsAdendum = "";
		this.observaciones = "";
		this.importe = BigDecimal.ZERO;
		this.fecha = Calendar.getInstance().getTime();
		this.fechaFinalizacion = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ObraSatics(Long id) {
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public Long getIdContratoObra() {
		return idContratoObra;
	}

	public void setIdContratoObra(Long idContratoObra) {
		this.idContratoObra = idContratoObra;
	}

	public Long getIdSatics() {
		return idSatics;
	}

	public void setIdSatics(Long idSatics) {
		this.idSatics = idSatics;
	}

	public String getNombreSatics() {
		return nombreSatics;
	}

	public void setNombreSatics(String nombreSatics) {
		this.nombreSatics = nombreSatics;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public Long getIdSaticsAdendum() {
		return idSaticsAdendum;
	}

	public void setIdSaticsAdendum(Long idSaticsAdendum) {
		this.idSaticsAdendum = idSaticsAdendum;
	}

	public String getNombreSaticsAdendum() {
		return nombreSaticsAdendum;
	}

	public void setNombreSaticsAdendum(String nombreSaticsAdendum) {
		this.nombreSaticsAdendum = nombreSaticsAdendum;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public int getPdfCargado() {
		return pdfCargado;
	}

	public void setPdfCargado(int pdfCargado) {
		this.pdfCargado = pdfCargado;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2	| 23/05/2016 | Javier Tirado	| Creando la clase ObraSatics
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */