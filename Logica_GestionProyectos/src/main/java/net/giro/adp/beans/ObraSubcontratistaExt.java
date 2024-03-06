package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObraSubcontratistaExt extends ObraSubcontratista implements Serializable {
	private static final long serialVersionUID = 1L;
	private int index;
	private boolean nuevo;
	private boolean porAnticipo;
	private boolean porRetencion;
	private boolean anticipo;
	private boolean estimacion;
	private boolean amortizacion;
	private boolean fondoGarantia;
	private boolean cargos;
	private boolean guardable;
	private List<ObraSubcontratistaImpuestosExt> listImpuestos;
	
	public ObraSubcontratistaExt() {
		super();
		this.index = -1;
		this.nuevo = true;
		this.guardable = false;
		this.porAnticipo = false;
		this.porRetencion = false;
		this.anticipo = false;
		this.estimacion = false;
		this.amortizacion = false;
		this.fondoGarantia = false;
		this.cargos = false;
		this.listImpuestos = new ArrayList<ObraSubcontratistaImpuestosExt>();
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean isNuevo() {
		return nuevo;
	}
	
	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
		this.guardable = nuevo ? nuevo : this.guardable;
	}
	
	public double getPorcentajeAnticipo() {
		return super.getPorcentajeAnticipo();
	}
	
	public void setPorcentajeAnticipo(double value) {
		this.porAnticipo = true;
		this.guardable = true;
		super.setPorcentajeAnticipo(value);
	}
	
	public double getPorcentajeRetencion() {
		return super.getPorcentajeRetencion();
	}
	
	public void setPorcentajeRetencion(double value) {
		this.porRetencion = true;
		this.guardable = true;
		super.setPorcentajeRetencion(value);
	}
	
	public BigDecimal getAnticipo() {
		return super.getAnticipo();
	}
	
	public void setAnticipo(BigDecimal value) {
		this.anticipo = true;
		this.guardable = true;
		super.setAnticipo(value);
	}
	
	public BigDecimal getEstimacion() {
		return super.getEstimacion();
	}
	
	public void setEstimacion(BigDecimal value) {
		this.estimacion = true;
		this.guardable = true;
		super.setEstimacion(value);
	}

	public BigDecimal getAmortizacion() {
		return super.getAmortizacion();
	}
	
	public void setAmortizacion(BigDecimal value) {
		this.amortizacion = true;
		this.guardable = true;
		super.setAmortizacion(value);
	}

	public BigDecimal getFondoGarantia() {
		return super.getFondoGarantia();
	}
	
	public void setFondoGarantia(BigDecimal value) {
		this.fondoGarantia = true;
		this.guardable = true;
		super.setFondoGarantia(value);
	}
	
	public BigDecimal getCargos() {
		return super.getCargos();
	}
	
	public void setCargos(BigDecimal value) {
		this.cargos = true;
		this.guardable = true;
		super.setCargos(value);
	}
	
	public List<ObraSubcontratistaImpuestosExt> getListImpuestos() {
		this.listImpuestos = (this.listImpuestos != null ? this.listImpuestos : new ArrayList<ObraSubcontratistaImpuestosExt>());
		return listImpuestos;
	}
	
	public void setListImpuestos(List<ObraSubcontratistaImpuestosExt> listImpuestos) {
		listImpuestos = (listImpuestos != null ? listImpuestos : new ArrayList<ObraSubcontratistaImpuestosExt>());
		this.listImpuestos = listImpuestos;
	}

	public boolean isGuardable() {
		return guardable;
	}

	public void setGuardable(boolean guardable) {
		this.guardable = guardable;
	}
	
	// --------------------------------------------------------------------------------
	// EXTENDIDOS 
	// --------------------------------------------------------------------------------

	public double getTotalImpuestos() {
		if (super.getImpuestos().doubleValue() > 0 && super.getRetenciones().doubleValue() > 0 && (super.getImpuestos().doubleValue() - super.getRetenciones().doubleValue()) > 0)
			return (super.getImpuestos().doubleValue() - super.getRetenciones().doubleValue());
		else if (super.getImpuestos().doubleValue() > 0 && super.getRetenciones().doubleValue() <= 0)
			return super.getImpuestos().doubleValue();
		else if (super.getImpuestos().doubleValue() <= 0 && super.getRetenciones().doubleValue() > 0)
			return super.getRetenciones().doubleValue();
		return 0;
	}
	
	public void setTotalImpuesots(double value) {}
	
	public boolean getModificado() {
		return (this.nuevo || this.porAnticipo || this.porRetencion || this.anticipo || this.estimacion || this.amortizacion || this.fondoGarantia || this.cargos);
	}
	
	public void setModificado(boolean value) {}

	// --------------------------------------------------------------------------------
	// METODOS 
	// --------------------------------------------------------------------------------
	
	public void addImpuesto(long idImpuesto, String nombreImpuesto, double tasa, double base, boolean retencion, boolean excluir) {
		ObraSubcontratistaImpuestosExt impuesto = null;
		BigDecimal baseImpuesto = null;
		BigDecimal importe = null;

		try {
			idImpuesto = idImpuesto > 0L ? idImpuesto : 0L;
			nombreImpuesto = nombreImpuesto != null && ! "".equals(nombreImpuesto.trim()) ? nombreImpuesto.trim() : "";
			tasa = tasa > 0 ? tasa : 0;
			base = base > 0 ? base : 0;
			baseImpuesto = new BigDecimal(base);
			importe = new BigDecimal(((base * tasa) / 100));
			// -----------------------------------------------------------------------------------------------------------
			impuesto = new ObraSubcontratistaImpuestosExt();
			impuesto.setId(0L);
			impuesto.setIdObraSubcontratista(null);
			impuesto.setIdImpuesto(idImpuesto);
			impuesto.setNombreImpuesto(nombreImpuesto);
			impuesto.setTasaImpuesto(tasa);
			impuesto.setBaseImpuesto(baseImpuesto);
			impuesto.setImporte(importe);
			impuesto.setRetencion((retencion ? 1 : 0));
			impuesto.setExcluir((excluir ? 1 : 0));
			impuesto.setCreadoPor(this.getCreadoPor());
			impuesto.setFechaCreacion(this.getFechaCreacion());
			impuesto.setModificadoPor(this.getModificadoPor());
			impuesto.setFechaModificacion(this.getFechaModificacion());
			// -----------------------------------------------------------------------------------------------------------
			this.listImpuestos.add(impuesto);
			recalcular();
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public void delImpuesto(long idImpuesto) {
		if (this.listImpuestos.isEmpty())
			return;
		for (ObraSubcontratistaImpuestosExt impuesto : this.listImpuestos) {
			if (idImpuesto == impuesto.getIdImpuesto()) {
				this.listImpuestos.remove(impuesto);
				recalcular();
				break;
			}
		}
	}
	
	public void delImpuesto(int index) {
		if (index < 0 || index > this.listImpuestos.size())
			return;
		this.listImpuestos.remove(index);
		recalcular();
	}
	
	public void calcular() {
		double porcentaje = 0;
		double value = 0;
		
		if (super.getEstimacion().doubleValue() > 0 && (this.porAnticipo || this.porRetencion)) {
			// Amortizacion
			porcentaje = super.getPorcentajeAnticipo();
			porcentaje = (porcentaje > 1 ? (porcentaje / 100) : porcentaje);
			value = (super.getEstimacion().doubleValue() * porcentaje);
			super.setAmortizacion(monto(value, 2)); 
			// Fondo de Garantia
			porcentaje = super.getPorcentajeRetencion();
			porcentaje = (porcentaje > 1 ? (porcentaje / 100) : porcentaje);
			value = (super.getEstimacion().doubleValue() * porcentaje);
			super.setFondoGarantia(monto(value, 2)); 
			// Subtotal: estimacion - (amortizacion + fondoGarantia)
			value = monto(super.getEstimacion(), 2) - (monto(super.getAmortizacion(), 2) + monto(super.getFondoGarantia(), 2));
			super.setSubtotal(monto(value, 2)); 
		}
		
		// Subtotal: anticipo
		if (super.getEstimacion().doubleValue() <= 0 && super.getAnticipo().doubleValue() > 0) 
			super.setSubtotal(super.getAnticipo());
		
		// Impuestos
		recalcularTotalesImpuestos();
		
		// Total
		value  = super.getSubtotal().doubleValue() + (super.getImpuestos().doubleValue() - super.getRetenciones().doubleValue());
		value += (super.getEstimacion().doubleValue() > 0 && super.getAnticipo().doubleValue() > 0 ? super.getAnticipo().doubleValue() : 0); // Añadimos anticipo si corresponde
		value -= (super.getCargos().doubleValue() > 0 ? super.getCargos().doubleValue() : 0); // Restamos cargos si corresponde
		super.setTotal(monto(value, 2));

		this.porAnticipo = false;
		this.porRetencion = false;
		this.anticipo = false;
		this.estimacion = false;
		this.amortizacion = false;
		this.fondoGarantia = false;
		this.cargos = false;
	}
	
	public void recalcular() {
		this.porAnticipo = true;
		this.porRetencion = true;
		calcular();
	}

	private void recalcularTotalesImpuestos() {
		BigDecimal impuestos = BigDecimal.ZERO;
		BigDecimal retenciones = BigDecimal.ZERO;
		BigDecimal baseImpuesto = BigDecimal.ZERO;
		
		if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
			baseImpuesto = super.getSubtotal();
			for (ObraSubcontratistaImpuestosExt impuesto : this.listImpuestos) {
				impuesto.setBaseImpuesto((impuesto.getBaseImpuesto().doubleValue() > 0 ? impuesto.getBaseImpuesto() : baseImpuesto));
				impuesto.recalcular();
				if (impuesto.isRetencion())
					retenciones = retenciones.add(impuesto.getImporte());
				else
					impuestos = impuestos.add(impuesto.getImporte());
			}
		}

		super.setImpuestos(impuestos);
		super.setRetenciones(retenciones);
	}
	
	// --------------------------------------------------------------------------------
	// PRIVADOS 
	// --------------------------------------------------------------------------------
	
	private Double monto(BigDecimal value, int decimales) {
		return value.setScale(decimales, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private BigDecimal monto(Double value, int decimales) {
		return new BigDecimal(value).setScale(decimales, BigDecimal.ROUND_HALF_EVEN);
	}
}
