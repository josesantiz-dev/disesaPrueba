package net.giro.inventarios.logica;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.dao.AlmacenMovimientosDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosInventarios;
import net.giro.respuesta.Respuesta;

@Stateless
public class AlmacenMovimientosFac implements AlmacenMovimientosRem {
	private static Logger log = Logger.getLogger(AlmacenMovimientosFac.class);
	private InfoSesion infoSesion;
	private AlmacenMovimientosDAO ifzAlmacenMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalles;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public AlmacenMovimientosFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzAlmacenMovimientos = (AlmacenMovimientosDAO) ctx.lookup("ejb:/Model_Inventarios//AlmacenMovimientosImp!net.giro.inventarios.dao.AlmacenMovimientosDAO");
            this.ifzMovimientosDetalles = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica Fac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(AlmacenMovimientos entity) throws Exception {
		try {
			entity = generaFolio(entity);
			entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			return this.ifzAlmacenMovimientos.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un poblema al guardar el movimiento", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception {
		try {
			for (AlmacenMovimientos entity : entities) {
				entity = generaFolio(entity);
				entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			}
			
			return this.ifzAlmacenMovimientos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Ocurrio un poblema al guardar o actualizar la lista de movimientos", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(AlmacenMovimientos entity) throws Exception {
		try {
			entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			this.ifzAlmacenMovimientos.update(entity);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al intentar actualizar el Movimiento", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelar(AlmacenMovimientos entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		TipoMovimientoInventario tipoMovimiento = null;
		
		try {
			if (this.infoSesion == null ) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al validar la Sesion de Usuario");
				return respuesta;
			}
			
			if (entity == null || entity.getId() == null || entity.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al recuperar el Movimiento(ENTRADA/SALIDA) indicado");
				return respuesta;
			}
			
			// Determinamos el Tipo de Movimiento
			tipoMovimiento = TipoMovimientoInventario.fromOrdinal(entity.getTipo());
			if (tipoMovimiento == null) {
				respuesta.getErrores().setCodigoError(-1);
				respuesta.getErrores().setDescError("Ocurrio un problema al determinar el Tipo de Movimiento");
				return respuesta;
			}
			
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(entity);

			respuesta.getBody().addValor("entity", entity);
			respuesta.getBody().addValor("movimiento", entity);

			// Lanzamos evento de cancelacion
			boCancelar(entity.getId());
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar cancelar la SBO", re);	
			respuesta.getErrores().setCodigoError(-1);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar la Solicitud a Bodega");
		}
		
		return respuesta;
	}

	@Override
	public void delete(Long idAlmacenMovimientos) throws Exception {
		try {
			this.ifzAlmacenMovimientos.delete(idAlmacenMovimientos);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al intentar Eliminar el Movimiento", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AlmacenMovimientos findById(Long idAlmacenMovimientos) {
		try {
			return this.ifzAlmacenMovimientos.findById(idAlmacenMovimientos);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findById", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, String orderBy, int limite) throws Exception {
		try {
			return this.findLike(value, idAlmacen, tipoMovimiento, tipoEntrada, false, false, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzAlmacenMovimientos.findLike(value, idAlmacen, tipoMovimiento, tipoEntrada, incluyeCancelados, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findLike", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			if (value instanceof java.lang.String) {
				if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
					return this.findLike(value.toString(), idAlmacen, tipoMovimiento, tipoEntrada, orderBy, limite);
				
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzAlmacenMovimientos.findLikeProperty(propertyName, value, idAlmacen, tipoMovimiento, tipoEntrada, incluyeCancelados, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findLikeProperty", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object value, long idAlmacen, int tipoMovimiento, String tipoEntrada, String orderBy, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, idAlmacen, tipoMovimiento, tipoEntrada, false, false, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String value, int tipoMovimiento, String tipoEntrada, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, tipoMovimiento, tipoEntrada, false, false, "", limite);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findLikeProperty", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value) {
		try {
			return this.findByProperty(propertyName, value, -1, "", 0);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findByProperty", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int tipoMovimiento, String tipoEntrada, int limite) {
		try {
			return this.ifzAlmacenMovimientos.findByProperty(propertyName, value, tipoMovimiento, tipoEntrada, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findByProperty", re);
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite) {
		try {
			return this.ifzAlmacenMovimientos.findSalidaByTraspaso(idTraspaso, tipoTraspaso, getIdEmpresa(), limite);
		} catch (Exception re) {
			log.error("Ocurrio un poblema al realizar la consulta findSalidaByTraspaso", re);
			throw re;
		}
	}

	@Override
	public boolean backOfficeInventario(Long idMovimiento, String referencia) throws Exception {
		AlmacenMovimientos movimiento = null; 
		List<MovimientosDetalle> detalles = null;
		TopicEventosInventarios evento = null;
		
		try {
			if (this.infoSesion == null ) {
				log.error("Ocurrio un problema al validar la Sesion de Usuario");
				return false;
			}
			
			movimiento = this.findById(idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L) {
				log.error("No se pudo recuperar el Movimiento indicado. Movimiento ID: " + idMovimiento);
				return false;
			}
			
			// Validamos los detalles del movimiento
			detalles = this.ifzMovimientosDetalles.findAll(idMovimiento); 
			if (detalles == null || detalles.isEmpty()) {
				log.error("El Movimiento indicado no tiene detalles. Movimiento ID: " + idMovimiento);
				return false;
			}

			referencia = (referencia != null && ! "".equals(referencia.trim()) ? referencia.trim() : "");
			if (movimiento.getTipo() == TipoMovimientoInventario.ENTRADA.ordinal()) 
				evento = movimiento.getSistema() == 1 ? TopicEventosInventarios.MOVIMIENTO : TopicEventosInventarios.POST_ENTRADA;
			else if (movimiento.getTipo() == TipoMovimientoInventario.SALIDA.ordinal()) 
				evento = TopicEventosInventarios.MOVIMIENTO;
			
			if (evento == null) {
				log.error("No se pudo determinar el evento para el Movimiento indicado. Movimiento ID: " + idMovimiento);
				return false;
			}
			
			// Generamos el mensaje, registramos y enviamos a JMS
			boInventario(evento, idMovimiento.toString(), referencia);
			return true;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar determinar el BackOffice de Inventarios", e);
			throw e;
		}
	}
	
	// ------------------------------------------------------------------------------------------------------
	// BACKOFFICE
	// ------------------------------------------------------------------------------------------------------

	private void boCancelar(Long idMovimiento) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			target = idMovimiento.toString();
			
			msgTopic = new MensajeTopic(TopicEventosInventarios.MOVIMIENTO_CANCEL, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:" + TopicEventosInventarios.MOVIMIENTO_CANCEL.toString() + "\n\n" + comando + "\n\n", e);
		}
	}
	
	private void boInventario(TopicEventosInventarios evento, String target, String referencia) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String atributos = "";
		String comando = "";
		
		try {
			msgTopic = new MensajeTopic(evento, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:" + evento.toString() + "\n\n" + comando, e);
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// --------------------------------------------------------------------------------------------------------

	@Override
	public AlmacenMovimientos convertir(AlmacenMovimientosExt target) {
		return this.convertidor.getPojo(target);
	}
	
	@Override
	public AlmacenMovimientosExt convertir(AlmacenMovimientos target) {
		return this.convertidor.getExtendido(target);
	}

	// --------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenMovimientosExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenMovimientosExt extendido) throws Exception {
		try {
			this.update(this.convertidor.getPojo(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenMovimientosExt findByIdExt(Long idAlmacenMovimientos) {
		try {
			return this.convertidor.getExtendido(this.findById(idAlmacenMovimientos));
		} catch (Exception re) {	
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	private AlmacenMovimientos generaFolio(AlmacenMovimientos movimiento) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy");
		List<Object> nativeResult = null;
		Object row = null;
		Object[] item = null;
		String queryString = "";
		Integer tipoMovimiento = 0;
		// --------------------------------
		String prefijo = "";
		String folio = "";
		int consecutivo = 0;
		
		try {
			if (movimiento.getSistema() == 1)
				return movimiento;
			if (movimiento.getFolio() != null && ! "".equals(movimiento.getFolio()))
				return movimiento;
			tipoMovimiento = movimiento.getTipo();
			prefijo = (tipoMovimiento == 0 ? "E" : (tipoMovimiento == 1 ? "S" : "X")) + formatter.format(Calendar.getInstance().getTime()) + "-";
			
			queryString += "select lpad(cast((coalesce(max(consecutivo),0) + 1) as varchar), 6, '0') as folio, (coalesce(max(consecutivo),0) + 1) as consecutivo ";
			queryString += "from almacen_movimientos where id_almacen = :idAlmacen and tipo = :tipoMovimiento and extract(year from fecha) = extract(year from current_date) ";
			queryString = queryString.replace(":idAlmacen", movimiento.getIdAlmacen().getId().toString());
			queryString = queryString.replace(":tipoMovimiento", tipoMovimiento.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult == null || nativeResult.isEmpty())
				return movimiento;
			
			row = nativeResult.get(0);
			item = (Object[]) row;
			folio = item[0].toString();
			consecutivo = (int) item[1];
			
			folio = prefijo + folio;
			movimiento.setFolio(folio);
			movimiento.setConsecutivo(consecutivo);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el Folio para el Movimiento indicado :: QUERY \n" + queryString, e);
		} 
		
		return movimiento;
	}
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
