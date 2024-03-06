package net.giro.cxc.realvirtual.beans;
 
public class AcuseRecepcion {
	private int state; 
	private String descripcion;
	private String xml;
	private String timbre;
	protected String fechaTimbrado;
	protected String uuid;
	protected String noCertificadoSat;
	protected String selloSat;
	protected String versionTimbre;
	
	public AcuseRecepcion() {
		this.descripcion = "";
		this.xml = "";
		this.timbre = "";
		this.fechaTimbrado = "";
		this.uuid = "";
		this.noCertificadoSat = "";
		this.selloSat = "";
		this.versionTimbre = "";
	}

	public int getState() {
		return state;
	}

	public void setState(int value) {
		this.state = value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		if (descripcion == null)
			descripcion = "";
		this.descripcion = descripcion;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		if (xml == null)
			xml = "";
		this.xml = xml;
	}

	public String getTimbre() {
		return timbre;
	}

	public void setTimbre(String timbre) {
		if (timbre == null)
			timbre = "";
		this.timbre = timbre;
	}

	public String getFechaTimbrado() {
		return fechaTimbrado;
	}

	public void setFechaTimbrado(String fechaTimbrado) {
		if (fechaTimbrado == null)
			fechaTimbrado = "";
		this.fechaTimbrado = fechaTimbrado;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		if (uuid == null)
			uuid = "";
		this.uuid = uuid;
	}

	public String getNoCertificadoSat() {
		return noCertificadoSat;
	}

	public void setNoCertificadoSat(String noCertificadoSat) {
		if (noCertificadoSat == null)
			noCertificadoSat = "";
		this.noCertificadoSat = noCertificadoSat;
	}

	public String getSelloSat() {
		return selloSat;
	}

	public void setSelloSat(String selloSat) {
		if (selloSat == null)
			selloSat = "";
		this.selloSat = selloSat;
	}
	
	public String getVersionTimbre() {
		return versionTimbre;
	}

	public void setVersionTimbre(String versionTimbre) {
		if (versionTimbre == null)
			versionTimbre = "";
		this.versionTimbre = versionTimbre;
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