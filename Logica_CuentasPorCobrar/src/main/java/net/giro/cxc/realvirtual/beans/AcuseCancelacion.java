package net.giro.cxc.realvirtual.beans;
 
public class AcuseCancelacion {
    private int state;
	private String descripcion;
	private String rfcSolicitante;
    private String fecha;
    private String UUIDs;
    private String noSerieFirmante;
    private String acuse;
    
    public AcuseCancelacion() {
    	this.descripcion = "";
    	this.acuse = "";
    	this.rfcSolicitante = "";
    	this.fecha = "";
    	this.UUIDs = "";
    	this.noSerieFirmante = "";
    }

	public int getState() {
		return state;
	}
	
	public void setState(int value) {
		this.state = value;
	}
	
    public String getDescripcion() {
    	if (this.descripcion == null)
    		this.descripcion = "";
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
    	if (descripcion == null)
    		descripcion = "";
        this.descripcion = descripcion;
    }
	
	public String getAcuse() {
    	if (this.acuse == null)
    		this.acuse = "";
        return acuse;
    }

    public void setAcuse(String acuse) {
    	if (acuse == null)
    		acuse = "";
        this.acuse = acuse;
    }

	public String getRfcSolicitante() {
    	if (this.rfcSolicitante == null)
    		this.rfcSolicitante = "";
		return rfcSolicitante;
	}

	public void setRfcSolicitante(String rfcSolicitante) {
    	if (rfcSolicitante == null)
    		rfcSolicitante = "";
		this.rfcSolicitante = rfcSolicitante;
	}

	public String getFecha() {
    	if (this.fecha == null)
    		this.fecha = "";
		return fecha;
	}

	public void setFecha(String fecha) {
    	if (fecha == null)
    		fecha = "";
		this.fecha = fecha;
	}

	public String getUUIDs() {
    	if (this.UUIDs == null)
    		this.UUIDs = "";
		return UUIDs;
	}

	public void setUUIDs(String UUIDs) {
    	if (UUIDs == null)
    		UUIDs = "";
		this.UUIDs = UUIDs;
	}

	public String getNoSerieFirmante() {
    	if (this.noSerieFirmante == null)
    		this.noSerieFirmante = "";
		return noSerieFirmante;
	}

	public void setNoSerieFirmante(String noSerieFirmante) {
    	if (noSerieFirmante == null)
    		noSerieFirmante = "";
		this.noSerieFirmante = noSerieFirmante;
	}
	
	@Override
	public String toString() {
		return this.state + " - " + this.descripcion;
	}
}
