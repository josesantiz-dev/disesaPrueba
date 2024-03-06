package net.giro.ne.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;

@Remote
public interface SucursalDAO extends DAO<Sucursal> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(Sucursal entity) throws Exception;
	
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities) throws Exception;

	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint;
}
