package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Localidad;

public class PersonaExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	private Boolean homonimo;
	private String nombre;
	private String primerNombre;
	private String segundoNombre;
	private String nombresPropios;
	private String primerApellido;
	private String segundoApellido;
	private String rfc;
	private String sexo;
	private Date fechaNacimiento;
	private ConValores estadoCivil;
	private Localidad localidad;
	private ConValores nacionalidad;
	private PersonaExt conyuge;
	private long numeroHijos;
	private String domicilio;
	private String telefono;
	private String correo;
	private Boolean finado;
	private ConValores tipoSangre;
	private Colonia colonia;
	private String nombreCompleto;
	private Long tipoPersona;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private Long banco;
	private int estatus; // 0: Activo, 1:Eliminado
	private int sistema; // 0: No, 1:Si
	
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
			ConValores tipoSangre, Colonia colonia, String nombreCompleto,Long tipoPersona,
			String numeroCuenta, String clabeInerbancaria) {
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
		this.tipoPersona = tipoPersona;
		this.numeroCuenta = numeroCuenta;
		this.clabeInterbancaria = clabeInerbancaria;
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

	public Boolean getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(Boolean homonimo) {
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

	public Long getTipoPersona() {
		return tipoPersona;
	}

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
}
