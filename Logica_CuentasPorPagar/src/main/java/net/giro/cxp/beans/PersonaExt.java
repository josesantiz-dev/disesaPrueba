package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Localidad;

public class PersonaExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private java.util.Date fechaCreacion;
	private long modificadoPor;
	private java.util.Date fechaModificacion;
	private Boolean homonimo;
	private java.lang.String nombre;
	private java.lang.String primerNombre;
	private java.lang.String segundoNombre;
	private java.lang.String nombresPropios;
	private java.lang.String primerApellido;
	private java.lang.String segundoApellido;
	private java.lang.String rfc;
	private java.lang.String sexo;
	private java.util.Date fechaNacimiento;
	private ConValores estadoCivil;
	private Localidad localidad;
	private ConValores nacionalidad;
	private PersonaExt conyuge;
	private long numeroHijos;
	private java.lang.String domicilio;
	private java.lang.String telefono;
	private java.lang.String correo;
	private Boolean finado;
	private ConValores tipoSangre;
	private Colonia colonia;
	private String nombreCompleto;
	private Long tipoPersona;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private CatBancosExt banco;
	
	public PersonaExt(){
		
	}

	public PersonaExt(long id, long creadoPor, Date fechaCreacion,
			long modificadoPor, Date fechaModificacion, Boolean homonimo,
			String nombre, String primerNombre, String segundoNombre,
			String nombresPropios, String primerApellido,
			String segundoApellido, String rfc, String sexo,
			Date fechaNacimiento, ConValores estadoCivil, Localidad localidad,
			ConValores nacionalidad, PersonaExt conyuge, long numeroHijos,
			String domicilio, String telefono, String correo, Boolean finado,
			ConValores tipoSangre, Colonia colonia, String nombreCompleto) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.homonimo = homonimo;
		this.nombre = nombre;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.nombresPropios = nombresPropios;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.rfc = rfc;
		this.sexo = sexo;
		this.fechaNacimiento = fechaNacimiento;
		this.estadoCivil = estadoCivil;
		this.localidad = localidad;
		this.nacionalidad = nacionalidad;
		this.conyuge = conyuge;
		this.numeroHijos = numeroHijos;
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.correo = correo;
		this.finado = finado;
		this.tipoSangre = tipoSangre;
		this.colonia = colonia;
		this.nombreCompleto = nombreCompleto;
	}

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

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Boolean getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(Boolean homonimo) {
		this.homonimo = homonimo;
	}

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(java.lang.String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public java.lang.String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(java.lang.String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public java.lang.String getNombresPropios() {
		return nombresPropios;
	}

	public void setNombresPropios(java.lang.String nombresPropios) {
		this.nombresPropios = nombresPropios;
	}

	public java.lang.String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(java.lang.String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public java.lang.String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(java.lang.String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public java.lang.String getRfc() {
		return rfc;
	}

	public void setRfc(java.lang.String rfc) {
		this.rfc = rfc;
	}

	public java.lang.String getSexo() {
		return sexo;
	}

	public void setSexo(java.lang.String sexo) {
		this.sexo = sexo;
	}

	public java.util.Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(java.util.Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public ConValores getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(ConValores estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public ConValores getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(ConValores nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public PersonaExt getConyuge() {
		return conyuge;
	}

	public void setConyuge(PersonaExt conyuge) {
		this.conyuge = conyuge;
	}

	public long getNumeroHijos() {
		return numeroHijos;
	}

	public void setNumeroHijos(long numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	public java.lang.String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(java.lang.String domicilio) {
		this.domicilio = domicilio;
	}

	public java.lang.String getTelefono() {
		return telefono;
	}

	public void setTelefono(java.lang.String telefono) {
		this.telefono = telefono;
	}

	public java.lang.String getCorreo() {
		return correo;
	}

	public void setCorreo(java.lang.String correo) {
		this.correo = correo;
	}

	public Boolean getFinado() {
		return finado;
	}

	public void setFinado(Boolean finado) {
		this.finado = finado;
	}

	public ConValores getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(ConValores tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public Colonia getColonia() {
		return colonia;
	}

	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
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

	public CatBancosExt getBanco() {
		return banco;
	}

	public void setBanco(CatBancosExt banco) {
		this.banco = banco;
	}

	/**
	 * 1:FISICA o PERSONA, 2:MORAL o NEGOCIO
	 * @return
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
	
}
