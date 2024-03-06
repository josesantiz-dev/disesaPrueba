package net.giro.adp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * concepto_presupuesto
 * @author javaz
 *
 */
public class ConceptoPresupuesto implements Serializable {	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String concepto;
	private String variable;
	private String formula;
	private String formulaPorcentajeInsumos;
	private String formulaPorcentajeTotal;
	private int orden;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private Long idEmpresa;

	
	public ConceptoPresupuesto() {
		this.concepto = "";
		this.variable = "";
		this.formula = "";
		this.formulaPorcentajeInsumos = "";
		this.formulaPorcentajeTotal = "";
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

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormulaPorcentajeInsumos() {
		return formulaPorcentajeInsumos;
	}

	public void setFormulaPorcentajeInsumos(String formulaPorcentajeInsumos) {
		this.formulaPorcentajeInsumos = formulaPorcentajeInsumos;
	}

	public String getFormulaPorcentajeTotal() {
		return formulaPorcentajeTotal;
	}

	public void setFormulaPorcentajeTotal(String formulaPorcentajeTotal) {
		this.formulaPorcentajeTotal = formulaPorcentajeTotal;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
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