package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * insumos_presupuesto
 * @author javaz
 *
 */
public class Insumos implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idPresupuesto;
	private Long idObra;
	private BigDecimal totalMateriales;
	private BigDecimal totalManoDeObra;
	private BigDecimal totalHerramientas;
	private BigDecimal totalOtros;
	private BigDecimal total;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private String observaciones;
	private String nombreArchivo;
	private Long idEmpresa;

	
	public Insumos() {
		this.totalMateriales = BigDecimal.ZERO;
		this.totalManoDeObra = BigDecimal.ZERO;
		this.totalHerramientas = BigDecimal.ZERO;
		this.totalOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Insumos(Long id) {
		this.id = id;
		this.totalMateriales = BigDecimal.ZERO;
		this.totalManoDeObra = BigDecimal.ZERO;
		this.totalHerramientas = BigDecimal.ZERO;
		this.totalOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Insumos(Long id, Long idPresupuesto, Long idObra, 
			BigDecimal totalMateriales, BigDecimal totalManoDeObra, BigDecimal totalHerramientas, 
			BigDecimal totalOtros, BigDecimal total, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion, int estatus) {
		super();
		this.id = id;
		this.idPresupuesto = idPresupuesto;
		this.idObra = idObra;
		this.totalMateriales = totalMateriales;
		this.totalManoDeObra = totalManoDeObra;
		this.totalHerramientas = totalHerramientas;
		this.totalOtros = totalOtros;
		this.total = total;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.estatus = estatus; 
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
	
	public Long getIdPresupuesto() {
		return idPresupuesto;
	}
	
	public void setIdPresupuesto(Long idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}
	
	public BigDecimal getTotalMateriales() {
		return totalMateriales;
	}
	
	public void setTotalMateriales(BigDecimal totalMateriales) {
		this.totalMateriales = totalMateriales;
	}
	
	public BigDecimal getTotalManoDeObra() {
		return totalManoDeObra;
	}
	
	public void setTotalManoDeObra(BigDecimal totalManoDeObra) {
		this.totalManoDeObra = totalManoDeObra;
	}
	
	public BigDecimal getTotalHerramientas() {
		return totalHerramientas;
	}
	
	public void setTotalHerramientas(BigDecimal totalHerramientas) {
		this.totalHerramientas = totalHerramientas;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
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

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public BigDecimal getTotalOtros() {
		return totalOtros;
	}

	public void setTotalOtros(BigDecimal totalOtros) {
		this.totalOtros = totalOtros;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */