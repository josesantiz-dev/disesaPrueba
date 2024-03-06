package net.giro.clientes.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;

public class NegocioExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private String nombre;
	private String domicilio;
	private String telefono;
	private ConValores giro;
	private Date inicioOperaciones;
	private String formaOperacion;
	private ConValores sector;
	private String referencia;
	private String nombreContacto;
	private String correoContacto;
	private Long bloqueado;
	private String tipoPersonalidad;
	private String rfc;
	private Colonia colonia;
	private boolean estatus;
	private String paginaWeb;
	private Long numeroEmpleados;
	private String numeroCuenta;
	private String clabeInterbancaria;
	private Long banco;
	private int sistema;
	
	public NegocioExt() {}
	
	public NegocioExt(long id, Long creadoPor, Date fechaCreacion,
			Long modificadoPor, Date fechaModificacion, String nombre,
			String domicilio, String telefono, ConValores giro,
			Date inicioOperaciones, String formaOperacion, ConValores sector,
			String referencia, String nombreContacto, String correoContacto,
			Long bloqueado, String tipoPersonalidad, String rfc, Colonia colonia) {
		this.id = id;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.giro = giro;
		this.inicioOperaciones = inicioOperaciones;
		this.formaOperacion = formaOperacion;
		this.sector = sector;
		this.referencia = referencia;
		this.nombreContacto = nombreContacto;
		this.correoContacto = correoContacto;
		this.bloqueado = bloqueado;
		this.tipoPersonalidad = tipoPersonalidad;
		this.rfc = rfc;
		this.colonia = colonia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public ConValores getGiro() {
		return giro;
	}

	public void setGiro(ConValores giro) {
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

	public ConValores getSector() {
		return sector;
	}

	public void setSector(ConValores sector) {
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

	public Long getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Long bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getTipoPersonalidad() {
		return tipoPersonalidad;
	}

	public void setTipoPersonalidad(String tipoPersonalidad) {
		this.tipoPersonalidad = tipoPersonalidad;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Colonia getColonia() {
		return colonia;
	}

	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}

	public boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(boolean estatus) {
		this.estatus = estatus;
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
