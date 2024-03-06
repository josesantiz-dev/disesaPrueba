package net.giro.inventarios.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.inventarios.beans.AlmacenTraspaso;

@Remote
public interface AlmacenTraspasoDAO extends DAO<AlmacenTraspaso>{
	
	public void delete(AlmacenTraspaso entity);

	public void update(AlmacenTraspaso entity);

	public AlmacenTraspaso findById(Integer id);

	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value);

	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen);

	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen);

	public List<AlmacenTraspaso> findAll();
	
	public List<AlmacenTraspaso> findAllActivos();
	
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen);

}
