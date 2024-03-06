package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.tyg.admon.Banco;

@Remote
public interface BancosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public long save(Banco entity) throws Exception;

	public List<Banco> saveOrUpdateList(List<Banco> entities) throws Exception;

    public void update(Banco entity) throws Exception;

    public void delete(Banco entity) throws Exception;
    
	public Banco findById(long id);
	
	public List<Banco> findByColumnName(String columnName, Object value);
    
    public List<Banco> findLikeColumnName(String columnName, String value);
}
