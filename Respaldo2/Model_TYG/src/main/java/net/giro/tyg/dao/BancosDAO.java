package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.tyg.admon.Banco;

@Remote
public interface BancosDAO extends DAO<Banco> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(Banco entity) throws Exception;
	
	public List<Banco> saveOrUpdateList(List<Banco> entities) throws Exception;
}
