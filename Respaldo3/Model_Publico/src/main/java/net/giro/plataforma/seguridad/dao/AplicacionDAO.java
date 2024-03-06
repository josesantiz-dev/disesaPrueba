package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Aplicacion;

@Remote
public interface AplicacionDAO extends DAO<Aplicacion> {
	public List<Aplicacion> findAll(String orderBy) throws Exception;
}
