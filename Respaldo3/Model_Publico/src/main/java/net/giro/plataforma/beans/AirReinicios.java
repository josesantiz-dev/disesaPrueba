package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * air_reinicios
 * @author javaz
 *
 */
public class AirReinicios implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String comando;
	private Date fechaProgramada;
	private Date fechaCreacion;
	// ---------------------------------------------
	private Date aviso;
	
	public AirReinicios() {
		this.comando = "";
		this.fechaProgramada = Calendar.getInstance().getTime();
		this.fechaCreacion = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public Date getFechaProgramada() {
		return fechaProgramada;
	}

	public void setFechaProgramada(Date fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
		programarAviso(5);
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	// ---------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------

	public Date getAviso() {
		if (this.aviso == null)
			programarAviso(5);
		return this.aviso;
	}

	// ---------------------------------------------
	// METODOS
	// ---------------------------------------------
	
	private void programarAviso(int minutos) {
		Calendar cal = null;
		
		this.aviso = null;
		if (this.fechaProgramada == null)
			return;
		
		minutos = (minutos != 0 ? Math.abs(minutos) : 5) * -1;
		cal = Calendar.getInstance();
		cal.setTime(this.fechaProgramada);
		cal.add(Calendar.MINUTE, minutos);
		this.aviso = cal.getTime();
	}
	
	public boolean validar(Date fecha) {
		return true;
	}
}
