package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.PolizasInterfaces;

@Remote
public interface PolizasInterfacesDAO extends DAO<PolizasInterfaces> {
	public long save(PolizasInterfaces entity, long codigoEmpresa) throws Exception;
	
	public List<PolizasInterfaces> saveOrUpdateList(List<PolizasInterfaces> entities, long codigoEmpresa) throws Exception;

	public List<PolizasInterfaces> findAll(long idMensajeTransaccion, String orderBy) throws Exception;
	
	public List<PolizasInterfaces> findByProperty(String propertyName, final Object value, String orderBy, long idEmpresa, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperty(String propertyName, final Object value, String orderBy, long idEmpresa, int limite) throws Exception;
}
