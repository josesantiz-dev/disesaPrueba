package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.AreaExt;

@Remote
public interface AreaRem {
	
	public Long save(Area entity) throws ExcepConstraint; 
	//public Long save(AreaExt entityExt) throws ExcepConstraint;
	
	public void delete(Area entity) throws ExcepConstraint;
	//public void delete(AreaExt entityExt) throws ExcepConstraint;

	public Area update(Area entity) throws ExcepConstraint;
	public Area update(AreaExt entity) throws ExcepConstraint;

	public Area findById(Long id);

	public List<Area> findByProperty(String propertyName, Object value);
	public List<AreaExt> findByPropertyExt(String propertyName, Object value);
	
	public List<Area> findAll();
	public List<Area> findAllActivos();
	
	public List<AreaExt> findAllExt();
	
	
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<AreaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	
}
