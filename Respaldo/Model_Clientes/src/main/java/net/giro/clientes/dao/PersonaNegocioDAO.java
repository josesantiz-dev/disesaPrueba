package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaNegocio;
import net.giro.comun.ExcepConstraint;

@Remote
public interface PersonaNegocioDAO extends DAO<PersonaNegocio> {
	public List<PersonaNegocio> findLikePropiedad(String propiedad, String valor, Long personaId) throws ExcepConstraint;
	
	public List<PersonaNegocio> findLikeNegocioPropiedad(String propiedad, String valor, Long negocioId) throws ExcepConstraint;
	
	public List<PersonaNegocio> findAccionistasLikeNegocio(Long negocioId) throws ExcepConstraint;
	
	public List<PersonaNegocio> findDirectivosLikeNegocio(Long negocioId) throws ExcepConstraint;
}
