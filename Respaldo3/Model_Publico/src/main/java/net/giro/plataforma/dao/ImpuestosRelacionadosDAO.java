package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.ImpuestosRelacionados;

@Remote
public interface ImpuestosRelacionadosDAO extends DAO<ImpuestosRelacionados> {
	public long save(ImpuestosRelacionados entity, long codigoEmpresa) throws Exception;
	
	public List<ImpuestosRelacionados> saveOrUpdateList(List<ImpuestosRelacionados> entities, long codigoEmpresa) throws Exception;
	
	public List<ImpuestosRelacionados> findByImpuesto(Long idImpuesto) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-04-15 | Javier Tirado 	| Creacion de EJB.
 */