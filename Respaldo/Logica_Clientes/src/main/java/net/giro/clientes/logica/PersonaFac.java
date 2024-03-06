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

import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConGrupoValores;

@Stateless
public class PersonaFac implements PersonaRem {
	private static Logger log = Logger.getLogger(PersonaFac.class);
	private InitialContext ctx = null;
	private PersonaDAO ifzPersona;
	private NegocioDAO ifzNegocio;

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

			this.ifzPersona =  (PersonaDAO) ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
			this.ifzNegocio =  (NegocioDAO) ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear CajaChicaFac", e);
			ctx = null;
		}
	}
	
	public Long save(Persona entity) throws ExcepConstraint {
		try {
			return this.ifzPersona.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void delete(Persona entity) throws ExcepConstraint {
		try {
			this.ifzPersona.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public Persona update(Persona entity) throws ExcepConstraint {
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