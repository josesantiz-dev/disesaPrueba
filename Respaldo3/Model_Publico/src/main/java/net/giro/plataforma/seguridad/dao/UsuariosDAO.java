package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Usuario;

@Remote
public interface UsuariosDAO extends DAO<Usuario> {
	public List<Usuario> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
	
	public List<Usuario> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
}
