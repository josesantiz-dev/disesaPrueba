package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.SolicitudBodegaEstatus;

@Remote
public interface SolicitudBodegaEstatusDAO extends DAO<SolicitudBodegaEstatus> {
	public long save(SolicitudBodegaEstatus entity, long codigoEmpresa) throws Exception;

	public List<SolicitudBodegaEstatus> findAll(long idSolicitud, String orderBy) throws Exception;

	public List<SolicitudBodegaEstatus> findAllByMovimiento(long idSolicitud, long idMovimiento) throws Exception;

	public List<SolicitudBodegaEstatus> findAllByTraspaso(long idSolicitud, long idTraspaso) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  3.1 | 2019-07-16 | Javier Tirado 	| Creacion de interface
 */
