package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.FormasPagos;

@Remote
public interface FormasPagosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(FormasPagos entity) throws Exception;

    public void update(FormasPagos entity) throws Exception;

    public void delete(FormasPagos entity) throws Exception;
    
	public FormasPagos findById(long id);
	
	public List<FormasPagos> findAll(String orderBy) throws Exception;
	
	public List<FormasPagos> findByColumnName(String columnName, Object value);
    
    public List<FormasPagos> findLikeColumnName(String columnName, String value);
	
	public List<FormasPagos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
    
    public List<FormasPagos> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;
}
