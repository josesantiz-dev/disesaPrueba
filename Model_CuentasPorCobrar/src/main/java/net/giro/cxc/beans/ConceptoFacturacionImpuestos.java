package net.giro.cxc.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * concepto_facturacion_impuestos
 * @author javaz
 *
 */
public class ConceptoFacturacionImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
    private Long id;
    private Long idConceptoFacturacion;
    private Long idImpuesto;
    private Date fechaCreacion;
    private long creadoPor;
    private Date fechaModificacion;
    private long modificadoPor;
    
    public ConceptoFacturacionImpuestos() {
		this.id = 0L;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public ConceptoFacturacionImpuestos(Long id) {
		this();
		this.id = id;
	}    
    
	public ConceptoFacturacionImpuestos(Long id, Long idConceptoFacturacion, Long idImpuesto, 
			Date fechaCreacion, long creadoPor, Date fechaModificacion, long modificadoPor) {	
		this.id = id;
		this.idConceptoFacturacion = idConceptoFacturacion;
		this.idImpuesto = idImpuesto;
		this.fechaCreacion = fechaCreacion;
		this.creadoPor = creadoPor;
		this.fechaModificacion = fechaModificacion;
		this.modificadoPor = modificadoPor;
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

	public Long getIdConceptoFacturacion() {
		return idConceptoFacturacion;
	}

	public void setIdConceptoFacturacion(Long idConceptoFacturacion) {
		this.idConceptoFacturacion = idConceptoFacturacion;
	}

	public Long getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(Long idImpuesto) {
		this.idImpuesto = idImpuesto;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}    
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */