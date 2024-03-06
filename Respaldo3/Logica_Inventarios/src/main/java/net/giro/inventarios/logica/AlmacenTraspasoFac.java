package net.giro.inventarios.logica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.dao.AlmacenTraspasoDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosInventarios;
import net.giro.respuesta.Respuesta;

@Stateless
public class AlmacenTraspasoFac implements AlmacenTraspasoRem {
	private static Logger log = Logger.getLogger(AlmacenTraspasoFac.class);
	private InfoSesion infoSesion;
	private AlmacenTraspasoDAO ifzAlmacenTraspaso;
	private TraspasoDetalleRem ifzDetalles;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public AlmacenTraspasoFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzAlmacenTraspaso = (AlmacenTraspasoDAO) ctx.lookup("ejb:/Model_Inventarios//AlmacenTraspasoImp!net.giro.inventarios.dao.AlmacenTraspasoDAO");
            this.ifzDetalles = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicilizar el EJB", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(AlmacenTraspaso entity) throws Exception {
		try {
			entity = generaFolio(entity);
			entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			return this.ifzAlmacenTraspaso.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar guardar el Traspaso", re);	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception {
		try {
			for (AlmacenTraspaso entity : entities) {
				entity = generaFolio(entity);
				entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			}
			
			return this.ifzAlmacenTraspaso.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar guardar el listado de Traspasos", re);	
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(AlmacenTraspaso entity) throws Exception {
		try {
			entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			this.ifzAlmacenTraspaso.update(entity);
		} catch (Exception re) {	
			log.error("Ocurrio un problema al intentar actualizar el Traspaso", re);	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelar(AlmacenTraspaso entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		TipoTraspaso tipoTraspaso = null;
		
		try {
			if (this.infoSesion == null ) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al validar la Sesion de Usuario");
				return respuesta;
			}
			
			if (entity == null || entity.getId() == null || entity.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al recuperar el Traspaso indicado");
				return respuesta;
			}
			
			// Determinamos el Tipo de Traspaso
			for (TipoTraspaso item : TipoTraspaso.values()) {
				if (item.ordinal() == entity.getTipo()) {
					tipoTraspaso = item;
					break;
				}
			}
			
			if (tipoTraspaso == null || tipoTraspaso.equals(TipoTraspaso.Ninguno)) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al determinar el Tipo de Traspaso");
				return respuesta;
			}
			
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(entity);

			respuesta.getBody().addValor("entity", entity);
			respuesta.getBody().addValor("traspaso", entity);
			respuesta.getBody().addValor("solicitud", entity);

			// Lanzamos evento de cancelacion
			boCancelar(entity.getId(), tipoTraspaso);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar cancelar la SBO", re);	
			respuesta.getErrores().setCodigoError(-1);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar la Solicitud a Bodega");
		}
		
		return respuesta;
	}

	@Override
	public void delete(long idAlmacenTraspaso) throws Exception {
		try {
			this.ifzAlmacenTraspaso.delete(idAlmacenTraspaso);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar borrar el Traspaso", re);	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AlmacenTraspaso findById(long idAlmacenTraspaso) {
		try {
			return this.ifzAlmacenTraspaso.findById(idAlmacenTraspaso);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar el Traspaso", re);	
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findLike(String value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzAlmacenTraspaso.findLike(value, idAlmacenOrigen, idAlmacenDestino, tipoTraspaso.ordinal(), incluyeCompleto, incluyeSistema, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		try {
			if (value instanceof java.lang.String) {
				if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
					return this.findLike(value.toString(), idAlmacenOrigen, idAlmacenDestino, tipoTraspaso, incluyeCompleto, incluyeSistema, orderBy, limite);
				
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzAlmacenTraspaso.findLikeProperty(propertyName, value, idAlmacenOrigen, idAlmacenDestino, tipoTraspaso.ordinal(), incluyeCompleto, incluyeSistema, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value) {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0L, TipoTraspaso.Ninguno, false, false, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzAlmacenTraspaso.findByProperty(propertyName, value, 0, true, false, getIdEmpresa(), 0);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos", re);	
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------

	@Override
	public List<AlmacenTraspaso> findLikePropertyTraspasoDevolucion(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		List<AlmacenTraspaso> resultados = null;
		List<AlmacenTraspaso> traspasos = null;
		List<AlmacenTraspaso> devoluciones = null;
		
		try {
			resultados   = new ArrayList<AlmacenTraspaso>();
			traspasos    = this.findTraspasoLikeProperty  (propertyName, value, idAlmacenOrigen, idAlmacenDestino, incluyeCompleto, incluyeSistema, orderBy, limite);
			devoluciones = this.findDevolucionLikeProperty(propertyName, value, idAlmacenOrigen, idAlmacenDestino, incluyeCompleto, incluyeSistema, orderBy, limite);
			if (traspasos != null && ! traspasos.isEmpty()) 
				resultados.addAll(traspasos);
			if (devoluciones != null && ! devoluciones.isEmpty()) 
				resultados.addAll(devoluciones);
			return resultados;
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos/Devoluciones", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findTraspasoLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		try {
			/*if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findTraspasoLike(value.toString(), idAlmacenOrigen, idAlmacenDestino, incluyeCompleto, incluyeSistema, orderBy, limite);*/
			return this.findLikeProperty(propertyName, value, idAlmacenOrigen, idAlmacenDestino, TipoTraspaso.TRASPASO, incluyeCompleto, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar los Traspasos", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findDevolucionLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		try {
			/*if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findDevolucionLike(value.toString(), idAlmacenOrigen, idAlmacenDestino, incluyeCompleto, incluyeSistema, orderBy, limite);*/
			return this.findLikeProperty(propertyName, value, idAlmacenOrigen, idAlmacenDestino, TipoTraspaso.DEVOLUCION, incluyeCompleto, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar las Devoluciones", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspaso> findSolicitudLikeProperty(String propertyName, Object value, long idAlmacen, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		try {
			/*if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findSolicitudLike(value.toString(), idAlmacen, incluyeCompleto, incluyeSistema, orderBy, limite);*/
			return this.findLikeProperty(propertyName, value, idAlmacen, 0L, TipoTraspaso.SOLICITUD_BODEGA, incluyeCompleto, incluyeSistema, orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar consultar las Solicitudes a Bidega", re);
			throw re;
		}
	}

	@Override
	public boolean postTraspaso(Long idTraspaso) {
		AlmacenTraspaso traspaso = null;
		List<TraspasoDetalle> productos = null;
		// -----------------------------------------
		String tipoTraspaso = "";
		
		try {
			if (this.infoSesion == null ) {
				log.error("Ocurrio un problema al validar la Sesion de Usuario");
				return false;
			}
			
			idTraspaso = (idTraspaso != null ? idTraspaso: 0L);
			traspaso = this.findById(idTraspaso);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L){
				log.error("Ocurrio un problema al recuperar el Traspaso indicado");
				return false;
			}

			tipoTraspaso = (traspaso.getTipo() == 1 ? "TR" : (traspaso.getTipo() == 2 ? "DE" : "?"));
			productos = this.ifzDetalles.findAll(idTraspaso);
			if (productos == null || productos.isEmpty()){
				log.error("El Traspaso indicado no tiene productos");
				return false;
			}
			
			// Generamos el mensaje, registramos y enviamos a JMS
			boPostTraspaso(idTraspaso, tipoTraspaso);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar lanzar el BackOffice de Inventarios para el Traspaso indicado", e);
			return false;
		} 

		return true;
	}

	// -----------------------------------------------------------------------------------------------
	// BACKOFFICE
	// -----------------------------------------------------------------------------------------------

	private void boCancelar(Long idTraspaso, TipoTraspaso tipoTraspaso) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		TopicEventosInventarios evento = null;
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			evento = (tipoTraspaso == TipoTraspaso.SOLICITUD_BODEGA ? TopicEventosInventarios.SOLICITUD_BODEGA_CANCELACION : TopicEventosInventarios.TRASPASOS_CANCEL);
			target = idTraspaso.toString();
			
			msgTopic = new MensajeTopic(evento, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:" + evento.toString() + "\n\n" + comando + "\n\n", e);
		}
	}
	
	private void boPostTraspaso(Long idTraspaso, String tipoTraspaso) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "";
		String referenciaExtra = "";
		String atributos = "";
		String comando = "";
		
		try {
			target = idTraspaso.toString();
			referencia = "SALIDA";
			referenciaExtra = tipoTraspaso;
			
			msgTopic = new MensajeTopic(TopicEventosInventarios.POST_TRASPASO, target, referencia, atributos, this.infoSesion);
			msgTopic.setReferenciaExtra(referenciaExtra);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:" + TopicEventosInventarios.POST_TRASPASO.toString() + "\n\n" + comando + "\n\n", e);
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// -----------------------------------------------------------------------------------------------
	
	@Override
	public AlmacenTraspaso convertir(AlmacenTraspasoExt entity) {
		return this.convertidor.getPojo(entity);
	}

	@Override
	public AlmacenTraspasoExt convertir(AlmacenTraspaso entity) {
		return this.convertidor.getExtendido(entity);
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------
	
	@Override
	public Long save(AlmacenTraspasoExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenTraspasoExt extendido) throws Exception {
		try {
			this.update(this.convertidor.getPojo(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenTraspasoExt findByIdExt(long idAlmacenTraspaso) {
		try {
			return this.convertidor.getExtendido(this.findById(idAlmacenTraspaso));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenTraspasoExt> findExtLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, TipoTraspaso tipoTraspaso, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, int limite) {
		List<AlmacenTraspasoExt> extendidos = new ArrayList<AlmacenTraspasoExt>();
		List<AlmacenTraspaso> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value, idAlmacenOrigen, idAlmacenDestino, tipoTraspaso, incluyeCompleto, incluyeSistema, orderBy, limite);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			for (AlmacenTraspaso entity : entities)
				extendidos.add(this.convertidor.getExtendido(entity));
		} catch (Exception re) {		
			throw re;
		}
		
		return extendidos;
	}
	
	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private AlmacenTraspaso generaFolio(AlmacenTraspaso traspaso) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy");
		List<Object> nativeResult = null;
		Object row = null;
		Object[] item = null;
		String queryString = "";
		// --------------------------------
		String prefijo = "";
		String folio = "";
		int consecutivo = 0;
		
		try {
			if (traspaso.getSistema() == 1)
				return traspaso;
			if (traspaso.getTipo() == TipoTraspaso.SOLICITUD_BODEGA.ordinal())
				return traspaso;
			if (traspaso.getFolio() != null && ! "".equals(traspaso.getFolio()))
				return traspaso;
			prefijo = "T" + formatter.format(Calendar.getInstance().getTime()) + "-";
			
			queryString += "select lpad(cast((coalesce(max(consecutivo),0) + 1) as varchar), 6, '0') as folio, (coalesce(max(consecutivo),0) + 1) as consecutivo ";
			queryString += "from almacen_traspaso where id_almacen_origen = :idAlmacen and tipo in (1,2) and extract(year from fecha) = extract(year from current_date) ";
			queryString = queryString.replace(":idAlmacen", traspaso.getIdAlmacenOrigen().getId().toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult == null || nativeResult.isEmpty())
				return traspaso;
			
			row = nativeResult.get(0);
			item = (Object[]) row;
			folio = item[0].toString();
			consecutivo = (int) item[1];
			
			folio = prefijo + folio;
			traspaso.setFolio(folio);
			traspaso.setConsecutivo(consecutivo);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el Folio para el Traspaso indicado :: QUERY \n" + queryString, e);
		} 
		
		return traspaso;
	}
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Añado disponibilidad del convertidor
 */