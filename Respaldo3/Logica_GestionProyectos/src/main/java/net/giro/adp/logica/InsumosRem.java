package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface InsumosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);

	public Long save(Insumos entity) throws Exception;

	//public Respuesta saveOrUpdate(Insumos explosionInsumos, List<InsumosDetalles> detalles);

	public List<Insumos> saveOrUpdateList(List<Insumos> entities) throws Exception;

	public void update(Insumos entity) throws Exception;
	
	public Respuesta cancelar(long idExplosionInsumos) throws Exception;

	public Respuesta cancelar(long idExplosionInsumos, boolean forzarCancelacion) throws Exception;
	
	public void delete(long idExplosionInsumos) throws Exception;
	
	public Insumos findById(long idExplosionInsumos) throws Exception;
	
	public Insumos findActual(long idObra) throws Exception;

	public long findIdActual(long idObra) throws Exception;

	public List<Insumos> findAll(long idObra, boolean incluyeCanceladas) throws Exception;

	public List<Insumos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<Insumos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception;
	
	public List<Insumos> findLike(String value, boolean incluyeCanceladas, int limite) throws Exception;

	public List<Insumos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Insumos> findLikeProperty(String propertyName, final Object value, boolean incluyeCanceladas, int limite) throws Exception;
	
	public Respuesta importar(byte[] fileSrc, String fileExtension, HashMap<String, String> insumosCellReference) throws Exception;
	
	public boolean comprobarInsumo(long idObra);

	/**
	 * Comprueba si la Obra indicada tiene cargada una Explosion de Insumos y el estatus de esta
	 * @param idObra
	 * @return 0:Sin explosion de Insumos, 1:Explosion de Insumos Activa, 2:Explosion de Insumos SUMINISTRADA
	 */
	public int comprobarExplosionInsumos(long idObra);

	/**
	 * Comprueba el estatus de la Explosion de Insumos indicada
	 * @param idObra
	 * @return -1:Sin explosion de insumos, 0:Activa, 1:Cancelada, 2:Suministrada
	 */
	public int estatusExplosionInsumos(long idExplosionInsumos);

	public List<Insumos> findByActivos(long idObra) throws Exception;
	
	public Insumos convertir(InsumosExt target) throws Exception;	
	
	public InsumosExt convertir(Insumos target) throws Exception;

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	public Long save(InsumosExt entityExt) throws Exception;

	//public Respuesta saveOrUpdateExt(InsumosExt explosionInsumos, List<InsumosDetallesExt> detalles) throws Exception;

	public void update(InsumosExt entityExt) throws Exception;
	
	public InsumosExt findByIdExt(long idExplosionInsumos) throws Exception;
	
	public InsumosExt findExtActual(long idObra) throws Exception;

	public List<InsumosExt> findExtAll(long idObra, boolean incluyeCanceladas) throws Exception;

	public List<InsumosExt> findExtByActivos(long idObra) throws Exception;
	
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, int limite) throws Exception;
	
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception;

	public List<InsumosExt> findLikeExt(String value, boolean incluyeCanceladas, int limite) throws Exception;

	public List<InsumosExt> findLikePropertyExt(String propertyName, final Object value, int limite) throws Exception;
	
	public List<InsumosExt> findLikePropertyExt(String propertyName, final Object value, boolean incluyeCanceladas, int limite) throws Exception;
}
