package net.giro.pruebas;


import org.apache.log4j.Logger;


public class Inventarios {
	private static Logger log = Logger.getLogger(Inventarios.class);
	private boolean operacionCompleta; 
	private int tipoMensaje;
	private String mensaje;
	
	
	public Inventarios () {
		
	}

	
	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		if (! value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(int tipoMensaje) {
		control(false, tipoMensaje, "ERROR");
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value))
			control(false, 1, "ERROR");
		else
			control(false, -1, value); 
	}
	
	private void control(boolean operacionCompleta, int tipoMensaje, String mensaje) {
		this.operacionCompleta = operacionCompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
	}

	public void buscar() {
		
	}
	
	public void nuevo() {
		
	}
	
	public void editar() {
		
	}
	
	public void guardar() {
		
	}
	
	public void eliminar() {
		
	}
	
	public void reporte() {
		
	}
}
