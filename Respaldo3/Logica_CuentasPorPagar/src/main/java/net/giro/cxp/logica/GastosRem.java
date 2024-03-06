package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.GastosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GastosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------
	
	public long save(GastosExt pojoGasto) throws Exception;

	public void update(GastosExt pojoGasto) throws Exception;

	public void delete(GastosExt pojoGasto) throws Exception;

	public GastosExt findById(Long id) throws Exception;

	public List<GastosExt> findAll() throws Exception;
	
	public List<GastosExt> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<GastosExt> findLikeProperty(String propertyName, final String value, int limite) throws Exception;
}
