package net.giro.ne.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.ne.beans.EmpresasUsuarios;

@Remote
public interface EmpresasUsuariosDAO extends DAO<EmpresasUsuarios> {
	public void orderBy(String orderBy);
	
	public List<EmpresasUsuarios> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpresasUsuarios> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}
