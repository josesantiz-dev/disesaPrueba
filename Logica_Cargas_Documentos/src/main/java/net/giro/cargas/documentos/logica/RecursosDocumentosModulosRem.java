package net.giro.cargas.documentos.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface RecursosDocumentosModulosRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(RecursosDocumentosModulos entity) throws Exception;

	public RecursosDocumentosModulos saveOrUpdate(RecursosDocumentosModulos entity) throws Exception;
	
	public List<RecursosDocumentosModulos> saveOrUpdateList(List<RecursosDocumentosModulos> entities) throws Exception;
	
	public RecursosDocumentosModulos update(RecursosDocumentosModulos entity) throws Exception;
	
	public void delete(long idRecursoDocumentoModulo) throws Exception;

	public RecursosDocumentosModulos findById(long idRecursoDocumentoModulo) throws Exception;

	public List<RecursosDocumentosModulos> findAll(long idRecursoDocumentoModulo) throws Exception;

	public List<RecursosDocumentosModulos> findAllByModulo(long idModulo) throws Exception;
	
	public List<RecursosDocumentosModulos> findLikeProperty(String property, Object value, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentosModulos> findByProperty(String property, Object value, String orderBy, int limite) throws Exception;
	
}
