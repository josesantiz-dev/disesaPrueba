package net.giro.contabilidad.filter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="gruposFilter")
public class GruposFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// ------------------------------------------------------
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String descripcionFilter;
	private String llavesFilter;
	// ------------------------------------------------------
	// Orders
	private SortOrder idOrder;
	private SortOrder descripcionOrder;
	private SortOrder llavesOrder;
	
	public GruposFilter() {
		this.idFilter = "";
		this.descripcionFilter = "";
		this.llavesFilter = "";
		setFiltrosActivos(false);
		resetOrder();
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
	
	public String getDescripcionFilter() {
		return descripcionFilter;
	}
	
	public void setDescripcionFilter(String descripcionFilter) {
		this.descripcionFilter = descripcionFilter;
	}
	
	public String getLlavesFilter() {
		return llavesFilter;
	}
	
	public void setLlavesFilter(String llavesFilter) {
		this.llavesFilter = llavesFilter;
	}

	public void toggleFilters() {
		this.filtrosActivos = ! this.filtrosActivos;
		if (! this.filtrosActivos) {
			this.idFilter = "";
			this.descripcionFilter = "";
			this.llavesFilter = "";
		}
	}

	// ------------------------------------------------------
	// Orders

	public SortOrder getIdOrder() {
		return idOrder;
	}
	
	public void setIdOrder(SortOrder idOrder) {
		this.idOrder = idOrder;
	}
	
	public void sortById() {
		if (this.idOrder.equals(SortOrder.unsorted))
			setIdOrder(SortOrder.ascending);
		else if (this.idOrder.equals(SortOrder.ascending))
			setIdOrder(SortOrder.descending);
		else
			setIdOrder(SortOrder.unsorted);
	}
	
	public SortOrder getDescripcionOrder() {
		return descripcionOrder;
	}
	
	public void setDescripcionOrder(SortOrder descripcionOrder) {
		this.descripcionOrder = descripcionOrder;
	}
	
	public void sortByDescripcion() {
		if (this.descripcionOrder.equals(SortOrder.unsorted))
			setDescripcionOrder(SortOrder.ascending);
		else if (this.descripcionOrder.equals(SortOrder.ascending))
			setDescripcionOrder(SortOrder.descending);
		else
			setDescripcionOrder(SortOrder.unsorted);
	}
	
	public SortOrder getLlavesOrder() {
		return llavesOrder;
	}
	
	public void setLlavesOrder(SortOrder llavesOrder) {
		this.llavesOrder = llavesOrder;
	}
	
	public void sortByLlaves() {
		if (this.llavesOrder.equals(SortOrder.unsorted))
			setLlavesOrder(SortOrder.ascending);
		else if (this.llavesOrder.equals(SortOrder.ascending))
			setLlavesOrder(SortOrder.descending);
		else
			setLlavesOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.descripcionOrder = SortOrder.unsorted;
		this.llavesOrder = SortOrder.unsorted;
	}
}
