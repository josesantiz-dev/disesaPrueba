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

	public void delete(Puesto entity) throws Exception;

	public Puesto findById(Long id);
	
	public List<Puesto> findAll();
	
	public List<Puesto> findAllActivos();

	public List<Puesto> findByProperty(String propertyName, Object value);
	
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}
