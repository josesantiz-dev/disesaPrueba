package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraSubcontratista;
import net.giro.adp.beans.ObraSubcontratistaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ObraSubcontratistaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public ObraSubcontratista save(ObraSubcontratista entity) throws Exception;
	
	public List<ObraSubcontratista> saveOrUpdateList(List<ObraSubcontratista> entities) throws Exception;

	public void delete(Long idObraSubcontratista) throws Exception;

	public List<ObraSubcontratista> findAll(long idObra, String orderBy) throws Exception;

	public List<ObraSubcontratista> findLikeProperty(String propertyName, Object value, long idObra, String orderBy, int limite) throws Exception;

	public List<ObraSubcontratista> findByProperty(String propertyName, Object value, long idObra, String orderBy, int limite) throws Exception;

	public Respuesta procesarCFDI(byte[] fileSrc, String fileName, String originalFileName) throws Exception;

	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public ObraSubcontratistaExt save(ObraSubcontratistaExt extendido) throws Exception;
	
	public List<ObraSubcontratistaExt> saveOrUpdateListExt(List<ObraSubcontratistaExt> extendidos) throws Exception;
	
	public List<ObraSubcontratistaExt> findAllExt(long idObra, String orderBy) throws Exception;
}
