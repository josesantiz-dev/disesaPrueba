package net.giro.ne.beans;

import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;


public class Empresa implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String empresa;
	private String rfc;
	private Long creadoPor;
	private Long modificadoPor;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Date fechaBaja;
	private Long monedaId;
	private Colonia	colonia;
	private String	domicilio;
	private ConValores	domicilio1;
	private ConValores	domicilio2;
	private ConValores	domicilio3;
	private ConValores	domicilio4;
	private ConValores	domicilio5;
	private String	descDomicilio1;
	private String	descDomicilio2;
	private String	descDomicilio3;
	private String	descDomicilio4;
	private String	descDomicilio5;
	private String paginaWeb;
	private String correoElectronico;
	private String telefono;

	public Empresa() {
	}

	public Empresa(Long id) {
		this.id = id;
	}
	
	public Empresa(Long id, String empresa) {
		this.id = id;
		this.empresa = empresa;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
 
	public Long getMonedaId() {
		return monedaId;
	}

	public void setMonedaId(Long monedaId) {
		this.monedaId = monedaId;
	}
	
	public Colonia getColonia() {
		return colonia;
	}

	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	
	public ConValores getDomicilio1() {
		return domicilio1;
	}

	public void setDomicilio1(ConValores domicilio1) {
		this.domicilio1 = domicilio1;
	}

	public ConValores getDomicilio2() {
		return domicilio2;
	}

	public void setDomicilio2(ConValores domicilio2) {
		this.domicilio2 = domicilio2;
	}

	public ConValores getDomicilio3() {
		return domicilio3;
	}

	public void setDomicilio3(ConValores domicilio3) {
		this.domicilio3 = domicilio3;
	}

	public ConValores getDomicilio4() {
		return domicilio4;
	}

	public void setDomicilio4(ConValores domicilio4) {
		this.domicilio4 = domicilio4;
	}

	public ConValores getDomicilio5() {
		return domicilio5;
	}

	public void setDomicilio5(ConValores domicilio5) {
		this.domicilio5 = domicilio5;
	}

	public String getDescDomicilio1() {
		return descDomicilio1;
	}

	public void setDescDomicilio1(String descDomicilio1) {
		this.descDomicilio1 = descDomicilio1;
	}

	public String getDescDomicilio2() {
		return descDomicilio2;
	}

	public void setDescDomicilio2(String descDomicilio2) {
		this.descDomicilio2 = descDomicilio2;
	}

	public String getDescDomicilio3() {
		return descDomicilio3;
	}

	public void setDescDomicilio3(String descDomicilio3) {
		this.descDomicilio3 = descDomicilio3;
	}

	public String getDescDomicilio4() {
		return descDomicilio4;
	}

	public void setDescDomicilio4(String descDomicilio4) {
		this.descDomicilio4 = descDomicilio4;
	}

	public String getDescDomicilio5() {
		return descDomicilio5;
	}

	public void setDescDomicilio5(String descDomicilio5) {
		this.descDomicilio5 = descDomicilio5;
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}