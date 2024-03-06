package net.giro.compras.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
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
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.dao.CotizacionDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@Stateless
public class CotizacionFac implements CotizacionRem {
	private static Logger log = Logger.getLogger(CotizacionFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private CotizacionDAO ifzCotizaciones;
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
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
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
	public Long save(Cotizacion entity) throws ExcepConstraint {
		try {
			return this.ifzCotizaciones.save(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.save(Cotizacion)", e);
			throw e;
		}
	}

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
	public void update(Cotizacion entity) throws ExcepConstraint {
		try {
			this.ifzCotizaciones.update(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.update(Cotizacion)", e);
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
	public CotizacionExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.CotizacionToCotizacionExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Cotizacion> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public List<CotizacionExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public List<CotizacionExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Cotizacion> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findInProperty(propertyName, values);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findInProperty(propertyName, values)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
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
				List<Persona> lista = this.ifzPersonas.findLikeColumnName(propertyName, value.toString());
				for (Persona var : lista) {
					listaExt.add(this.convertidor.PersonaToPersonaExt(var));
				}
			} else {
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
		Respuesta respuesta = new Respuesta();
		
		try {
			if ("P".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarPersonasContacto(idProveedor);
				if (respuesta != null) {
					if(respuesta.getErrores().getCodigoError() > 0) return null;
					List<ContactoPersonaExt> listPersonaContacto = (List<ContactoPersonaExt>) respuesta.getBody().getValor("personasContacto");
					if (listPersonaContacto == null || listPersonaContacto.isEmpty()) return null;
					return listPersonaContacto.get(0).getIdPersonaContacto();
				}
			}
			
			if ("N".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarNegociosContacto(idProveedor);
				if (respuesta != null) {
					if(respuesta.getErrores().getCodigoError() > 0) return null;
					List<ContactoNegocioExt> listContactos = (List<ContactoNegocioExt>) respuesta.getBody().getValor("negociosContacto");
					if (listContactos == null || listContactos.isEmpty()) return null;
					return listContactos.get(0).getIdPersonaContacto();
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findContactoByProveedor(PersonaExt, tipoPersona)", e);
			throw e;
		}
		
		return null;
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzCotizaciones.findConsecutivoByProveedor(idProveedor);
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
			return this.ifzCotizaciones.findByProperties(params);
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
			return this.ifzCotizaciones.findLikeProperties(params);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperties(params)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
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
			log.error("Error en CotizacionFac.findExtByProperties(propertyName, value, max)", e);
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
			log.error("Error en CotizacionFac.findLikeProperties(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<Cotizacion> findByPropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findByPropertyWithObra(propertyName, value, idObra, max);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByPropertyWithObra(propertyName, value, idObra, max)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	public List<Cotizacion> findLikePropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		try {
			this.ifzCotizaciones.OrderBy(orderBy);
			this.ifzCotizaciones.estatus(estatusId);
			return this.ifzCotizaciones.findLikePropertyWithObra(propertyName, value, idObra, max);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikePropertyWithObra(propertyName, value, idObra, max)", e);
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
			return this.ifzCotizaciones.findInPropertyWithObra(propertyName, values, idObra);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findInPropertyWithObra(propertyName, values, idObra)", e);
			throw e;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public List<CotizacionExt> findExtByPropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findByPropertyWithObra(propertyName, value, idObra, max);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtByPropertyWithObra(propertyName, value, idObra, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<CotizacionExt> findExtLikePropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		List<CotizacionExt> listaExt = new ArrayList<CotizacionExt>();
		
		try {
			List<Cotizacion> lista = this.findLikePropertyWithObra(propertyName, value, idObra, max);
			if (lista != null) {
				for(Cotizacion var : lista) {
					listaExt.add(this.convertidor.CotizacionToCotizacionExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtLikePropertyWithObra(propertyName, value, idObra, max)", e);
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
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. Normal y extendido
 */