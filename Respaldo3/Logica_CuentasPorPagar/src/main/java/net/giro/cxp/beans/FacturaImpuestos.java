package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

public class FacturaImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	private PagosGastosDetExt pojoFactura;
	private List<PagosGastosDetImpuestosExt> impuestos;
	private List<PagosGastosDetImpuestosExt> retenciones;
	private double totalImpuestos;
	private double totalRetenciones;
	private boolean seleccionado;
	private boolean editando;
	private int index;
	
	public FacturaImpuestos() {
		this.pojoFactura = new PagosGastosDetExt();
		this.impuestos = new ArrayList<PagosGastosDetImpuestosExt>();
		this.retenciones = new ArrayList<PagosGastosDetImpuestosExt>();
		this.totalImpuestos = 0;
		this.totalRetenciones = 0;
		this.seleccionado = false;
		this.index = -1;
	}

	public FacturaImpuestos(PagosGastosDetExt factura, List<PagosGastosDetImpuestosExt> impuestos, List<PagosGastosDetImpuestosExt> retenciones) {
		this();
		if (factura == null)
			return;
		this.setPojoFactura(factura);
		this.setListImpuestos(impuestos);
		this.setListRetenciones(retenciones);
	}
	
	public PagosGastosDetExt getPojoFactura() {
		if (this.pojoFactura == null)
			this.pojoFactura = new PagosGastosDetExt();
		return pojoFactura;
	}

	public void setPojoFactura(PagosGastosDetExt pojoFactura) {
		if (pojoFactura == null)
			pojoFactura = new PagosGastosDetExt();
		this.pojoFactura = pojoFactura;
	}

	public List<PagosGastosDetImpuestosExt> getListImpuestos() {
		if (this.impuestos == null)
			this.impuestos = new ArrayList<PagosGastosDetImpuestosExt>();
		return impuestos;
	}

	public void setListImpuestos(List<PagosGastosDetImpuestosExt> impuestos) {
		if (impuestos == null)
			impuestos = new ArrayList<PagosGastosDetImpuestosExt>();
		this.impuestos = impuestos;
		calcularTotalImpuestos();
	}
	
	public List<PagosGastosDetImpuestosExt> getListRetenciones() {
		if (this.retenciones == null)
			this.retenciones = new ArrayList<PagosGastosDetImpuestosExt>();
		return retenciones;
	}

	public void setListRetenciones(List<PagosGastosDetImpuestosExt> retenciones) {
		if (retenciones == null)
			retenciones = new ArrayList<PagosGastosDetImpuestosExt>();
		this.retenciones = retenciones;
		calcularTotalRetenciones();
	}

	public double getTotalImpuestos() {
		return totalImpuestos;
	}

	public void setTotalImpuestos(double totalImpuestos) {}

	public double getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(double totalRetenciones) {}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public boolean isEditando() {
		return editando;
	}

	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	// --------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------
	
	public void seleccionado() {
		this.seleccionado = ! this.seleccionado;
	}
	
	public FacturaImpuestos copy() {
		return new FacturaImpuestos(this.pojoFactura, this.impuestos, this.retenciones);
	}
	
	@Override
	public Object clone() {  
		FacturaImpuestos dest = null;
		
	    try {
	    	dest = new FacturaImpuestos(); 
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
	    	BeanUtils.copyProperties(dest, this); 
	        return dest;
	    } catch (Exception e) { 
	        return null; 
	    }
	}

	// --------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------
	
	private void calcularTotalImpuestos() {
		this.totalImpuestos = 0;
		if (this.impuestos != null && ! this.impuestos.isEmpty()) {
			for (PagosGastosDetImpuestosExt impuesto : this.impuestos)
				this.totalImpuestos += impuesto.getValor().doubleValue();
		}
	}

	private void calcularTotalRetenciones() {
		this.totalRetenciones = 0;
		if (this.retenciones != null && ! this.retenciones.isEmpty()) {
			for (PagosGastosDetImpuestosExt retencion : this.retenciones)
				this.totalRetenciones += retencion.getValor().doubleValue();
		}
	}
}
