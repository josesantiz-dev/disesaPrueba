package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface InsumosDetallesRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(InsumosDetalles entity) throws Exception;

	public List<InsumosDetalles> saveOrUpdateList(List<InsumosDetalles> entities) throws Exception;

	public void update(InsumosDetalles entity) throws Exception;

	public void delete(Long entity) throws Exception;

	public InsumosDetalles findById(Long id);
	
	public List<InsumosDetalles> findAll(Long idInsumos);
	
	public List<InsumosDetalles> findByProperty(String propertyName, final Object value, int max);
	
	public List<InsumosDetalles> findLikeProperty(String propertyName, final Object value, int max);

	public InsumosDetalles convertir(InsumosDetallesExt target);
	
	public InsumosDetallesExt convertir(InsumosDetalles target);

	public List<InsumosDetallesExt> extenderInsumosDetalles(List<InsumosDetalles> lista) throws Exception;

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(InsumosDetallesExt entityExt) throws Exception;

	public List<InsumosDetallesExt> saveOrUpdateListExt(List<InsumosDetallesExt> entities) throws Exception;

	public void update(InsumosDetallesExt entityExt) throws Exception;

	public InsumosDetallesExt findByIdExt(Long id) throws Exception;
	
	public List<InsumosDetallesExt> findAllExt(Long idInsumos) throws Exception;

	public List<InsumosDetallesExt> findByPropertyExt(String propertyName, final Object value, int max) throws Exception;

	public List<InsumosDetallesExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodos extenderInsumosDetalles y convertir (x2)
 */