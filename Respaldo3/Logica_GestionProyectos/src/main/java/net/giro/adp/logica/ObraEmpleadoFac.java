package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.dao.ObraEmpleadoDAO;
import net.giro.adp.dao.ObraEmpleadoHistoricoDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.respuesta.Respuesta;

@Stateless
public class ObraEmpleadoFac implements ObraEmpleadoRem {
	private static Logger log = Logger.getLogger(ObraEmpleadoFac.class);
	private InfoSesion infoSesion;
	private ObraEmpleadoDAO ifzObraEmpleados;
	private ObraEmpleadoHistoricoDAO ifzHistorico;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public ObraEmpleadoFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzObraEmpleados = (ObraEmpleadoDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraEmpleadoImp!net.giro.adp.dao.ObraEmpleadoDAO");
            this.ifzHistorico = (ObraEmpleadoHistoricoDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraEmpleadoHistoricoImp!net.giro.adp.dao.ObraEmpleadoHistoricoDAO");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("ObraEmpleadoFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear ObraEmpleadoFac", e);
			ctx = null;
		}
	}
	
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(ObraEmpleado entity) throws Exception {
		try {
			return this.ifzObraEmpleados.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Error en ObraEmpleadoFac.save(Entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities) throws Exception {
		try {
			return this.ifzObraEmpleados.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraEmpleadoFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(ObraEmpleado entity) throws Exception {
		ObraEmpleado original = null;
		
		try {
			original = this.findById(entity.getId());
			if (original != null && (original.getIdObra().getId().longValue() != entity.getIdObra().getId().longValue() || original.getIdEmpleado().longValue() != entity.getIdEmpleado().longValue()))
				this.ifzHistorico.save(original, getCodigoEmpresa());
			this.ifzObraEmpleados.update(entity);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.update(Entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(Long idObraEmpleado) throws Exception {
		ObraEmpleado entity = null;
		
		try {
			if (idObraEmpleado == null || idObraEmpleado.longValue() <= 0L)
				return;
			
			entity = this.findById(idObraEmpleado);
			if (entity != null)
				this.ifzHistorico.save(entity.getIdObra().getId(), entity.getIdEmpleado(), entity.getFechaCreacion(), getUsuario(), getCodigoEmpresa());
			this.ifzObraEmpleados.delete(idObraEmpleado);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.delete(idEntity)", re);
			throw re;
		}
	}

	@Override
	public ObraEmpleado findById(Long idObraEmpleado) {
		try {
			return this.ifzObraEmpleados.findById(idObraEmpleado);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findById(idObraEmpleado)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findAll(long idObra) throws Exception {
		try {
			return this.findAll(idObra, null);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findAll(idObra)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findAll(long idObra, String orderBy) throws Exception {
		try {
			return this.ifzObraEmpleados.findAll(idObra, orderBy);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findAll(idObra, orderBy)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, null, limite);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value, limite)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, String orderBy, int limite) {
		try {
			return this.ifzObraEmpleados.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findLikeProperty(propertyName, value, limite)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int limite) {
		try {
			return this.findByProperty(propertyName, value, null, limite);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value, limite)", re);
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, String orderBy, int limite) {
		try {
			return this.ifzObraEmpleados.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {	
			log.error("Error en ObraEmpleadoFac.findByProperty(propertyName, value, limite)", re);
			throw re;
		}
	}

	@Override
	public Respuesta altaRetroactiva(long idRegistro, Date fechaBase) {
		ObraEmpleadoExt registro = null;
		Respuesta respuesta = null;
		Date fechaInicial = null;
		Calendar cal = null;
		int semana = 0;
		
		try {
			cal = Calendar.getInstance();
			cal.setTime(fechaBase);
			semana = cal.get(Calendar.WEEK_OF_YEAR);
			respuesta = new Respuesta();
			respuesta.getBody().addValor("semana", semana);
			registro = this.findByIdExt(idRegistro);
			if (registro == null || registro.getId() == null || registro.getId() <= 0L) {
				log.error("No se pudo recuperar el registro de asignacion del Empleado a Obra");
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("Ocurrio un problema al recuperar el registro de alta en obra del Empleado");
				return respuesta;
			}
			
			fechaInicial = this.inicioSemanaLaboral(fechaBase);
			if (fechaInicial.compareTo(fechaBase) < 0) {
				log.info("Aplico alta retroactiva para el Empleado " + registro.getEmpleadoNombre() + " en la Obra " + registro.getObraNombre() + ". Semana #" + semana);
				respuesta.getBody().addValor("mensaje", "Se lanzo alta retroactiva del Empleado en la semana #" + semana);
				sendAltaRetroactivaAsistencia(registro.getEmpleadoId(), registro.getObraId(), fechaBase);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar lanzar el Alta Retroactiva para Empleados Semanales", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema grave al procesar el alta retroactiva");
		}

		return respuesta;
	}

	// ------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ------------------------------------------------------------------------------------------------------------
	
	@Override
	public Long save(ObraEmpleadoExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(extendido));
		} catch (Exception e) {
			log.error("Error en salvar", e);
			throw e;
		}
	}
	
	@Override
	public void update(ObraEmpleadoExt extendido) throws Exception {
		try {
			this.update(this.convertidor.getPojo(extendido));
		} catch (Exception e) {
			log.error("Error en update", e);
			throw e;
		}
	}
	
	@Override
	public ObraEmpleadoExt findByIdExt(Long idObraEmpleado) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(idObraEmpleado));
		} catch (Exception e) {
			log.error("Error en findByIdExt", e);
			throw e;
		}
	}

	@Override
	public List<ObraEmpleadoExt> findExtAll(long idObra) throws Exception {
		List<ObraEmpleadoExt> extendidos = new ArrayList<ObraEmpleadoExt>();
		List<ObraEmpleado> lista = null;
		
		try {
			lista = this.findAll(idObra, null);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (ObraEmpleado var : lista) 
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en findExtAll(idObra)", e);
			throw e;
		}
		
		return extendidos;
	}

	// ------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------

	private void sendAltaRetroactivaAsistencia(Long idEmpleado, Long idObra, Date fechaBase) {
		MensajeTopic msgTopic = null;
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			// Validamos datos
			log.info("Validamos datos ... ");
			idObra = (idObra != null ? idObra : 0L);
			idEmpleado = (idEmpleado != null ? idEmpleado : 0L);
			if (idEmpleado <= 0L || idObra <= 0L) {
				log.info("Evento '" + TopicEventosRH.EMPLEADO_ALTA_RETROACTIVA.toString() + "' abortado ... Empleado: " + idEmpleado + ", Obra: " + idObra);
				return;
			}

			log.info("Preparamos evento ... ");
			target = idEmpleado.toString();
			referencia = idObra.toString();
			log.info("Lanzando evento ... ");
			msgTopic = new MensajeTopic(TopicEventosRH.EMPLEADO_ALTA_RETROACTIVA, target, referencia, atributos, this.infoSesion);
			msgTopic.setFecha(fechaBase);
			msgTopic.enviar();
			log.info("Evento '" + TopicEventosRH.EMPLEADO_ALTA_RETROACTIVA.toString() + "' lanzado!");
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al lanzar el Alta Retroactiva en Asistencia (topic/RH:EM_AS)\n\n" + comando + "\n\n", e);
		}
	}

	private Date inicioSemanaLaboral(Date fechaBase) {
		Calendar resultado = Calendar.getInstance();
		String dia = "";
		int diaActual = 0;
		int diaInicial = 0;
		int diferencia = 0;
		
		dia = getPerfilValue("SYS_SEMANA_LABORAL_OBRA_INICIO");
		if (dia == null || "".equals(dia.trim()))
			dia = "Domingo";
		switch (dia) {
			case "Domingo": diaInicial = 1; break;
			case "Lunes": diaInicial = 2; break;
			case "Martes": diaInicial = 3; break;
			case "Miercoles": diaInicial = 4; break;
			case "Jueves": diaInicial = 5; break;
			case "Viernes": diaInicial = 6; break;
			case "Sabado": diaInicial = 7; break;
		}

		resultado.setTime(fechaBase);
		diaActual = resultado.get(Calendar.DAY_OF_WEEK);

		if (diaActual > diaInicial) 
			diferencia = diaActual - diaInicial;
		else if (diaActual < diaInicial)
			diferencia = (diaActual + 7) - diaInicial;
		else
			return resultado.getTime();

		diferencia *= -1;
		resultado.add(Calendar.DAY_OF_YEAR, diferencia);
		return resultado.getTime();
	}

	@SuppressWarnings("unchecked")
	private String getPerfilValue(String perfilName) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' ";
			queryString = queryString.replace(":perfilName", perfilName);
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
		}
		
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
	
	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
	
	private Long getUsuario() {
		return (this.infoSesion != null) ? this.infoSesion.getAcceso().getUsuario().getId() : 1L;
	}
}
