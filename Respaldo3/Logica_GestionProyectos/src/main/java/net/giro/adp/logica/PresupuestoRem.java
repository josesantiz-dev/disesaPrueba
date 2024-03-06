package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface PresupuestoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Presupuesto entity) throws Exception;
	
	public List<Presupuesto> saveOrUpdateList(List<Presupuesto> entities) throws Exception;

	public void update(Presupuesto entity) throws Exception;
	
	public void delete(Long id) throws Exception;
	
	public Presupuesto findById(Long idPresupuesto);

	public Presupuesto findActual(long idObra);

	public List<Presupuesto> findAll();

	public List<Presupuesto> findAll(long idObra);

	public List<Presupuesto> findAll(long idObra, int limite);

	public List<Presupuesto> findByProperty(String propertyName, Object value, int max);
	
	public List<Presupuesto> findLikeProperty(String propertyName, final Object value, int max);
	
	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout) throws Exception;
	
	public Presupuesto convertir(PresupuestoExt pojoTarget) throws Exception;

	public PresupuestoExt convertir(Presupuesto pojoTarget) throws Exception;

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	public Long save(PresupuestoExt entityExt) throws Exception;

	public void update(PresupuestoExt entityExt) throws Exception;

	public PresupuestoExt findByIdExt(Long idPresupuesto) throws Exception;

	public PresupuestoExt findActualExt(long idObra) throws Exception;

	public List<PresupuestoExt> findAllExt() throws Exception;

	public List<PresupuestoExt> findAllExt(long idObra) throws Exception;

	public List<PresupuestoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception;

	public List<PresupuestoExt> findLikePropertyExt(String propertyName, final Object value, int max) throws Exception;

}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-15 | Javier Tirado 	| Añado metodos para extender el entity Presupuesto y viceversa.
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodo procesarLayoutPPT
 */