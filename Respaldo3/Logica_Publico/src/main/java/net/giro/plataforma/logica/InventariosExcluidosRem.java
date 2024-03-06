package net.giro.plataforma.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.InventariosExcluidos;

@Remote
public interface InventariosExcluidosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(InventariosExcluidos entity) throws Exception;
	
	public List<InventariosExcluidos> saveOrUpdateList(List<InventariosExcluidos> entities) throws Exception;
	
	public List<InventariosExcluidos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<InventariosExcluidos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
}
