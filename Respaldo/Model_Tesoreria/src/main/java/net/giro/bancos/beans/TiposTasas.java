package net.giro.bancos.beans;

public class TiposTasas implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.String clave;
	java.math.BigDecimal valorTasa;
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

	public java.lang.String getClave() {
		return clave;
	}

	public void setClave(java.lang.String clave) {
		this.clave = clave;
	}

	public java.math.BigDecimal getValorTasa() {
		return valorTasa;
	}

	public void setValorTasa(java.math.BigDecimal valorTasa) {
		this.valorTasa = valorTasa;
	}

	public net.giro.bancos.beans.Tasas getTasas() {
		return tasas;
	}

	public void setTasas(net.giro.bancos.beans.Tasas tasas) {
		this.tasas = tasas;
	}

}
