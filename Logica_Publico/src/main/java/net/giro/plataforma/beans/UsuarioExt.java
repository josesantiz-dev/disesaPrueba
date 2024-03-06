package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * dc8deac2731
 * @author javaz
 */
public class UsuarioExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String usuario;
	private String password;
	private Long idEmpleado;
	private String nombre;
	private String correo;
	private String correoClave;
	private String puesto;
	private Date fechaInicio;
	private Date fechaTerminacion;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Long expirado;
	private Date fechaCambio;
	private Boolean bloqueado;
	private Date ultimoAcceso;
	private String usuarioSpark;
	private Long version;
	
	public UsuarioExt() {
		this.usuario = "";
		this.nombre = "";
		this.puesto = "";
		this.correo = "";
		this.correoClave = "";
		this.password = "";
		this.usuarioSpark = "";
		this.fechaInicio = Calendar.getInstance().getTime();
		this.fechaTerminacion = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.fechaCambio = Calendar.getInstance().getTime();
		this.ultimoAcceso = Calendar.getInstance().getTime();
	}
	
	public UsuarioExt(long id) {
		this();
		this.id = id;
	}
	
	public UsuarioExt(long id, String usuario, String nombre, String puesto,
			String password, Date fechaIni, Date fechaTerm, Long creadoPor,
			Date fechaCreacion, Long modificadoPor, Date fechaModificacion,
			Long expirado, Date fechaCambio, Boolean bloqueado, Date ultimoAcceso,
			String usuarioSpark, String correo,  Long version) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.nombre = nombre;
		this.puesto = puesto;
		this.password = password;
		this.fechaInicio = fechaIni;
		this.fechaTerminacion = fechaTerm;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.expirado = expirado;
		this.fechaCambio = fechaCambio;
		this.bloqueado = bloqueado;
		this.ultimoAcceso = ultimoAcceso;
		this.usuarioSpark = usuarioSpark;
		this.correo = correo;
		this.version = version;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCorreoClave() {
		return correoClave;
	}

	public void setCorreoClave(String correoClave) {
		this.correoClave = correoClave;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaTerminacion() {
		return fechaTerminacion;
	}

	public void setFechaTerminacion(Date fechaTerminacion) {
		this.fechaTerminacion = fechaTerminacion;
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

	public Long getExpirado() {
		return expirado;
	}

	public void setExpirado(Long expirado) {
		this.expirado = expirado;
	}

	public Date getFechaCambio() {
		return fechaCambio;
	}

	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Date getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(Date ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public String getUsuarioSpark() {
		return usuarioSpark;
	}

	public void setUsuarioSpark(String usuarioSpark) {
		this.usuarioSpark = usuarioSpark;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
