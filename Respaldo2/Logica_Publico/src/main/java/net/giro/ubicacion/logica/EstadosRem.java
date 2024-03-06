package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Estado;

@Remote
public interface EstadosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<Estado> saveOrUpdateList(List<Estado> entities) throws Exception;

	public long salvar(Estado estado) throws Exception;

	public void eliminar(Estado pojoEstado) throws Exception;
	
	public List<Estado> buscarEstados(String tipoBusqueda, String valorBusqueda) throws Exception;

	public List<ConValores> buscarZonasEconomicas() throws Exception;
}
