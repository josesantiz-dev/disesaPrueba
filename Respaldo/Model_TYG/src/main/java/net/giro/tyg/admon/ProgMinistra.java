package net.giro.tyg.admon;

public class ProgMinistra implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.Long sucursal;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	java.util.Date del;
	java.util.Date al;
	java.math.BigDecimal total;
	java.lang.String estatus;
	java.lang.Long transferenciaId;
	java.math.BigDecimal montoAutorizado;
	java.lang.String observaciones;
	java.math.BigDecimal montoRevisado;
	java.lang.Long numLote;
	java.util.Date fechaRevision;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getSucursal() {
		return sucursal;
	}

	public void setSucursal(java.lang.Long sucursal) {
		this.sucursal = sucursal;
	}

	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public java.util.Date getDel() {
		return del;
	}

	public void setDel(java.util.Date del) {
		this.del = del;
	}

	public java.util.Date getAl() {
		return al;
	}

	public void setAl(java.util.Date al) {
		this.al = al;
	}

	public java.math.BigDecimal getTotal() {
		return total;
	}

	public void setTotal(java.math.BigDecimal total) {
		this.total = total;
	}

	public java.lang.String getEstatus() {
		return estatus;
	}

	public void setEstatus(java.lang.String estatus) {
		this.estatus = estatus;
	}

	public java.lang.Long getTransferenciaId() {
		return transferenciaId;
	}

	public void setTransferenciaId(java.lang.Long transferenciaId) {
		this.transferenciaId = transferenciaId;
	}

	public java.math.BigDecimal getMontoAutorizado() {
		return montoAutorizado;
	}

	public void setMontoAutorizado(java.math.BigDecimal montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}

	public java.lang.String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(java.lang.String observaciones) {
		this.observaciones = observaciones;
	}

	public java.math.BigDecimal getMontoRevisado() {
		return montoRevisado;
	}

	public void setMontoRevisado(java.math.BigDecimal montoRevisado) {
		this.montoRevisado = montoRevisado;
	}

	public java.lang.Long getNumLote() {
		return numLote;
	}

	public void setNumLote(java.lang.Long numLote) {
		this.numLote = numLote;
	}

	public java.util.Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(java.util.Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

}
