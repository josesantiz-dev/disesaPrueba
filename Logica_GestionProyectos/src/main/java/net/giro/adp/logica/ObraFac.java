package net.giro.adp.logica;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.TipoObraAutorizadas;
import net.giro.adp.beans.TipoObraJerarquia;
import net.giro.adp.beans.TipoObraRevisadas;
import net.giro.adp.beans.TipoSub;
import net.giro.adp.dao.ObraDAO;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.respuesta.Respuesta;
import net.giro.respuesta.RespuestaError;

@Stateless
public class ObraFac implements ObraRem {
	private static Logger log = Logger.getLogger(ObraFac.class);
	private InfoSesion infoSesion;
	private ObraDAO ifzObras;
	private ObraAlmacenesRem ifzAlmacenes;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public ObraFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzObras = (ObraDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzAlmacenes = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
            this.ifzPersonas = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
            this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
            this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("ObraFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear ObraFac", e);
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(Obra entity) throws Exception {
		try {
			return eventoAltaObra(this.ifzObras.save(entity, getCodigoEmpresa()));
		} catch (Exception re) {	
			log.error("Error en ObraFac.save(obra)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<Obra> saveOrUpdateList(List<Obra> entities) throws Exception {
		try {
			return this.ifzObras.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			log.error("Error en ObraFac.saveOrUpdateList(List<Obra> entities)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(Obra entity) throws Exception {
		try {
			this.ifzObras.update(entity);
		} catch (Exception re) {	
			log.error("Error en ObraFac.update(Entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void delete(Long idObra) throws Exception {
		try {
			this.ifzObras.delete(idObra);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Respuesta cancelar(Obra entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<RespuestaError> errores = null;
		
		try {
			errores =  new ArrayList<RespuestaError>();
			if (comprobarIngresos(entity.getId())) 
				errores.add(new RespuestaError(2L, "La Obra tiene Ingresos registrados"));
			if (comprobarEgresos(entity.getId())) 
				errores.add(new RespuestaError(3L, "La Obra tiene Egresos registrados"));
			if (comprobarOrdenesCompra(entity.getId())) 
				errores.add(new RespuestaError(4L, "La Obra tiene Ordenes de Compra registradas"));
			if (comprobarExplosionInsumos(entity.getId())) 
				errores.add(new RespuestaError(5L, "La Obra tiene Explosion de Insumos registrada"));
			
			if (! errores.isEmpty()) {
				respuesta.getErrores().setErrores(errores);
				respuesta.getErrores().setCodigoError(errores.get(0).getCodigoError());
				respuesta.getErrores().setDescError(errores.get(0).getDescError());
				return respuesta;
			}

			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setEstatus(0L);
			this.update(entity);
			respuesta.getBody().addValor("obra", entity);
		} catch (Exception e) {	
			log.error("Ocurrio un problema al intentar cancelar la Obra", e);
			respuesta.getErrores().addCodigo("GP", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Error desconocido");
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Obra findById(Long idObra) {
		try {
			return this.ifzObras.findById(idObra);
		} catch (Exception re) {	
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
			
			this.update(entity);
			return entity;
		} catch (Exception e) {	
			log.error("Error en ObraFac.revisado(Entity)", e);
			throw e;
		}
	}

	@Override
	public Obra autorizado(Obra entity, long autorizadaPor, Date fechaAutorizada) throws Exception {
		try {
			entity.setModificadoPor(autorizadaPor);
			entity.setFechaModificacion(fechaAutorizada);
			
			if (entity.getAutorizado() == 0) {
				entity.setAutorizado(1);
				entity.setIdUsuarioAutorizo(autorizadaPor);
				entity.setFechaAutorizacion(fechaAutorizada);
			} else {
				entity.setRevisado(0);
				entity.setRevisadoPor(0);
				entity.setFechaRevisado(null);
			}
			
			this.update(entity);
			return entity;
		} catch (Exception e) {	
			log.error("Error en ObraFac.revisado(Entity)", e);
			throw e;
		}
	}

	@Override
	public List<Obra> findAll() throws Exception {
		try {
			return this.findAll(0L, false, 0, 0, null);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar todas las obras. Metodo: findAll()", re);
			throw re;
		} 
	}

	@Override
	public List<Obra> findAll(long idObraPrincipal, boolean incluyeCanceladas, int revisadas, int autorizadas, String orderBy) throws Exception {
		try {
			return this.ifzObras.findAll(idObraPrincipal, incluyeCanceladas, revisadas, autorizadas, getIdEmpresa(), orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar todas las obras. Metodo: findAll(incluyeCanceladas, revisadas, autorizadas, orderBy)", re);
			throw re;
		} 
	}

	@Override
	public List<Obra> findByProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, String orderBy, int limite) throws Exception {
		try {
			if (value.getClass() == java.lang.String.class && value.toString().trim().contains(" "))
				value = value.toString().trim().replace(" ", "%");
			return this.ifzObras.findByProperty(propertyName, value, idObraPrincipal, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisadas, autorizadas, jerarquia, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las Obras. Metodo findByProperty(propertyName, value, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisadas, autorizadas, jerarquia, orderBy, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findByProperty(String propertyName, Object value, int tipo) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 0L, tipo, false, false, 0, 0, 0, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las Obras. Metodo findByProperty(propertyName, value, tipo)", re);
			throw re;
		} 
	}

	@Override
	public List<Obra> findByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 0L, 0, false, false, 0, 0, 0, "", 0);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public List<Obra> ObrafindByProperty(String propertyName, final Object value) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 0L, 0, false, false, 0, 0, 0, "", 0);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public List<Obra> busquedaDinamica(String value) throws Exception {
		try {
			return this.findLike(value, 0L, 0L, 0, true, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, "", 1000);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las obras. Metodo: busquedaDinamica(value)", re);
			throw re;
		}
	}
	
	@Override
	public List<Obra> findLike(String value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, TipoObraRevisadas revisado, TipoObraAutorizadas autorizado, TipoObraJerarquia jerarquia, String orderBy, int limite) throws Exception {
		try {
			if (value.trim().contains(" "))
				value = value.trim().replace(" ", "%");
			return this.ifzObras.findLike(value, idObraPrincipal, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisado.ordinal(), autorizado.ordinal(), jerarquia.ordinal(), getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {	
			log.error("Ocurrio un problema al consultar las Obras. Metodo: findLike(value, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisado, autorizado, jerarquia, orderBy, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findLike(String value, boolean incluyeAdministrativas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.findLike(value, 0L, 0L, 0, incluyeAdministrativas, incluyeCanceladas, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, TipoObraRevisadas revisado, TipoObraAutorizadas autorizado, TipoObraJerarquia jerarquia, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idObraPrincipal, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisado, autorizado, jerarquia, orderBy, limite);
			if (value.getClass() == java.lang.String.class && value.toString().trim().contains(" "))
				value = value.toString().trim().replace(" ", "%");
			return this.ifzObras.findLikeProperty(propertyName, value, idObraPrincipal, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisado.ordinal(), autorizado.ordinal(), jerarquia.ordinal(), getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las Obras. Metodo: findLikeProperty(propertyName, value, idSucursal, tipo, incluyeAdministrativas, incluyeCanceladas, revisado, autorizado, jerarquia, orderBy, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, Object value, boolean incluyeAdministrativas, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, 0, incluyeAdministrativas, incluyeCanceladas, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, Object value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, 0, incluyeAdministrativas, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, tipo, false, incluyeCanceladas, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, 0, false, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, "", 0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> findLikePropertyByAlmacen(String propertyName, Object propertyValue, long idAlmacen, int limite) throws Exception {
		List<Obra> resultado = new ArrayList<Obra>();
		List<ObraAlmacenes> obras = null;
		String property = "";
		
		try {
			property = propertyName;
			if (propertyName != null && ! "".equals(propertyName.trim()) && ! propertyName.trim().contains("*"))
				property = "idObra." + propertyName;
			if (propertyValue.getClass() == java.lang.String.class && propertyValue.toString().trim().contains(" "))
				propertyValue = propertyValue.toString().trim().replace(" ", "%");
			this.ifzAlmacenes.setInfoSesion(this.infoSesion);
			obras = this.ifzAlmacenes.findLikeProperty(property, propertyValue, idAlmacen, "model.nombreObra", limite);
			if (obras != null && ! obras.isEmpty()) {
				for (ObraAlmacenes obra : obras) 
					resultado.add(obra.getIdObra());
			}
			
			return resultado;
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las obras. Metodo: findLikePropertyByAlmacen(propertyName, propertyValue, idAlmacen, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findInProperty(String propertyName, List<Object> values, String orderBy) throws Exception {
		try {
			return this.ifzObras.findInProperty(propertyName, values, getIdEmpresa(), orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las obras. Metodo: findInProperty(propertyName, values, orderBy)", re);
			throw re;
		} 
	}

	@Override
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception {
		try {
			return this.findInProperty(propertyName, values, null);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public List<Obra> findPrincipalLikeProperty(String propertyName, Object value, int tipoObra, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, tipoObra, false, incluyeCanceladas, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Principales, orderBy, limite);
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	public List<Obra> findPrincipalLikeSucursal(String propertyName, Object value, int tipoObra, long idSucursal, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, idSucursal, tipoObra, false, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Principales, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en ObraFac.findPrincipalLike(String propertyName, Object value, int tipoObra, long idSucursal, String orderBy, int limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findSubObraLikeProperty(String propertyName, Object value, int tipoObra, Long idObra, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, idObra, 0L, tipoObra, false, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Subobras, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en ObraFac.findSubObraLikeProperty(String propertyName, Object value, int tipoObra, long idSucursal, String orderBy, int limite)", re);
			throw re;
		}
	}

	@Override
	public List<Obra> findSubObraLikeSucursal(String propertyName, Object value, int tipoObra, Long idObra, long idSucursal, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, idObra, idSucursal, tipoObra, false, false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Subobras, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en ObraFac.findSubObraLikeSucursal(String propertyName, Object value, int tipoObra, long idSucursal, String orderBy, int limite)", re);
			throw re;
		}
	}

	@Override
	public List<Persona> findClienteLikeProperty(String propertyName, String value, String tipo) throws Exception {
		List<Persona> listResult = new ArrayList<Persona>();
		
		try {
			if (value.toString().trim().contains(" "))
				value = value.toString().trim().replace(" ", "%");
			if (tipo == null || "".equals(tipo) || "P".equals(tipo)) {
				this.ifzPersonas.setInfoSesion(this.infoSesion);
				listResult = this.ifzPersonas.findLikeProperty(propertyName, value, 0);
			} else {
				this.ifzNegocios.setInfoSesion(this.infoSesion);
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
	public List<TipoSub> findAllTipoSub() throws Exception {
		try {
			return this.ifzObras.findAllTipoSub();
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar todas las obras. Metodo: findAllTipoSub()", re);
			throw re;
		} 
	}
	// -------------------------------------------------------------------------------------------
	// CONVERSION
	// -------------------------------------------------------------------------------------------

	@Override
	public Obra convertir(ObraExt target) throws Exception {
		return this.convertidor.getPojo(target);
	}

	@Override
	public ObraExt convertir(Obra target) throws Exception {
		return this.convertidor.getExtendido(target);
	}

	@Override
	public List<Obra> convertirList(List<ObraExt> targetList) throws Exception {
		List<Obra> resultado = new ArrayList<Obra>();
		
		for (ObraExt var : targetList)
			resultado.add(this.convertidor.getPojo(var));
		
		return resultado;
	}

	@Override
	public List<ObraExt> extenderLista(List<Obra> targetList) throws Exception {
		List<ObraExt> resultado = new ArrayList<ObraExt>();
		
		for (Obra var : targetList)
			resultado.add(this.convertidor.getExtendido(var));
		
		return resultado;
	}

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public Long save(ObraExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en ObraFac.save(EntityExt)", e);
			throw e;
		}
	}
	
	@Override
	public void update(ObraExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en ObraFac.update(EntityExt)", e);
			throw e;
		}
	}

	@Override
	public ObraExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<ObraExt> findLikePropertyExt(String propertyName, String value, int opcion, String orderBy, int limite) throws Exception {
		List<ObraExt> extendidos = new ArrayList<ObraExt>();
		List<Obra> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value, opcion, false, orderBy, limite);
			if (entities != null && ! entities.isEmpty()) {
				for (Obra entity : entities) 
					extendidos.add(this.convertidor.getExtendido(entity));
			}
		} catch (Exception e) {
			log.error("Error en findLikePropertyExt", e);
			throw e;
		}
		
		return extendidos;
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

	// ----------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------

	private Long eventoAltaObra(Long idObra) throws Exception {
		MensajeTopic msgTopic = null;
		String target = "";
		String referencia = "";
		String atributos = "";
		
		try {
			if (idObra != null && idObra > 0L) {
				target = idObra.toString();
				msgTopic = new MensajeTopic(TopicEventosGP.OBRA_ALTA, target, referencia, atributos, this.infoSesion);
				msgTopic.enviar();
			}
		} catch (Exception re) {	
			log.error("Ocurrio un problema al enviar el evento de ALTA de OBRA", re);
			throw re;
		}
		
		return idObra;
	}

	@SuppressWarnings("unchecked")
	private boolean comprobarIngresos(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return false;
			queryString += "select count(id) as ingresos from factura where id_obra = :idObra and estatus = 1 ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigInteger) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar los ingresos de la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return (resultado > 0);
	}

	@SuppressWarnings("unchecked")
	private boolean comprobarEgresos(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return false;
			queryString += "select count(*) as egresos from pagos_gastos a inner join pagos_gastos_det b on b.pagos_gastos_id = a.pagos_gastos_id where :idObra in (a.id_obra, b.id_obra) and a.estatus <> 'X' ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigInteger) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar los egresos de la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return (resultado > 0);
	}

	@SuppressWarnings("unchecked")
	private boolean comprobarOrdenesCompra(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return false;
			queryString += "select count(id) as ordenes from orden_compra where id_obra = :idObra and sistema in (0,1) and estatus in (0,2) ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigInteger) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar las ordenes de compra de la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return (resultado > 0);
	}

	@SuppressWarnings("unchecked")
	private boolean comprobarExplosionInsumos(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return false;
			queryString += "select count(id) as explosion from insumos_presupuesto where id_obra = :idObra and estatus = 0 ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigInteger) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar si tiene explosion de insumos la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return (resultado > 0);
	}
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
	
	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
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
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findClienteByProperty y findClienteLikeProperty
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Añado los metodos findByMultiProperties y findLikeMultiProperties
 * 1.2 | 2017-04-17 | Javier Tirado 	| Implemento el metodo findEstatusCanceladoObras
 */