package net.giro.contabilidad.filter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="asigGruposFilter")
public class AsignacionGruposFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	// ------------------------------------------------------
	// Filters
	private boolean filtrosActivos;
	private String idFilter;
	private String codigoFilter;
	private String transaccionFilter;
	private String grupoCreditoFilter;
	private String grupoDebitoFilter;
	private String formaPagoFilter;
	private String conceptoFilter;
	// ------------------------------------------------------
	// Orders
	private SortOrder idOrder;
	private SortOrder codigoOrder;
	private SortOrder transaccionOrder;
	private SortOrder grupoCreditoOrder;
	private SortOrder grupoDebitoOrder;
	private SortOrder formaPagoOrder;
	private SortOrder conceptoOrder;
	
	public AsignacionGruposFilter() {
		this.idFilter = "";
		this.codigoFilter = "";
		this.transaccionFilter = "";
		this.grupoCreditoFilter = "";
		this.grupoDebitoFilter = "";
		this.formaPagoFilter = "";
		this.conceptoFilter = "";
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

	public String getCodigoFilter() {
		return codigoFilter;
	}

	public void setCodigoFilter(String codigoFilter) {
		this.codigoFilter = codigoFilter;
	}

	public String getTransaccionFilter() {
		return transaccionFilter;
	}

	public void setTransaccionFilter(String transaccionFilter) {
		this.transaccionFilter = transaccionFilter;
	}

	public String getGrupoCreditoFilter() {
		return grupoCreditoFilter;
	}
	
	public void setGrupoCreditoFilter(String grupoCreditoFilter) {
		this.grupoCreditoFilter = grupoCreditoFilter;
	}

	public String getGrupoDebitoFilter() {
		return grupoDebitoFilter;
	}
	
	public void setGrupoDebitoFilter(String grupoDebitoFilter) {
		this.grupoDebitoFilter = grupoDebitoFilter;
	}
	
	public String getFormaPagoFilter() {
		return formaPagoFilter;
	}
	
	public void setFormaPagoFilter(String formaPagoFilter) {
		this.formaPagoFilter = formaPagoFilter;
	}
	
	public String getConceptoFilter() {
		return conceptoFilter;
	}
	
	public void setConceptoFilter(String conceptoFilter) {
		this.conceptoFilter = conceptoFilter;
	}

	public void toggleFilters() {
		this.filtrosActivos = ! this.filtrosActivos;
		if (! this.filtrosActivos) {
			this.idFilter = "";
			this.codigoFilter = "";
			this.transaccionFilter = "";
			this.grupoCreditoFilter = "";
			this.grupoDebitoFilter = "";
			this.formaPagoFilter = "";
			this.conceptoFilter = "";
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

	public SortOrder getCodigoOrder() {
		return codigoOrder;
	}

	public void setCodigoOrder(SortOrder codigoOrder) {
		this.codigoOrder = codigoOrder;
	}
	
	public void sortByCodigo() {
		if (this.codigoOrder.equals(SortOrder.unsorted))
			setCodigoOrder(SortOrder.ascending);
		else if (this.codigoOrder.equals(SortOrder.ascending))
			setCodigoOrder(SortOrder.descending);
		else
			setCodigoOrder(SortOrder.unsorted);
	}

	public SortOrder getTransaccionOrder() {
		return transaccionOrder;
	}

	public void setTransaccionOrder(SortOrder transaccionOrder) {
		this.transaccionOrder = transaccionOrder;
	}
	
	public void sortByTransaccion() {
		if (this.transaccionOrder.equals(SortOrder.unsorted))
			setTransaccionOrder(SortOrder.ascending);
		else if (this.transaccionOrder.equals(SortOrder.ascending))
			setTransaccionOrder(SortOrder.descending);
		else
			setTransaccionOrder(SortOrder.unsorted);
	}

	public SortOrder getGrupoCreditoOrder() {
		return grupoCreditoOrder;
	}

	public void setGrupoCreditoOrder(SortOrder grupoCreditoOrder) {
		this.grupoCreditoOrder = grupoCreditoOrder;
	}

	public void sortByGrupoCredito() {
		if (this.grupoCreditoOrder.equals(SortOrder.unsorted))
			setGrupoCreditoOrder(SortOrder.ascending);
		else if (this.grupoCreditoOrder.equals(SortOrder.ascending))
			setGrupoCreditoOrder(SortOrder.descending);
		else
			setGrupoCreditoOrder(SortOrder.unsorted);
	}

	public SortOrder getGrupoDebitoOrder() {
		return grupoDebitoOrder;
	}

	public void setGrupoDebitoOrder(SortOrder grupoDebitoOrder) {
		this.grupoDebitoOrder = grupoDebitoOrder;
	}

	public void sortByGrupoDebito() {
		if (this.grupoDebitoOrder.equals(SortOrder.unsorted))
			setGrupoDebitoOrder(SortOrder.ascending);
		else if (this.grupoDebitoOrder.equals(SortOrder.ascending))
			setGrupoDebitoOrder(SortOrder.descending);
		else
			setGrupoDebitoOrder(SortOrder.unsorted);
	}

	public SortOrder getFormaPagoOrder() {
		return formaPagoOrder;
	}

	public void setFormaPagoOrder(SortOrder formaPagoOrder) {
		this.formaPagoOrder = formaPagoOrder;
	}

	public void sortByFormaPago() {
		if (this.formaPagoOrder.equals(SortOrder.unsorted))
			setFormaPagoOrder(SortOrder.ascending);
		else if (this.formaPagoOrder.equals(SortOrder.ascending))
			setFormaPagoOrder(SortOrder.descending);
		else
			setFormaPagoOrder(SortOrder.unsorted);
	}

	public SortOrder getConceptoOrder() {
		return conceptoOrder;
	}

	public void setConceptoOrder(SortOrder conceptoOrder) {
		this.conceptoOrder = conceptoOrder;
	}

	public void sortByConcepto() {
		if (this.conceptoOrder.equals(SortOrder.unsorted))
			setConceptoOrder(SortOrder.ascending);
		else if (this.conceptoOrder.equals(SortOrder.ascending))
			setConceptoOrder(SortOrder.descending);
		else
			setConceptoOrder(SortOrder.unsorted);
	}

	public void resetOrder() {
		this.idOrder = SortOrder.unsorted;
		this.codigoOrder = SortOrder.unsorted;
		this.transaccionOrder = SortOrder.unsorted;
		this.grupoCreditoOrder = SortOrder.unsorted;
		this.grupoDebitoOrder = SortOrder.unsorted;
		this.formaPagoOrder = SortOrder.unsorted;
		this.conceptoOrder = SortOrder.unsorted;
	}
}
