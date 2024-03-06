package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraCobranzaImpuestos;

@Remote
public interface ObraCobranzaImpuestosDAO extends DAO<ObraCobranzaImpuestos> {
	public long save(ObraCobranzaImpuestos entity, long codigoEmpresa) throws Exception;
	
	public List<ObraCobranzaImpuestos> saveOrUpdateList(List<ObraCobranzaImpuestos> entities, long codigoEmpresa) throws Exception;

	public List<ObraCobranzaImpuestos> findAll(long idObraCobranza, String orderBy) throws Exception;
}
