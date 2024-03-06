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
	private long idPresupuesto;
	private long idObra;
	private String nombreObra;
	private BigDecimal totalMateriales;
	private BigDecimal totalManoDeObra;
	private BigDecimal totalHerramientas;
	private BigDecimal totalEquipos;
	private BigDecimal totalOtros;
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

	public Insumos() {
		this.nombreObra = "";
		this.observaciones = "";
		this.nombreArchivo = "";
		this.moneda = "";
		this.totalMateriales = BigDecimal.ZERO;
		this.totalManoDeObra = BigDecimal.ZERO;
		this.totalHerramientas = BigDecimal.ZERO;
		this.totalEquipos = BigDecimal.ZERO;
		this.totalOtros = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Insumos(Long id) {
		this();
		this.id = id;
	}
	
	public Insumos(Long id, Long idPresupuesto, Long idObra, 
			BigDecimal totalMateriales, BigDecimal totalManoDeObra, BigDecimal totalHerramientas, 
			BigDecimal totalEquipos,BigDecimal totalOtros, BigDecimal total, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion, int estatus) {
		this();
		this.id = id;
		this.idPresupuesto = idPresupuesto;
		this.idObra = idObra;
		this.totalMateriales = totalMateriales;
		this.totalManoDeObra = totalManoDeObra;
		this.totalHerramientas = totalHerramientas;
		this.totalEquipos = totalEquipos;
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

	public long getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(long idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}

	public long getIdObra() {
		return idObra;
	}

	public void setIdObra(long idObra) {
		this.idObra = idObra;
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
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

	public BigDecimal getTotalEquipos() {
		return totalEquipos;
	}

	public void setTotalEquipos(BigDecimal totalEquipos) {
		this.totalEquipos = totalEquipos;
	}

	public BigDecimal getTotalOtros() {
		return totalOtros;
	}

	public void setTotalOtros(BigDecimal totalOtros) {
		this.totalOtros = totalOtros;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */