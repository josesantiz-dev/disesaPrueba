package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.MenuFuncion;

@Remote
public interface MenuFuncionDAO extends DAO<MenuFuncion> {
	public void orderBy(String orderBy);
	
	public List<MenuFuncion> findByMenuId(long id) throws Exception;
	
	public List<MenuFuncion> findByProperty(String propertyName, final Object value, int limite) throws Exception;
}
