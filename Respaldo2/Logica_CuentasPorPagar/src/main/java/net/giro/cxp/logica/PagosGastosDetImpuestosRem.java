package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PagosGastosDetImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastosDetImpuestos entity) throws Exception;
	
	public List<PagosGastosDetImpuestos> saveOrUpdateList(List<PagosGastosDetImpuestos> entities) throws Exception;

	public void update(PagosGastosDetImpuestos entity) throws Exception;
	
	public void delete(PagosGastosDetImpuestos entity) throws Exception;
	
	public PagosGastosDetImpuestos findById(Long id);

	public List<PagosGastosDetImpuestos> findByProperty(String propertyName,Object value);
		
	public List<PagosGastosDetImpuestos> findAll();
	
	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max);
	
	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value,int max);

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	public Long save(PagosGastosDetImpuestosExt entityExt) throws Exception;

	public PagosGastosDetImpuestos update(PagosGastosDetImpuestosExt entity) throws Exception;

	public void delete(PagosGastosDetImpuestosExt entityExt) throws Exception;

	public List<PagosGastosDetImpuestosExt> findLikePojoCompletoExt(PagosGastosDetExt entityExt, int max) throws Exception;
}
