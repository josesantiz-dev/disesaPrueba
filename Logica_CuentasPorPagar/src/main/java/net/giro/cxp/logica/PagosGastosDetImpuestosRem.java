package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PagosGastosDetImpuestosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastosDetImpuestos entity) throws Exception;
	
	public List<PagosGastosDetImpuestos> saveOrUpdateList(List<PagosGastosDetImpuestos> entities) throws Exception;

	public void update(PagosGastosDetImpuestos entity) throws Exception;
	
	public void delete(long idPagosGastosDet) throws Exception;
	
	public PagosGastosDetImpuestos findById(Long idPagosGastosDetImpuestos);

	public List<PagosGastosDetImpuestos> findAll(long idPagosGastosDet);

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	public Long save(PagosGastosDetImpuestosExt entityExt) throws Exception;
	
	public List<PagosGastosDetImpuestosExt> saveOrUpdateListExt(List<PagosGastosDetImpuestosExt> entities) throws Exception;

	public void update(PagosGastosDetImpuestosExt entity) throws Exception;
	
	public List<PagosGastosDetImpuestosExt> findExtAll(long idPagosGastosDet) throws Exception;
}
