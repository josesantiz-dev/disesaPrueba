package net.giro.cxc.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.giro.cxc.util.FACTURA_PAGO_ESTATUS;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;

/**
 * factura_pagos
 * @author javaz
 */
public class FacturaPagosExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date fecha;
	private FacturaExt idFactura;
	private Long idBeneficiario;
	private String beneficiario;
	private String tipoBeneficiario;
	private FormasPagos idFormaPago; 
	//private String formaPago;
	private String referenciaFormaPago;
	private Banco idBancoOrigen; 
	private BigDecimal pagoRecibido;
	private BigDecimal diferencia;
	private BigDecimal pago;
	private Moneda idMoneda;
	//private String moneda;
	private double tipoCambio;
	private CuentaBancaria idCuentaDeposito; 
	//private String cuentaBanco;
	//private String cuentaNumero;
	private int parcialidad;
	private BigDecimal saldoAnterior;
	private BigDecimal saldoInsoluto;
	private String observaciones;
	// -----------------------------
	private int timbrado; // TIMBRADO: 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	private long timbradoPor;
	private FacturaPagosTimbre idTimbre;
	private String uuid;
	private String serie;
	private String folio;
	private String agrupador;
	// -----------------------------
	private long canceladoPor;	
	private Date fechaCancelacion;
	// -----------------------------
	private int sistema; // SISTEMA: 0-Normal, 1-Sistema
	private int estatus; // ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public FacturaPagosExt() {
		this.pago = BigDecimal.ZERO;
		this.saldoAnterior = BigDecimal.ZERO;
		this.saldoInsoluto = BigDecimal.ZERO;
		this.beneficiario = "";
		this.tipoBeneficiario = "";
		this.referenciaFormaPago = "";
		this.observaciones = "";
		this.uuid = "";
		this.serie = "";
		this.folio = "";
		this.agrupador = "";
		this.fecha = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		// -------------------------------------------------------
		this.idFactura = new FacturaExt();
		this.idTimbre = new FacturaPagosTimbre();
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public FacturaExt getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(FacturaExt idFactura) {
		this.idFactura = idFactura;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		if (beneficiario == null)
			beneficiario = "";
		this.beneficiario = beneficiario;
	}

	public String getTipoBeneficiario() {
		return tipoBeneficiario;
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		if (tipoBeneficiario == null)
			tipoBeneficiario = "";
		this.tipoBeneficiario = tipoBeneficiario;
	}

	public FormasPagos getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(FormasPagos idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public String getReferenciaFormaPago() {
		return referenciaFormaPago;
	}

	public void setReferenciaFormaPago(String referenciaFormaPago) {
		if (referenciaFormaPago == null)
			referenciaFormaPago = "";
		this.referenciaFormaPago = referenciaFormaPago;
	}

	public Banco getIdBancoOrigen() {
		return idBancoOrigen;
	}

	public void setIdBancoOrigen(Banco idBancoOrigen) {
		this.idBancoOrigen = idBancoOrigen;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		if (saldoAnterior == null)
			saldoAnterior = BigDecimal.ZERO;
		this.saldoAnterior = saldoAnterior;
	}

	public BigDecimal getPagoRecibido() {
		return pagoRecibido;
	}

	public void setPagoRecibido(BigDecimal pagoRecibido) {
		if (pagoRecibido == null)
			pagoRecibido = BigDecimal.ZERO;
		this.pagoRecibido = pagoRecibido;
	}

	public BigDecimal getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(BigDecimal diferencia) {
		if (diferencia == null)
			diferencia = BigDecimal.ZERO;
		this.diferencia = diferencia;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public void setPago(BigDecimal pago) {
		if (pago == null)
			pago = BigDecimal.ZERO;
		this.pago = pago;
		calculaInsoluto();
	}

	public BigDecimal getSaldoInsoluto() {
		return saldoInsoluto;
	}

	public void setSaldoInsoluto(BigDecimal saldoInsoluto) {
		if (saldoInsoluto == null)
			saldoInsoluto = BigDecimal.ZERO;
		this.saldoInsoluto = saldoInsoluto;
	}

	public Moneda getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Moneda idMoneda) {
		this.idMoneda = idMoneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public CuentaBancaria getIdCuentaDeposito() {
		return idCuentaDeposito;
	}

	public void setIdCuentaDeposito(CuentaBancaria idCuentaDeposito) {
		this.idCuentaDeposito = idCuentaDeposito;
	}

	public int getParcialidad() {
		return parcialidad;
	}

	public void setParcialidad(int parcialidad) {
		this.parcialidad = parcialidad;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		if (observaciones == null)
			observaciones = "";
		this.observaciones = observaciones;
	}
	
	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	 * @return
	 */
	public int getTimbrado() {
		return timbrado;
	}

	/**
	 * TIMBRADO: 0-Sin Timbrar, 1-Timbrado, 2-Timbre Cancelado
	 * @param timbrado
	 */
	public void setTimbrado(int timbrado) {
		this.timbrado = timbrado;
	}

	public long getTimbradoPor() {
		return timbradoPor;
	}

	public void setTimbradoPor(long timbradoPor) {
		this.timbradoPor = timbradoPor;
	}

	public FacturaPagosTimbre getIdTimbre() {
		if (idTimbre == null)
			idTimbre = new FacturaPagosTimbre();
		return idTimbre;
	}

	public void setIdTimbre(FacturaPagosTimbre idTimbre) {
		this.idTimbre = idTimbre;
		populateTimbre();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		if (uuid == null)
			uuid = "";
		this.uuid = uuid;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		if (serie == null)
			serie = "";
		this.serie = serie;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		if (folio == null)
			folio = "";
		this.folio = folio;
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		if (agrupador == null)
			agrupador = "";
		this.agrupador = agrupador;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Sistema
	 * @return
	 */
	public int getSistema() {
		return sistema;
	}

	/**
	 * SISTEMA: 0-Normal, 1-Sistema
	 * @param sistema
	 */
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * ESTATUS: 0-Activo, 1-Cancelado, 2-Pendiente Cancelacion, 3-Pendiente Aprobacion
	 * @param estatus
	 */
	public void setEstatus(FACTURA_PAGO_ESTATUS estatus) {
		if (estatus != null)
			this.estatus = estatus.ordinal();
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

	public long getCanceladoPor() {
		return canceladoPor;
	}

	public void setCanceladoPor(long canceladoPor) {
		this.canceladoPor = canceladoPor;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	
	// -------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------------

	public String getFacturaDescripcion() {
		if (this.idFactura != null && this.idFactura.getId() != null && this.idFactura.getIdObra() != null)
			return this.idFactura.getFolioFactura() + " - " + this.idFactura.getIdObra().getClienteNombre();
		return "";
	}
	
	public void setFacturaDescripcion(String value) { }

	public double getFacturado() {
		if (this.idFactura != null && this.idFactura.getId() != null && this.idFactura.getIdObra() != null)
			return this.idFactura.getTotal().doubleValue();
		return 0;
	}
	
	public void setFacturado(double facturado) { }

	public String getFormaPago() {
		if (this.idFormaPago != null && this.idFormaPago.getId() != null && this.idFormaPago.getId() > 0L)
			return this.idFormaPago.getFormaPago();
		return "";
	}
	
	public void setFormaPago(String value) {}
	
	public String getCuentaDeposito() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getId() > 0L)
			return this.idCuentaDeposito.getDescripcion();
		return "";
	}
	
	public void setCuentaDeposito(String value) {}
	
	public String getCuentaBanco() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getInstitucionBancaria() != null && this.idCuentaDeposito.getInstitucionBancaria().getId() > 0L)
			return this.idCuentaDeposito.getInstitucionBancaria().getNombreCorto();
		return "";
	}
	
	public void setCuentaBanco(String banco) {}
	
	public String getCuentaNumero() {
		if (this.idCuentaDeposito != null && this.idCuentaDeposito.getId() > 0L)
			return this.idCuentaDeposito.getNumeroDeCuenta();
		return "";
	}
	
	public void setCuentaNumero(String numeroDeCuenta) {}
	
	public String getMoneda() {
		if (this.idMoneda != null && this.idMoneda.getId() > 0L)
			return this.idMoneda.getAbreviacion();
		return "";
	}
	
	public void setMoneda(String value) {}

	public void calculaSaldo() {
		double facturado = 0;
		double saldo = 0;
		double anterior = 0;
		
		if (this.saldoAnterior == null)
			this.saldoAnterior = BigDecimal.ZERO;
		
		if (this.idFactura != null) {
			facturado = this.idFactura.getTotal().doubleValue();
			saldo = this.idFactura.getSaldo().doubleValue();
			if (saldo == facturado)
				anterior = facturado;
			else
				anterior = saldo;
			this.saldoAnterior = new BigDecimal(anterior);
		}
	}

	public void calculaInsoluto() {
		double insoluto = 0;
		
		if (this.saldoAnterior == null)
			this.saldoAnterior = BigDecimal.ZERO;
		if (this.pago == null)
			this.pago = BigDecimal.ZERO;
		
		if (this.saldoAnterior.doubleValue() > 0 && this.pago.doubleValue() > 0) {
			insoluto = this.saldoAnterior.doubleValue() - this.pago.doubleValue();
			this.saldoInsoluto = new BigDecimal(insoluto);
		}
	}
	
	private void populateTimbre() {
		if (this.idTimbre == null)
			return;
		this.setUuid(this.idTimbre.getUuid());
		this.setSerie(this.idTimbre.getSerie());
		this.setFolio(this.idTimbre.getFolio());
		this.setTimbrado(this.idTimbre.getTimbrado());
		this.setTimbradoPor(this.idTimbre.getTimbradoPor());
	}

	// -------------------------------------------------------------------------------------------------
	// COPIA
	// -------------------------------------------------------------------------------------------------
	
	public FacturaPagosExt getCopia() {
		return this.Copia();
	}
	
	public FacturaPagosExt Copia() {
		FacturaPagosExt dest = new FacturaPagosExt();

		dest.setId(this.id);
		dest.setFecha(this.fecha);
		dest.setIdFactura(this.idFactura);
		dest.setIdBeneficiario(this.idBeneficiario);
		dest.setBeneficiario(this.beneficiario);
		dest.setTipoBeneficiario(this.tipoBeneficiario);
		dest.setIdFormaPago(this.idFormaPago);
		dest.setReferenciaFormaPago(this.referenciaFormaPago);
		dest.setIdBancoOrigen(this.idBancoOrigen);
		dest.setPagoRecibido(this.pagoRecibido);
		dest.setDiferencia(this.diferencia);
		dest.setPago(this.pago);
		dest.setIdMoneda(this.idMoneda);
		dest.setTipoCambio(this.tipoCambio);
		dest.setIdCuentaDeposito(this.idCuentaDeposito);
		dest.setParcialidad(this.parcialidad);
		dest.setSaldoAnterior(this.saldoAnterior);
		dest.setSaldoInsoluto(this.saldoInsoluto);
		dest.setObservaciones(this.observaciones);
		dest.setTimbrado(this.timbrado);
		dest.setTimbradoPor(this.timbradoPor);
		dest.setIdTimbre(this.idTimbre);
		dest.setUuid(this.uuid);
		dest.setSerie(this.serie);
		dest.setFolio(this.folio);
		dest.setAgrupador(this.agrupador);
		dest.setSistema(this.sistema);
		dest.setEstatus(this.estatus);
		dest.setCreadoPor(this.creadoPor);
		dest.setFechaCreacion(this.fechaCreacion);
		dest.setModificadoPor(this.modificadoPor);
		dest.setFechaModificacion(this.fechaModificacion);
		dest.setCanceladoPor(this.canceladoPor);
		dest.setFechaCancelacion(this.fechaCancelacion);
		
		return dest;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 * 	2.1 | 2018-03-28 | javier Tirado 	| Anado nuevas propiedades: idMoneda, tipoCambio, parcialidad, saldoAnterior, saldoInsoluto, timbrado, codigo, mensaje, xmlPrevio, xmlTimbrado y cadenaOriginal 
 *  1.2 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */