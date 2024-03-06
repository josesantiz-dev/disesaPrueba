package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

public class InsumosExt implements Serializable {
	private static final long serialVersionUID = -2893962306564997955L;
	private Long id;
	private Obra idObra;
	private String nombreObra;
	private Presupuesto idPresupuesto;
	private BigDecimal montoMateriales;
	private BigDecimal montoManoDeObra;
	private BigDecimal montoHerramientas;
	private BigDecimal montoEquipos;
	private BigDecimal montoOtros;
	private BigDecimal total;
	private String observaciones;
	private String nombreArchivo;
	private long idMoneda;
	private String moneda;
	private double tipoCambio;
	private long idEmpresa;
	private int estatus; // ESTATUS: 0 - Activa, 1 - Cancelada, 2 - Suministrada
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public InsumosExt() {
		this.nombreObra = "";
		this.observaciones = "";
		this.nombreArchivo = "";
		this.moneda = "";
		this.montoMateriales = BigDecimal.ZERO;
		this.montoManoDeObra = BigDecimal.ZERO;
		this.montoHerramientas = BigDecimal.ZERO;
		this.montoEquipos = BigDecimal.ZERO;
		this.montoOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public InsumosExt(Long id) {
		this();
		this.id = id;
	}
	
	public InsumosExt(Long id, Presupuesto idPresupuesto, Obra idObra,
			BigDecimal montoMateriales, BigDecimal montoManoDeObra, BigDecimal montoHerramientas, 
			BigDecimal montoEquipos,BigDecimal montoOtros, BigDecimal total, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion,
			int estatus, String observaciones, String nombreArchivo) {
		this();
		this.id = id;
		this.idPresupuesto = idPresupuesto;
		this.idObra = idObra;
		this.montoMateriales = montoMateriales;
		this.montoManoDeObra = montoManoDeObra;
		this.montoHerramientas = montoHerramientas;
		this.montoEquipos = montoEquipos;
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

	public Obra getIdObra() {
		return idObra;
	}

	public void setIdObra(Obra idObra) {
		this.idObra = idObra;
		if (idObra != null) 
			this.nombreObra = idObra.getNombre();
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public Presupuesto getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(Presupuesto idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
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

	public BigDecimal getMontoEquipos() {
		return montoEquipos;
	}

	public void setMontoEquipos(BigDecimal montoEquipos) {
		this.montoEquipos = montoEquipos;
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

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	/**
	 * ESTATUS: 0 - Activa, 1 - Cancelada, 2 - Suministrada
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Activa, 1 - Cancelada, 2 - Suministrada
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
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
	
	// --------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------
	
	public String getPresupuesto() {
		if (this.idPresupuesto != null &&  this.idPresupuesto.getId() > 0L)
			return this.idPresupuesto.getId() + " - " ;
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
				this.montoEquipos.doubleValue() +
				this.montoOtros.doubleValue()));
	}
	
	public void setTotalCalculado(BigDecimal value) {}

	// --------------------------------------------------------------------------
	// METODOS
	// --------------------------------------------------------------------------
	
	public void calcularTotal() {
		this.total = this.getTotalCalculado();
	}
	
 	public InsumosExt getCopia() {
		return this.Copia();
	}
	
	public InsumosExt Copia() {
		InsumosExt dest = new InsumosExt();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(dest, this);
		} catch (Exception e) {
			dest.setId(this.id);
			dest.setIdPresupuesto(this.idPresupuesto);
			dest.setIdObra(this.idObra);
			dest.setNombreObra(this.nombreObra);
			dest.setMontoMateriales(this.montoMateriales);
			dest.setMontoManoDeObra(this.montoManoDeObra);
			dest.setMontoHerramientas(this.montoHerramientas);
			dest.setMontoEquipos(this.montoEquipos);
			dest.setMontoOtros(this.montoOtros);
			dest.setTotal(this.total);
			dest.setObservaciones(this.observaciones);
			dest.setNombreArchivo(this.nombreArchivo);
			dest.setIdMoneda(this.idMoneda);
			dest.setMoneda(this.moneda);
			dest.setTipoCambio(this.tipoCambio);
			dest.setIdEmpresa(this.idEmpresa);
			dest.setEstatus(this.estatus);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
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