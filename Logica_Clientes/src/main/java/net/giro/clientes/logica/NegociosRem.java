package net.giro.clientes.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.CanalesNegocioExt;
import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.DirectorioTelefonicoExt;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.EstadosNegocioExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.PersonaNegocioExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface NegociosRem {
	public InfoSesion getInfoSesion();
	
	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(Negocio entity) throws Exception;
	
	public List<Negocio> saveOrUpdateList(List<Negocio> entities) throws Exception;

	public void update(Negocio entity) throws Exception;
	
	public void delete(Negocio entity) throws Exception;
	
	public void delete(long idNegocio) throws Exception;
	
	public ConValores buscarActividad(Negocio negocio) throws Exception;
	
	public List<ConValores> buscarGiros() throws Exception;
	
	public List<PersonaNegocioExt> buscarPersonaNegocio(Negocio negocio) throws Exception;
	
	public List<ConValores> buscarSectores() throws Exception;
	
	public Respuesta buscarCanales(Negocio pojoNegocio); 
	
	public Respuesta buscarContactos(Negocio pojoNegocio, long idEstatus);
	
	public Respuesta buscarGiro(long idGiro);
	
	public Respuesta buscarGiros(String nombre);
	
	public Respuesta buscarEstados(Negocio pojoNegocio);
	
	public Respuesta buscarNegocios(String tipoBusquedaNegocio, String valorBusquedaNegocio);
	
	public Respuesta buscarPersonas(String tipoBusqueda, String valorBusqueda);
	
	/**
	 * Metodo que devuelve la direccion del Negocio indicado.
	 * @param idNegocio
	 * @return Respuesta Valores: pojo DomicilioExt, domicilio, numero_exterior, numero_interior, colonia, codigo_postal, ciudad o localidad, municipio, estado, pais
	 * @throws Exception
	 */
	public Respuesta buscarDomicilio(long idNegocio) throws Exception;

	public Negocio findById(long idNegocio);
	
	public List<Negocio> findAll();
	
	public List<Negocio> findAll(String nombre) throws Exception;

	public Respuesta cambiarEstatusContacto(ContactoNegocioExt pojoContactoNegocioExt);
	
	public Respuesta cargarTiposCanales();

	public Respuesta eliminarCanal(CanalesNegocioExt pojoCanalExt);
	
	public Respuesta eliminarNegocio(Negocio negocio);
	
	public Respuesta eliminarPersonaNegocio(PersonaNegocioExt personaNegocioExt);
	
	public Respuesta eliminarEstadosNegocio(EstadosNegocioExt pojoEstadosNegocioExt);
	
	public Respuesta habilitarDomicilio(DomicilioExt pojoDomicilioExt);

	public Respuesta salvarCanal(CanalesNegocioExt pojoCanalExt);
	
	public Respuesta salvarContacto(ContactoNegocioExt pojoContactoNegocioExt);
	
	public Respuesta salvarDomicilio(Negocio pojoNegocio, DomicilioExt pojoDomicilioExt);
	
	public Respuesta salvarDomicilio(DomicilioExt domicilio);
	
	public Respuesta salvarDomicilioPrincipal(Negocio pojoNegocio, DomicilioExt pojoDomicilioExt);
	
	public Respuesta salvarEstadosNegocio(EstadosNegocioExt pojoEstadosNegocioExt);
	
	public Respuesta salvarNegocio(Negocio negocio);
	
	public Respuesta salvarPersonaNegocio(PersonaNegocioExt personaNegocioExt);
	
	public Respuesta salvarDirectorio(DirectorioTelefonicoExt pojoDirectorioExt);

	public HashMap<Long, String> getDescEstatusNegocio();
	
	public HashMap<Long, String> getDescEstatusContacto();
	
	public List<Negocio> findByProperty(String propertyName, Object value, int limite);
	
	public List<Negocio> findLikeProperty(String propertyName, String value, int limite);
	
	public List<Negocio> findLikeColumnName(String propertyName, String value);
}

/* ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   |       AUTOR      | DESCRIPCIÓN
 * ----------------------------------------------------------------------------------------------------------------
 *    1.2   | 2016-11-05 | Javier Tirado	| Añado metodos findAll y findAll(String)
 */