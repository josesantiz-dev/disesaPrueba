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
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface NegociosRem {
	public ConValores buscarActividad(Negocio negocio) throws ExcepConstraint;
	public List<ConValores> buscarGiros() throws ExcepConstraint;
	public List<PersonaNegocioExt> buscarPersonaNegocio(Negocio negocio) throws ExcepConstraint;
	public List<ConValores> buscarSectores() throws ExcepConstraint;
	public Respuesta buscarCanales(Negocio pojoNegocio); 
	public Respuesta buscarContactos(Negocio pojoNegocio, Long idEstatus);
	public Respuesta buscarGiro(Long idGiro);
	public Respuesta buscarGiros(String nombre);
	public Respuesta buscarEstados(Negocio pojoNegocio);
	public Respuesta buscarNegocios(String tipoBusquedaNegocio, String valorBusquedaNegocio);
	public Respuesta buscarPersonas(String tipoBusqueda, String valorBusqueda);

	public Negocio findById(Long id);
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

	public InfoSesion getInfoSesion();
	public void setInfoSesion(InfoSesion infoSesion);

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