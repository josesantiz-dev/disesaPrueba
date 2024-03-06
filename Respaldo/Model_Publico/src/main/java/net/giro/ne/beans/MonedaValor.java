package net.giro.ne.beans;

public class MonedaValor implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	long creadoPor;
	java.util.Date fechaCreacion;
	long modificadoPor;
	java.util.Date fechaModificacion;
	net.giro.ne.beans.Moneda monedaBase;
	net.giro.ne.beans.Moneda monedaDestino;
	java.util.Date desde;
	java.util.Date hasta;
	java.math.BigDecimal valor;


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

	public net.giro.ne.beans.Moneda getMonedaBase() {
		return monedaBase;
	}

	public void setMonedaBase(net.giro.ne.beans.Moneda monedaBase) {
		this.monedaBase = monedaBase;
	}

	public net.giro.ne.beans.Moneda getMonedaDestino() {
		return monedaDestino;
	}

	public void setMonedaDestino(net.giro.ne.beans.Moneda monedaDestino) {
		this.monedaDestino = monedaDestino;
	}

	public java.util.Date getDesde() {
		return desde;
	}

	public void setDesde(java.util.Date desde) {
		this.desde = desde;
	}

	public java.util.Date getHasta() {
		return hasta;
	}

	public void setHasta(java.util.Date hasta) {
		this.hasta = hasta;
	}

	public java.math.BigDecimal getValor() {
		return valor;
	}

	public void setValor(java.math.BigDecimal valor) {
		this.valor = valor;
	}

}
