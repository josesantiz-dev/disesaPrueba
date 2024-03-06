package net.giro.tyg.admon;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * monedas_valores
 * @author javaz
 *
 */
public class MonedasValores implements java.io.Serializable {
	private static final long serialVersionUID = 3385696465484716223L;
	
	private Long id;
	private Moneda monedaBase;
	private Moneda monedaDestino;
	private Date fechaDesde;
	private Date fechaHasta;
	private BigDecimal valor;
	private Long creadoPor;
	private Date fechaCreacion;
	private Long modificadoPor;
	private Date fechaModificacion;


	public MonedasValores() {
		this.fechaDesde = Calendar.getInstance().getTime();
		this.fechaHasta = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.valor = BigDecimal.ZERO;
	}
	
	public MonedasValores(Long id) {
		this();
		this.id = id;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Moneda getMonedaBase() {
		return monedaBase;
	}

	public void setMonedaBase(Moneda monedaBase) {
		this.monedaBase = monedaBase;
	}

	public Moneda getMonedaDestino() {
		return monedaDestino;
	}

	public void setMonedaDestino(Moneda monedaDestino) {
		this.monedaDestino = monedaDestino;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(Long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
}
