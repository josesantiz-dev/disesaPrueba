package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;

@Remote
public interface LocalidadesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<Localidad> saveOrUpdateList(List<Localidad> entities) throws Exception;

	public long salvar(Localidad localidad) throws Exception;

	public void eliminar(Localidad localidad) throws Exception;
	
	public List<Localidad> buscarLocalidades(String tipoBusqueda, String valorBusqueda) throws Exception;

	public List<ConValores> buscarZonas() throws Exception;

	public List<Municipio> buscarMunicipios() throws Exception;

	public List<ConValores> buscarGradoMarginalidad() throws Exception;
}
