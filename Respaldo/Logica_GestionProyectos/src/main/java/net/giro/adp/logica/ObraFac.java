package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.dao.ObraDAO;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.publico.respuesta.Respuesta;

@Stateless
public class ObraFac implements ObraRem {
	private static Logger log = Logger.getLogger(ObraFac.class);
	private InitialContext ctx;
	private ObraDAO ifzObras;
	//private AutentificacionRem ifzAuth;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private ConGrupoValoresRem ifzGrupos;
	private ConValoresRem ifzConValores;
	private ConvertExt convertidor;
	private static Long estatusId;
	private static String orderBy;
	
	public ObraFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzObras 	 	= (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzPersonas 	= (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
            this.ifzNegocios 	= (NegociosRem) this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
            this.ifzGrupos 		= (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
            this.ifzConValores 	= (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
            //this.ifzAuth 		= (AutentificacionRem) this.ctx.lookup("ejb:/Logica_Publico//AutentificacionFac!net.giro.plataforma.seguridad.logica.AutentificacionRem?stateful");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("ObraFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear ObraFac", e);
			ctx = null;
		}
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		ObraFac.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		estatusId = estatus;
	}

	@Override
	public Long save(Obra entity) throws ExcepConstraint {
		try {
			return this.ifzObras.save(entity);
		} catch (RuntimeException re) {	
			log.error("Error en ObraFac.save(Entity)", re);
			throw re;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzObras.delete(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public void update(Obra entity) throws ExcepConstraint {
		try {
			this.ifzObras.update(entity);
		} catch (RuntimeException re) {	
			log.error("Error en ObraFac.update(Entity)", re);
			throw re;
		}
	}

	@Override
	public Obra findById(Long id) {
		try {
			return this.ifzObras.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@Override
	public Obra revisado(Obra entity, long revisadoPor, Date fechaRevisado) throws Exception {
		try {
			entity.setModificadoPor(revisadoPor);
			entity.setFechaModificacion(fechaRevisado);
			
			if (entity.getRevisado() == 0) {
				entity.setRevisado(1);
				entity.setRevisadoPor(revisadoPor);
				entity.setFechaRevisado(fechaRevisado);
			} else {
				entity.setRevisado(0);
				entity.setRevisadoPor(0);
				entity.setFechaRevisado(null);
			}
			
			this.ifzObras.update(entity);
			return entity;
		} catch (Exception e) {	
			log.error("Error en ObraFac.revisado(Entity)", e);
			throw e;
		}
	}

	@Override
	public List<Obra> findAll() throws Exception {
		try {
			this.ifzObras.estatus(null);
			this.ifzObras.orderBy(null);
			return this.findLikeProperty("nombre", "", 0);
		} catch (Exception re) {		
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByProperty(String propertyName, Object value, int opcion) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findByProperty(propertyName, value, opcion);
		} catch (Exception re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.findByProperty(propertyName, value, 0);
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	public List<Obra> findByPropertyPojoCompleto(String propertyName, String tipo, long value) throws Exception {
		try{
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch(RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findObraPrincipalByProperty(String propertyName, Object value, int tipoObra, Long idObra) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findObraPrincipalByProperty(propertyName, value, tipoObra, idObra);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByProperties(HashMap<String, Object> params, int tipoObra) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			return this.ifzObras.findByProperties(params, tipoObra);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findByProperties(params, tipoObra)", e);
			throw e;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, final String value) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.findLikeProperty(propertyName, value, 0);
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, String value, int opcion) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findLikeProperty(propertyName, value.toString(), opcion);
		} catch (Exception re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	public List<Obra> findLikeProperty(String propertyName, String value, boolean incluyeAdministrativas) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findLikeProperty(propertyName, value, incluyeAdministrativas);
		} catch (Exception re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	public List<Obra> findObraPrincipalLikeProperty(String propertyName, Object value, int tipoObra, Long idObra) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			
			return this.ifzObras.findObraPrincipalLikeProperty(propertyName, value, tipoObra, idObra);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	public List<Obra> findLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			return this.ifzObras.findLikeProperties(params, tipoObra);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findByProperties(params, tipoObra)", e);
			throw e;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByMultiProperties(HashMap<String, Object> params, String unionProps, int tipoObra, int limite) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			return this.ifzObras.findByMultiProperties(params, unionProps, tipoObra, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findByMultiProperties(params, unionProps, tipoObra, limite)", e);
			throw e;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findLikeMultiProperties(HashMap<String, String> params, String unionProps, int tipoObra, int limite) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			return this.ifzObras.findLikeMultiProperties(params, unionProps, tipoObra, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findLikeMultiProperties(params, unionProps, tipoObra, limite)", e);
			throw e;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Persona> findPersonaLikeProperty(String propertyName, String value, String tipo) throws Exception {
		return this.findClienteLikeProperty(propertyName, value, tipo);
	}
	
	@Override
	public List<Persona> findClienteByProperty(String propertyName, Object value, String tipo) throws Exception {
		List<Persona> listResult = new ArrayList<Persona>();
		
		try {
			if (tipo == null || "".equals(tipo) || "P".equals(tipo)) {
				listResult = this.ifzPersonas.findByProperty(propertyName, value, 0);
			} else {
				List<Negocio> listNegocios = this.ifzNegocios.findByProperty(propertyName, value, 0);
				if (listNegocios != null && ! listNegocios.isEmpty()) {
					for (Negocio var : listNegocios)
						listResult.add(this.convertidor.NegocioToPersona(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en ObraFac.findClienteByProperty(String propertyName, Object value, String tipo)", e);
			throw e;
		}
		
		return listResult;
	}

	@Override
	public List<Persona> findClienteLikeProperty(String propertyName, String value, String tipo) throws Exception {
		List<Persona> listResult = new ArrayList<Persona>();
		
		try {
			if (tipo == null || "".equals(tipo) || "P".equals(tipo)) {
				listResult = this.ifzPersonas.findLikeProperty(propertyName, value, 0);
			} else {
				List<Negocio> lista = this.ifzNegocios.findLikeProperty(propertyName, value, 0);
				if (lista != null && ! lista.isEmpty()) {
					for (Negocio var : lista) 
						listResult.add(this.convertidor.NegocioToPersona(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en ObraFac.findClienteLikeProperty(String propertyName, String value, String tipo)", e);
			throw e;
		}
		
		return listResult;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Long findEstatusCanceladoObras() throws Exception {
		Respuesta res = null;
		ConGrupoValores pojoGrupo = null;
		List<ConGrupoValores> lista = null;
		
		try {
			res = this.ifzGrupos.findByProperty("nombre", "SYS_ESTATUS_OBRA");
			lista = (List<ConGrupoValores>) res.getBody().getValor("listGrupoValores");
			pojoGrupo = lista.get(0);
			
			return this.findEstatusCanceladoObras(pojoGrupo);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Long findEstatusCanceladoObras(ConGrupoValores pojoGpoEstatusObras) throws Exception {
		Long estatus = null;
		
		List<ConValores> listEstatusObras = this.ifzConValores.findAll(pojoGpoEstatusObras);
		for (ConValores var : listEstatusObras) {
			if (esEstatusCancelado(var.getValor().toLowerCase())) {
				estatus = var.getId();
				break;
			}
		}
		
		// TODO: ARREGLO MIENTRAS SE DEFINE EL ESTATUS "CANCELADO" PARA OBRAS: POR AHORA NO ESTA EN SYS_ESTATUS_OBRA
		if (estatus == null)
			estatus = 0L;
		
		return estatus;
	}
	
    private boolean esEstatusCancelado(String value) {
    	if (value == null || "".equals(value))
    		value = "";
    	
    	if ("cancelado".equals(value.toLowerCase()))
    		return true;
    	if ("cancelada".equals(value.toLowerCase()))
    		return true;
    	if (value.toLowerCase().contains("cancelado"))
    		return true;
    	if (value.toLowerCase().contains("cancelada"))
    		return true;
    	
    	return false;
    }

	@Override
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			this.ifzObras.estatus(estatusId);
			this.ifzObras.orderBy(orderBy);
			return this.ifzObras.findInProperty(propertyName, values);
		} catch (Exception re) {	
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

    /*private List<Obra> aplicaPerfilObra(List<Obra> target) {
		List<Obra> resultado = new ArrayList<Obra>();
		String valPerfil = "";

		if (target != null && ! target.isEmpty()) {
			if (this.ifzAuth != null) {
				// Comprobamos el perfil: "S", significa que debe ver las obras administrativas
				valPerfil = this.ifzAuth.getPerfil("EGRESOS_OPERACION");
				if (valPerfil != null && "S".equals(valPerfil)) 
					return target;
			}
			
			for (Obra var : target) {
				if (var.getTipoObra() == 4) continue;
				resultado.add(var);
			}
			
			target.clear();
			target.addAll(resultado);
		}
		
		return target;
	}*/

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public Long save(ObraExt entityExt) throws ExcepConstraint {
		try {
			Obra entity = this.convertidor.ObraExtToObra(entityExt);
			return this.save(entity);
		} catch (Exception e) {
			log.error("Error en ObraFac.save(EntityExt)", e);
			throw e;
		}
	}
	
	@Override
	public void update(ObraExt entityExt) throws ExcepConstraint {
		try {
			Obra entity = this.convertidor.ObraExtToObra(entityExt);
			this.update(entity);
		} catch (Exception e) {
			log.error("Error en ObraFac.update(EntityExt)", e);
			throw e;
		}
	}

	@Override
	public Obra convertir(ObraExt target) throws Exception {
		log.info("Convirtiendo extendido a pojo OBRA");
		return this.convertidor.ObraExtToObra(target);
	}

	@Override
	public ObraExt convertir(Obra target) throws Exception {
		log.info("Extendiento pojo OBRA");
		return this.convertidor.ObraToObraExt(target);
	}

	@Override
	public ObraExt revisado(ObraExt entityExt, long revisadoPor, Date fechaRevisado) throws Exception {
		try {
			Obra entity = this.convertidor.ObraExtToObra(entityExt);
			return this.convertidor.ObraToObraExt(this.revisado(entity, revisadoPor, fechaRevisado));
		} catch (Exception e) {
			log.error("Error en ObraFac.revisado(EntityExt)", e);
			throw e;
		}
	}

	@Override
	public ObraExt findByIdExt(Long id) throws Exception {
		try {
			Obra pojoAux = this.findById(id);
			if (pojoAux != null)
				return this.convertidor.ObraToObraExt(pojoAux);
		} catch (RuntimeException re) {	
			throw re;
		}
		
		return null;
	}
	
	@Override
	public List<ObraExt> findExtByProperties(HashMap<String, Object> params, int tipoObra) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findByProperties(params, tipoObra);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findExtByProperties(params, tipoObra)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraExt> findExtLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findLikeProperties(params, tipoObra);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraFac.findExtLikeProperties(params, tipoObra)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraExt> findByPropertyExt(String propertyName, Object value) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findByProperty(propertyName, value);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<ObraExt> findLikePropertyExt(String propertyName, String value) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findLikeProperty(propertyName, value);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ObraExt> findByPropertyExt(String propertyName, Object value, int opcion) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findByProperty(propertyName, value, opcion);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<ObraExt> findLikePropertyExt(String propertyName, String value, int opcion) throws Exception {
		List<ObraExt> listaExt = new ArrayList<ObraExt>();
		
		try {
			List<Obra> lista = this.findLikeProperty(propertyName, value, opcion);
			for (Obra var : lista) {
				listaExt.add(this.convertidor.ObraToObraExt(var));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public PersonaExt extenderPersona(Persona pojoPersona) throws Exception {
		try {
			return this.convertidor.PersonaToPersonaExt(pojoPersona);
		} catch (Exception e) {
			log.error("Error en ObraFac.extenderPersona(Persona pojoPersona)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaExt> findClienteExtLikeProperty(String propertyName, String value, String tipo) throws Exception {
		List<PersonaExt> listaExt = new ArrayList<PersonaExt>();
		
		try {
			List<Persona> listResult = this.findClienteLikeProperty(propertyName, value, tipo);
			if (listResult != null && ! listResult.isEmpty()) {
				for (Persona var : listResult) {
					listaExt.add(this.convertidor.PersonaToPersonaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en ObraFac.findClienteExtLikeProperty(String propertyName, String value, String tipo)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<PersonaExt> findPersonaExtLikeProperty(String propertyName, String value, String tipo) throws Exception {
		return this.findClienteExtLikeProperty(propertyName, value, tipo);
	}

	@Override
	public List<PersonaExt> findClienteExtByProperty(String propertyName, Object value, String tipo) throws Exception {
		List<PersonaExt> listaExt = new ArrayList<PersonaExt>();
		
		try {
			List<Persona> listResult = this.findClienteByProperty(propertyName, value, tipo);
			if (listResult != null && ! listResult.isEmpty()) {
				for (Persona var : listResult) {
					listaExt.add(this.convertidor.PersonaToPersonaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en ObraFac.findClienteExtByProperty(String propertyName, Object value, String tipo)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Obra> convertirList(List<ObraExt> targetList) throws Exception {
		List<Obra> resultado = new ArrayList<Obra>();
		
		for (ObraExt var : targetList)
			resultado.add(this.convertidor.ObraExtToObra(var));
		
		return resultado;
	}

	@Override
	public List<ObraExt> extenderLista(List<Obra> targetList) throws Exception {
		List<ObraExt> resultado = new ArrayList<ObraExt>();
		
		for (Obra var : targetList)
			resultado.add(this.convertidor.ObraToObraExt(var));
		
		return resultado;
	}
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
 * 1.2 | 2017-04-17 | Javier Tirado 	| Implemento el metodo findEstatusCanceladoObras
 */