package net.giro.adp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.comun.ExcepConstraint;

@Remote
public interface InsumosDetallesRem {
	public void showSystemOuts(boolean value);
	
	public InsumosDetalles convertir(InsumosDetallesExt target);
	
	public InsumosDetallesExt convertir(InsumosDetalles target);

	public Long save(InsumosDetalles entity) throws ExcepConstraint;
	
	public Long save(InsumosDetallesExt entityExt) throws ExcepConstraint;
	
	public void update(InsumosDetalles entity) throws ExcepConstraint;
	
	public void update(InsumosDetallesExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;
	
	public List<InsumosDetalles> findAll();

	public InsumosDetalles findById(Long id);
	
	public InsumosDetallesExt findByIdExt(Long id) throws Exception;

	public List<InsumosDetalles> findByProperty(String propertyName, final Object value, int max);
	
	public List<InsumosDetallesExt> findByPropertyExt(String propertyName, final Object value, int max) throws Exception;

	public List<InsumosDetalles> findLikeProperty(String propertyName, final Object value, int max);
	
	public List<InsumosDetallesExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;
	
	public List<InsumosDetallesExt> extenderInsumosDetalles(List<InsumosDetalles> lista) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodos extenderInsumosDetalles y convertir (x2)
 */