package net.giro.tyg.admon;

public class Cheques implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	java.lang.Long id;
	java.lang.String control;
	java.lang.Long bancoId;
	java.util.Date fecha;
	java.math.BigDecimal importe;
	java.lang.String folio;
	java.lang.String tipo;
	java.lang.String estatus;
	java.lang.Long creadoPor;
	java.util.Date fechaCreacion;
	java.lang.Long modificadoPor;
	java.util.Date fechaModificacion;
	java.lang.Long ministracion;
	java.lang.String concMensaje;
	java.lang.Long concUsuarioId;
	java.util.Date concFecha;
	java.lang.String poliza;
	java.lang.String grupo;
	java.lang.String transaccionId;
	java.lang.String tarjetaOperativaId;
	java.lang.Long app;
	java.lang.Long transaccionVersionId;
	java.lang.Long disposicion;
	java.lang.String tipoTrans;


	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getControl() {
		return control;
	}

	public void setControl(java.lang.String control) {
		this.control = control;
	}

	public java.lang.Long getBancoId() {
		return bancoId;
	}

	public void setBancoId(java.lang.Long bancoId) {
		this.bancoId = bancoId;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public java.math.BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(java.math.BigDecimal importe) {
		this.importe = importe;
	}

	public java.lang.String getFolio() {
		return folio;
	}

	public void setFolio(java.lang.String folio) {
		this.folio = folio;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}

	public java.lang.String getEstatus() {
		return estatus;
	}

	public void setEstatus(java.lang.String estatus) {
		this.estatus = estatus;
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

	public java.lang.Long getMinistracion() {
		return ministracion;
	}

	public void setMinistracion(java.lang.Long ministracion) {
		this.ministracion = ministracion;
	}

	public java.lang.String getConcMensaje() {
		return concMensaje;
	}

	public void setConcMensaje(java.lang.String concMensaje) {
		this.concMensaje = concMensaje;
	}

	public java.lang.Long getConcUsuarioId() {
		return concUsuarioId;
	}

	public void setConcUsuarioId(java.lang.Long concUsuarioId) {
		this.concUsuarioId = concUsuarioId;
	}

	public java.util.Date getConcFecha() {
		return concFecha;
	}

	public void setConcFecha(java.util.Date concFecha) {
		this.concFecha = concFecha;
	}

	public java.lang.String getPoliza() {
		return poliza;
	}

	public void setPoliza(java.lang.String poliza) {
		this.poliza = poliza;
	}

	public java.lang.String getGrupo() {
		return grupo;
	}

	public void setGrupo(java.lang.String grupo) {
		this.grupo = grupo;
	}

	public java.lang.String getTransaccionId() {
		return transaccionId;
	}

	public void setTransaccionId(java.lang.String transaccionId) {
		this.transaccionId = transaccionId;
	}

	public java.lang.String getTarjetaOperativaId() {
		return tarjetaOperativaId;
	}

	public void setTarjetaOperativaId(java.lang.String tarjetaOperativaId) {
		this.tarjetaOperativaId = tarjetaOperativaId;
	}

	public java.lang.Long getApp() {
		return app;
	}

	public void setApp(java.lang.Long app) {
		this.app = app;
	}

	public java.lang.Long getTransaccionVersionId() {
		return transaccionVersionId;
	}

	public void setTransaccionVersionId(java.lang.Long transaccionVersionId) {
		this.transaccionVersionId = transaccionVersionId;
	}

	public java.lang.Long getDisposicion() {
		return disposicion;
	}

	public void setDisposicion(java.lang.Long disposicion) {
		this.disposicion = disposicion;
	}

	public java.lang.String getTipoTrans() {
		return tipoTrans;
	}

	public void setTipoTrans(java.lang.String tipoTrans) {
		this.tipoTrans = tipoTrans;
	}

}
