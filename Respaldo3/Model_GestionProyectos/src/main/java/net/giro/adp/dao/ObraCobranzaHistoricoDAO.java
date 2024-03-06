package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraCobranzaHistorico;

@Remote
public interface ObraCobranzaHistoricoDAO extends DAO<ObraCobranzaHistorico> {
	public void orderBy(String orderBy);

	public long save(ObraCobranzaHistorico entity, long codigoEmpresa) throws Exception;
	
	public List<ObraCobranzaHistorico> saveOrUpdateList(List<ObraCobranzaHistorico> entities, long codigoEmpresa) throws Exception;
	
	public List<ObraCobranzaHistorico> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<ObraCobranzaHistorico> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaDAO