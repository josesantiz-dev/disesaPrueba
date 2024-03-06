package net.giro.contabilidad.filter;

import java.io.Serializable;

import org.richfaces.model.Filter;

public class PolizasFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cuentaContable;
    private String concepto;
    private String referencia;
    private Long idOperacion;
    
    public Filter<?> getMileageFilterImpl() {
        return null; /*new Filter<PolizasInterfaces>() {
            public boolean accept(InventoryItem item) {
                Long mileage = getMileageFilter();
                if (mileage == null || mileage == 0 || mileage.compareTo(item.getMileage().longValue()) >= 0) {
                    return true;
                }
                return false;
            }
        };*/
    }
 
    public Filter<?> getFilterVendor() {
        return null;/*new Filter<InventoryItem>() {
            public boolean accept(InventoryItem t) {
                String vendor = getVendorFilter();
                if (vendor == null || vendor.length() == 0 || vendor.equals(t.getVendor())) {
                    return true;
                }
                return false;
            }
        };*/
    }

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Long getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(Long idOperacion) {
		this.idOperacion = idOperacion;
	}
}
