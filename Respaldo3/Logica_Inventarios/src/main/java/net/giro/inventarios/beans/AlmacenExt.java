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
	private String nombreSucursal;
	private Empleado idEncargado;
	private String nombreEncargado;
	private DomicilioExt idDomicilio; 
	private String domicilio;
	private int permitirSBO; // Permitir Solicitud a Bodega
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public AlmacenExt() {
		this.nombre = "";
		this.nombreSucursal = "";
		this.nombreEncargado = "";
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
		if (sucursal != null)
			this.nombreSucursal = sucursal.getSucursal();
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public Empleado getIdEncargado() {
		return idEncargado;
	}

	public void setIdEncargado(Empleado idEncargado) {
		this.idEncargado = idEncargado;
		if (idEncargado != null)
			this.nombreEncargado = idEncargado.getNombre();
	}

	public String getNombreEncargado() {
		return nombreEncargado;
	}

	public void setNombreEncargado(String nombreEncargado) {
		this.nombreEncargado = nombreEncargado;
	}

	public DomicilioExt getPojoDomicilio() {
		return idDomicilio;
	}

	public void setPojoDomicilio(DomicilioExt pojoDomicilio) {
		this.idDomicilio = pojoDomicilio;
		if (pojoDomicilio != null)
			this.domicilio = pojoDomicilio.getDomicilio();
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	/**
	 * Permitir Solicitud a Bodega
	 * @return
	 */
	public int getPermitirSBO() {
		return permitirSBO;
	}

	/**
	 * Permitir Solicitud a Bodega
	 * @param permitirSBO
	 */
	public void setPermitirSBO(int permitirSBO) {
		this.permitirSBO = permitirSBO;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
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

	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------
	
	public String getSucursalNombre() {
		if (this.idSucursal != null && this.idSucursal.getId() > 0L)
			return this.idSucursal.getSucursal();
		return "";
	}
	
	public void setSucursalNombre(String value) {}
	
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