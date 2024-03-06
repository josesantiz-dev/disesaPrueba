package net.giro.compras.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface CotizacionRem {
	public void showSystemOuts(boolean value);
	
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(Cotizacion entity) throws Exception;

    public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities) throws Exception;
	
	public void update(Cotizacion entity) throws Exception;

	public Respuesta cancelar(long idOrdenCompra, long idUsuario) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public Cotizacion findById(Long idCotizacion);

	public List<Cotizacion> findAll(long idObra) throws Exception;
	
	public List<Cotizacion> findAll(long idObra, int estatus, String orderBy) throws Exception;

	public List<Cotizacion> findByRequisicion(long idRequisicion) throws Exception;

	public List<Cotizacion> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Cotizacion> findByProperty(String propertyName, final Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception;
	
	public List<Cotizacion> findLike(String value, long idObra, int estatus, int limite) throws Exception;

	public List<Cotizacion> findLike(String value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception;

	public List<Cotizacion> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception;

	// --------------------------------------------------------------------------------------------------------------------
	
	public Cotizacion asignarFolio(Cotizacion cotizacion) throws Exception;

	public List<SolicitudBodega> solicitudBodega(long idCotizacion) throws Exception;
	
	public Respuesta solicitudBodega(Long idObra, Long idCotizacion, Long idRequisicion, List<SolicitudBodega> listSolicitudes);
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
	
	/**
	 * Comprueba si existe Cotizacion de Explosion de Insumos con la Obra indicada y que esta tenga Solicitud a Bodega elaborada
	 * @param idObra
	 * @return
	 * @throws Exception
	 */
	public boolean comprobarSolicitudBodega(Long idObra) throws Exception;
	
	public Cotizacion convertir(CotizacionExt target) throws Exception;
	
	public CotizacionExt convertir(Cotizacion target) throws Exception;

	// --------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------------------
	
	public Long save(CotizacionExt entityExt) throws Exception;

	public void update(CotizacionExt entityExt) throws Exception;

	public CotizacionExt findExtById(Long idCotizacion) throws Exception;

	public PersonaExt findContactoByProveedor(PersonaExt idProveedor, String tipoPersona) throws Exception;

	public List<PersonaExt> findPersonaLikeProperty(String propertyName, final Object value, String tipoPersona, int limite) throws Exception;

	// --------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------------------
	
	public boolean backOfficeCotizacionPreciosOrdenCompra(Long idCotizacion);
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. Normal y extendido
 */