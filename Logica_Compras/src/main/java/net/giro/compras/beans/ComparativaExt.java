package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.clientes.beans.PersonaExt;

public class ComparativaExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String folio;
	private Date fecha;
	private ObraExt idObra;
	private PersonaExt idSolicita;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public ComparativaExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public ComparativaExt(Long id) {
		this();
		this.id = id;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ObraExt getIdObra() {
		return idObra;
	}

	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
	}

	public PersonaExt getIdSolicita() {
		return idSolicita;
	}

	public void setIdSolicita(PersonaExt idSolicita) {
		this.idSolicita = idSolicita;
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
	
	
	
	public String getObra() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getId() > 0L && this.idObra.getNombre() != null)
			return this.idObra.getId() + " - " + this.idObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public String getObraNombre() {
		if (this.idObra != null && this.idObra.getId() != null && this.idObra.getId() > 0L && this.idObra.getNombre() != null)
			return this.idObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
	public String getSolicita() {
		if (this.idSolicita != null && this.idSolicita.getId() > 0L && this.idSolicita.getNombre() != null)
			return this.idSolicita.getId() + " - " + this.idSolicita.getNombre();
		return "";
	}
	
	public void setSolicita(String value) {}
	
	public String getSolicitaNombre() {
		if (this.idSolicita != null && this.idSolicita.getId() > 0L && this.idSolicita.getNombre() != null)
			return this.idSolicita.getNombre();
		return "";
	}
	
	public void setSolicitaNombre(String value) {}
	
	
	
	public ComparativaExt getCopia() {
		return this.Copia();
	}
	
	public ComparativaExt Copia() {
		ComparativaExt dest = new ComparativaExt();
		
		try {
			dest.setId(this.id);
			dest.setFolio(this.folio);
			dest.setFecha(this.fecha);
			dest.setIdObra(this.idObra);
			dest.setIdSolicita(this.idSolicita);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
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
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */