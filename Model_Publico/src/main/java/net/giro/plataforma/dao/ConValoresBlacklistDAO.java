package net.giro.plataforma.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.ConValoresBlacklist;

@Remote
public interface ConValoresBlacklistDAO extends DAO<ConValoresBlacklist> {
	public long save(ConValoresBlacklist entity, long codigoEmpresa) throws Exception;

	public ConValoresBlacklist save(ConValores conValores, long creadoPor, long idEmpresa, long codigoEmpresa) throws Exception;
	
	public List<ConValoresBlacklist> saveOrUpdateList(List<ConValoresBlacklist> entities, long codigoEmpresa) throws Exception;
	
	public List<ConValoresBlacklist> findAll(long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception;
	
	public List<ConValoresBlacklist> findByLikeProperty(String propertyName, Object propertyValue, long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<ConValoresBlacklist> findByByProperty(String propertyName, Object propertyValue, long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Creacion de EJB.
 */
