package net.giro.cxp.beans;

import java.util.Date;

public class SucursalBancariaExt implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6216158712857148475L;
	private long id;
	private String nombre;
	private String domicilio;
	private Long estado;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;
	private CatBancosExt catBancoId;
	private String sucursalBancariaId;
	
	public long getId() {
		return id;
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
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
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
	public CatBancosExt getCatBancoId() {
		return catBancoId;
	}
	public void setCatBancoId(CatBancosExt catBancoId){
		this.catBancoId = catBancoId;
	}
	public String getSucursalBancariaId() {
		return sucursalBancariaId;
	}
	public void setSucursalBancariaId(String sucursalBancariaId) {
		this.sucursalBancariaId = sucursalBancariaId;
	}


	


}
