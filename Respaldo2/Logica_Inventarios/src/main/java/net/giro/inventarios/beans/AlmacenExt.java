package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.ne.beans.Sucursal;
import net.giro.rh.admon.beans.Empleado;

/**
 * almacen
 * @author javaz
 *
 */
public class AlmacenExt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String identificador;
	private int tipo; // 1 - GENERAL, 2 - OBRA
	private Sucursal idSucursal;
	private Empleado idEncargado;
	private DomicilioExt idDomicilio; 
	private String domicilio;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private Long idEmpresa;
	
	
	public AlmacenExt() {
		this.nombre = "";
		this.domicilio = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * 1 - GENERAL, 2 - OBRA
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * 1 - GENERAL, 2 - OBRA
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Sucursal getSucursal() {
		return idSucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.idSucursal = sucursal;
	}

	public Empleado getIdEncargado() {
		return idEncargado;
	}

	public void setIdEncargado(Empleado idEncargado) {
		this.idEncargado = idEncargado;
	}

	public DomicilioExt getPojoDomicilio() {
		return idDomicilio;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		this.idDomicilio = pojoDomicilio;
		this.domicilio = this.idDomicilio.getDomicilio();
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public int getEstatus() {
		return estatus;
	}

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

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------
	
	public String getEncargado() {
		if (this.idEncargado != null && this.idEncargado.getId() != null && this.idEncargado.getId() > 0L)
			return this.idEncargado.getId() + " - " + this.idEncargado.getNombre();
		return "";
	}
	
	public void setEncargado(String value) {}
	
	public String getEncargadoNombre() {
		if (this.idEncargado != null && this.idEncargado.getId() != null && this.idEncargado.getId() > 0L)
			return this.idEncargado.getNombre();
		return "";
	}
	
	public void setEncargadoNombre(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */