package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.ne.beans.Regiones;
import net.giro.plataforma.InfoSesion;

@Remote
public interface RegionesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<Regiones> saveOrUpdateList(List<Regiones> entities) throws Exception;

	public long salvar(Regiones regiones) throws Exception;

	public void eliminar(Regiones regiones) throws Exception;
	
	public List<Regiones> buscarRegiones(String tipoBusqueda, String valorBusqueda) throws Exception;
}
