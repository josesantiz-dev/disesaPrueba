package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CatBancos;

@Remote
public interface CatBancosRem {
	public long save(CatBancos entity) throws ExcepConstraint;

    public void update(CatBancos entity) throws ExcepConstraint;

    public void delete(CatBancos entity) throws ExcepConstraint;
    
	public CatBancos findById(long id);
	
	public List<CatBancos> findByColumnName(String columnName, Object value);
    
    public List<CatBancos> findLikeColumnName(String columnName, String value);
}
