package net.giro.adp.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoHistorico;

@Remote
public interface ObraEmpleadoHistoricoDAO extends DAO<ObraEmpleadoHistorico> {
	public long save(ObraEmpleadoHistorico entity, long codigoEmpresa) throws Exception;
	
	public long save(long idObra, long idEmpleado, Date fechaAsignacion, long eliminadoPor, long codigoEmpresa) throws Exception;

	public long save(ObraEmpleado historico, long codigoEmpresa) throws Exception;
	
	public List<ObraEmpleadoHistorico> saveOrUpdateList(List<ObraEmpleadoHistorico> entities, long codigoEmpresa) throws Exception;

	public List<ObraEmpleadoHistorico> findByObra(long idObra);

	public List<ObraEmpleadoHistorico> findByEmpleado(long idEmpleado);
}
