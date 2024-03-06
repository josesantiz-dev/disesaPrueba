package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.giro.plataforma.beans.ConValores;
import net.giro.tyg.admon.FormasPagos;

public class AsignacionGruposExt implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private Transacciones idTransaccion; // NUMERIC
	private Grupos idGrupoDebito; // NUMERIC
	private Grupos idGrupoCredito; // NUMERIC
	private Conceptos idConcepto;
	private FormasPagos idFormaPago; // NUMERIC
	private ConValores tipoPoliza; //ConValores tipoPoliza;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public AsignacionGruposExt() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public AsignacionGruposExt(Long id) {
		this();
		this.id = id;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Transacciones getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Transacciones idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public Grupos getIdGrupoDebito() {
		return idGrupoDebito;
	}

	public void setIdGrupoDebito(Grupos idGrupoDebito) {
		this.idGrupoDebito = idGrupoDebito;
	}

	public Grupos getIdGrupoCredito() {
		return idGrupoCredito;
	}

	public void setIdGrupoCredito(Grupos idGrupoCredito) {
		this.idGrupoCredito = idGrupoCredito;
	}

	public Conceptos getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Conceptos idConcepto) {
		this.idConcepto = idConcepto;
	}

	public FormasPagos getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(FormasPagos idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public ConValores getTipoPoliza() {
		return tipoPoliza;
	}

	public void setTipoPoliza(ConValores tipoPoliza) {
		this.tipoPoliza = tipoPoliza;
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

	// ---------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------
	
	public String getTransaccion() {
		if (this.idTransaccion != null && this.idTransaccion.getId() != null && this.idTransaccion.getId() > 0L && this.idTransaccion.getDescripcion() != null)
			return this.idTransaccion.getId() + " - " + this.idTransaccion.getDescripcion();
		return "";
	}
	
	public void setTransaccion(String value) {}

	public long getTransaccionId() {
		if (this.idTransaccion != null && this.idTransaccion.getId() != null && this.idTransaccion.getId() > 0L)
			return this.idTransaccion.getId();
		return 0L;
	}
	
	public void setTransaccionId(long value) {}

	public String getTransaccionCodigo() {
		if (this.idTransaccion != null && this.idTransaccion.getId() != null && this.idTransaccion.getId() > 0L)
			return this.idTransaccion.getCodigo().toString();
		return "";
	}
	
	public void setTransaccionCodigo(String value) {}

	public String getTransaccionDescripcion() {
		if (this.idTransaccion != null && this.idTransaccion.getId() != null && this.idTransaccion.getId() > 0L && this.idTransaccion.getDescripcion() != null)
			return this.idTransaccion.getDescripcion();
		return "";
	}
	
	public void setTransaccionDescripcion(String value) {}
	
	public String getGrupoDebito() {
		if (this.idGrupoDebito != null && this.idGrupoDebito.getId() != null && this.idGrupoDebito.getId() > 0L && this.idGrupoDebito.getDescripcion() != null)
			return this.idGrupoDebito.getId() + " - " + this.idGrupoDebito.getDescripcion();
		return "";
	}
	
	public void setGrupoDebito(String value) {}

	public long getGrupoDebitoId() {
		if (this.idGrupoDebito != null && this.idGrupoDebito.getId() != null && this.idGrupoDebito.getId() > 0L)
			return this.idGrupoDebito.getId();
		return 0L;
	}
	
	public void setGrupoDebitoId(long value) {}

	public String getGrupoDebitoDescripcion() {
		if (this.idGrupoDebito != null && this.idGrupoDebito.getId() != null && this.idGrupoDebito.getId() > 0L && this.idGrupoDebito.getDescripcion() != null)
			return this.idGrupoDebito.getDescripcion();
		return "";
	}
	
	public void setGrupoDebitoDescripcion(String value) {}
	
	public String getGrupoCredito() {
		if (this.idGrupoCredito != null && this.idGrupoCredito.getId() != null && this.idGrupoCredito.getId() > 0L && this.idGrupoCredito.getDescripcion() != null)
			return this.idGrupoCredito.getId() + " - " + this.idGrupoCredito.getDescripcion();
		return "";
	}
	
	public void setGrupoCredito(String value) {}

	public long getGrupoCreditoId() {
		if (this.idGrupoCredito != null && this.idGrupoCredito.getId() != null && this.idGrupoCredito.getId() > 0L)
			return this.idGrupoCredito.getId();
		return 0L;
	}
	
	public void setGrupoCreditoId(long value) {}

	public String getGrupoCreditoDescripcion() {
		if (this.idGrupoCredito != null && this.idGrupoCredito.getId() != null && this.idGrupoCredito.getId() > 0L && this.idGrupoCredito.getDescripcion() != null)
			return this.idGrupoCredito.getDescripcion();
		return "";
	}
	
	public void setGrupoCreditoDescripcion(String value) {}

	public String getConcepto() {
		if (this.idConcepto != null && this.idConcepto.getId() != null && this.idConcepto.getId() > 0L && this.idConcepto.getDescripcion() != null)
			return this.idConcepto.getId() + " - " + this.idConcepto.getDescripcion();
		return "";
	}
	
	public void setConcepto(String value) {}

	public long getConceptoId() {
		if (this.idConcepto != null && this.idConcepto.getId() != null && this.idConcepto.getId() > 0L)
			return this.idConcepto.getId();
		return 0L;
	}
	
	public void setConceptoId(long value) {}

	public String getConceptoDescripcion() {
		if (this.idConcepto != null && this.idConcepto.getId() != null && this.idConcepto.getId() > 0L && this.idConcepto.getDescripcion() != null)
			return this.idConcepto.getDescripcion();
		return "";
	}
	
	public void setConceptoDescripcion(String value) {}
	
	public String getFormaPago() {
		if (this.idFormaPago != null && this.idFormaPago.getId() > 0L && this.idFormaPago.getFormaPago() != null)
			return this.idFormaPago.getId() + " - " + this.idFormaPago.getFormaPago();
		return "";
	}
	
	public void setFormaPago(String value) {}

	public long getFormaPagoId() {
		if (this.idFormaPago != null && this.idFormaPago.getId() > 0L)
			return this.idFormaPago.getId();
		return 0L;
	}
	
	public void setFormaPagoId(long value) {}

	public String getFormaPagoDescripcion() {
		if (this.idFormaPago != null && this.idFormaPago.getId() > 0L && this.idFormaPago.getFormaPago() != null)
			return this.idFormaPago.getFormaPago();
		return "";
	}
	
	public void setFormaPagoDescripcion(String value) {}
	
	public String getTipoPolizaFull() {
		if (this.tipoPoliza != null && this.tipoPoliza.getId() > 0L && this.tipoPoliza.getValor() != null)
			return this.tipoPoliza.getId() + " - " + this.tipoPoliza.getValor();
		return "";
	}
	
	public void setTipoPolizaFull(String value) {}
	
	public Long getTipoPolizaId() {
		if (this.tipoPoliza != null && this.tipoPoliza.getId() > 0L )
			return this.tipoPoliza.getId();
		return 0L;
	}
	
	public void setTipoPolizaId(Long value) {}
	
	public String getTipoPolizaDescripcion() {
		if (this.tipoPoliza != null && this.tipoPoliza.getId() > 0L && this.tipoPoliza.getValor() != null)
			return this.tipoPoliza.getId() + " - " + this.tipoPoliza.getValor();
		return "";
	}
	
	public void setTipoPolizaDescripcion(String value) {}

	// -------------------------------------------------------------------------------------------------------
	// COPIA
	// -------------------------------------------------------------------------------------------------------
	
	public AsignacionGruposExt getCopia() {
		return this.copia();
	}
	
	public AsignacionGruposExt copia() {
		try {
			AsignacionGruposExt dest = new AsignacionGruposExt();
			dest.setId(this.id);
			dest.setIdTransaccion(this.idTransaccion);
			dest.setIdGrupoDebito(this.idGrupoDebito);
			dest.setIdGrupoCredito(this.idGrupoCredito);
			dest.setIdConcepto(this.idConcepto);
			dest.setIdFormaPago(this.idFormaPago);
			dest.setTipoPoliza(this.tipoPoliza);
			dest.setCreadoPor(this.creadoPor);
			dest.setFechaCreacion(this.fechaCreacion);
			dest.setModificadoPor(this.modificadoPor);
			dest.setFechaModificacion(this.fechaModificacion);
			return dest;
		} catch(Exception e) {
			System.out.println("Error al intentar recuperar copia de Logica_Contabilidad.AsignacionGruposExt");
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */