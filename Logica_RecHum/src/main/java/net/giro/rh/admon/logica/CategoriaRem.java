package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.Categoria;

@Remote
public interface CategoriaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Categoria entity) throws Exception;
	
	public List<Categoria> saveOrUpdateList(List<Categoria> entities) throws Exception;

	public void update(Categoria entity) throws Exception;

	public void delete(Categoria entity) throws Exception;

	public Categoria findById(Long id);

	public List<Categoria> findAll();
	
	public List<Categoria> findAllActivos();

	public List<Categoria> findByProperty(String propertyName, Object value);

	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}
