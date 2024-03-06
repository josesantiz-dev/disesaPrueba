package net.giro.adp.logica;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.beans.ObraCobranzaHistorico;
import net.giro.adp.dao.ObraCobranzaDAO;
import net.giro.adp.dao.ObraCobranzaHistoricoDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosGP;

@Stateless
public class ObraCobranzaFac implements ObraCobranzaRem {
	private static Logger log = Logger.getLogger(ObraCobranzaFac.class);
	private InfoSesion infoSesion;
	private ObraCobranzaDAO ifzBase;
	private ObraCobranzaHistoricoDAO ifzHistorico;
	private NQueryRem ifzQuery;
	
	public ObraCobranzaFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try{
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ObraCobranzaDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraCobranzaImp!net.giro.adp.dao.ObraCobranzaDAO");
			this.ifzHistorico = (ObraCobranzaHistoricoDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraCobranzaHistoricoImp!net.giro.adp.dao.ObraCobranzaHistoricoDAO");
			this.ifzQuery = (NQueryRem)	ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraCobranzaFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ObraCobranza entity) throws Exception {
		try {
			return this.ifzBase.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.save(ObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranza> saveOrUpdateList(List<ObraCobranza> entities) throws Exception {
		try {
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraCobranza entity) throws Exception {
		try {
			this.ifzBase.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.update(ObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idObraCobranza) throws Exception {
		ObraCobranza entity = null;
		ObraCobranzaHistorico historico = null;
		
		try {
			if (idObraCobranza == null || idObraCobranza <= 0L)
				return;
			
			entity = this.findById(idObraCobranza);
			if (entity != null) {
				// Añadimos a historico
				historico = new ObraCobranzaHistorico();
				historico.fromCobranza(entity);
				if (this.infoSesion != null)
					historico.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				this.ifzHistorico.save(historico, getCodigoEmpresa());
			}
			
			// borramos
			this.ifzBase.delete(idObraCobranza);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.delete(idObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public void deleteAll(List<ObraCobranza> entities) throws Exception {
		List<ObraCobranzaHistorico> historicos = null;
		ObraCobranzaHistorico historico = null;
		
		try {
			if (entities == null || entities.isEmpty())
				return;
			
			historicos = new ArrayList<ObraCobranzaHistorico>();
			for (ObraCobranza entity : entities) {
				// Añadimos a historico
				historico = new ObraCobranzaHistorico();
				historico.fromCobranza(entity);
				if (this.infoSesion != null)
					historico.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				historicos.add(historico);
				// borramos
				this.ifzBase.delete(entity.getId());
			}
			
			if (historicos != null && ! historicos.isEmpty())
				this.ifzHistorico.saveOrUpdateList(historicos, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.deleteAll(entities)", e);
			throw e;
		}
	}

	@Override
	public ObraCobranza findById(Long idObraCobranza) throws Exception {
		try {
			return this.ifzBase.findById(idObraCobranza);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findById(idObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranza> findAll(Long idObra, String orderBy, int limite) throws Exception {
		List<ObraCobranza> cobranza = null;
		
		try {
			cobranza = this.ifzBase.findAll(idObra, orderBy, limite);
			cobranza = (cobranza != null ? cobranza : new ArrayList<ObraCobranza>());
			cobranza = validarFacturas(cobranza);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findAll(idObra, orderBy, limite)", e);
			throw e;
		}
		
		return cobranza;
	}

	@Override
	public List<ObraCobranza> findByProperty(String propertyName, Object value, Long idObra, String orderBy, int limite) throws Exception {
		try {
			return this.ifzBase.findByProperty(propertyName, value, idObra, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByProperty(propertyName, value, idObra,orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraCobranza> findLikeProperty(String propertyName, Object value, Long idObra, String orderBy, int limite) throws Exception {
		try {
			return this.ifzBase.findLikeProperty(propertyName, value, idObra, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findLikeProperty(propertyName, value, idObra, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ObraCobranza> findByFactura(long idFactura) throws Exception {
		List<ObraCobranza> cobranza = null;
		
		try {
			//cobranza = this.findByProperty("idFactura", idFactura, "", 0);
			cobranza = this.ifzBase.findBySpecific(0L, idFactura, 0L, getIdEmpresa(), null, 0);
			return (cobranza != null ? cobranza : new ArrayList<ObraCobranza>());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByFactura(idFactura)", e);
			throw e;
		} 
	}

	@Override
	public void actualizarCobranza(Long idObra, List<ObraCobranza> entities) throws Exception {
		MensajeTopic msgTopic = null;
		String target = "";
		String referencia = "";
		String atributos = "";
		// --------------------------------------------
		Type tipo = null;
		Gson gson = null;
		
		try {
			idObra = (idObra != null ? idObra : 0L);
			if (entities == null || entities.isEmpty())
				return;
			
			gson = new Gson();
			tipo = new TypeToken<List<ObraCobranza>>() {}.getType();
			atributos = gson.toJson(entities, tipo);
			target = (idObra != null ? idObra.toString() : "0");
			atributos = (atributos != null ? atributos : "");
			
			msgTopic = new MensajeTopic(TopicEventosGP.COBRANZA_ACTUALIZAR, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al autoenviar mensaje JMS\n" + this.getClass().getCanonicalName() + ".autoMessage(evento, target, referencia, atributos)", e);
		} 
	}
	
	/*@Override
	public ObraCobranza comprobarFactura(Long idObra, Long idFactura) throws Exception {
		List<ObraCobranza> lista = null;
		
		try {
			idObra = (idObra != null && idObra > 0 ? idObra : 0L);
			idFactura = (idFactura != null && idFactura > 0 ? idFactura : 0L);
			lista = this.ifzBase.findBySpecific(idObra, idFactura, 0L, getIdEmpresa(), null, 0);
			return (lista != null && ! lista.isEmpty() ? lista.get(0) : null);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.comprobarConcepto(idObra, idFactura)", e);
			throw e;
		}
	}

	@Override
	public ObraCobranza comprobarConcepto(Long idObra, Long idFactura, Long idConcepto) throws Exception {
		List<ObraCobranza> lista = null;
		
		try {
			idObra = (idObra != null && idObra > 0 ? idObra : 0L);
			idFactura = (idFactura != null && idFactura > 0 ? idFactura : 0L);
			idConcepto = (idConcepto != null && idConcepto > 0 ? idConcepto : 0L);
			lista = this.ifzBase.findBySpecific(idObra, idFactura, idConcepto, getIdEmpresa(), null, 0);
			return (lista != null && ! lista.isEmpty() ? lista.get(0) : null);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.comprobarConcepto(idObra, idFactura, idConcepto)", e);
			throw e;
		}
	}*/

	// -----------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------------------------
	
	private List<ObraCobranza> validarFacturas(List<ObraCobranza> cobranza) {
		List<ObraCobranza> borrables = null;
		List<Long> facturas = null;
		String lista = null;
		
		try {
			if (cobranza ==  null || cobranza.isEmpty())
				return cobranza;
			
			facturas = new ArrayList<Long>();
			for (ObraCobranza factura : cobranza) 
				facturas.add(factura.getIdFactura());
			
			// Validacion de Facturas
			borrables = new ArrayList<ObraCobranza>();
			lista = StringUtils.join(facturas, ",");
			lista = validarFacturas(lista);
			for (ObraCobranza factura : cobranza) {
				if (! lista.contains(factura.getIdFactura().toString())) {
					borrables.add(factura);
					cobranza.remove(factura);
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar las Facturas para Cobranza", e);
		}
		
		return cobranza;
	}

	@SuppressWarnings("unchecked")
	private String validarFacturas(String facturas) {
		List<Object> rows = null;
		String queryString = "";
		
		try {
			if (facturas == null || "".equals(facturas.trim()))
				return "";
			queryString = "select string_agg(cast(id as varchar), ',') as lista from factura where tipo_comprobante = 'I' and estatus = 1 and id in (:lista);";
			queryString = queryString.replace(":lista", facturas);
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				facturas = rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el Tipo de Comprobante y Estatus de las Facturas indicadas: " + facturas, e);
		}
		
		return facturas;
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaFac