package net.giro.cxp.beans;

import java.util.Date;

import net.giro.ne.beans.CalendarioDiaInhabil;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Regiones;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Colonia;

public class SucursalExt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	long modificadoPor;
	Date fechaCreacion;
	Date fechaModificacion;
	String sucursal;
	Colonia colonia;
	String domicilio;
	ConValores domicilio1;
	ConValores domicilio2;
	ConValores domicilio3;
	ConValores domicilio4;
	ConValores domicilio5;
	String descDomicilio1;
	String descDomicilio2;
	String descDomicilio3;
	String descDomicilio4;
	String descDomicilio5;
	Empresa empresa;
	Regiones region;
	CalendarioDiaInhabil calendarioDiaInhabil;
	String segmentoContable;
	String segmentoNegocio;
	
	public SucursalExt() {
		this.id = 0L;
		this.creadoPor = 0L;
		this.modificadoPor = 0L;
		this.fechaCreacion = new Date();
		this.fechaModificacion = new Date();
		this.sucursal = "";
		this.colonia = new Colonia();
		this.domicilio = "";
		this.domicilio1 = new ConValores();
		this.domicilio2 = new ConValores();
		this.domicilio3 = new ConValores();
		this.domicilio4 = new ConValores();
		this.domicilio5 = new ConValores();
		this.descDomicilio1 = "";
		this.descDomicilio2 = "";
		this.descDomicilio3 = "";
		this.descDomicilio4 = "";
		this.descDomicilio5 = "";
		this.empresa = new Empresa();
		this.region = new Regiones();;
		this.calendarioDiaInhabil = new CalendarioDiaInhabil();
		this.segmentoContable = "";
		this.segmentoNegocio = "";
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

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
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

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Regiones getRegion() {
		return region;
	}

	public void setRegion(Regiones region) {
		this.region = region;
	}

	public CalendarioDiaInhabil getCalendarioDiaInhabil() {
		return calendarioDiaInhabil;
	}

	public void setCalendarioDiaInhabil(CalendarioDiaInhabil calendarioDiaInhabil) {
		this.calendarioDiaInhabil = calendarioDiaInhabil;
	}

	public String getSegmentoContable() {
		return segmentoContable;
	}

	public void setSegmentoContable(String segmentoContable) {
		this.segmentoContable = segmentoContable;
	}

	public String getSegmentoNegocio() {
		return segmentoNegocio;
	}

	public void setSegmentoNegocio(String segmentoNegocio) {
		this.segmentoNegocio = segmentoNegocio;
	}

}
