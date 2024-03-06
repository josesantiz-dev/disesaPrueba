package net.giro.adp.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.TipoObraAutorizadas;
import net.giro.adp.beans.TipoObraJerarquia;
import net.giro.adp.beans.TipoObraRevisadas;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ObraRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Obra entity) throws Exception;

	public List<Obra> saveOrUpdateList(List<Obra> entities) throws Exception;

	public void delete(Long idObra) throws Exception;

	public void update(Obra entity) throws Exception;

	public Respuesta cancelar(Obra entity) throws Exception;

	public Obra findById(Long idObra);

	public Obra revisado(Obra entity, long revisadoPor, Date fechaRevisado) throws Exception;

	public Obra autorizado(Obra entity, long autorizadaPor, Date fechaAutorizada) throws Exception;
	
	public List<Obra> findAll() throws Exception;

	public List<Obra> findAll(long idObraPrincipal, boolean incluyeCanceladas, int revisadas, int autorizadas, String orderBy) throws Exception;
	
	public List<Obra> findByProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, String orderBy, int limite) throws Exception;

	public List<Obra> findByProperty(String propertyName, Object value, int tipo) throws Exception;
	
	public List<Obra> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<Obra> busquedaDinamica(String value) throws Exception;

	public List<Obra> findLike(String value, boolean incluyeAdministrativas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Obra> findLike(String value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, TipoObraRevisadas revisado, TipoObraAutorizadas autorizado, TipoObraJerarquia jerarquia, String orderBy, int limite) throws Exception;

	public List<Obra> findLikeProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, TipoObraRevisadas revisado, TipoObraAutorizadas autorizado, TipoObraJerarquia jerarquia, String orderBy, int limite) throws Exception; 

	public List<Obra> findLikeProperty(String propertyName, Object value, boolean incluyeAdministrativas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;

	public List<Obra> findLikeProperty(String propertyName, Object value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception;

	public List<Obra> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeCanceladas, String orderBy, int limite) throws Exception; 

	public List<Obra> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception;
	
	public List<Persona> findClienteLikeProperty(String propertyName, String value, String tipo) throws Exception;

	public List<Obra> findLikePropertyByAlmacen(String propertyName, Object propertyValue, long idAlmacen, int limite) throws Exception;

	public List<Obra> findInProperty(String propertyName, List<Object> values, String orderBy) throws Exception;

	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception;

	// -------------------------------------------------------------------------------------------
	
	public List<Obra> findPrincipalLikeProperty(String propertyName, Object value, int tipoObra, boolean incluyeCanceladas, String orderBy, int limite) throws Exception;
	
	public List<Obra> findPrincipalLikeSucursal(String propertyName, Object value, int tipoObra, long idSucursal, String orderBy, int limite) throws Exception;
	
	public List<Obra> findSubObraLikeProperty(String propertyName, Object value, int tipoObra, Long idObra, String orderBy, int limite) throws Exception;
	
	public List<Obra> findSubObraLikeSucursal(String propertyName, Object value, int tipoObra, Long idObra, long idSucursal, String orderBy, int limite) throws Exception;

	// -------------------------------------------------------------------------------------------
	// CONVERSIONES
	// -------------------------------------------------------------------------------------------

	public Obra convertir(ObraExt target) throws Exception;
	
	public ObraExt convertir(Obra target) throws Exception;

	public List<Obra> convertirList(List<ObraExt> targetList) throws Exception;
	
	public List<ObraExt> extenderLista(List<Obra> targetList) throws Exception;
	
	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------
	
	public Long save(ObraExt entity) throws Exception;
		
	public void update(ObraExt entity) throws Exception;

	public ObraExt findByIdExt(Long idObra) throws Exception;
	
	public List<ObraExt> findLikePropertyExt(String propertyName, final String value, int opcion, String orderBy, int limite) throws Exception;
	
	public PersonaExt extenderPersona(Persona pojoPersona) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findClienteByProperty y findClienteLikeProperty
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Añado los metodos findByMultiProperties y findLikeMultiProperties
 * 1.2 | 2017-04-17 | Javier Tirado 	| Añado el metodo findEstatusCanceladoObras
 */