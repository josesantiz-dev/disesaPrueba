package net.giro.inventarios.filter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="invFilter")
public class InventariosFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String codigoFilter;
	private String descripcionFilter;
	private String umFilter;
	private String familiaFilter;
	private String monedaFilter;
	// Orders
	private SortOrder idOrder;
	private SortOrder codigoOrder;
	private SortOrder descripcionOrder;
	private SortOrder umOrder;
	private SortOrder familiaOrder;
	private SortOrder monedaOrder;
	
	public InventariosFilter() {
		setFiltrosActivos(true);
		toggleFilters();
		resetOrder();
	}

	public void toggleFilters() {
		this.filtrosActivos = ! this.filtrosActivos;
		if (! this.filtrosActivos) {
			this.idFilter = "";
			this.codigoFilter = "";
			this.descripcionFilter = "";
			this.umFilter = "";
			this.familiaFilter = "";
			this.monedaFilter = "";
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

	public void sortByCodigo() {
		if (this.codigoOrder.equals(SortOrder.unsorted))
			setCodigoOrder(SortOrder.ascending);
		else if (this.codigoOrder.equals(SortOrder.ascending))
			setCodigoOrder(SortOrder.descending);
		else
			setCodigoOrder(SortOrder.unsorted);
	}

	public void sortByDescripcion() {
		if (this.descripcionOrder.equals(SortOrder.unsorted))
			setDescripcionOrder(SortOrder.ascending);
		else if (this.descripcionOrder.equals(SortOrder.ascending))
			setDescripcionOrder(SortOrder.descending);
		else
			setDescripcionOrder(SortOrder.unsorted);
	}

	public void sortByUm() {
		if (this.umOrder.equals(SortOrder.unsorted))
			setUmOrder(SortOrder.ascending);
		else if (this.umOrder.equals(SortOrder.ascending))
			setUmOrder(SortOrder.descending);
		else
			setUmOrder(SortOrder.unsorted);
	}

	public void sortByFamilia() {
		if (this.familiaOrder.equals(SortOrder.unsorted))
			setFamiliaOrder(SortOrder.ascending);
		else if (this.familiaOrder.equals(SortOrder.ascending))
			setFamiliaOrder(SortOrder.descending);
		else
			setFamiliaOrder(SortOrder.unsorted);
	}

	public void sortByMonedao() {
		if (this.monedaOrder.equals(SortOrder.unsorted))
			setMonedaOrder(SortOrder.ascending);
		else if (this.monedaOrder.equals(SortOrder.ascending))
			setMonedaOrder(SortOrder.descending);
		else
			setMonedaOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.codigoOrder = SortOrder.unsorted;
		this.descripcionOrder = SortOrder.unsorted;
		this.umOrder = SortOrder.unsorted;
		this.familiaOrder = SortOrder.unsorted;
		this.monedaOrder = SortOrder.unsorted;
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

	public String getCodigoFilter() {
		return codigoFilter;
	}

	public void setCodigoFilter(String codigoFilter) {
		this.codigoFilter = codigoFilter;
	}

	public String getDescripcionFilter() {
		return descripcionFilter;
	}

	public void setDescripcionFilter(String descripcionFilter) {
		this.descripcionFilter = descripcionFilter;
	}

	public String getUmFilter() {
		return umFilter;
	}

	public void setUmFilter(String umFilter) {
		this.umFilter = umFilter;
	}

	public String getFamiliaFilter() {
		return familiaFilter;
	}

	public void setFamiliaFilter(String familiaFilter) {
		this.familiaFilter = familiaFilter;
	}

	public String getMonedaFilter() {
		return monedaFilter;
	}

	public void setMonedaFilter(String monedaFilter) {
		this.monedaFilter = monedaFilter;
	}

	// ------------------------------------------------------
	// Orders
	
	public SortOrder getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(SortOrder idOrder) {
		this.idOrder = idOrder;
	}

	public SortOrder getCodigoOrder() {
		return codigoOrder;
	}

	public void setCodigoOrder(SortOrder codigoOrder) {
		this.codigoOrder = codigoOrder;
	}

	public SortOrder getDescripcionOrder() {
		return descripcionOrder;
	}

	public void setDescripcionOrder(SortOrder descripcionOrder) {
		this.descripcionOrder = descripcionOrder;
	}

	public SortOrder getUmOrder() {
		return umOrder;
	}

	public void setUmOrder(SortOrder umOrder) {
		this.umOrder = umOrder;
	}

	public SortOrder getFamiliaOrder() {
		return familiaOrder;
	}

	public void setFamiliaOrder(SortOrder familiaOrder) {
		this.familiaOrder = familiaOrder;
	}

	public SortOrder getMonedaOrder() {
		return monedaOrder;
	}

	public void setMonedaOrder(SortOrder monedaOrder) {
		this.monedaOrder = monedaOrder;
	}
}
