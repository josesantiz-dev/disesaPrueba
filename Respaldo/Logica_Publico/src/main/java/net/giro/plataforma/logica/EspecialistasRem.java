package net.giro.plataforma.logica;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.OficialExt;
import net.giro.publico.respuesta.Respuesta;

@Remote
public interface EspecialistasRem {
	public Respuesta cargarOficiales();
	public Respuesta cargarUsuarios();
	
	public Respuesta guardarOficial(OficialExt pojoOficialExt);
	
	public void setInfoSesion(InfoSesion infoSesion);
}
