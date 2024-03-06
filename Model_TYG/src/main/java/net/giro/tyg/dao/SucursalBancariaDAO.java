package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.SucursalBancaria;

@Remote
public interface SucursalBancariaDAO extends DAO<SucursalBancaria> {
    public long save(SucursalBancaria entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<SucursalBancaria> saveOrUpdateList(List<SucursalBancaria> entities, Long idEmpresa) throws Exception;

	public List<SucursalBancaria> findAll();

	public List<SucursalBancaria> findByProperty(String propertyName, Object value, Long idEmpresa, int limite);

	public List<SucursalBancaria> findLikeProperty(String propertyName, Object value, Long idEmpresa, int limite);
}
