package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Regiones;


@Remote
public interface RegionesRem {
	List<Regiones> buscarRegiones(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	long salvar(Regiones regiones) throws ExcepConstraint;

	void eliminar(Regiones regiones) throws ExcepConstraint;
}
