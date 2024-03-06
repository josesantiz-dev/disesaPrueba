package net.giro.rh.admon.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.clientes.beans.Persona;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;

public class EmpleadoExt implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// Fields
	private Long id;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private Empresa empresa;
	private Area area;
	private PuestoCategoriaExt puestoCategoria;
	private Date fechaIngreso;
	private String numeroSeguridadSocial;
	private String nombreCasoEmergencia;
	private Long externo;
	private Persona persona;  
	private int estatus;
	private String email;
	
	
	//Campos que me dijo Augusto que agregara a la tabla de empleados
	private Long homonimo;
	private String nombre;
	private String primerNombre;
	private String segundoNombre;
	private String nombresPropios;
	private String primerApellido;
	private String segundoApellido;

	private String clave;
	private Sucursal sucursal;
	
	private String areaDescripcion;
	private String puestoDescripcion;
	private String categoriaDescripcion;
	private String nombreSucursal;
	
	public EmpleadoExt() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
		
		if (area != null && area.getDescripcion() != null)
			this.areaDescripcion = area.getDescripcion();
	}

	public PuestoCategoriaExt getPuestoCategoria() {
		return puestoCategoria;
	}

	public void setPuestoCategoria(PuestoCategoriaExt puestoCategoria) {
		this.puestoCategoria = puestoCategoria;
		
		if (puestoCategoria != null && puestoCategoria.getCategoria() != null && puestoCategoria.getCategoria().getDescripcion() != null)
			this.categoriaDescripcion = puestoCategoria.getCategoria().getDescripcion();
		
		if (puestoCategoria != null && puestoCategoria.getPuesto() != null && puestoCategoria.getPuesto().getDescripcion() != null)
			this.puestoDescripcion = puestoCategoria.getPuesto().getDescripcion();
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

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
		if (sucursal != null && sucursal.getSucursal() != null)
			this.nombreSucursal = sucursal.getSucursal();
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
