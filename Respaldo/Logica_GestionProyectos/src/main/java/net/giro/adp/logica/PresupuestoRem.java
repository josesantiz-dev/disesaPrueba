package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoExt;
import net.giro.comun.ExcepConstraint;
import net.giro.respuesta.Respuesta;

@Remote
public interface PresupuestoRem {
	public Long save(Presupuesto entity) throws ExcepConstraint;
	
	public Long save(PresupuestoExt entityExt) throws ExcepConstraint;
	
	public void update(Presupuesto entity) throws ExcepConstraint;
	
	public void update(PresupuestoExt entityExt) throws ExcepConstraint;
	
	public void delete(Long id) throws ExcepConstraint;
	
	public List<Presupuesto> findAll();

	public Presupuesto findById(Long id);
	
	public PresupuestoExt findByIdExt(Long id) throws Exception;

	public List<Presupuesto> findByProperty(String propertyName, Object value, int max);
	
	public List<PresupuestoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;

	public List<Presupuesto> findLikeProperty(String propertyName, final Object value, int max);
	
	public List<PresupuestoExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;
	
	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout) throws Exception;
	
	public Presupuesto convertir(PresupuestoExt pojoTarget) throws Exception;
	
	public PresupuestoExt convertir(Presupuesto pojoTarget) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-15 | Javier Tirado 	| Añado metodos para extender el entity Presupuesto y viceversa.
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado  metodo procesarLayoutPPT
 */