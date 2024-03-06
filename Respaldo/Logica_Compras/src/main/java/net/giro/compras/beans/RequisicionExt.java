package net.giro.compras.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.adp.beans.ObraExt;
import net.giro.rh.admon.beans.EmpleadoExt;

public class RequisicionExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private ObraExt idObra;
	private EmpleadoExt idSolicita;
	private String nombreObra;
	private String nombreSolicita;
	private Date fecha;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private int autorizado;
	private long idUsuarioAutorizo;
	private Date fechaAutorizacion;
	private int tipo;
	private Long idMoneda;
	
	
	public RequisicionExt() {
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaAutorizacion = Calendar.getInstance().getTime();
		this.nombreObra = "";
		this.nombreSolicita = "";
	}
	
	public RequisicionExt(Long id) {
		this();
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObraExt getIdObra() {
		return idObra;
	}

	public void setIdObra(ObraExt idObra) {
		this.idObra = idObra;
		this.nombreObra = idObra.getNombre();
	}

	public EmpleadoExt getIdSolicita() {
		return idSolicita;
	}

	public void setIdSolicita(EmpleadoExt idSolicita) {
		this.idSolicita = idSolicita;
		this.nombreSolicita = idSolicita.getNombre();
	}

	public String getNombreObra() {
		return nombreObra;
	}

	public void setNombreObra(String nombreObra) {
		this.nombreObra = nombreObra;
	}

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * 0:Activa, 1:Eliminada, 2:Cotizacion
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0:Activa, 1:Eliminada, 2:Cotizacion
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

	public int getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(int autorizado) {
		this.autorizado = autorizado;
	}

	public long getIdUsuarioAutorizo() {
		return idUsuarioAutorizo;
	}

	public void setIdUsuarioAutorizo(long idUsuarioAutorizo) {
		this.idUsuarioAutorizo = idUsuarioAutorizo;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Concepto de Operacion
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * Tipo: 1 - Inventario 2 - Concepto de Operacion
	 * @return
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	// -----------------------------------------------------------------------------------------
	
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

	// -----------------------------------------------------------------------------------------	
	
	public RequisicionExt getCopia() {
		return this.Copia();
	}
	
	public RequisicionExt Copia() {
		RequisicionExt dest = new RequisicionExt();
		
		try {
			dest.setId(this.id);
			dest.setIdObra(this.idObra);
			dest.setIdSolicita(this.idSolicita);
			dest.setNombreObra(this.nombreObra);
			dest.setNombreSolicita(this.nombreSolicita);
			dest.setFecha(this.fecha);
			dest.setEstatus(this.estatus);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			dest.setAutorizado(this.autorizado);
			dest.setIdUsuarioAutorizo(this.idUsuarioAutorizo);
			dest.setFechaAutorizacion(this.fechaAutorizacion);
			dest.setTipo(this.tipo);
			dest.setIdMoneda(this.idMoneda);
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