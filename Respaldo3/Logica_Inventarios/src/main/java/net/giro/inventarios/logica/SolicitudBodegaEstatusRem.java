package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.SolicitudBodegaEstatus;
import net.giro.plataforma.InfoSesion;

@Remote
public interface SolicitudBodegaEstatusRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(SolicitudBodegaEstatus entity) throws Exception;

	public long save(long idSolicitud, long idMovimiento, long idTraspaso) throws Exception;
	
	public List<SolicitudBodegaEstatus> saveOrUpdateList(List<SolicitudBodegaEstatus> entities) throws Exception;

	public void update(SolicitudBodegaEstatus entity) throws Exception;

	public SolicitudBodegaEstatus saveMovimiento(long idSolicitud, long idMovimiento) throws Exception;

	public SolicitudBodegaEstatus saveTraspaso(long idSolicitud, long idTraspaso) throws Exception;

	public void delete(long idSolicitudBodegaEstatus) throws Exception;
	
	public SolicitudBodegaEstatus findById(long idSolicitudBodegaEstatus) throws Exception;

	public List<SolicitudBodegaEstatus> findAll(long idSolicitud, String orderBy) throws Exception;

	public List<SolicitudBodegaEstatus> findAllByMovimiento(long idSolicitud, long idMovimiento) throws Exception;

	public List<SolicitudBodegaEstatus> findAllByTraspaso(long idSolicitud, long idTraspaso) throws Exception;
	
	public SolicitudBodegaEstatus findLast(long idSolicitud) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  3.1 | 2019-07-16 | Javier Tirado 	| Creacion de interface
 */
