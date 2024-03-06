package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.plataforma.beans.ManualesProcesosAplicaciones;

@Remote
public interface ManualesProcesosAplicacionesDAO extends DAO<ManualesProcesosAplicaciones> {
	public long save(ManualesProcesosAplicaciones entity, long codigoEmpresa) throws Exception;
	
	public List<ManualesProcesosAplicaciones> saveOrUpdateList(List<ManualesProcesosAplicaciones> entities, long codigoEmpresa) throws Exception;

	public List<ManualesProcesosAplicaciones> findByManualProceso(long idManualProceso, boolean incluyeCancelados, String orderBy) throws Exception;

	public List<ManualesProcesosAplicaciones> findByAplicacion(long idAplicacion, boolean incluyeCancelados, String orderBy) throws Exception;
}
