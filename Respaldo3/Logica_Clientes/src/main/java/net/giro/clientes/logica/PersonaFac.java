package net.giro.clientes.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;

@Stateless
public class PersonaFac implements PersonaRem {
	private static Logger log = Logger.getLogger(PersonaFac.class);
	private InitialContext ctx = null;
	private PersonaDAO ifzPersona;
	private NegocioDAO ifzNegocio;
	private ClientesRem ifzClientes;
	private InfoSesion infoSesion;
	// property constants
	public static final String RFC = "rfc";
	public static final String CURP = "curp";
	public static final String APELLIDO_PATERNO = "apellidoPaterno";
	public static final String APELLIDO_MATERNO = "apellidoMaterno";
	public static final String NOMBRE = "nombre";
	public static final String PERSONALIDAD = "personalidad";
	public static final String CREADO_POR = "creadoPor";
	public static final String MODIFICADO_POR = "modificadoPor";
	public static final String CALLE = "calle";
	public static final String ENTRE_CALLES = "entreCalles";
	public static final String _YCALLE = "YCalle";
	public static final String NO_INTERNO = "noInterno";
	public static final String NO_EXTERNO = "noExterno";
	public static final String COLONIA_ID = "coloniaId";
	public static final String NACIONALIDAD_ID = "nacionalidadId";
	
	public PersonaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	        this.ctx = new InitialContext(p);
			this.ifzPersona = (PersonaDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
			this.ifzNegocio = (NegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear CajaChicaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(Persona entity) throws Exception {
		try {
			return this.ifzPersona.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Clientes.PersonaFac.save(Persona)", e);
			throw e;
		}
	}

	@Override
	public List<Persona> saveOrUpdateList(List<Persona> entities) throws Exception {
		try {
			return this.ifzPersona.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Clientes.PersonaFac.saveOrUpdateList(List<Persona> entities)", e);
			throw e;
		}
	}

	public void delete(Persona entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(1L);
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	public void delete(long idPersona) throws Exception {
		try {
			this.delete(this.ifzPersona.findById(idPersona));
		} catch (Exception re) {
			throw re;
		}
	}
	
	public Persona update(Persona entity) throws Exception {
		try {
			this.ifzPersona.update(entity);
			return entity;
		} catch (Exception re) {
			throw re;
		}
	}
	
	public Persona findById(Long id) {		
		try {
			return this.ifzPersona.findById(id);
		} catch (Exception re) {			
			throw re;
		}
	}
	
	public Persona findByIdPojoCompleto(Long id) {		
		try {
			return this.ifzPersona.findById(id);
		} catch (Exception re) {			
			throw re;
		}
	}

	public List<Persona> findAll() {
		try {
			return this.findAll(false, false, "");
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Persona> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy) {
		try {
			return this.ifzPersona.findAll(incluyeEliminados, incluyeSistema, orderBy);
		} catch (Exception re) {
			throw re;
		}
	}

	public List<Persona> findByProperty(String propertyName, final Object value) {
		try {
			return this.findByProperty(propertyName, value, false, false, "", 0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Persona> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		try {
			return this.ifzPersona.findByProperty(propertyName, value, incluyeEliminados, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Persona> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		try {
			return this.ifzPersona.findLike(value, incluyeEliminados, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Persona> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value, incluyeEliminados, incluyeSistema, orderBy, limite);
			return this.ifzPersona.findLikeProperty(propertyName, value, incluyeEliminados, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, ConGrupoValores valGpo, String tipoPersona, int max, Boolean valido) {
		List<Persona> result = new ArrayList<Persona>();
		
		try {
			if ("P".equals(tipoPersona)) {
				result = this.findLikeProperty(propertyName, value.toString(), false, false, "", 0);//.findLikeClaveNombre(propertyName, value, valGpo.getId(), tipoPersona, max, valido);
			} else {
				List<Negocio> listNegocio = this.ifzNegocio.findLikeProperty(propertyName, value.toString(), false, false, "", 0);//.findLikeColumnName(propertyName, value.toString(), max);
				for (Negocio negocio : listNegocio) {
					Persona pojoPersona = new Persona();
					pojoPersona.setId(negocio.getId());
					pojoPersona.setNombre(negocio.getNombre());
					pojoPersona.setRfc(negocio.getRfc());
					result.add(pojoPersona);
				}
			}
			
		} catch (Exception re) {
			throw re;
		}
		
		return result;
	}	

	public List<Persona> findLikeColumnName(String propertyName, String value) {
		try {
			return this.findLikeProperty(propertyName, value, false, false, "", 0); // this.ifzPersona.findLikeColumnName(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}
	
 	public List<Persona> findLikePersonas(Object value,int max) {
		try {
			return this.ifzPersona.findLikeProperty("nombre", value.toString(), false, false, "", 0); // findLikePersonas(value, max);
		} catch (Exception re) {
			throw re;
		}
	}
	
	public List<Persona> findLikeProveedor(Object value, ConGrupoValores valGpo, String tipoPersona, Long tipoGastoAsociado, int max) {
		try {
			return this.findLikeProperty("*", value.toString(), false, false, "", 0); // this.ifzPersona.findLikeProveedor(value, valGpo.getId(), tipoPersona, tipoGastoAsociado, max);
		} catch (Exception re ) {
			throw re;
		}
	}

	public HashMap<String, Long> getIdListLikeNombre(String value) throws Exception {
		HashMap<String, Long> resultado = new HashMap<String, Long>();
		List<Persona> personas = null;
		int id = 1;
		
		try {
			personas = this.findLike(value, false, false, "", 0);
			if (personas == null || personas.isEmpty())
				return resultado;
			
			for (Persona pojoPersona : personas) {
				resultado.put("valor" + id, pojoPersona.getId());
				id++;
			}
			
			return resultado;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<Persona> findByProperty(String propertyName, Object value, int limite) {
		try {
			return this.findByProperty(propertyName, value, false, false, "", limite); // this.ifzPersona.findByProperty(propertyName, value, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Persona> findLikeProperty(String propertyName, String value, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, false, false, "", 0); // this.ifzPersona.findLikeProperty(propertyName, value, limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzPersona.findByProperties(params);
		} catch (Exception e) {
			log.error("Error en Logica_Clientes.PersonaFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<Persona> findLikePropertyPersonaNegocio(String propertyName, String value, Integer limite) throws Exception {
		List<Persona> listPersonas = new ArrayList<Persona>();
		
		try {
			if (limite == null || limite <= 0)
				limite = 0;
			
			// Recuperamos personas
			listPersonas = this.findLikeProperty(propertyName, value, false, false, "", 0); // this.ifzPersona.findLikeProperty(propertyName, value, limite);
			if (listPersonas == null)
				listPersonas = new ArrayList<Persona>();
			
			for (Persona var : listPersonas)
				var.setTipoPersona(1L);

			// Recuperamos negocios y los convertimos a personas
			List<Negocio> listNegocio = this.ifzNegocio.findLikeProperty(propertyName, value, false, false, "", 0); // .findLikeProperty(propertyName, value, limite);
			if (listNegocio != null && ! listNegocio.isEmpty()) {
				Persona pojoPersona = null;
				for (Negocio negocio : listNegocio) {
					// Convertimos cada negocio a persona
					pojoPersona = new Persona();
					pojoPersona.setId(negocio.getId());
					pojoPersona.setNombre(negocio.getNombre());
					pojoPersona.setRfc(negocio.getRfc());
					pojoPersona.setBanco(negocio.getBanco());
					pojoPersona.setClabeInterbancaria(negocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(0L);
					
					// Añadimos a listado
					listPersonas.add(pojoPersona);
				}
			}

			// Ordenamos resultados por nombre
			Collections.sort(
				listPersonas, 
				new Comparator<Persona>() {
					@Override
					public int compare(Persona o1, Persona o2) {
			    		int c = 0;
		    			c = o2.getTipoPersona().compareTo(o1.getTipoPersona());
			    		if (c == 0)
			    			c = o1.getNombre().trim().compareTo(o2.getNombre().trim());
			    		return c;
					}
				}
			);
		} catch (Exception e) {
			log.error("Error en Logica_Clientes.PersonaFac.findLikePropertyPersonaNegocio(propertyName, value, limite)", e);
			throw e;
		}
		
		return listPersonas;
	}

	@Override
	public List<Persona> busquedaDinamica(String value) throws Exception {
		List<Persona> resultado = new ArrayList<Persona>();
		List<Persona> listPersonas = new ArrayList<Persona>();
		List<Negocio> listNegocio = null;
		Persona pojoPersona = null;
		
		try {
			// Recuperamos personas
			listPersonas = this.findLike(value, false, false, "", 0); 
			if (listPersonas == null)
				listPersonas = new ArrayList<Persona>();
			for (Persona var : listPersonas) {
				if (var.getEstatus() == 1)
					continue;
				if (var.getSistema() == 1)
					continue;
				var.setTipoPersona(1L);
				var.setNombre(var.getNombre() + " (Persona)");
				resultado.add(var);
			}

			// Recuperamos negocios y los convertimos a personas
			listNegocio = this.ifzNegocio.findLike(value, false, false, "", 0);
			if (listNegocio != null && ! listNegocio.isEmpty()) {
				for (Negocio negocio : listNegocio) {
					if (negocio.getEstatus() == 2L)
						continue;
					if (negocio.getSistema() == 1)
						continue;
					// Convertimos cada negocio a persona
					pojoPersona = new Persona();
					pojoPersona.setId(negocio.getId());
					pojoPersona.setNombre(negocio.getNombre() + " (Negocio)");
					pojoPersona.setRfc(negocio.getRfc());
					pojoPersona.setBanco(negocio.getBanco());
					pojoPersona.setClabeInterbancaria(negocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(2L);
					// Añadimos a listado
					resultado.add(pojoPersona);
				}
			}

			// Ordenamos resultados por nombre
			Collections.sort(resultado, new Comparator<Persona>() {
				@Override
				public int compare(Persona o1, Persona o2) {
		    		int c = 0;
	    			c = o1.getNombre().trim().compareTo(o2.getNombre().trim());
		    		if (c == 0)
		    			c = o2.getTipoPersona().compareTo(o1.getTipoPersona());
		    		return c;
				}
			});
		} catch (Exception e) {
			log.error("Error en Logica_Clientes.PersonaFac.findLikePropertyPersonaNegocio(propertyName, value, limite)", e);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public List<Persona> busquedaDinamica(String propertyName, String propertyValue) throws Exception {
		List<Persona> resultado = new ArrayList<Persona>();
		List<Persona> listPersonas = new ArrayList<Persona>();
		List<Negocio> listNegocio = null;
		Persona pojoPersona = null;
		
		try {
			// Recuperamos personas
			listPersonas = this.findLikeProperty(propertyName, propertyValue, false, false, "", 0); // this.ifzPersona.findLikeProperty(propertyName, propertyValue, 0);
			if (listPersonas == null)
				listPersonas = new ArrayList<Persona>();
			for (Persona var : listPersonas) {
				if (var.getEstatus() == 1)
					continue;
				if (var.getSistema() == 1)
					continue;
				var.setTipoPersona(1L);
				var.setNombre(var.getNombre() + " (Persona)");
				resultado.add(var);
			}

			// Recuperamos negocios y los convertimos a personas
			listNegocio = this.ifzNegocio.findLikeProperty(propertyName, propertyValue, false, false, "", 0); // .findLikeProperty(propertyName, propertyValue, 0);
			if (listNegocio != null && ! listNegocio.isEmpty()) {
				for (Negocio negocio : listNegocio) {
					if (negocio.getEstatus() == 2L)
						continue;
					if (negocio.getSistema() == 1)
						continue;
					// Convertimos cada negocio a persona
					pojoPersona = new Persona();
					pojoPersona.setId(negocio.getId());
					pojoPersona.setNombre(negocio.getNombre() + " (Negocio)");
					pojoPersona.setRfc(negocio.getRfc());
					pojoPersona.setBanco(negocio.getBanco());
					pojoPersona.setClabeInterbancaria(negocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(2L);
					// Añadimos a listado
					resultado.add(pojoPersona);
				}
			}

			// Ordenamos resultados por nombre
			Collections.sort(resultado, new Comparator<Persona>() {
				@Override
				public int compare(Persona o1, Persona o2) {
		    		int c = 0;
	    			c = o1.getNombre().trim().compareTo(o2.getNombre().trim());
		    		if (c == 0)
		    			c = o2.getTipoPersona().compareTo(o1.getTipoPersona());
		    		return c;
				}
			});
		} catch (Exception e) {
			log.error("Error en Logica_Clientes.PersonaFac.findLikePropertyPersonaNegocio(propertyName, value, limite)", e);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public Respuesta buscarDomicilio(long idPersona) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<DomicilioExt> lista = null;
		DomicilioExt dom = null;
		Persona persona = null;

		try {
			// Recuperamos los domicilios registrados de la Persona
			persona = this.findById(idPersona);
			lista = this.ifzClientes.buscarDomicilioExt(persona); 
			
			// Recuperamos el domicilio activo principal o el primer domicilio activo
			for (DomicilioExt var : lista) {
				if (dom == null && var.getEstatus())
					dom = var;
				if (var.getEstatus() && dom.getPrincipal()) {
					dom = var;
					break;
				}
			}
			
			// Validamos domicilio
			if (dom == null) {
				respuesta.getErrores().setDescError("La Persona indicada no tiene capturado un Domicilio o ninguno esta activo");
				respuesta.getErrores().setCodigoError(1L);
				return respuesta;
			}
			
			respuesta.getBody().addValor("pojo", dom);
			respuesta.getBody().addValor("domicilio", getParte(dom, "calles", "Conocido"));
			respuesta.getBody().addValor("numero_exterior", getParte(dom, "numero_exterior", "SN"));
			respuesta.getBody().addValor("numero_interior", getParte(dom, "numero_interior", ""));
			
			if ("XEXX010101000".equals(persona.getRfc())) {
				respuesta.getBody().addValor("colonia", "");
				respuesta.getBody().addValor("codigo_postal", "SN");
				respuesta.getBody().addValor("ciudad", "");
				respuesta.getBody().addValor("localidad", "");
				respuesta.getBody().addValor("municipio", "");
				respuesta.getBody().addValor("estado", "");
				respuesta.getBody().addValor("id_colonia", 0L);
				respuesta.getBody().addValor("id_ciudad", 0L);
				respuesta.getBody().addValor("id_localidad", 0L);
				respuesta.getBody().addValor("id_municipio", 0L);
				respuesta.getBody().addValor("id_estado", 0L);
				respuesta.getBody().addValor("pais", "");
			} else if (dom.getColonia() != null) {
				respuesta.getBody().addValor("colonia", dom.getColonia().getNombre());
				respuesta.getBody().addValor("codigo_postal", dom.getColonia().getCp());
				respuesta.getBody().addValor("ciudad", dom.getColonia().getLocalidad().getNombre());
				respuesta.getBody().addValor("localidad", dom.getColonia().getLocalidad().getNombre());
				respuesta.getBody().addValor("municipio", dom.getColonia().getLocalidad().getMunicipio().getNombre());
				respuesta.getBody().addValor("estado", dom.getColonia().getLocalidad().getMunicipio().getEstado().getNombre());
				respuesta.getBody().addValor("id_colonia", dom.getColonia().getId());
				respuesta.getBody().addValor("id_ciudad", dom.getColonia().getLocalidad().getId());
				respuesta.getBody().addValor("id_localidad", dom.getColonia().getLocalidad().getId());
				respuesta.getBody().addValor("id_municipio", dom.getColonia().getLocalidad().getMunicipio().getId());
				respuesta.getBody().addValor("id_estado", dom.getColonia().getLocalidad().getMunicipio().getEstado().getId());
				respuesta.getBody().addValor("pais", "Mexico");
			}
			
			return respuesta;
		} catch (Exception e) {
			throw e;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Devuelve la direccion de acuerdo a la parte indicada
	 * @param dom Objeto extendido domicilio
	 * @param parte full, todo, calle, calles, domicilio, direccion, numero_exterior, numero_interior
	 * @param defaultValue 
	 * @return
	 */
	private String getParte(DomicilioExt dom, String parte, String defaultValue) {
		String resultado = "";
		
		if (parte != null && "calle,calles".contains(parte.trim())) {
			// Calle principal
			if (dom.getCatDomicilio1().longValue() == 20 || dom.getCatDomicilio1().longValue() == 10000778)
				resultado = dom.getDescripcionDomicilio1();

			// Otras Calles
			if (dom.getCatDomicilio2().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio2();
			
			if (dom.getCatDomicilio3().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio3();
			
			if (dom.getCatDomicilio4().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio4();
			
			if (dom.getCatDomicilio5().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio5();
		} else if (parte != null && "numero_exterior".equals(parte.trim())) {
			if (dom.getCatDomicilio1().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 10000098)
				resultado = dom.getDescripcionDomicilio5();
			else
				resultado = defaultValue;
		} else if (parte != null && "numero_interior".equals(parte.trim())) {
			if (dom.getCatDomicilio1().longValue() == 45)
				resultado = dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 45)
				resultado = dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 45)
				resultado = dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 45)
				resultado = dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 45)
				resultado = dom.getDescripcionDomicilio5();
			else
				resultado = defaultValue;
		} else if (parte == null || "".equals(parte.trim()) || "full,todo".contains(parte.trim())) {
			// Calle principal
			if (dom.getCatDomicilio1().longValue() == 20 || dom.getCatDomicilio1().longValue() == 10000778)
				resultado = dom.getDescripcionDomicilio1();

			// Numero Exterior
			if (dom.getCatDomicilio1().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 10000098)
				resultado += "#" + dom.getDescripcionDomicilio5();

			// Numero Interior
			if (dom.getCatDomicilio1().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio1();
			else if (dom.getCatDomicilio2().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio3().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio4().longValue() == 45)
				resultado += " Int. " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio5().longValue() == 45)
				resultado = dom.getDescripcionDomicilio5();

			// Otras calles
			if (dom.getCatDomicilio2().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio2();
			else if (dom.getCatDomicilio2().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio2();
			
			if (dom.getCatDomicilio3().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio3();
			else if (dom.getCatDomicilio3().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio3();
			
			if (dom.getCatDomicilio4().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio4();
			else if (dom.getCatDomicilio4().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio4();
			
			if (dom.getCatDomicilio5().longValue() == 20)
				resultado += " " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 21)
				resultado += " e/ " + dom.getDescripcionDomicilio5();
			else if (dom.getCatDomicilio5().longValue() == 191)
				resultado += " y " + dom.getDescripcionDomicilio5();
		} else {
			resultado = defaultValue;
		}
		
		return resultado;
	}

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();//.getId();
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
 * 2.1 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */