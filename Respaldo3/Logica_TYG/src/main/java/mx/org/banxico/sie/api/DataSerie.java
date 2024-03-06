package mx.org.banxico.sie.api;

public class DataSerie {
	private String fecha;
	private String dato;

	public String getFecha() {
		if (fecha == null)
			fecha = "";
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDato() {
		if (dato == null)
			dato = "";
		return dato;
	}

	public void setDato(String dato) {
		this.dato = dato;
	}
}
