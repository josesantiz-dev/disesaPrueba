package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.ContactoPersonaExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.dao.CotizacionDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicComprasEventos;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Stateless
public class CotizacionFac implements CotizacionRem {
	private static Logger log = Logger.getLogger(CotizacionFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private CotizacionDAO ifzCotizaciones;
	private CotizacionDetalleRem ifzCotDetalles;
	private OrdenCompraRem ifzOrdenes;
	private ClientesRem ifzClientes;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Integer estatusId;
	
	
	public CotizacionFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzCotizaciones = (CotizacionDAO) this.ctx.lookup("ejb:/Model_Compras//CotizacionImp!net.giro.compras.dao.CotizacionDAO");
			this.ifzCotDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzOrdenes = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzPersonas = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocios = (NegociosRem) this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("CotizacionFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.CotizacionFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void OrderBy(String orderBy) {
		CotizacionFac.orderBy = orderBy;
	}

	@Override
	public void estatus(Integer estatus) {
		CotizacionFac.estatusId = estatus;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Cotizacion entity) throws ExcepConstraint {
		try {
			return this.ifzCotizaciones.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.save(Cotizacion)", e);
			throw e;
		}
	}

	@Override
	public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities) throws Exception {
		try {
			return this.ifzCotizaciones.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.saveOrUpdateList(List<Cotizacion> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Cotizacion entity) throws ExcepConstraint {
		try {
			this.ifzCotizaciones.update(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.update(Cotizacion)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idCotizacion, long idUsuario) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<CotizacionDetalle> listDetalles = null;
		List<OrdenCompra> listOrdenes = null;
		Cotizacion pojoCotizacion = null;
		String ordenCompra = "";
		
		try {
			this.ifzOrdenes.setInfoSesion(this.infoSesion);
			this.ifzOrdenes.estatus(0L);
			listOrdenes = this.ifzOrdenes.findByProperty("idCotizacion", idCotizacion, 0);
			if (listOrdenes != null && ! listOrdenes.isEmpty()) {
				ordenCompra = "multiples Ordenes de Compra";
				if (listOrdenes.size() == 1)
					ordenCompra = "la Orden de Compra " + listOrdenes.get(0).getFolio();
				log.error("No se puede Cancelar la Cotizacion porque esta total o parcialmente en " + ordenCompra);
				respuesta.getBody().addValor("ordenes", listOrdenes);
				respuesta.getErrores().addCodigo("COMPRAS", 2L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se puede Cancelar la Cotizacion porque esta total o parcialmente en " + ordenCompra);
				return respuesta;
			}
			
			// Recupero los productos de la Orden de Compra para el BackOffice
			this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Cotizacion indicada");
				respuesta.getErrores().addCodigo("COMPRAS", 3L);
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("Ocurrio un problema al intentar recuperar los detalles de la Cotizacion indicada");
				return respuesta;
			}
			
			// Cancela la Orden de Compra
			pojoCotizacion = this.findById(idCotizacion);
			pojoCotizacion.setEstatus(1);
			pojoCotizacion.setModificadoPor(getIdUsuario(idUsuario));
			pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(pojoCotizacion);
			
			// Envio mensaje a COMPRAS:Back Office Cotizacion
			boRequisicion(pojoCotizacion.getIdRequisicion(), 0L, listDetalles);
			
			respuesta.getBody().addValor("cotizacion", pojoCotizacion);
		} catch (Exception e) {
			log.error("error en Compras.CotizacionFac.cancelar(long idCotizacion, long idUsuario)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar cancelar la Cotizacion indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzCotizaciones.delete(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Cotizacion findById(Long id) {
		try {
			return this.ifzCotizaciones.findById(id);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Cotizacion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findByProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findLikeProperty(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public List<Cotizacion> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findInProperty(propertyName, values, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzCotizaciones.findConsecutivoByProveedor(idProveedor, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findConsecutivoByProveedor(idProveedor)", e);
			throw e;
		}
	}

	@Override
	public List<Cotizacion> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findByProperties(params, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperties(params)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findLikeProperties(params, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperties(params)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	public List<Cotizacion> findByPropertyWithObra(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findByPropertyWithObra(propertyName, value, idObra, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByPropertyWithObra(propertyName, value, idObra, limite)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	public List<Cotizacion> findLikePropertyWithObra(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findLikePropertyWithObra(propertyName, value, idObra, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikePropertyWithObra(propertyName, value, idObra, limite)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	public List<Cotizacion> findInPropertyWithObra(String propertyName, List<Object> values, long idObra) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findInPropertyWithObra(propertyName, values, idObra, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findInPropertyWithObra(propertyName, values, idObra)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public boolean backOfficeRequisicion(Long idCotizacion) {
		List<CotizacionDetalle> cotDetalles = null;
		Cotizacion pojoCotizacion = null;
		Long idRequisicion = 0L;
		
		try {
			this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			pojoCotizacion = this.findById(idCotizacion);
			idRequisicion = pojoCotizacion.getIdRequisicion();
			cotDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			
			if (cotDetalles == null || cotDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			boRequisicion(idRequisicion, idCotizacion, cotDetalles);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean backOfficeCotizacionPreciosOrdenCompra(Long idCotizacion) {
		List<CotizacionDetalle> cotDetalles = null;
		
		try {
			this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			cotDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			if (cotDetalles == null || cotDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			boCotizacionPreciosOrdenCompra(idCotizacion, cotDetalles);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	@Override
	public Cotizacion convertir(CotizacionExt target) throws Exception {
		try {
			return this.convertidor.CotizacionExtToCotizacion(target);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.convertir(CotizacionExt target)", e);
			throw e;
		}
	}

	@Override
	public CotizacionExt convertir(Cotizacion target) throws Exception {
		try {
			return this.convertidor.CotizacionToCotizacionExt(target);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.convertir(Cotizacion target)", e);
			throw e;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	@Override
	public Long save(CotizacionExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.CotizacionExtToCotizacion(entityExt));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.save(CotizacionExt)", e);
			throw e;
		}
	}

	@Override
	public void update(CotizacionExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.CotizacionExtToCotizacion(entityExt));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.update(CotizacionExt)", e);
			throw e;
		}
	}

	@Override
	public CotizacionExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.CotizacionToCotizacionExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<CotizacionExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtInProperty(String propertyName, List<Object> values) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findInProperty(propertyName, values);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtInProperty(propertyName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<PersonaExt> findPersonaLikeProperty(String propertyName, Object value, String tipoPersona, int limite) throws Exception {
		List<PersonaExt> listaExt = new ArrayList<PersonaExt>();
		
		try {
			if ("P".equals(tipoPersona)) {
				this.ifzPersonas.setInfoSesion(this.infoSesion);
				List<Persona> lista = this.ifzPersonas.findLikeColumnName(propertyName, value.toString());
				for (Persona var : lista) {
					listaExt.add(this.convertidor.PersonaToPersonaExt(var));
				}
			} else {
				this.ifzNegocios.setInfoSesion(this.infoSesion);
				List<Negocio> lista = this.ifzNegocios.findLikeProperty(propertyName, value.toString(), 0);
				for (Negocio var : lista) {
					listaExt.add(this.convertidor.NegocioToPersonaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findPersonaLikeProperty(propertyName, value, tipoPersona, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PersonaExt findContactoByProveedor(PersonaExt idProveedor, String tipoPersona) throws Exception {
		List<ContactoPersonaExt> listPersonasContactos = null;
		List<ContactoNegocioExt> listNegociosContactos = null;
		Respuesta respuesta = new Respuesta();
		PersonaExt pojoContacto = null;
		
		try {
			if (tipoPersona == null || "".equals(tipoPersona.trim()))
				return pojoContacto;
			
			if ("P".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarPersonasContacto(idProveedor);
				if (respuesta != null) {
					if (respuesta.getErrores().getCodigoError() > 0) 
						return pojoContacto;
					
					listPersonasContactos = (List<ContactoPersonaExt>) respuesta.getBody().getValor("personasContacto");
					if (listPersonasContactos == null || listPersonasContactos.isEmpty()) 
						return pojoContacto;
					
					for (ContactoPersonaExt var : listPersonasContactos) {
						if (var.getEstatusId().longValue() == 1) {
							pojoContacto = var.getIdPersonaContacto();
							break;
						}
					}
				}
			} else if ("N".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarNegociosContacto(idProveedor);
				if (respuesta != null) {
					if(respuesta.getErrores().getCodigoError() > 0) 
						return pojoContacto;
					
					listNegociosContactos = (List<ContactoNegocioExt>) respuesta.getBody().getValor("negociosContacto");
					if (listNegociosContactos == null || listNegociosContactos.isEmpty()) 
						return pojoContacto;
					
					for (ContactoNegocioExt var : listNegociosContactos) {
						if (var.getEstatusId().longValue() == 1) {
							pojoContacto = var.getIdPersonaContacto();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findContactoByProveedor(PersonaExt, tipoPersona)", e);
			throw e;
		}
		
		return pojoContacto;
	}

	@Override
	public List<CotizacionExt> findExtByProperties(HashMap<String, Object> params) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findByProperties(params);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtByProperties(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<CotizacionExt> findExtLikeProperties(HashMap<String, String> params) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findLikeProperties(params);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperties(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtByPropertyWithObra(String propertyName, Object value, long idObra, int limite) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findByPropertyWithObra(propertyName, value, idObra, limite);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtByPropertyWithObra(propertyName, value, idObra, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtLikePropertyWithObra(String propertyName, Object value, long idObra, int limite) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findLikePropertyWithObra(propertyName, value, idObra, limite);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtLikePropertyWithObra(propertyName, value, idObra, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtInPropertyWithObra(String propertyName, List<Object> values, long idObra) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findInPropertyWithObra(propertyName, values, idObra);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtInPropertyWithObra(propertyName, values, idObra)", e);
			throw e;
		}
		
		return listaExt;
	}

	// ------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------

	private void boRequisicion(Long idRequisicion, Long idCotizacion, List<CotizacionDetalle> listDetalles) {
		MensajeTopic msgCompras = null;
		Gson gson = new Gson();
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		Topic topicCompras = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		Object tmp = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idRequisicion == null || idRequisicion <= 0L || listDetalles == null || listDetalles.isEmpty()) {
				log.warn("No se especificaron parametros suficientes para lanzar el BackOffice Requisicion");
				return;
			}
			
			if (idCotizacion == null)
				idCotizacion = 0L;
			
			gson = new Gson();
			target = idRequisicion.toString();
			referencia = idCotizacion.toString();
			atributos = gson.toJson(listDetalles);
			msgCompras = new MensajeTopic(TopicComprasEventos.BO_RQ, target, referencia, atributos);
			comando = gson.toJson(msgCompras);
			
			tmp = this.ctx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();
	
			// Instanciamos conectamos con TOPIC
			topicCompras = (Topic) this.ctx.lookup("topic/COMPRAS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicCompras);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ", e);
		}
	}
	
	private void boCotizacionPreciosOrdenCompra(Long idCotizacion, List<CotizacionDetalle> listDetalles) {
		MensajeTopic msgCompras = null;
		Gson gson = new Gson();
		String comando = "";
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		Topic topicCompras = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		Object tmp = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idCotizacion == null || idCotizacion <= 0L || listDetalles == null || listDetalles.isEmpty()) {
				log.warn("No se especificaron parametros suficientes para lanzar el BackOffice CotizacionPreciosOrdenCompra");
				return;
			}
			
			gson = new Gson();
			target = idCotizacion.toString();
			atributos = gson.toJson(listDetalles);
			msgCompras = new MensajeTopic(TopicComprasEventos.BO_CO$, target, referencia, atributos);
			comando = gson.toJson(msgCompras);
			
			tmp = this.ctx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();
	
			// Instanciamos conectamos con TOPIC
			topicCompras = (Topic) this.ctx.lookup("topic/COMPRAS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicCompras);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO_CO$", e);
		}
	}

	private Long getIdUsuario() {
		Long resultado = 0L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getAcceso().getUsuario().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getIdUsuario(long defaultValue) {
		long resultado = 0;
		
		resultado = getIdUsuario();
		if (resultado == 0)
			resultado = defaultValue;
		else if (resultado == 1 && defaultValue > 0 && resultado != defaultValue)
			resultado = defaultValue;
		
		return resultado;
	}
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. Normal y extendido
 */