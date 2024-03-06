package net.giro.ubicacion.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Estado;


@Remote
public interface EstadosRem {
	List<Estado> buscarEstados(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint;

	List<ConValores> buscarZonasEconomicas() throws ExcepConstraint;

	long salvar(Estado estado) throws ExcepConstraint;

	void eliminar(Estado pojoEstado) throws ExcepConstraint;
}
