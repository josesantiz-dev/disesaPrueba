package net.giro.plataforma.ubicacion.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;

/**
 * a810505fff
 * @author javaz
 *
 */
public class Localidad implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id; // aa
	private String nombre; // af
	private Municipio municipio; // ag
	private ConValores zona; // ah
	private String regpob1; // ai
	private String regpob2; // aj
	private String regpob3; // ak
	private long habitantes; // al
	private long lada; //am
	private BigDecimal indiceMarginalidad; // an
	private String gradoMarginalidad; // ao
	private long creadoPor; // ab
	private Date fechaCreacion; // ac
	private long modificadoPor; // ad
	private Date fechaModificacion; //ae
	
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

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public ConValores getZona() {
		return zona;
	}

	public void setZona(ConValores zona) {
		this.zona = zona;
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

	public long getHabitantes() {
		return habitantes;
	}

	public void setHabitantes(long habitantes) {
		this.habitantes = habitantes;
	}

	public long getLada() {
		return lada;
	}

	public void setLada(long lada) {
		this.lada = lada;
	}

	public BigDecimal getIndiceMarginalidad() {
		return indiceMarginalidad;
	}

	public void setIndiceMarginalidad(BigDecimal indiceMarginalidad) {
		this.indiceMarginalidad = indiceMarginalidad;
	}

	public String getGradoMarginalidad() {
		return gradoMarginalidad;
	}

	public void setGradoMarginalidad(String gradoMarginalidad) {
		this.gradoMarginalidad = gradoMarginalidad;
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
