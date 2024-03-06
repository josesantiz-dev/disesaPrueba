package net.giro.plataforma.ubicacion.beans;

import java.io.Serializable;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

/**
 * bc3400dd42
 * @author javaz
 *
 */
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id; // aa
	private String nombre; // af
	private String regpob1; // ag
	private String regpob2; // ah
	private String regpob3; // ai
	private String abreviatura; // aj
	private String curp; // ak
	private ConValores zonaEconomica; // al
	private long creadoPor; // ab
	private Date fechaCreacion; // ac
	private long modificadoPor; // ad
	private Date fechaModificacion; // ae
	
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

	public String getRegpob1() {
		return regpob1;
	}

	public void setRegpob1(String regpob1) {
		this.regpob1 = regpob1;
	}

	public String getRegpob2() {
		return regpob2;
	}

	public void setRegpob2(String regpob2) {
		this.regpob2 = regpob2;
	}

	public String getRegpob3() {
		return regpob3;
	}

	public void setRegpob3(String regpob3) {
		this.regpob3 = regpob3;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public ConValores getZonaEconomica() {
		return zonaEconomica;
	}

	public void setZonaEconomica(ConValores zonaEconomica) {
		this.zonaEconomica = zonaEconomica;
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
}
