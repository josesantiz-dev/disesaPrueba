package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.TraspasoBodegaBodega;

@Remote
public interface TraspasoBodegaBodegaDAO extends DAO<TraspasoBodegaBodega> {
	public long save(TraspasoBodegaBodega entity, long codigoEmpresa) throws Exception;

	public long save(long idTraspaso, long idObraOrigen, long idObraDestino, long codigoEmpresa) throws Exception;

	public TraspasoBodegaBodega findByTraspaso(long idTraspaso);

	public List<TraspasoBodegaBodega> findAll(long idTraspaso);

	public List<TraspasoBodegaBodega> findAllObraOrigen(long idObra);

	public List<TraspasoBodegaBodega> findAllObraDestino(long idObra);
}
