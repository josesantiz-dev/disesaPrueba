package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface RequisicionRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public Long save(Requisicion entity) throws Exception;
	
	public void update(Requisicion entity) throws Exception;
    
    public List<Requisicion> saveOrUpdateList(List<Requisicion> entities) throws Exception;
	
	public Respuesta cancelar(long idRequisicion, long idUsuario) throws Exception;
	
	public void delete(long idRequisicion) throws Exception;

	public Requisicion findById(long idRequisicion);
	
	public List<Requisicion> findAll(long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception;
	
	public List<Requisicion> findLike(String value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception;

	public List<Requisicion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception;
	
	public List<Requisicion> findByProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception;

	public List<Requisicion> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Requisicion> findByProperties(HashMap<String, Object> params, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception;

	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	/**
	 * Recupera la(s) Solicitud(es) a Bodega generadas
	 * @param idRequisicion
	 * @return
	 * @throws Exception
	 */
	public List<SolicitudBodega> solicitudBodega(long idRequisicion) throws Exception;
	
	//------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	//------------------------------------------------------------------------------------------------------
	
	public Long save(RequisicionExt entityExt) throws Exception;
	
	public void update(RequisicionExt entityExt) throws Exception;
	
	public RequisicionExt findExtById(Long idRequisicion) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. Normal y extendido
 */