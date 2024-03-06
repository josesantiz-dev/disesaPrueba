package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Banco;

@Remote
public interface BancosDAO extends DAO<Banco> {
	public long save(Banco entity, Long idEmpresa) throws ExcepConstraint;
	
	public List<Banco> saveOrUpdateList(List<Banco> entities, Long idEmpresa) throws Exception;

	public List<Banco> findAll();

	public List<Banco> findByProperty(String propertyName, Object value, Long idEmpresa, int limite);

	public List<Banco> findLikeProperty(String propertyName, Object value, Long idEmpresa, int limite);
}
