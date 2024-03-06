package net.proc.socket.respuesta;

import java.util.HashMap;

public class RespuestaProcesos {
	private HashMap<String, HashMap<String, Object>> procesos;
	
	public RespuestaProcesos(){
		
	}

	public HashMap<String, HashMap<String, Object>> getProcesos() {
		return procesos;
	}

	public void setProcesos(HashMap<String, HashMap<String, Object>> procesos) {
		this.procesos = procesos;
	}
}
