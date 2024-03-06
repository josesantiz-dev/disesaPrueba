package net.giro.rh.admon.beans; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * c5f822917f
 * @author javaz
 */
public class Empleado implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idPersona; // af
	private String clave;
	private String nombre;
	private String primerNombre;
	private String segundoNombre;
	private String nombresPropios;
	private String primerApellido;
	private String segundoApellido;
	private Long homonimo;
	private Long idEmpresa;
	private Long idArea; // aj
	private String areaDescripcion;	
	private Long idPuestoCategoria;	// ak
	private String puestoDescripcion;
	private String categoriaDescripcion;
	private Date fechaIngreso; // al
	private Long idSucursal;
	private String nombreSucursal;
	private String numeroSeguridadSocial; // am
	private String nombreCasoEmergencia; // an
	private String email;
	private String emailClave;
	private Long externo;
	private int altaSeguroSocial;
	private Date fechaAltaSeguroSocial;
	private int estatus; // 0=Activo, 1=baja, 2=Finiquito, 3=Incapacitado
	private Long creadoPor;
	private Long modificadoPor;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private int sistema;
	
	
	public Empleado() {
		this.id = 0L;
		this.clave = "";
		this.nombre = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.nombresPropios = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.numeroSeguridadSocial = "";
		this.nombreCasoEmergencia = "";
		this.nombreSucursal = "";
		this.areaDescripcion = "";
		this.puestoDescripcion = "";
		this.categoriaDescripcion = "";
		this.email = "";
		this.emailClave = "";
		this.fechaIngreso = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public Empleado(Long id) {
		this();
		this.id = id;
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

	/**
	 * af
	 * @return
	 */
	public Long getIdPersona() {
		return idPersona;
	}

	/**
	 * af
	 * @return
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		if (this.nombre == null || "".equals(this.nombre.trim())) 
			this.nombre = this.getNameRaw();
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombrePorApellidos() {
		return this.getNameApels();
	}
	
	public void setNombrePorApellidos(String value) {}

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

	public Long getHomonimo() {
		return homonimo;
	}

	public void setHomonimo(Long homonimo) {
		this.homonimo = homonimo;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdArea() {
		return idArea;
	}

	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public String getAreaDescripcion() {
		return areaDescripcion;
	}

	public void setAreaDescripcion(String areaDescripcion) {
		this.areaDescripcion = areaDescripcion;
	}

	public Long getIdPuestoCategoria() {
		return idPuestoCategoria;
	}

	public void setIdPuestoCategoria(Long idPuestoCategoria) {
		this.idPuestoCategoria = idPuestoCategoria;
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

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailClave() {
		return emailClave;
	}

	public void setEmailClave(String emailClave) {
		this.emailClave = emailClave;
	}

	public Long getExterno() {
		return externo;
	}

	public void setExterno(Long externo) {
		this.externo = externo;
	}

	public int getAltaSeguroSocial() {
		return altaSeguroSocial;
	}

	public void setAltaSeguroSocial(int altaSeguroSocial) {
		this.altaSeguroSocial = altaSeguroSocial;
	}

	public Date getFechaAltaSeguroSocial() {
		return fechaAltaSeguroSocial;
	}

	public void setFechaAltaSeguroSocial(Date fechaAltaSeguroSocial) {
		this.fechaAltaSeguroSocial = fechaAltaSeguroSocial;
	}

	/**
	 * estatus:  0=Activo, 1=baja, 2=Finiquito, 3=Incapacitado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * estatus:  0=Activo, 1=baja, 2=Finiquito, 3=Incapacitado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	// -----------------------------------------------------------------------------------------------
	// Extendidos
	// -----------------------------------------------------------------------------------------------
	
	private String getNameRaw() {
		List<String> name = new ArrayList<String>();

		if (this.primerNombre != null && ! "".equals(this.primerNombre.trim()))
			name.add(this.primerNombre);
		if (this.segundoNombre != null && ! "".equals(this.segundoNombre.trim()))
			name.add(this.segundoNombre);
		if (this.primerApellido != null && ! "".equals(this.primerApellido.trim()))
			name.add(this.primerApellido);
		if (this.segundoApellido != null && ! "".equals(this.segundoApellido.trim()))
			name.add(this.segundoApellido);
			
		return StringUtils.join(name, " ");
	}
	
	private String getNameApels() {
		List<String> name = new ArrayList<String>();

		if (this.primerApellido != null && ! "".equals(this.primerApellido.trim()))
			name.add(this.primerApellido);
		if (this.segundoApellido != null && ! "".equals(this.segundoApellido.trim()))
			name.add(this.segundoApellido);
		if (this.primerNombre != null && ! "".equals(this.primerNombre.trim()))
			name.add(this.primerNombre);
		if (this.segundoNombre != null && ! "".equals(this.segundoNombre.trim()))
			name.add(this.segundoNombre);
			
		return StringUtils.join(name, " ");
	}
}