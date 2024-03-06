package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

public class ConceptoFacturacionImpuestosExt implements Serializable {
private static final long serialVersionUID = 1L;
	
    private Long id;
    private ConceptoFacturacion idConceptoFacturacion;
    private ConValores idImpuesto;
    private BigDecimal monto;
    private Date fechaCreacion;
    private long creadoPor;
    private Date fechaModificacion;
    private long modificadoPor;
    
    public ConceptoFacturacionImpuestosExt() {
	}

	public ConceptoFacturacionImpuestosExt(Long id) {
		this.id = id;
	}    
    
	public ConceptoFacturacionImpuestosExt(Long id, ConceptoFacturacion idConceptoFacturacion, ConValores idImpuesto, 
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

	public ConceptoFacturacion getIdConceptoFacturacion() {
		return idConceptoFacturacion;
	}

	public void setIdConceptoFacturacion(ConceptoFacturacion idConceptoFacturacion) {
		this.idConceptoFacturacion = idConceptoFacturacion;
	}

	public ConValores getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(ConValores idImpuesto) {
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

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */