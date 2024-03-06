package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;

@Remote
public interface FuncionDAO extends DAO<Funcion> {
	public List<Funcion> findLikeNombreAplicacion(String nombFuncion, Aplicacion app, int max) throws Exception;
	
	public List<Funcion> findAll(long idAplicacion, String orderBy) throws Exception;
}
