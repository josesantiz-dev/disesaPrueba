package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxp.beans.PagosGastosRfcPermitidos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PagosGastosRfcPermitidosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(PagosGastosRfcPermitidos entity) throws Exception;
	
	public List<PagosGastosRfcPermitidos> saveOrUpdateList(List<PagosGastosRfcPermitidos> entities) throws Exception;

	public void update(PagosGastosRfcPermitidos pojoEntity) throws Exception;

	public PagosGastosRfcPermitidos findById(Long idPagosGastosRfcPermitidos);
	
	public List<PagosGastosRfcPermitidos> findAll() throws Exception;

	public List<PagosGastosRfcPermitidos> findAll(String rfc) throws Exception;

	public List<PagosGastosRfcPermitidos> findAll(String rfc, String orderBy) throws Exception;

	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;

	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	/**
	 * Comprobacion para determinar si permitimos o no la carga de un CFDI (xml)
	 * @param tipoPagosGasto
	 * @param rfc
	 * @param tipoComprobante
	 * @param metodoPago
	 * @return
	 * @throws Exception
	 */
	public boolean comprobarPermiso(String tipoPagosGasto, String rfc, String tipoComprobante, String metodoPago) throws Exception;
}

/* 
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ---------------------------------------------------------------------------------------------------------------- 
 *  VER |   FECHA    |   	AUTOR 		| DESCRIPCIÓN 
 * ---------------------------------------------------------------------------------------------------------------- 
 *  2.2 | 2019-03-09 | Javier Tirado 	| Creado
 */