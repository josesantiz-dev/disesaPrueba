package net.giro.contabilidad.filter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="tranFilter")
public class TransaccionesFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String codigoFilter;
	private String descripcionFilter;
	private String operacionFilter;
	private String glosaFilter;
	private String moduloFilter;
	// Orders
	private SortOrder idOrder;
	private SortOrder codigoOrder;
	private SortOrder descripcionOrder;
	private SortOrder operacionOrder;
	private SortOrder glosaOrder;
	private SortOrder moduloOrder;
	
	public TransaccionesFilter() {
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
			this.operacionFilter = "";
			this.glosaFilter = "";
			this.moduloFilter = "";
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

	public void sortByOperacion() {
		if (this.operacionOrder.equals(SortOrder.unsorted))
			setOperacionOrder(SortOrder.ascending);
		else if (this.operacionOrder.equals(SortOrder.ascending))
			setOperacionOrder(SortOrder.descending);
		else
			setOperacionOrder(SortOrder.unsorted);
	}

	public void sortByGlosa() {
		if (this.glosaOrder.equals(SortOrder.unsorted))
			setGlosaOrder(SortOrder.ascending);
		else if (this.glosaOrder.equals(SortOrder.ascending))
			setGlosaOrder(SortOrder.descending);
		else
			setGlosaOrder(SortOrder.unsorted);
	}

	public void sortByModulo() {
		if (this.moduloOrder.equals(SortOrder.unsorted))
			setModuloOrder(SortOrder.ascending);
		else if (this.moduloOrder.equals(SortOrder.ascending))
			setModuloOrder(SortOrder.descending);
		else
			setModuloOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.codigoOrder = SortOrder.unsorted;
		this.descripcionOrder = SortOrder.unsorted;
		this.operacionOrder = SortOrder.unsorted;
		this.glosaOrder = SortOrder.unsorted;
		this.moduloOrder = SortOrder.unsorted;
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

	public String getOperacionFilter() {
		return operacionFilter;
	}

	public void setOperacionFilter(String operacionFilter) {
		this.operacionFilter = operacionFilter;
	}

	public String getGlosaFilter() {
		return glosaFilter;
	}

	public void setGlosaFilter(String glosaFilter) {
		this.glosaFilter = glosaFilter;
	}

	public String getModuloFilter() {
		return moduloFilter;
	}

	public void setModuloFilter(String moduloFilter) {
		this.moduloFilter = moduloFilter;
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

	public SortOrder getOperacionOrder() {
		return operacionOrder;
	}

	public void setOperacionOrder(SortOrder operacionOrder) {
		this.operacionOrder = operacionOrder;
	}

	public SortOrder getGlosaOrder() {
		return glosaOrder;
	}

	public void setGlosaOrder(SortOrder glosaOrder) {
		this.glosaOrder = glosaOrder;
	}

	public SortOrder getModuloOrder() {
		return moduloOrder;
	}

	public void setModuloOrder(SortOrder moduloOrder) {
		this.moduloOrder = moduloOrder;
	}
}
