package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface PagosGastosDetRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastosDet entity) throws Exception;

	public List<PagosGastosDet> saveOrUpdateList(List<PagosGastosDet> entities) throws Exception;

	public void update(PagosGastosDet entity) throws Exception;
	
	public void delete(PagosGastosDet entity) throws Exception;

	public PagosGastosDet findById(Long id);

	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value);
	
	public List<PagosGastosDet> findByProperty(String propertyName, Object value);

	public List<PagosGastosDet> findAll();
	
	public List<PagosGastosDet> findLikePojoCompleto(Object entity, int max);
	
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max);
	
	public Double saldoRepCajaChica(Object value);
	
	public String eliminaDetGastos(PagosGastosDet value) throws Exception ;
	
	public List<PagosGastosDet> findPagoProvisionado(String propertyName, Object value, String sucursales);

	public Respuesta analizaFactura(byte[] fileSrc) throws Exception;

	public Respuesta cargaFacturaXML(byte[] fileSrc, Long idComprobacion) throws Exception;

	public void eliminarFactura(long idFactura) throws Exception;
	
	public String getFacturaProperty(long idFactura, String property);
	
	public void setFacturaProperty(long idFactura, String property, String value) throws Exception;

	public List<PagosGastosDet> findByPagosGastos(PagosGastos entity, int limite) throws Exception;
	
	public PagosGastosDetExt convertir(PagosGastosDet entity) throws Exception;
	
	public PagosGastosDet convertir(PagosGastosDetExt entityExt) throws Exception;

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	public Long save(PagosGastosDetExt entityExt) throws Exception;

	public void update(PagosGastosDetExt entityExt) throws Exception;

	public void delete(PagosGastosDetExt entityExt) throws Exception;
	
	public List<PagosGastosDetExt> findByPagosGastosExt(PagosGastos entity, int limite) throws Exception;

	public List<PagosGastosDetExt> findByPagosGastosExt(PagosGastosExt entityExt, int limite) throws Exception;
	
	public List<PagosGastosDetExt> findLikePojoCompletoExt(Object entityExt, int max) throws Exception;
}
