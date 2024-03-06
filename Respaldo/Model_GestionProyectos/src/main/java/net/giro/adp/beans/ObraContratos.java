package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class ObraContratos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Obra idObra;
	private Long idSubcontratante;
	private String nombreSubcontratante;
	private BigDecimal monto;
	private BigDecimal iva;
	private String observaciones;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	
	public ObraContratos() {
		this.monto = BigDecimal.ZERO;
		this.iva = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ObraContratos(Long id) {
		super();
		this.id = id;
		this.monto = BigDecimal.ZERO;
		this.iva = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ObraContratos(Long id, Obra idObra, Long idSubcontratante, BigDecimal monto, BigDecimal iva, String observaciones, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idObra = idObra;
		this.idSubcontratante = idSubcontratante;
		this.monto = monto;
		this.iva = iva;
		this.observaciones = observaciones;
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
	}

	public Long getIdSubcontratante() {
		return idSubcontratante;
	}

	public void setIdSubcontratante(Long idSubcontratante) {
		this.idSubcontratante = idSubcontratante;
	}
	
	public String getNombreSubcontratante() {
		return nombreSubcontratante;
	}
	
	public void setNombreSubcontratante(String nombreSubcontratante) {
		this.nombreSubcontratante = nombreSubcontratante;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */