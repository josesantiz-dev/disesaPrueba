package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;


@Remote
public interface LocalidadesRem {
	List<Localidad> buscarLocalidades(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	List<ConValores> buscarZonas() throws ExcepConstraint;

	long salvar(Localidad localidad) throws ExcepConstraint;

	void eliminar(Localidad localidad) throws ExcepConstraint;

	List<Municipio> buscarMunicipios() throws ExcepConstraint;

	List<ConValores> buscarGradoMarginalidad() throws ExcepConstraint;
}
