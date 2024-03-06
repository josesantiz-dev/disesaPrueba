package net.giro.cargas.documentos.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;

import javax.ejb.Remote;

@Remote
public interface RecursosDocumentosModulosDAO extends DAO<RecursosDocumentosModulos> {
	public long save(RecursosDocumentosModulos entity, long codigoEmpresa) throws Exception;
	
	public List<RecursosDocumentosModulos> saveOrUpdateList(List<RecursosDocumentosModulos> entities, long codigoEmpresa) throws Exception;

	public List<RecursosDocumentosModulos> findAll(long idRecursoDocumento) throws Exception;
	
	public List<RecursosDocumentosModulos> findLikeProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentosModulos> findByProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
}
