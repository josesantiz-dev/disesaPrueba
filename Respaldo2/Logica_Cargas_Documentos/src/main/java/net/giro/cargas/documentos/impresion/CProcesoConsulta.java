package net.giro.cargas.documentos.impresion;


import java.io.Serializable;
import java.util.Date;

public class CProcesoConsulta implements Serializable {
	private static final long serialVersionUID = 1l;

	Long id;
    String programa;
    String descripcion;
    
    Date nextFireTime;
    Date prevFireTime;
    int priority;
    Date startTime;
    Date endTime;
     
    String phase;
    String status;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public Date getPrevFireTime() {
		return prevFireTime;
	}
	public void setPrevFireTime(Date prevFireTime) {
		this.prevFireTime = prevFireTime;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPhaseCompleto() {
		String resultado = "";
			if("X".equals(phase))
				resultado = "Cancelado";
			else if("Q".equals(phase))
				resultado = "En cola programado";
			else if("S".equals(phase))
				resultado = "Suspendido";
			else if("E".equals(phase))
				resultado = "Error";
			else if("T".equals(phase))
				resultado = "Terminado";
			else if("P".equals(phase))
				resultado = "Pendiente";
			else if("R".equals(phase))
				resultado = "Ejecucion";
			
			if("N".equals(status))
				resultado += " Normal";
			else if("E".equals(status))
				resultado += " en Error";
			else if("X".equals(status))
				resultado += " por Cancelacion";
			
			return resultado;
	}
	
	public void setPhaseCompleto(String phaseCompleto) { /* nanai */}
     
    
}
