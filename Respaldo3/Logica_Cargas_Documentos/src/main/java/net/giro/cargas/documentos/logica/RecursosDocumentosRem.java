package net.giro.cargas.documentos.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cargas.documentos.beans.RecursosDocumentos;
import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;
import net.giro.cargas.documentos.util.TipoRecursosDocumentos;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface RecursosDocumentosRem {
	public void setInfoSesion(InfoSesion infoSesion);
    
	public Long save(RecursosDocumentos entity) throws Exception;

	public List<RecursosDocumentos> saveOrUpdateList(List<RecursosDocumentos> entities) throws Exception;

	public RecursosDocumentos update(RecursosDocumentos entity) throws Exception;
	
	public void delete(long idRecursoDocumento) throws Exception;

	public RecursosDocumentos findById(long idRecursoDocumento) throws Exception;

	public List<RecursosDocumentos> findLike(String value, String orderBy, int limite) throws Exception;
	
	public List<RecursosDocumentos> findLikeProperty(String property, Object value, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentos> findByProperty(String property, Object value, String orderBy, int limite) throws Exception;

	public List<RecursosDocumentos> findByTipoAplicacion(TipoRecursosDocumentos tipo, long idModulo) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------------
	// RecursosDocumentosModulos
	// -----------------------------------------------------------------------------------------------------------------------------

	public RecursosDocumentosModulos saveOrUpdateModulo(long idRecursoDocumento, RecursosDocumentosModulos recursoDocumentoModulo) throws Exception;

	public List<RecursosDocumentosModulos> saveModulos(long idRecursoDocumento, List<RecursosDocumentosModulos> recursosDocumentosModulos) throws Exception;

	public List<RecursosDocumentosModulos> findAllModulos(long idRecursoDocumento) throws Exception;

	public void deleteAllModulos(List<RecursosDocumentosModulos> recursosDocumentosModulos) throws Exception;

	public void deleteModulo(long idRecursoDocumentoModulo) throws Exception;

	// -----------------------------------------------------------------------------------------------------------------------------
	// Importar/Exportar Recurso
	// -----------------------------------------------------------------------------------------------------------------------------
	
	public Respuesta importar(RecursosDocumentos entity, byte[] fileSource, String path) throws Exception;

	public byte[] exportar(long idRecursoDocumento, String path) throws Exception;

	public byte[] exportar(RecursosDocumentos entity, String path) throws Exception;
}
