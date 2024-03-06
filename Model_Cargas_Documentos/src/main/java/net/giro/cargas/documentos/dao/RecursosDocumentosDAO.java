package net.giro.cargas.documentos.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.cargas.documentos.beans.RecursosDocumentos;

import javax.ejb.Remote;

@Remote
public interface RecursosDocumentosDAO extends DAO<RecursosDocumentos> {
	public long save(RecursosDocumentos entity, long codigoEmpresa) throws Exception;
	
	public List<RecursosDocumentos> saveOrUpdateList(List<RecursosDocumentos> entities, long codigoEmpresa) throws Exception;

	public List<RecursosDocumentos> findLike(String value, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<RecursosDocumentos> findLikeProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentos> findByProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentos> findAllById(List<Long> listIds, int tipo, long idEmpresa, String orderBy) throws Exception;
}
