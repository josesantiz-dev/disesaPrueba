package net.giro.plataforma.ubicacion.beans;

/**
 * bc3400dd42
 * @author javaz
 *
 */
public class Estado implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String nombre;
	java.lang.String regpob1;
	java.lang.String regpob2;
	java.lang.String regpob3;
	java.lang.String abreviatura;
	java.lang.String curp;
	net.giro.plataforma.beans.ConValores zonaEconomica;


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

	public java.lang.String getNombre() {
		return nombre;
	}

	public void setNombre(java.lang.String nombre) {
		this.nombre = nombre;
	}

	public java.lang.String getRegpob1() {
		return regpob1;
	}

	public void setRegpob1(java.lang.String regpob1) {
		this.regpob1 = regpob1;
	}

	public java.lang.String getRegpob2() {
		return regpob2;
	}

	public void setRegpob2(java.lang.String regpob2) {
		this.regpob2 = regpob2;
	}

	public java.lang.String getRegpob3() {
		return regpob3;
	}

	public void setRegpob3(java.lang.String regpob3) {
		this.regpob3 = regpob3;
	}

	public java.lang.String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(java.lang.String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public java.lang.String getCurp() {
		return curp;
	}

	public void setCurp(java.lang.String curp) {
		this.curp = curp;
	}

	public net.giro.plataforma.beans.ConValores getZonaEconomica() {
		return zonaEconomica;
	}

	public void setZonaEconomica(net.giro.plataforma.beans.ConValores zonaEconomica) {
		this.zonaEconomica = zonaEconomica;
	}

}
