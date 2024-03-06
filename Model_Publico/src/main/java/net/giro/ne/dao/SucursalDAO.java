package net.giro.ne.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.ne.beans.Sucursal;

@Remote
public interface SucursalDAO extends DAO<Sucursal> {
	public long save(Sucursal entity, long empresa) throws Exception;
	
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities, long empresa) throws Exception;

	public List<Sucursal> findLikePropiedad(String propiedad, String valor, long empresa) throws Exception;
	
	public List<Sucursal> findAll(String orderBy, long idEmpresa);
}
