package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Municipio;

@Remote
public interface MunicipiosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public List<Municipio> saveOrUpdateList(List<Municipio> entities) throws Exception;

	public long salvar(Municipio municipio) throws ExcepConstraint;

	public void eliminar(Municipio municipio) throws ExcepConstraint;
	
	public List<Municipio> buscarMunicipios(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	public List<Estado> buscarEstados() throws ExcepConstraint;
}
