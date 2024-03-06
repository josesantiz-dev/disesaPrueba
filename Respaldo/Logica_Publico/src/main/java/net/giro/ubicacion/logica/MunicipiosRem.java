package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Municipio;


@Remote
public interface MunicipiosRem {
	List<Municipio> buscarMunicipios(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	List<Estado> buscarEstados() throws ExcepConstraint;

	long salvar(Municipio municipio) throws ExcepConstraint;

	void eliminar(Municipio municipio) throws ExcepConstraint;
}
