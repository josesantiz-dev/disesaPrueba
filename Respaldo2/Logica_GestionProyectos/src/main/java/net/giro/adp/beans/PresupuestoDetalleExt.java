package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class PresupuestoDetalleExt implements Serializable {
	private static final long serialVersionUID = 1L;

	//private Long idPlantilla;
	private Long id;
	private Long idPresupuesto;
	private ConceptoPresupuesto conceptoPresupuesto;
	private BigDecimal monto;
	private double porcentaje;
	private double porcentajeTotales;
	private String notas;
	private String formula;
	private double ejercido;
	private double transito;
	private double aditiva;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private boolean montoCapturable;
	private boolean porcentajeCapturable;
	private boolean porcentajeTotalesCapturable;
	

	public PresupuestoDetalleExt() {
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.montoCapturable = false;
		this.porcentajeCapturable = false;
		this.porcentajeTotalesCapturable = false;
	}
	
	public PresupuestoDetalleExt(Long id) {
		super();
		this.id = id;
		this.monto = BigDecimal.ZERO;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.montoCapturable = false;
		this.porcentajeCapturable = false;
		this.porcentajeTotalesCapturable = false;
	}

	public PresupuestoDetalleExt(Long id, Long idPresupuesto, ConceptoPresupuesto idConceptoPresupuesto, BigDecimal monto,
			double porcentaje, double porcentajeTotales, String notas, String formula, double ejercido, double transito,
			double aditiva, long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idPresupuesto = idPresupuesto;
		this.conceptoPresupuesto = idConceptoPresupuesto;
		this.monto = monto;
		this.porcentaje = porcentaje;
		this.porcentajeTotales = porcentajeTotales;
		this.notas = notas;
		this.formula = formula;
		this.ejercido = ejercido;
		this.transito = transito;
		this.aditiva = aditiva;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.montoCapturable = false;
		this.porcentajeCapturable = false;
		this.porcentajeTotalesCapturable = false;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(Long idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}

	public ConceptoPresupuesto getConceptoPresupuesto() {
		return conceptoPresupuesto;
	}

	public void setConceptoPresupuesto(ConceptoPresupuesto conceptoPresupuesto) {
		this.conceptoPresupuesto = conceptoPresupuesto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public double getPorcentajeTotales() {
		return porcentajeTotales;
	}

	public void setPorcentajeTotales(double porcentajeTotales) {
		this.porcentajeTotales = porcentajeTotales;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public double getEjercido() {
		return ejercido;
	}

	public void setEjercido(double ejercido) {
		this.ejercido = ejercido;
	}

	public double getTransito() {
		return transito;
	}

	public void setTransito(double transito) {
		this.transito = transito;
	}

	public double getAditiva() {
		return aditiva;
	}

	public void setAditiva(double aditiva) {
		this.aditiva = aditiva;
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

	public boolean isMontoCapturable() {
		return montoCapturable;
	}

	public void setMontoCapturable(boolean montoCapturable) {
		this.montoCapturable = montoCapturable;
	}

	public boolean isPorcentajeCapturable() {
		return porcentajeCapturable;
	}

	public void setPorcentajeCapturable(boolean porcentajeCapturable) {
		this.porcentajeCapturable = porcentajeCapturable;
	}

	public boolean isPorcentajeTotalesCapturable() {
		return porcentajeTotalesCapturable;
	}

	public void setPorcentajeTotalesCapturable(boolean porcentajeTotalesCapturable) {
		this.porcentajeTotalesCapturable = porcentajeTotalesCapturable;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 05/05/2016 | Daniel Azamar	| Creando la clase PresupuestoDetalleExt
 *  2.2	| 22/06/2016 | Javier Tirado	| Agrego la propiedad porcentajeTotales
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */