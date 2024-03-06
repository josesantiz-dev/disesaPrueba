package net.giro.inventarios.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;
import net.giro.inventarios.beans.AuditoriaInventarios;

public interface AuditoriaInventariosDAO extends DAO<AuditoriaInventarios> {
	public long save(AuditoriaInventarios entity, long codigoEmpresa) throws Exception;
	
	public List<AuditoriaInventarios> saveOrUpdateList(List<AuditoriaInventarios> entities, long codigoEmpresa) throws Exception;
	
	public List<AuditoriaInventarios> findAll(long idProducto, long idEmpresa, String orderBy) throws Exception;
	
	public List<AuditoriaInventarios> findByProperty(String propertyName, Object propertyValue, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<AuditoriaInventarios> findLikeProperty(String propertyName, Object propertyValue, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<AuditoriaInventarios> findByDates(Date fechaDesde, Date fechaHasta, long idProducto, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2019-11-29 | Javier Tirado 	| Creacion de EJB
 */
