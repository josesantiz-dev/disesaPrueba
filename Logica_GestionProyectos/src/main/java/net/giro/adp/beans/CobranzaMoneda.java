package net.giro.adp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CobranzaMoneda implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idMoneda;
	private String moneda;
	private String abreviatura;
	private List<ObraCobranza> listCobranza;
	// ----------------------------------------------
	private double porcentajeIva;
	private double facturado;
	private double anticipo;
	private double estimacion;
	private double amortizacion;
	private double fondoGarantia;
	private double pagoFondoGarantia;
	private double subtotal;
	private double iva;
	private double cargos;
	private double total;
	private double cobrado;
	private int paginacion;
	
	public CobranzaMoneda() {
		this.moneda = "";
		this.abreviatura = "";
		this.paginacion = 1;
		this.listCobranza = new ArrayList<ObraCobranza>();
	}
	
	public CobranzaMoneda(long idMoneda, String moneda, String abreviatura, List<ObraCobranza> cobranza) {
		this();
		if (idMoneda > 0L)
			this.idMoneda = idMoneda;
		if (moneda != null && ! "".equals(moneda.trim()))
			this.moneda = moneda;
		if (abreviatura != null && ! "".equals(abreviatura.trim()))
			this.abreviatura = abreviatura;
		if (cobranza != null && ! cobranza.isEmpty()) 
			setListCobranza(cobranza);
	}
	
	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public List<ObraCobranza> getListCobranza() {
		return listCobranza;
	}

	public void setListCobranza(List<ObraCobranza> listCobranza) {
		this.listCobranza = listCobranza;
		calcular();
	}

	public int getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(int paginacion) {
		this.paginacion = paginacion;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getFacturado() {
		return facturado;
	}

	public void setFacturado(double facturado) {
		this.facturado = facturado;
	}

	public double getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(double anticipo) {
		this.anticipo = anticipo;
	}

	public double getEstimacion() {
		return estimacion;
	}

	public void setEstimacion(double estimacion) {
		this.estimacion = estimacion;
	}

	public double getAmortizacion() {
		return amortizacion;
	}

	public void setAmortizacion(double amortizacion) {
		this.amortizacion = amortizacion;
	}

	public double getFondoGarantia() {
		return fondoGarantia;
	}

	public void setFondoGarantia(double fondoGarantia) {
		this.fondoGarantia = fondoGarantia;
	}

	public double getpagoFondoGarantia() {
		return pagoFondoGarantia;
	}

	public void setpagoFondoGarantia(double pagoFondoGarantia) {
		this.pagoFondoGarantia = pagoFondoGarantia;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getCargos() {
		return cargos;
	}

	public void setCargos(double cargos) {
		this.cargos = cargos;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getCobrado() {
		return cobrado;
	}

	public void setCobrado(double cobrado) {
		this.cobrado = cobrado;
	}
	
	// -----------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------
	
	public String getDescripcion() {
		return "Cobranza " + this.abreviatura + " (" + this.moneda + ")";
	}
	
	public void setDescripcion(String value) {}
	
	public void calcular() {
		this.facturado = 0;
		this.anticipo = 0;
		this.estimacion = 0;
		this.amortizacion = 0;
		this.fondoGarantia = 0;
		this.pagoFondoGarantia = 0;
		this.subtotal = 0;
		this.iva = 0;
		this.cargos = 0;
		this.total = 0;
		this.cobrado = 0;
		
		if (this.listCobranza == null || this.listCobranza.isEmpty())
			return;
		
		this.porcentajeIva = this.listCobranza.get(0).getPorcentajeIva().doubleValue();
		for (ObraCobranza item : this.listCobranza) {
			this.facturado += item.getFacturaTotal().doubleValue();
			this.anticipo += item.getAnticipo().doubleValue();
			this.estimacion += item.getEstimacion().doubleValue();
			this.amortizacion += item.getAmortizacion().doubleValue();
			this.fondoGarantia += item.getFondoGarantia().doubleValue();
			this.pagoFondoGarantia += item.getPagoFondoGarantia().doubleValue();
			this.subtotal += item.getSubtotal().doubleValue();
			this.iva += item.getIva().doubleValue();
			this.cargos += item.getCargos().doubleValue();
			this.total += item.getTotal().doubleValue();
			this.cobrado += item.getFacturaCobrado().doubleValue();
		}
	}
}
