package net.giro.cxc.fe;
 
public class AcuseCancelacion {
    private int state;
	private String descripcion;
    private String acuse;
    protected String rfcSolicitante;
    protected String fecha;
    protected String UUIDs;
    protected String noSerieFirmante;

	public int getState() {
		return state;
	}
	
	public void setState(int value) {
		this.state = value;
	}
	
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String value) {
        this.descripcion = value;
    }
	
	public String getAcuse() {
        return acuse;
    }

    public void setAcuse(String value) {
        this.acuse = value;
    }

	public String getRfcSolicitante() {
		return rfcSolicitante;
	}

	public void setRfcSolicitante(String rfcSolicitante) {
		this.rfcSolicitante = rfcSolicitante;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getUUIDs() {
		return UUIDs;
	}

	public void setUUIDs(String UUIDs) {
		this.UUIDs = UUIDs;
	}

	public String getNoSerieFirmante() {
		return noSerieFirmante;
	}

	public void setNoSerieFirmante(String noSerieFirmante) {
		this.noSerieFirmante = noSerieFirmante;
	}
	
	@Override
	public String toString() {
		return this.state + " - " + this.descripcion;
	}
}
