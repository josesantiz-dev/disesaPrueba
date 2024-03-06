package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * e769c352b7
 * @author javaz
 *
 */
public class Negocio implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String domicilio;
	private Long colonia; // at
	private String telefono;
	private String rfc;
	private Long giro; // ai
	private Date inicioOperaciones;
	private String formaOperacion;
	private Long sector; // al
	private String referencia;
	private String nombreContacto;
	private String correoContacto;
	private String tipoPersonalidad; // M - Moral, F - Fisica
	private String paginaWeb;
	private Long numeroEmpleados; // aw
	private Long banco; // ay
	private String clabeInterbancaria;
	private String numeroCuenta;
	private long bloqueado; // ap :: BLOQUEADO: 0 - No, 1 - Si
	private int sistema; // SISTEMA: 0 - No, 1 - Si
	private long estatus; // au :: ESTATUS: 1 - Activo, 2 - Eliminado,Inabilitado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Negocio() {
		this.nombre = "";
		this.domicilio = "";
		this.telefono = "";
		this.rfc = "";
		this.formaOperacion = "";
		this.referencia = "";
		this.nombreContacto = "";
		this.correoContacto = "";
		this.tipoPersonalidad = "";
		this.paginaWeb = "";
		this.clabeInterbancaria = "";
		this.numeroCuenta = "";
		this.colonia = 0L;
		this.giro = 0L;
		this.sector = 0L;
		this.numeroEmpleados = 0L;
		this.banco = 0L;
		this.fechaCreacion = Calendar.getInstance().getTime();
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

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public Long getColonia() {
		return colonia;
	}

	public void setColonia(Long colonia) {
		this.colonia = colonia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Long getGiro() {
		return giro;
	}

	public void setGiro(Long giro) {
		this.giro = giro;
	}

	public Date getInicioOperaciones() {
		return inicioOperaciones;
	}

	public void setInicioOperaciones(Date inicioOperaciones) {
		this.inicioOperaciones = inicioOperaciones;
	}

	public String getFormaOperacion() {
		return formaOperacion;
	}

	public void setFormaOperacion(String formaOperacion) {
		this.formaOperacion = formaOperacion;
	}

	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getCorreoContacto() {
		return correoContacto;
	}

	public void setCorreoContacto(String correoContacto) {
		this.correoContacto = correoContacto;
	}

	/**
	 * M - Moral, F - Fisica
	 * @param tipoPersonalidad
	 */
	public String getTipoPersonalidad() {
		return tipoPersonalidad;
	}

	/**
	 * M - Moral, F - Fisica
	 * @param tipoPersonalidad
	 */
	public void setTipoPersonalidad(String tipoPersonalidad) {
		this.tipoPersonalidad = tipoPersonalidad;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public Long getNumeroEmpleados() {
		return numeroEmpleados;
	}

	public void setNumeroEmpleados(Long numeroEmpleados) {
		this.numeroEmpleados = numeroEmpleados;
	}

	public Long getBanco() {
		return banco;
	}

	public void setBanco(Long banco) {
		this.banco = banco;
	}

	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}

	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	/**
	 * ap :: BLOQUEADO: 0 - No, 1 - Si
	 * @return
	 */
	public long getBloqueado() {
		return bloqueado;
	}

	/**
	 * ap :: BLOQUEADO: 0 - No, 1 - Si
	 * @param bloqueado
	 */
	public void setBloqueado(long bloqueado) {
		this.bloqueado = bloqueado;
	}

	/**
	 * SISTEMA: 0 - No, 1 - Si
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * SISTEMA: 0 - No, 1 - Si
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * au :: ESTATUS: 1 - Activo, 2 - Eliminado,Inabilitado
	 * @return
	 */
	public long getEstatus() {
		return estatus;
	}

	/**
	 * au :: ESTATUS: 1 - Activo, 2 - Eliminado,Inabilitado
	 * @param estatus
	 */
	public void setEstatus(long estatus) {
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
