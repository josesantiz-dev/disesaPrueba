package net.giro.clientes.logica;

import java.util.ArrayList;
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
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);

			this.ifzPersona = (PersonaDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
			this.ifzNegocio = (NegocioDAO) this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear CajaChicaFac", e);
			ctx = null;
		}
	}

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
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
			this.ifzPersona.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public Persona update(Persona entity) throws Exception {
		try {
			this.ifzPersona.update(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public Persona findById(Long id) {		
		try {
			return this.ifzPersona.findById(id);
		} catch (RuntimeException re) {			
			throw re;
		}
	}
	
	public Persona findByIdPojoCompleto(Long id) {		
		try {
			return this.ifzPersona.findById(id);
		} catch (RuntimeException re) {			
			throw re;
		}
	}
	
	public List<Persona> findByProperty(String propertyName,final Object value) {
		try {
			return this.ifzPersona.findByColumnName(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Persona> findLikeClaveNombre(String propertyName, Object value, ConGrupoValores valGpo, String tipoPersona, int max, Boolean valido) {
		List<Persona>  result = new ArrayList<Persona>();
		
		try {
			if ("P".equals(tipoPersona)) {
				result = this.ifzPersona.findLikeClaveNombre(propertyName, value, valGpo.getId(), tipoPersona, max, valido);
			} else {
				List<Negocio> listNegocio = this.ifzNegocio.findLikeColumnName(propertyName, value.toString(), max);
				for (Negocio negocio : listNegocio) {
					Persona pojoPersona = new Persona();
					pojoPersona.setId(negocio.getId());
					pojoPersona.setNombre(negocio.getNombre());
					result.add(pojoPersona);
				}
			}
			
		} catch (RuntimeException re) {
			throw re;
		}
		
		return result;
	}	

	public List<Persona> findLikeColumnName(String propertyName, String value) {
		try {
			return this.ifzPersona.findLikeColumnName(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
 	public List<Persona> findLikePersonas(Object value,int max) {
		try {
			return this.ifzPersona.findLikePersonas(value, max);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<Persona> findAll() {
		try {
			return this.ifzPersona.findAll();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Persona> findLikeProveedor(Object value, ConGrupoValores valGpo, String tipoPersona, Long tipoGastoAsociado, int max) {
		try {
			return this.ifzPersona.findLikeProveedor(value, valGpo.getId(), tipoPersona, tipoGastoAsociado, max);
		} catch (RuntimeException re ) {
			throw re;
		}
	}

	public HashMap<String, Long> getIdListLikeNombre(String value) throws Exception {
		try {
			return this.ifzPersona.getIdListLikeNombre(value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Persona> findByProperty(String propertyName, Object value, int limite) {
		try {
			return this.ifzPersona.findByProperty(propertyName, value, limite);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Persona> findLikeProperty(String propertyName, String value, int limite) {
		try {
			return this.ifzPersona.findLikeProperty(propertyName, value, limite);
		} catch (RuntimeException re) {
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
			listPersonas = this.ifzPersona.findLikeProperty(propertyName, value, limite);
			if (listPersonas == null)
				listPersonas = new ArrayList<Persona>();
			
			for (Persona var : listPersonas)
				var.setTipoPersona(1L);

			// Recuperamos negocios y los convertimos a personas
			List<Negocio> listNegocio = this.ifzNegocio.findLikeProperty(propertyName, value, limite);
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
					
					// A�adimos a listado
					listPersonas.add(pojoPersona);
				}
			}

			// Ordenamos resultados por nombre
			Collections.sort(
				listPersonas, 
				new Comparator<Persona>() {
					@Override
					public int compare(Persona o1, Persona o2) {
						return o1.getNombre().trim().compareTo(o2.getNombre().trim());
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
	public Respuesta buscarDomicilio(long idPersona) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<DomicilioExt> lista = null;
		DomicilioExt dom = null;

		try {
			// Recuperamos los domicilios registrados de la Persona
			lista = this.ifzClientes.buscarDomicilioExt(this.findById(idPersona)); 
			
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
			
			return respuesta;
		} catch (Exception e) {
			throw e;
		}
	}
	
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
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCI�N
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2016-11-09 | Javier Tirado 	| A�ado los metodos findByProperty y findLikeProperty
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */