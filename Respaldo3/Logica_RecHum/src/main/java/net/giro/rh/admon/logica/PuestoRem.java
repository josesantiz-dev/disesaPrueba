package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Puesto;

@Remote
public interface PuestoRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(Puesto entity) throws Exception;
	
	public List<Puesto> saveOrUpdateList(List<Puesto> entities) throws Exception;

	public void update(Puesto entity) throws Exception;

	public void delete(long idPuesto) throws Exception;

	public Puesto findById(long idPuesto) throws Exception;

	public List<Puesto> findAll() throws Exception;

	public List<Puesto> findAll(boolean incluyeEliminados) throws Exception;

	public List<Puesto> findByProperty(String propertyName, Object value) throws Exception;

	public List<Puesto> findByProperty(String propertyName, Object value, boolean incluyeEliminados) throws Exception;

	public List<Puesto> findLikeProperty(String propertyName, String value) throws Exception;

	public List<Puesto> findLikeProperty(String propertyName, String value, boolean incluyeEliminados) throws Exception;
}
