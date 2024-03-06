package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * c81498d458
 * @author javaz
 */
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long homonimo;
	private Long tipoPersona; // 1:FISICA o PERSONA, 2:MORAL o NEGOCIO
	private String nombre;
	private String primerNombre;
	private String segundoNombre;
	private String nombresPropios;
	private String primerApellido;
	private String segundoApellido;
	private String rfc;
	private String sexo;
	private Date fechaNacimiento;
	private Long estadoCivil;
	private Long localidad;
	private Long nacionalidad;
	private Persona conyuge;
	private long numeroHijos;
	private String domicilio;
	private String telefono;
	private String correo;
	private long finado;
	private Long tipoSangre;
	private Long colonia;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private Long banco;
	private int tipoAsignacion; // 0: Persona, Proveedor, Contacto 1: Empleado
	private int sistema; // 0: No, 1:Si
	private int estatus; // 0: Activo, 1:Eliminado
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(long homonimo) {
		this.homonimo = homonimo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getNombresPropios() {
		return nombresPropios;
	}

	public void setNombresPropios(String nombresPropios) {
		this.nombresPropios = nombresPropios;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(Long estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Long getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Long localidad) {
		this.localidad = localidad;
	}

	public Long getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Long nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Persona getConyuge() {
		return conyuge;
	}

	public void setConyuge(Persona conyuge) {
		this.conyuge = conyuge;
	}

	public long getNumeroHijos() {
		return numeroHijos;
	}

	public void setNumeroHijos(long numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public long getFinado() {
		return finado;
	}

	public void setFinado(long finado) {
		this.finado = finado;
	}

	public Long getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(Long tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public Long getColonia() {
		return colonia;
	}

	public void setColonia(Long colonia) {
		this.colonia = colonia;
	}

	/**
	 * 1:FISICA o PERSONA, 2:MORAL o NEGOCIO
	 * @param tipoPersona
	 */
	public Long getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * 1:FISICA o PERSONA, 2:MORAL o NEGOCIO
	 * @param tipoPersona
	 */
	public void setTipoPersona(Long tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}

	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}

	public Long getBanco() {
		return banco;
	}

	public void setBanco(Long banco) {
		this.banco = banco;
	}

	/**
	 * 0: Activo, 1:Eliminado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * 0: Activo, 1:Eliminado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * 0: No, 1:Si
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * 0: No, 1:Si
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * 0: Persona, Proveedor, Contacto 1: Empleado
	 * @return
	 */
	public int getTipoAsignacion() {
		return tipoAsignacion;
	}

	/**
	 * 0: Persona, Proveedor, Contacto 1: Empleado
	 * @param tipoAsignacion
	 */
	public void setTipoAsignacion(int tipoAsignacion) {
		this.tipoAsignacion = tipoAsignacion;
	}
}
