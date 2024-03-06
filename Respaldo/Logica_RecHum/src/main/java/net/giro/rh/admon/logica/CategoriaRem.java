package net.giro.rh.admon.logica;

import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Categoria;

@Remote
public interface CategoriaRem{

	public Long save(Categoria entity) throws ExcepConstraint;
	//public Long save(CategoriaExt entityExt) throws ExcepConstraint;
	
	public void delete(Categoria entity) throws ExcepConstraint;
	//public void delete(CategoriaExt entityExt) throws ExcepConstraint;

	public Categoria update(Categoria entity) throws ExcepConstraint;
	//public Categoria update(CategoriaExt entity) throws ExcepConstraint;

	public Categoria findById(Long id);

	public List<Categoria> findByProperty(String propertyName, Object value);

	public List<Categoria> findAll();
	public List<Categoria> findAllActivos();
	
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<CategoriaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	
}
