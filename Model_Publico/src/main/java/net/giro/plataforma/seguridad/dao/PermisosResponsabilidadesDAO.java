package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.PermisosResponsabilidad;

@Remote
public interface PermisosResponsabilidadesDAO extends DAO<PermisosResponsabilidad> {
	public long save(PermisosResponsabilidad entity, long codigoEmpresa) throws Exception;
	
	public List<PermisosResponsabilidad> saveOrUpdateList(List<PermisosResponsabilidad> entities, long codigoEmpresa) throws Exception;

	public List<PermisosResponsabilidad> findAll(long idResponsabilidad, long idEmpresa, String orderBy) throws Exception;

	//public List<PermisosResponsabilidad> findLikeProperty(String propertyName, final Object value, long idResponsabilidad, long idEmpresa, String orderBy, int limite) throws Exception;

	//public List<PermisosResponsabilidad> findByProperty(String propertyName, final Object value, long idResponsabilidad, long idEmpresa, String orderBy, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-27 | Javier Tirado 	| Creacion de EJB
 */