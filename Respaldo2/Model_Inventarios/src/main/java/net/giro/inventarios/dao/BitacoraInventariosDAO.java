package net.giro.inventarios.dao;

import java.util.List;

import net.giro.DAO;
import net.giro.inventarios.beans.BitacoraInventarios;

public interface BitacoraInventariosDAO extends DAO<BitacoraInventarios> {
	public List<BitacoraInventarios> findAll();
	
	public List<BitacoraInventarios> findByProperty(String propertyName, Object propertyValue, long idUsuario, int limite);
	
	public List<BitacoraInventarios> findLikeProperty(String propertyName, Object propertyValue, long idUsuario, int limite);
}
