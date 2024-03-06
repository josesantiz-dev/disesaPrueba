package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.PermisosUsuario;

@Remote
public interface PermisosUsuariosDAO extends DAO<PermisosUsuario> {
	public long save(PermisosUsuario entity, long codigoEmpresa) throws Exception;
	
	public List<PermisosUsuario> saveOrUpdateList(List<PermisosUsuario> entities, long codigoEmpresa) throws Exception;

	public List<PermisosUsuario> findAll(long idUsuario, long idEmpresa, String orderBy) throws Exception;

	//public List<PermisosUsuario> findLikeProperty(String propertyName, final Object value, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception;

	//public List<PermisosUsuario> findByProperty(String propertyName, final Object value, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-27 | Javier Tirado 	| Creacion de EJB
 */