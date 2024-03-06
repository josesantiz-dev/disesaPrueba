package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraCobranza;

@Remote
public interface ObraCobranzaDAO extends DAO<ObraCobranza> {
	public long save(ObraCobranza entity, long codigoEmpresa) throws Exception;
	
	public List<ObraCobranza> saveOrUpdateList(List<ObraCobranza> entities, long codigoEmpresa) throws Exception;

	public List<ObraCobranza> findAll(long idObra, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperty(String propertyName, final Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findByProperty(String propertyName, final Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ObraCobranza> findBySpecific(long idObra, long idFactura, long idConcepto, long idEmpresa, String orderBy, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaDAO