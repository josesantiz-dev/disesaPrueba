package net.giro.rh.admon.beans; 


import java.io.Serializable;
import java.util.Date;


public class Empleado  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Long modificadoPor;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Long idPersona; 
	private Long idArea;	
	private Long idPuestoCategoria;	
	private Date fechaIngreso;
	private Long idSucursal;
	private String numeroSeguridadSocial;
	private String nombreCasoEmergencia;
	private Long externo;
	private int estatus;
	private String email;
	//private Long idEmpresa;	//private Empresa empresa; Eliminada
	//private Persona persona;  
	//private PuestoCategoria puestoCategoria;
	//private Area area;
	
	//Campos que me dijo Augusto que agregara a la tabla de empleados
	private Long homonimo;
	private String nombre;
	private String primerNombre;
	private String segundoNombre;
	private String nombresPropios;
	private String primerApellido;
	private String segundoApellido;
	private String clave;
	
	private String areaDescripcion;
	private String puestoDescripcion;
	private String categoriaDescripcion;
	private String nombreSucursal;
	
	
	public Empleado() { }
	
	public Empleado(Long id) {
		this.id = id;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {	//meotodo necesatio para tomar el correlativo
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

	public Long getIdArea() {
		return this.idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public Long getIdPuestoCategoria() {
		return this.idPuestoCategoria;
	}

	public void setIdPuestoCategoria(Long idPuestoCategoria) {
		this.idPuestoCategoria = idPuestoCategoria;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNumeroSeguridadSocial() {
		return numeroSeguridadSocial;
	}

	public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
		this.numeroSeguridadSocial = numeroSeguridadSocial;
	}

	public String getNombreCasoEmergencia() {
		return nombreCasoEmergencia;
	}

	public void setNombreCasoEmergencia(String nombreCasoEmergencia) {
		this.nombreCasoEmergencia = nombreCasoEmergencia;
	}

	public Long getExterno() {
		return externo;
	}

	public void setExterno(Long externo) {
		this.externo = externo;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Long getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(Long homonimo) {
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

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getAreaDescripcion() {
		return areaDescripcion;
	}
	

	public void setAreaDescripcion(String areaDescripcion) {
		this.areaDescripcion = areaDescripcion;
	}
	

	public String getPuestoDescripcion() {
		return puestoDescripcion;
	}
	

	public void setPuestoDescripcion(String puestoDescripcion) {
		this.puestoDescripcion = puestoDescripcion;
	}
	

	public String getCategoriaDescripcion() {
		return categoriaDescripcion;
	}
	

	public void setCategoriaDescripcion(String categoriaDescripcion) {
		this.categoriaDescripcion = categoriaDescripcion;
	}
	

	public String getNombreSucursal() {
		return nombreSucursal;
	}
	

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}
}