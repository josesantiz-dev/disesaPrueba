package net.giro.adp.filters;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="obrasFilter")
public class ObrasFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String nombreFilter;
	private String clienteFilter;
	private String monedaFilter;
	private String tipoFilter;
	private String jerarquiaFilter;
	// Orders
	private SortOrder idOrder;
	private SortOrder nombreOrder;
	private SortOrder clienteOrder;
	private SortOrder monedaOrder;
	private SortOrder tipoOrder;
	private SortOrder jerarquiaOrder;
	
	public ObrasFilter() {
		setFiltrosActivos(true);
		toggleFilters();
		resetOrder();
	}

	public void toggleFilters() {
		this.filtrosActivos = ! this.filtrosActivos;
		if (! this.filtrosActivos) {
			this.idFilter = "";
			this.nombreFilter = "";
			this.clienteFilter = "";
			this.monedaFilter = "";
			this.tipoFilter = "";
			this.jerarquiaFilter = "";
		}
	}

	public void sortById() {
		if (this.idOrder.equals(SortOrder.unsorted))
			setIdOrder(SortOrder.ascending);
		else if (this.idOrder.equals(SortOrder.ascending))
			setIdOrder(SortOrder.descending);
		else
			setIdOrder(SortOrder.unsorted);
	}

	public void sortByNombre() {
		if (this.nombreOrder.equals(SortOrder.unsorted))
			setNombreOrder(SortOrder.ascending);
		else if (this.nombreOrder.equals(SortOrder.ascending))
			setNombreOrder(SortOrder.descending);
		else
			setNombreOrder(SortOrder.unsorted);
	}

	public void sortByCliente() {
		if (this.clienteOrder.equals(SortOrder.unsorted))
			setClienteOrder(SortOrder.ascending);
		else if (this.clienteOrder.equals(SortOrder.ascending))
			setClienteOrder(SortOrder.descending);
		else
			setClienteOrder(SortOrder.unsorted);
	}

	public void sortByMoneda() {
		if (this.monedaOrder.equals(SortOrder.unsorted))
			setMonedaOrder(SortOrder.ascending);
		else if (this.monedaOrder.equals(SortOrder.ascending))
			setMonedaOrder(SortOrder.descending);
		else
			setMonedaOrder(SortOrder.unsorted);
	}

	public void sortByTipo() {
		if (this.tipoOrder.equals(SortOrder.unsorted))
			setTipoOrder(SortOrder.ascending);
		else if (this.tipoOrder.equals(SortOrder.ascending))
			setTipoOrder(SortOrder.descending);
		else
			setTipoOrder(SortOrder.unsorted);
	}

	public void sortByJerarquia() {
		if (this.jerarquiaOrder.equals(SortOrder.unsorted))
			setJerarquiaOrder(SortOrder.ascending);
		else if (this.jerarquiaOrder.equals(SortOrder.ascending))
			setJerarquiaOrder(SortOrder.descending);
		else
			setJerarquiaOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.nombreOrder = SortOrder.unsorted;
		this.clienteOrder = SortOrder.unsorted;
		this.monedaOrder = SortOrder.unsorted;
		this.tipoOrder = SortOrder.unsorted;
		this.jerarquiaOrder = SortOrder.unsorted;
	}

	// ------------------------------------------------------
	// Filters
	
	public boolean isFiltrosActivos() {
		return filtrosActivos;
	}

	public void setFiltrosActivos(boolean filtrosActivos) {
		this.filtrosActivos = filtrosActivos;
	}

	public String getIdFilter() {
		return idFilter;
	}

	public void setIdFilter(String idFilter) {
		this.idFilter = idFilter;
	}

	public String getNombreFilter() {
		return nombreFilter;
	}

	public void setNombreFilter(String nombreFilter) {
		this.nombreFilter = nombreFilter;
	}

	public String getClienteFilter() {
		return clienteFilter;
	}

	public void setClienteFilter(String clienteFilter) {
		this.clienteFilter = clienteFilter;
	}

	public String getMonedaFilter() {
		return monedaFilter;
	}

	public void setMonedaFilter(String monedaFilter) {
		this.monedaFilter = monedaFilter;
	}

	public String getTipoFilter() {
		return tipoFilter;
	}

	public void setTipoFilter(String TipoFilter) {
		this.tipoFilter = TipoFilter;
	}

	public String getJerarquiaFilter() {
		return jerarquiaFilter;
	}

	public void setJerarquiaFilter(String jerarquiaFilter) {
		this.jerarquiaFilter = jerarquiaFilter;
	}

	// ------------------------------------------------------
	// Orders
	
	public SortOrder getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(SortOrder idOrder) {
		this.idOrder = idOrder;
	}

	public SortOrder getNombreOrder() {
		return nombreOrder;
	}

	public void setNombreOrder(SortOrder nombreOrder) {
		this.nombreOrder = nombreOrder;
	}

	public SortOrder getClienteOrder() {
		return clienteOrder;
	}

	public void setClienteOrder(SortOrder clienteOrder) {
		this.clienteOrder = clienteOrder;
	}

	public SortOrder getMonedaOrder() {
		return monedaOrder;
	}

	public void setMonedaOrder(SortOrder monedaOrder) {
		this.monedaOrder = monedaOrder;
	}

	public SortOrder getTipoOrder() {
		return tipoOrder;
	}

	public void setTipoOrder(SortOrder tipoOrder) {
		this.tipoOrder = tipoOrder;
	}

	public SortOrder getJerarquiaOrder() {
		return jerarquiaOrder;
	}

	public void setJerarquiaOrder(SortOrder jerarquiaOrder) {
		this.jerarquiaOrder = jerarquiaOrder;
	}
}
