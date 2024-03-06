package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class InsumosExt implements Serializable {
	private static final long serialVersionUID = -2893962306564997955L;
	
	private Long id;
	private Presupuesto idPresupuesto;
	private Obra idObra;
	private BigDecimal montoMateriales;
	private BigDecimal montoManoDeObra;
	private BigDecimal montoHerramientas;
	private BigDecimal montoOtros;
	private BigDecimal total;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int estatus;
	private String observaciones;
	private String nombreArchivo;
	private Long idEmpresa;
	
	
	public InsumosExt() {
		this.montoMateriales = BigDecimal.ZERO;
		this.montoManoDeObra = BigDecimal.ZERO;
		this.montoHerramientas = BigDecimal.ZERO;
		this.montoOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public InsumosExt(Long id) {
		super();
		this.id = id;
		this.montoMateriales = BigDecimal.ZERO;
		this.montoManoDeObra = BigDecimal.ZERO;
		this.montoHerramientas = BigDecimal.ZERO;
		this.montoOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public InsumosExt(Long id, Presupuesto idPresupuesto, Obra idObra,
			BigDecimal montoMateriales, BigDecimal montoManoDeObra, BigDecimal montoHerramientas, 
			BigDecimal montoOtros, BigDecimal total, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion,
			int estatus, String observaciones, String nombreArchivo) {
		super();
		this.id = id;
		this.idPresupuesto = idPresupuesto;
		this.idObra = idObra;
		this.montoMateriales = montoMateriales;
		this.montoManoDeObra = montoManoDeObra;
		this.montoHerramientas = montoHerramientas;
		this.montoOtros = montoOtros;
		this.total = total;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.estatus = estatus;
		this.observaciones = observaciones;
		this.nombreArchivo = nombreArchivo;
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Presupuesto getIdPresupuesto() {
		return idPresupuesto;
	}
	
	public void setIdPresupuesto(Presupuesto idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
	}
	
	public BigDecimal getMontoMateriales() {
		return montoMateriales;
	}
	
	public void setMontoMateriales(BigDecimal montoMateriales) {
		this.montoMateriales = montoMateriales;
	}
	
	public BigDecimal getMontoManoDeObra() {
		return montoManoDeObra;
	}
	
	public void setMontoManoDeObra(BigDecimal montoManoDeObra) {
		this.montoManoDeObra = montoManoDeObra;
	}
	
	public BigDecimal getMontoHerramientas() {
		return montoHerramientas;
	}
	
	public void setMontoHerramientas(BigDecimal montoHerramientas) {
		this.montoHerramientas = montoHerramientas;
	}

	public BigDecimal getMontoOtros() {
		return montoOtros;
	}

	public void setMontoOtros(BigDecimal montoOtros) {
		this.montoOtros = montoOtros;
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

	// --------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------
	
	public String getPresupuesto() {
		if (this.idPresupuesto != null &&  this.idPresupuesto.getId() > 0L)
			return this.idPresupuesto.getId() + " - " ;//+ this.idPresupuesto.getPlantilla();
		return "";
	}
		
	public void setPresupuesto(String value) {}
		
	public String getObra() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getNombre() != null)
			return this.idObra.getId() + " - " + this.idObra.getNombre();
		return "";
	}
		
	public void setObra(String value) {}
	
	public String getObraNombre() {
		if (this.idObra != null && this.idObra.getNombre() != null)
			return this.idObra.getNombre();
		return "";
	}
		
	public void setObraNombre(String value) {}
	
	public BigDecimal getTotalCalculado() {
		return new BigDecimal((
				this.montoMateriales.doubleValue() + 
				this.montoManoDeObra.doubleValue() + 
				this.montoHerramientas.doubleValue() +
				this.montoOtros.doubleValue()));
	}
	
	public void setTotalCalculado(BigDecimal value) {}

	// --------------------------------------------------------------------------
	// METODOS
	// --------------------------------------------------------------------------
	
 	public InsumosExt getCopia() {
		return this.Copia();
	}
	
	public InsumosExt Copia() {
		InsumosExt dest = new InsumosExt();
		
		try {
			dest.setId(this.id);
			dest.setIdPresupuesto(this.idPresupuesto);
			dest.setIdObra(this.idObra);
			dest.setMontoMateriales(this.montoMateriales);
			dest.setMontoManoDeObra(this.montoManoDeObra);
			dest.setMontoHerramientas(this.montoHerramientas);
			dest.setMontoOtros(this.montoOtros);
			dest.setTotal(this.total);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setObservaciones(this.observaciones);
			dest.setEstatus(this.estatus);
			dest.setNombreArchivo(this.nombreArchivo);
			dest.setIdEmpresa(this.idEmpresa);
		} catch (Exception e) {
			throw e;
		}
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 05/05/2016 | Daniel Azamar	| Modificando el campo idPresupuesto del tipo "PresupuestoObraExt" al tipo "Presupuesto"
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */