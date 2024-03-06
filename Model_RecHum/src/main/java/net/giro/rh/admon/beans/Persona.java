package net.giro.rh.admon.beans;

public class Persona implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private long creadoPor;
	private java.util.Date fechaCreacion;
	private long modificadoPor;
	private java.util.Date fechaModificacion;
	private long homonimo;
	private java.lang.String nombre;
	private java.lang.String primerNombre;
	private java.lang.String segundoNombre;
	private java.lang.String nombresPropios;
	private java.lang.String primerApellido;
	private java.lang.String segundoApellido;
	private java.lang.String rfc;
	private java.lang.String sexo;
	private java.util.Date fechaNacimiento;
	private java.lang.Long estadoCivil;
	private java.lang.Long localidad;
	private java.lang.Long nacionalidad;
	private Persona conyuge;
	private long numeroHijos;
	private java.lang.String domicilio;
	private java.lang.String telefono;
	private java.lang.String correo;
	private long finado;
	private java.lang.Long tipoSangre;
	java.lang.Long colonia;
	private Long tipoPersona;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private Long banco;

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

	public long getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(long homonimo) {
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

	public java.lang.Long getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(java.lang.Long estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public java.lang.Long getLocalidad() {
		return localidad;
	}

	public void setLocalidad(java.lang.Long localidad) {
		this.localidad = localidad;
	}

	public java.lang.Long getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(java.lang.Long nacionalidad) {
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

	public long getFinado() {
		return finado;
	}

	public void setFinado(long finado) {
		this.finado = finado;
	}

	public java.lang.Long getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(java.lang.Long tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public java.lang.Long getColonia() {
		return colonia;
	}

	public void setColonia(java.lang.Long colonia) {
		this.colonia = colonia;
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

}
