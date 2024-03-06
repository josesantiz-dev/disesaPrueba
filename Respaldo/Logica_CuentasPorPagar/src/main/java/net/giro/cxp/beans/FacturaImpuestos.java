package net.giro.cxp.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FacturaImpuestos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PagosGastosDetExt pojoFactura;
	private List<PagosGastosDetImpuestosExt> listImpuestos;
	private List<PagosGastosDetImpuestosExt> listRetenciones;

	private boolean seleccionado;
	
	public FacturaImpuestos(){
		this.listImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
		this.listRetenciones = new ArrayList<PagosGastosDetImpuestosExt>();
	}
	
	public PagosGastosDetExt getPojoFactura() {
		return pojoFactura;
	}

	public void setPojoFactura(PagosGastosDetExt pojoFactura) {
		this.pojoFactura = pojoFactura;
	}

	public List<PagosGastosDetImpuestosExt> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<PagosGastosDetImpuestosExt> listImpuestos) {
		this.listImpuestos = listImpuestos;
	}
	
	public List<PagosGastosDetImpuestosExt> getListRetenciones() {
		return listRetenciones;
	}

	public void setListRetenciones(List<PagosGastosDetImpuestosExt> listRetencionesExt) {
		this.listRetenciones = listRetencionesExt;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
}
