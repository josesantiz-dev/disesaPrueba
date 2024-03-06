package net.giro.contabilidad.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.SortOrder;

@ViewScoped
@ManagedBean(name="contaFiltersAndOrders")
public class ContabilidadFiltersAndOrders implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, HashMap<String, Object>> filters;
	private HashMap<String, HashMap<String, SortOrder>> orders;
	
	public ContabilidadFiltersAndOrders() {
		this.filters = new HashMap<String, HashMap<String, Object>>();
		this.orders = new HashMap<String, HashMap<String, SortOrder>>();
	}

	// ------------------------------------------------------
	// Filters
	
	public String getFilter() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String action = "";
		String filtro = "";
		
		params = fc.getExternalContext().getRequestParameterMap();
		if (! params.containsKey("action")) 
			return "";

		action = params.get("action");
		if (! this.filters.get(action).containsKey(filtro))
			return "";
		return (String) this.filters.get(action).get(filtro);
	}
	
	public void setFilter(String value) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String action = "";
		String filtro = "";
		
		params = fc.getExternalContext().getRequestParameterMap();
		if (! params.containsKey("action")) 
			return;

		action = params.get("action");
		if (! this.filters.get(action).containsKey(filtro))
			return;
		this.filters.get(action).put(filtro, value);
	}

	public void toggleFilters() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		boolean filtrosActivos = false;
		String action = "";
		
		params = fc.getExternalContext().getRequestParameterMap();
		if (! params.containsKey("action")) 
			return;
		
		action = params.get("action");
		if (! this.filters.get(action).containsKey("activos"))
			this.filters.get(action).put("activos", filtrosActivos);
		
		filtrosActivos = ! (boolean) this.filters.get(action).get("activos");
		if (! filtrosActivos) {
			for (Entry<String, Object> filtro : this.filters.get(action).entrySet())
				if (filtro.getValue().getClass() == java.lang.String.class)
					filtro.setValue("");
				
		}
	}

	// ------------------------------------------------------
	// Orders

	public SortOrder getSort() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String action = "";
		String orden = "";
		
		params = fc.getExternalContext().getRequestParameterMap();
		if (! params.containsKey("action")) 
			return null;

		action = getAction();
		orden = getKey();
		if (! this.orders.get(action).containsKey(orden))
			return SortOrder.unsorted;
		return (SortOrder) this.orders.get(action).get(orden);
	}
	
	public void setSort(SortOrder value) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String action = "";
		String orden = "";
		
		params = fc.getExternalContext().getRequestParameterMap();
		if (! params.containsKey("action")) 
			return;

		action = params.get("action");
		if (! this.orders.get(action).containsKey(orden))
			return;
		this.orders.get(action).put(orden, value);
	}

	public void sort() {
		SortOrder value = null;
		
		value = getSort();
		if (value.equals(SortOrder.unsorted))
			setSort(SortOrder.ascending);
		else if (value.equals(SortOrder.ascending))
			setSort(SortOrder.descending);
		else
			setSort(SortOrder.unsorted);
	}
	
	private String getAction() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String value = "";

		params = fc.getExternalContext().getRequestParameterMap();
		if (params.containsKey("action")) 
			value = params.get("action");
		return value;
	}
	
	private String getKey() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = null;
		String value = "";

		params = fc.getExternalContext().getRequestParameterMap();
		if (params.containsKey("orden")) 
			value = params.get("orden");
		return value;
	}
}
