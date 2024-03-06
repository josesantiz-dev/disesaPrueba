package net.giro.cxc.fe;
 
public class AcuseRecepcion {
	private int state; 
	private String descripcion;
	private String xml;
	private String timbre;
	private String fechaTimbrado;
	private String uuid;
	private String noCertificadoSat;
	private String selloSat;
		
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

	public String getXml() {
		return xml;
	}

	public void setXml(String value) {
		this.xml = value;
	}

	public String getTimbre() {
		return timbre;
	}

	public void setTimbre(String timbre) {
		this.timbre = timbre;
	}

	public String getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNoCertificadoSat() {
		return noCertificadoSat;
	}

	public void setNoCertificadoSat(String noCertificadoSat) {
		this.noCertificadoSat = noCertificadoSat;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		this.selloSat = selloSat;
	}
	
	@Override
	public String toString() {
		if (this.descripcion != null) {
			if (this.state > 0 && this.descripcion.contains(String.valueOf(this.state)))
				return this.descripcion;
			return this.state + " - " + this.descripcion;
		}
		
		return "";
	}
}