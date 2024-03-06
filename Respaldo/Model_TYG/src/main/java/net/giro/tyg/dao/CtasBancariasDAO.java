package net.giro.tyg.dao;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;


@Remote
public interface CtasBancariasDAO extends DAO<CtasBanco>{
	public long save(CtasBanco entity) throws ExcepConstraint, RuntimeException;

	public void delete(CtasBanco entity);
	
	//public void update(CtasBanco entity);

	public CtasBanco findById(long id);

	public CtasBanco findAllById(long id);

	public List<CtasBanco> findAllByProperty(String propertyName, String value, int maximo,String empresas);
	
	public List<CtasBanco> findByProperty(String propertyName, Object value,String empresas);
	
	public List<CtasBanco> findLikeClaveNombreCuenta(String value, int max, String empresas, Integer empresaId);
	
	public List<CtasBanco> findAll(String empresas);
	
	public List<CtasBanco> findTodas();
	
	public boolean esBancoCierre(final Object value);
}
