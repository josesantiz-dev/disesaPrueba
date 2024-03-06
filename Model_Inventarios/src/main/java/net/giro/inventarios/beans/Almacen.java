package net.giro.inventarios.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * almacen
 * @author javaz
 *
 */
public class Almacen implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String identificador;
	private int tipo; // 1:General, 2:Obra, 3:General (Administrativo), 4:Obra (Administrativo)
	private long idSucursal;
	private String nombreSucursal;
	private long idEncargado;
	private String nombreEncargado;
	private long idDomicilio;
	private String domicilio;
	private int permitirSBO; // Permitir Solicitud a Bodega
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Almacen() {
		this.nombre = "";
		this.nombreSucursal = "";
		this.nombreEncargado = "";
		this.domicilio = "";
		this.idEncargado = 0L;
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
	 * 1:General, 2:Obra, 3:General (Administrativo), 4:Obra (Administrativo)
	 * @return
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * 1:General, 2:Obra, 3:General (Administrativo), 4:Obra (Administrativo)
	 * @param tipo
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public long getIdEncargado() {
		return idEncargado;
	}

	public void setIdEncargado(long idEncargado) {
		this.idEncargado = idEncargado;
	}

	public String getNombreEncargado() {
		return nombreEncargado;
	}

	public void setNombreEncargado(String nombreEncargado) {
		this.nombreEncargado = nombreEncargado;
	}

	public long getIdDomicilio() {
		return idDomicilio;
	}

	public void setIdDomicilio(long idDomicilio) {
		this.idDomicilio = idDomicilio;
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
	
	// --------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------
	
	/**
	 * AL: Almacen, BO: Bodega
	 * @return
	 */
	public String getTipoAlmacen() {
		return (this.tipo == 1 || this.tipo == 3) ? "AL" : "BO";
	}

	/**
	 * AL: Almacen, BO: Bodega
	 * @return
	 */
	public void setTipoAlmacen(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */