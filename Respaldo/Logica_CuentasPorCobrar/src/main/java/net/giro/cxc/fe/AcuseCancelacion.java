package net.giro.cxc.fe;
 
 
public class AcuseCancelacion {
   
    private String acuse;
	private String descripcion;
    private int state;

    
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
    
	public int getState() {
		return state;
	}
	
	public void setState(int value) {
		this.state = value;
	}
	
}
