package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.Gastos;

@Remote
public interface GastosDAO extends DAO<Gastos> {
	public void setEmpresa(Long idEmpresa);
		
	public long save(Gastos entity) throws Exception;
	
	public List<Gastos> saveOrUpdateList(List<Gastos> entities) throws Exception;
}
