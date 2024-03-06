package net.giro.bancos.beans;

public class ValoresTasas implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.Integer anio;
	java.math.BigDecimal enero;
	java.math.BigDecimal febrero;
	java.math.BigDecimal marzo;
	java.math.BigDecimal abril;
	java.math.BigDecimal mayo;
	java.math.BigDecimal junio;
	java.math.BigDecimal julio;
	java.math.BigDecimal agosto;
	java.math.BigDecimal septiembre;
	java.math.BigDecimal octubre;
	java.math.BigDecimal noviembre;
	java.math.BigDecimal diciembre;
	net.giro.bancos.beans.Tasas tasas;


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

	public java.lang.Integer getAnio() {
		return anio;
	}

	public void setAnio(java.lang.Integer anio) {
		this.anio = anio;
	}

	public java.math.BigDecimal getEnero() {
		return enero;
	}

	public void setEnero(java.math.BigDecimal enero) {
		this.enero = enero;
	}

	public java.math.BigDecimal getFebrero() {
		return febrero;
	}

	public void setFebrero(java.math.BigDecimal febrero) {
		this.febrero = febrero;
	}

	public java.math.BigDecimal getMarzo() {
		return marzo;
	}

	public void setMarzo(java.math.BigDecimal marzo) {
		this.marzo = marzo;
	}

	public java.math.BigDecimal getAbril() {
		return abril;
	}

	public void setAbril(java.math.BigDecimal abril) {
		this.abril = abril;
	}

	public java.math.BigDecimal getMayo() {
		return mayo;
	}

	public void setMayo(java.math.BigDecimal mayo) {
		this.mayo = mayo;
	}

	public java.math.BigDecimal getJunio() {
		return junio;
	}

	public void setJunio(java.math.BigDecimal junio) {
		this.junio = junio;
	}

	public java.math.BigDecimal getJulio() {
		return julio;
	}

	public void setJulio(java.math.BigDecimal julio) {
		this.julio = julio;
	}

	public java.math.BigDecimal getAgosto() {
		return agosto;
	}

	public void setAgosto(java.math.BigDecimal agosto) {
		this.agosto = agosto;
	}

	public java.math.BigDecimal getSeptiembre() {
		return septiembre;
	}

	public void setSeptiembre(java.math.BigDecimal septiembre) {
		this.septiembre = septiembre;
	}

	public java.math.BigDecimal getOctubre() {
		return octubre;
	}

	public void setOctubre(java.math.BigDecimal octubre) {
		this.octubre = octubre;
	}

	public java.math.BigDecimal getNoviembre() {
		return noviembre;
	}

	public void setNoviembre(java.math.BigDecimal noviembre) {
		this.noviembre = noviembre;
	}

	public java.math.BigDecimal getDiciembre() {
		return diciembre;
	}

	public void setDiciembre(java.math.BigDecimal diciembre) {
		this.diciembre = diciembre;
	}

	public net.giro.bancos.beans.Tasas getTasas() {
		return tasas;
	}

	public void setTasas(net.giro.bancos.beans.Tasas tasas) {
		this.tasas = tasas;
	}

}
