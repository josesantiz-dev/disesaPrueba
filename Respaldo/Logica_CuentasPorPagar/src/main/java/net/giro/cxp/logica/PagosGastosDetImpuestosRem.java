package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;

@Remote
public interface PagosGastosDetImpuestosRem {
	public Long save(PagosGastosDetImpuestos entity) throws ExcepConstraint;
	public Long save(PagosGastosDetImpuestosExt entityExt) throws Exception;

	public PagosGastosDetImpuestos update(PagosGastosDetImpuestos entity) throws ExcepConstraint;
	public PagosGastosDetImpuestos update(PagosGastosDetImpuestosExt entity) throws Exception;
	
	public void delete(PagosGastosDetImpuestos entity) throws ExcepConstraint;
	public void delete(PagosGastosDetImpuestosExt entityExt) throws Exception;

	public PagosGastosDetImpuestos findById(Long id);

	public List<PagosGastosDetImpuestos> findByProperty(String propertyName,Object value);
		
	public List<PagosGastosDetImpuestos> findAll();
	
	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max);
	public List<PagosGastosDetImpuestosExt> findLikePojoCompletoExt(PagosGastosDetExt entityExt, int max) throws Exception;
	
	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value,int max);
}
