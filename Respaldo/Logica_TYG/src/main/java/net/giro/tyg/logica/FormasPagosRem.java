package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.FormasPagos;

@Remote
public interface FormasPagosRem {
	public long save(FormasPagos entity) throws ExcepConstraint;

    public void update(FormasPagos entity) throws ExcepConstraint;

    public void delete(FormasPagos entity) throws ExcepConstraint;
    
	public FormasPagos findById(long id);
	
	public List<FormasPagos> findByColumnName(String columnName, Object value);
    
    public List<FormasPagos> findLikeColumnName(String columnName, String value);
}
