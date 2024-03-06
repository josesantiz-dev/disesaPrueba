package net.giro.contabilidad.filter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="msgFilter")
public class MensajeTransaccionesFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// ----------------------------------------------------------
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String fechaFilter;
	private String transaccionFilter;
	private String operacionFilter;
	private String personaNegocioFilter;
	private String monedaFilter;
	// ----------------------------------------------------------
	// Orders
	private SortOrder idOrder;
	private SortOrder fechaOrder;
	private SortOrder transaccionOrder;
	private SortOrder operacionOrder;
	private SortOrder personaNegocioOrder;
	private SortOrder monedaOrder;
	
	public MensajeTransaccionesFilter() {
		setFiltrosActivos(true);
		toggleFilters();
		resetOrder();
	}

	public void toggleFilters() {
		this.filtrosActivos = ! this.filtrosActivos;
		if (! this.filtrosActivos) {
			this.idFilter = "";
			this.fechaFilter = "";
			this.transaccionFilter = "";
			this.operacionFilter = "";
			this.personaNegocioFilter = "";
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

	public void sortByFecha() {
		if (this.fechaOrder.equals(SortOrder.unsorted))
			setFechaOrder(SortOrder.ascending);
		else if (this.fechaOrder.equals(SortOrder.ascending))
			setFechaOrder(SortOrder.descending);
		else
			setFechaOrder(SortOrder.unsorted);
	}

	public void sortByTransaccion() {
		if (this.transaccionOrder.equals(SortOrder.unsorted))
			setTransaccionOrder(SortOrder.ascending);
		else if (this.transaccionOrder.equals(SortOrder.ascending))
			setTransaccionOrder(SortOrder.descending);
		else
			setTransaccionOrder(SortOrder.unsorted);
	}

	public void sortByOperacion() {
		if (this.operacionOrder.equals(SortOrder.unsorted))
			setOperacionOrder(SortOrder.ascending);
		else if (this.operacionOrder.equals(SortOrder.ascending))
			setOperacionOrder(SortOrder.descending);
		else
			setOperacionOrder(SortOrder.unsorted);
	}

	public void sortByPersonaNegocio() {
		if (this.personaNegocioOrder.equals(SortOrder.unsorted))
			setPersonaNegocioOrder(SortOrder.ascending);
		else if (this.personaNegocioOrder.equals(SortOrder.ascending))
			setPersonaNegocioOrder(SortOrder.descending);
		else
			setPersonaNegocioOrder(SortOrder.unsorted);
	}

	public void sortByMoneda() {
		if (this.monedaOrder.equals(SortOrder.unsorted))
			setMonedaOrder(SortOrder.ascending);
		else if (this.monedaOrder.equals(SortOrder.ascending))
			setMonedaOrder(SortOrder.descending);
		else
			setMonedaOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.fechaOrder = SortOrder.unsorted;
		this.transaccionOrder = SortOrder.unsorted;
		this.operacionOrder = SortOrder.unsorted;
		this.personaNegocioOrder = SortOrder.unsorted;
		this.monedaOrder = SortOrder.unsorted;
	}

	// ----------------------------------------------------------
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

	public String getFechaFilter() {
		return fechaFilter;
	}

	public void setFechaFilter(String fechaFilter) {
		this.fechaFilter = fechaFilter;
	}

	public String getTransaccionFilter() {
		return transaccionFilter;
	}

	public void setTransaccionFilter(String transaccionFilter) {
		this.transaccionFilter = transaccionFilter;
	}

	public String getOperacionFilter() {
		return operacionFilter;
	}

	public void setOperacionFilter(String operacionFilter) {
		this.operacionFilter = operacionFilter;
	}

	public String getPersonaNegocioFilter() {
		return personaNegocioFilter;
	}

	public void setPersonaNegocioFilter(String personaNegocioFilter) {
		this.personaNegocioFilter = personaNegocioFilter;
	}

	public String getMonedaFilter() {
		return monedaFilter;
	}

	public void setMonedaFilter(String monedaFilter) {
		this.monedaFilter = monedaFilter;
	}

	// ----------------------------------------------------------
	// Orders

	public SortOrder getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(SortOrder idOrder) {
		this.idOrder = idOrder;
	}

	public SortOrder getFechaOrder() {
		return fechaOrder;
	}

	public void setFechaOrder(SortOrder fechaOrder) {
		this.fechaOrder = fechaOrder;
	}

	public SortOrder getTransaccionOrder() {
		return transaccionOrder;
	}

	public void setTransaccionOrder(SortOrder transaccionOrder) {
		this.transaccionOrder = transaccionOrder;
	}

	public SortOrder getOperacionOrder() {
		return operacionOrder;
	}

	public void setOperacionOrder(SortOrder operacionOrder) {
		this.operacionOrder = operacionOrder;
	}

	public SortOrder getPersonaNegocioOrder() {
		return personaNegocioOrder;
	}

	public void setPersonaNegocioOrder(SortOrder personaNegocioOrder) {
		this.personaNegocioOrder = personaNegocioOrder;
	}

	public SortOrder getMonedaOrder() {
		return monedaOrder;
	}

	public void setMonedaOrder(SortOrder monedaOrder) {
		this.monedaOrder = monedaOrder;
	}
}
