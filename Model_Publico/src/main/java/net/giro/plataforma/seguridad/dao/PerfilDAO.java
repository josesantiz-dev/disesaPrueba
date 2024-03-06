package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Perfil;

@Remote
public interface PerfilDAO extends DAO<Perfil> {
	public List<Perfil> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Perfil> findByProperty(String propertyName, Object value, int limite) throws Exception;
}
