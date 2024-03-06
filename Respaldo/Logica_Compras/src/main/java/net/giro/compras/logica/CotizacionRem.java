package net.giro.compras.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionExt;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface CotizacionRem {
	public void setInfoSecion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void OrderBy(String orderBy);
	public void estatus(Integer estatus);

	public Long save(Cotizacion entity) throws ExcepConstraint;
	public Long save(CotizacionExt entityExt) throws ExcepConstraint;
	
	public void update(Cotizacion entity) throws ExcepConstraint;
	public void update(CotizacionExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public Cotizacion findById(Long id);
	public CotizacionExt findExtById(Long id) throws Exception;
	
	public List<Cotizacion> findByProperty(String propertyName, final Object value, int max) throws Exception;
	public List<CotizacionExt> findExtByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<Cotizacion> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	public List<CotizacionExt> findExtLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Cotizacion> findInProperty(String propertyName, List<Object> values) throws Exception;	
	public List<CotizacionExt> findExtInProperty(String propertyName, List<Object> values) throws Exception;
	
	public List<PersonaExt> findPersonaLikeProperty(String propertyName, final Object value, String tipoPersona, int limite) throws Exception;
	public PersonaExt findContactoByProveedor(PersonaExt idProveedor, String tipoPersona) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	public List<Cotizacion> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<CotizacionExt> findExtByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<CotizacionExt> findExtLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<Cotizacion> findByPropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;

	public List<Cotizacion> findLikePropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;
	
	public List<Cotizacion> findInPropertyWithObra(String columnName, List<Object> values, long idObra) throws Exception;
	
	public List<CotizacionExt> findExtByPropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;

	public List<CotizacionExt> findExtLikePropertyWithObra(String propertyName, final Object value, long idObra, int max) throws Exception;
	
	public List<CotizacionExt> findExtInPropertyWithObra(String columnName, List<Object> values, long idObra) throws Exception;
	
	public Cotizacion convertir(CotizacionExt target) throws Exception;
	
	public CotizacionExt convertir(Cotizacion target) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. Normal y extendido
 */