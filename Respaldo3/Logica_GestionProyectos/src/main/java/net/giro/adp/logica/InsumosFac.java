package net.giro.adp.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.adp.beans.InsumoRow;
import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.dao.InsumosDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.comun.TipoInsumo;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.respuesta.Respuesta;

@Stateless
public class InsumosFac implements InsumosRem {
	private static Logger log = Logger.getLogger(InsumosFac.class);
	private InitialContext ctx;	
	private ConvertExt convertidor;
	private String modulo = "INSUMOS";
	private InsumosDAO ifzInsumos;
	private ProductoRem ifzProductos;
	private ConGrupoValoresRem ifzGrupos;
	private ConValoresRem ifzConValores;
	private NQueryRem ifzQuery;
	private ConGrupoValores grupoUnidades;
	private InsumosDetallesExt detalleInsumo;
	private List<InsumosDetallesExt> listMateriales;
	private List<InsumosDetallesExt> listManoDeObra;
	private List<InsumosDetallesExt> listHerramientas;
	private List<InsumosDetallesExt> listEquipos;
	private List<InsumosDetallesExt> listOtros;
	private List<InsumoRow> listInsumos;
	private List<InsumoRow>	listNoEncontrados;
	private HashMap<String, String> layoutReference; 
	private boolean iniciaCategoria = false; 
	private InfoSesion infoSesion;
	
	public InsumosFac() {
		Hashtable<String, Object> environtment = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environtment);
            this.ifzInsumos = (InsumosDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//InsumosImp!net.giro.adp.dao.InsumosDAO");
            //this.ifzDetalles = (InsumosDetallesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
            this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
            this.ifzGrupos = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
            this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzQuery = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("InsumosFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear InsumosFac", e);
			ctx = null;
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
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Insumos entity) throws Exception {
		try {
			return this.ifzInsumos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Insumos> saveOrUpdateList(List<Insumos> entities) throws Exception {
		try {
			return this.ifzInsumos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en InsumosFac.saveOrUpdateList(List<Insumos> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Insumos entity) throws Exception {
		try {
			this.ifzInsumos.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idExplosionInsumos) throws Exception {
		try {
			return this.cancelar(idExplosionInsumos, false);
		} catch (Exception e) {
			log.error("Error en InsumosFac.cancelar(idExplosionInsumos)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelar(long idExplosionInsumos, boolean forzarCancelacion) throws Exception {
		Respuesta respuesta = new Respuesta();
		Insumos explosionInsumos = null;
		
		try {
			explosionInsumos = this.findById(idExplosionInsumos);
			if (explosionInsumos == null || explosionInsumos.getId() == null || explosionInsumos.getId() <= 0L) {
				respuesta.getErrores().setCodigoError(404L);
				respuesta.getErrores().setDescError("No se pudo recuperar La Explosion de Insumos indicada");
				return respuesta;
			}
			
			if (! forzarCancelacion) {
				if (validarSuministro(idExplosionInsumos)) {
					respuesta.getErrores().setCodigoError(1L);
					respuesta.getErrores().setDescError("La Explosion de Insumos tiene productos con suministro");
					return respuesta;
				}
				
				if (validarOrdenesCompra(explosionInsumos.getIdObra())) {
					respuesta.getErrores().setCodigoError(2L);
					respuesta.getErrores().setDescError("La Explosion de Insumos tiene Ordenes de Compra generadas");
					return respuesta;
				}
				
				if (validarCotizaciones(explosionInsumos.getIdObra())) {
					respuesta.getErrores().setCodigoError(3L);
					respuesta.getErrores().setDescError("La Explosion de Insumos tiene Cotizaciones generadas");
					return respuesta;
				}
			}
			
			// Cancelamos (Actualizamos el estatus) la Explosion de Insumos
			explosionInsumos.setEstatus(1);
			explosionInsumos.setModificadoPor(1L);
			explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.infoSesion != null)
				explosionInsumos.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(explosionInsumos);
			
			// Lanzamos evento de cambio de estatus de explosion de insumos
			bo_explosionInsumosEstatus(idExplosionInsumos);
			respuesta.getBody().addValor("explosionInsumos", explosionInsumos);
		} catch (Exception e) {
			log.error("Error en InsumosFac.cancelar(idExplosionInsumos, forzarCancelacion)", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al cancelar la Explosion de Insumos indicada");
		} 

		return respuesta;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(long idExplosionInsumos) throws Exception {
		try {
			this.ifzInsumos.delete(idExplosionInsumos);
		} catch (Exception e) {
			log.error("Error en InsumosFac.delete(idExplosionInsumos)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Insumos findById(long idExplosionInsumos) throws Exception {
		try {
			return this.ifzInsumos.findById(idExplosionInsumos);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findById(idExplosionInsumos)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findAll(long idObra, boolean incluyeCanceladas) throws Exception {
		try {
			return this.ifzInsumos.findAll(idObra, incluyeCanceladas);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findAll(idObra, incluyeCanceladas)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Insumos findActual(long idObra) throws Exception {
		try {
			return this.ifzInsumos.findActual(idObra);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findActual(idObra)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long findIdActual(long idObra) throws Exception {
		Insumos explosionInsumos = null; 
		
		try {
			explosionInsumos = this.findActual(idObra);
			if (explosionInsumos != null && explosionInsumos.getId() != null && explosionInsumos.getId() > 0L)
				return explosionInsumos.getId();
			return 0L;
		} catch (Exception e) {
			log.error("Error en InsumosFac.findIdActual(idObra)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.ifzInsumos.findByProperty(propertyName, value, incluyeCanceladas, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByProperty(propertyName, value, incluyeCanceladas, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findLike(String value, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			value = (value != null && ! "".equals(value.trim()) ? value.trim().replace(" ", "%") : "");
			return this.ifzInsumos.findLike(value, incluyeCanceladas, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLike(value, incluyeCanceladas, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), incluyeCanceladas, limite);
			if (value instanceof String)
				value = (value != null && ! "".equals(value.toString().trim()) ? value.toString().trim().replace(" ", "%") : "");
			return this.ifzInsumos.findLikeProperty(propertyName, value, incluyeCanceladas, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikeProperty(propertyName, value, incluyeCanceladas, limite)", e);
			throw e;
		}
	}

	@Override
	public Respuesta importar(byte[] fileSrc, String fileExtension, HashMap<String, String> insumosCellReference) throws Exception {
		Respuesta resp = new Respuesta();
		InsumosExt explosionInsumos = null;
		double montoTotal = 0;
		
		try {
			if (fileExtension == null || "".equals(fileExtension))
				fileExtension = "xls";
			
			// leemos el archivo anexo
			log.info("Leyendo archivo");
			resp = leerEXCEL(fileSrc, insumosCellReference);
			if (resp.getErrores().getCodigoError() != 0L)
				return resp;
			log.info(this.listInsumos.size() + " registros leidos");

			// Comprobamos y generamos las listas correpondientes de insumos
			log.info("Comprobamos registros");
			resp = this.comprobarInsumos();
			if (resp.getErrores().getCodigoError() != 0L)
				return resp;
			
			// Generamos el pojo de INSUMO con los totales de los detalles y total general.
			log.info("Generando Explosion Insumos");
			explosionInsumos = new InsumosExt();
			explosionInsumos.setCreadoPor(this.infoSesion.getAcceso().getId());
			explosionInsumos.setFechaCreacion(Calendar.getInstance().getTime());
			explosionInsumos.setModificadoPor(this.infoSesion.getAcceso().getId());
			explosionInsumos.setFechaModificacion(Calendar.getInstance().getTime());
			
			montoTotal = 0;
			for (InsumosDetallesExt var : this.listMateriales)
				montoTotal += var.getMonto().doubleValue();
			explosionInsumos.setMontoMateriales(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for (InsumosDetallesExt var : this.listManoDeObra)
				montoTotal += var.getMonto().doubleValue();
			explosionInsumos.setMontoManoDeObra(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for (InsumosDetallesExt var : this.listHerramientas)
				montoTotal += var.getMonto().doubleValue();
			explosionInsumos.setMontoHerramientas(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for (InsumosDetallesExt var : this.listEquipos)
				montoTotal += var.getMonto().doubleValue();
			explosionInsumos.setMontoEquipos(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for (InsumosDetallesExt var : this.listOtros)
				montoTotal += var.getMonto().doubleValue();
			explosionInsumos.setMontoOtros(new BigDecimal(montoTotal));
			
			explosionInsumos.calcularTotal(); //.setTotal(explosionInsumos.getTotalCalculado());

			// Generamos la respuesta
			resp.getBody().addValor("explosionInsumos", explosionInsumos);
			resp.getBody().addValor("materiales", this.listMateriales);
			resp.getBody().addValor("manoDeObra", this.listManoDeObra);
			resp.getBody().addValor("herramientas", this.listHerramientas);
			resp.getBody().addValor("equipos", this.listEquipos);
			resp.getBody().addValor("otros", this.listOtros);
			resp.getBody().addValor("productos", this.listNoEncontrados);
			log.info("Explosion de Insumos generada"
					+ "\n---> Registros (" + this.listInsumos.size() + ")"
					+ "\n---> Materiales (" + this.listMateriales.size() + ")"
					+ "\n---> Mano de Obra (" + this.listManoDeObra.size() + ")"
					+ "\n---> Herramientas (" + this.listHerramientas.size() + ")"
					+ "\n---> Equipos (" + this.listEquipos.size() + ")"
					+ "\n---> Otros (" + this.listOtros.size() + ")"
					+ "\n---> Sin procesar (" + this.listNoEncontrados.size() + ")");
		} catch (Exception e) {
			log.error("Error al leer el archivo XLS", e);
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			resp.getErrores().setDescError("Error al analizar el archivo de Explosion de Insumos");
		}
		
		return resp;
	}

	@Override
	public boolean comprobarInsumo(long idObra) {
		List<Insumos> explosionesInsumos = null; 
		try {
			explosionesInsumos = this.ifzInsumos.findAll(idObra, false);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar comprobar si la Obra indicada tiene Explosion de Insumos: " + idObra, e);
		}
		
		return (explosionesInsumos != null && ! explosionesInsumos.isEmpty());
	}

	@Override
	public int comprobarExplosionInsumos(long idObra) {
		Insumos explosionInsumos = null;
		
		try {
			if (idObra <= 0L)
				return 0;
			explosionInsumos = this.findActual(idObra);
			if (explosionInsumos == null || explosionInsumos.getId() == null || explosionInsumos.getId() <= 0L)
				return 0;
			if (explosionInsumos.getEstatus() == 0)
				return 1;
			if (explosionInsumos.getEstatus() == 1)
				return 2;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el estatus de la Explosion de Insumos de la Obra indicada: " + idObra, e);
		}
		
		return -1;
	}

	@Override
	public int estatusExplosionInsumos(long idExplosionInsumos) {
		Insumos explosionInsumos = null;
		
		try {
			explosionInsumos = this.findById(idExplosionInsumos);
			if (explosionInsumos != null && explosionInsumos.getId() != null && explosionInsumos.getId() > 0L)
				return explosionInsumos.getEstatus();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el estatus de la Explosion de Insumos indicada: " + idExplosionInsumos, e);
		}
		
		return -1;
	}

	@Override
	public List<Insumos> findByActivos(long idObra) throws Exception {
		try {
			return this.ifzInsumos.findAllActivos(idObra);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findAllActivos(idObra)", e);
			throw e;
		}
	}

	@Override
	public Insumos convertir(InsumosExt target) throws Exception {
		return this.convertidor.getPojo(target);
	}

	@Override
	public InsumosExt convertir(Insumos target) throws Exception {
		return this.convertidor.getExtendido(target);
	}

	// -------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------------------

	@Override
	public Long save(InsumosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(InsumosExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entityExt)", e);
			throw e;
		}
	}

	@Override
	public InsumosExt findByIdExt(long idExplosionInsumos) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findById(idExplosionInsumos));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByIdExt(idExplosionInsumos)", e);
			throw e;
		}
	}

	@Override
	public InsumosExt findExtActual(long idObra) throws Exception {
		try {
			return this.convertidor.getExtendido(this.findActual(idObra));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findExtActual(idObra)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findExtAll(long idObra, boolean incluyeCanceladas) throws Exception {
		List<InsumosExt> extendidos = new ArrayList<InsumosExt>();
		List<Insumos> lista = null;
		
		try {
			lista = this.findAll(idObra, incluyeCanceladas);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (Insumos var : lista)
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findAll(idObra, incluyeCanceladas)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<InsumosExt> findExtByActivos(long idObra) throws Exception {
		List<InsumosExt> extendidos = new ArrayList<InsumosExt>();
		List<Insumos> lista = null;
		
		try {
			lista = this.findByActivos(idObra);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (Insumos var : lista)
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findExtByActivos(idObra)", e);
			throw e;
		}
		
		return extendidos;
	}
	
	@Override
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByPropertyExt(propertyName, value, false, limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception {
		List<InsumosExt> extendidos = new ArrayList<InsumosExt>();
		List<Insumos> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value, incluyeCanceladas, limite);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (Insumos var : lista)
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, incluyeCanceladas, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public List<InsumosExt> findLikeExt(String value, boolean incluyeCanceladas, int limite) throws Exception {
		List<InsumosExt> extendidos = new ArrayList<InsumosExt>();
		List<Insumos> lista = null;
		
		try {
			lista = this.findLike(value, incluyeCanceladas, limite);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (Insumos var : lista)
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikeExt(value, incluyeCanceladas, limite)", e);
			throw e;
		}
		
		return extendidos;
	}
	
	@Override
	public List<InsumosExt> findLikePropertyExt(String propertyName, Object value, int limite) throws Exception {
		try {
			return findLikePropertyExt(propertyName, value, false, limite);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findLikePropertyExt(String propertyName, Object value, boolean incluyeCanceladas, int limite) throws Exception {
		List<InsumosExt> extendidos = new ArrayList<InsumosExt>();
		List<Insumos> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value, incluyeCanceladas, limite);
			if (lista == null || lista.isEmpty())
				return extendidos;
			for (Insumos var : lista)
				extendidos.add(this.convertidor.getExtendido(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, incluyeCanceladas, limite)", e);
			throw e;
		}
		
		return extendidos;
	}

	// ----------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	private Respuesta saveOrUpdate(Insumos explosionInsumos, List<InsumosDetalles> detalles) {
		Respuesta respuesta = new Respuesta();
		Long idExplosionInsumos = 0L;
		
		try {
			if (explosionInsumos == null || detalles == null || detalles.isEmpty()) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico la Explosion de Insumos o esta vacia");
				return null;
			}
			
			idExplosionInsumos = explosionInsumos.getId();
			if (idExplosionInsumos == null || idExplosionInsumos <= 0L)
				idExplosionInsumos = this.save(explosionInsumos);
			else
				this.update(explosionInsumos);

			explosionInsumos.setId(idExplosionInsumos);
			for (InsumosDetalles detalle : detalles)
				detalle.setIdInsumo(idExplosionInsumos);
			
			//this.ifzDetalles.setInfoSesion(this.infoSesion);
			//detalles = this.ifzDetalles.saveOrUpdateList(detalles);
			
			// Lanzamos evento de cambio de estatus de explosion de insumos
			bo_explosionInsumosEstatus(idExplosionInsumos);
			
			respuesta.getBody().addValor("explosionInsumos", explosionInsumos);
			respuesta.getBody().addValor("detalles", detalles);
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar la Explosion de Insumos", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("No indico la Explosion de Insumos o esta vacia");
		}
		
		return respuesta;
	}

	@SuppressWarnings("unused")
	private Respuesta saveOrUpdateExt(InsumosExt explosionInsumos, List<InsumosDetallesExt> detalles) throws Exception {
		Respuesta respuesta = new Respuesta();
		Long idExplosionInsumos = 0L;
		
		try {
			if (explosionInsumos == null || detalles == null || detalles.isEmpty()) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico la Explosion de Insumos o esta vacia");
				return null;
			}
			
			idExplosionInsumos = explosionInsumos.getId();
			if (idExplosionInsumos == null || idExplosionInsumos <= 0L)
				idExplosionInsumos = this.save(explosionInsumos);
			else
				this.update(explosionInsumos);
			explosionInsumos.setId(idExplosionInsumos);
			for (InsumosDetallesExt detalle : detalles)
				detalle.setIdInsumos(explosionInsumos);
			
			//this.ifzDetalles.setInfoSesion(this.infoSesion);
			//detalles = this.ifzDetalles.saveOrUpdateListExt(detalles);
			
			// Lanzamos evento de cambio de estatus de explosion de insumos
			bo_explosionInsumosEstatus(idExplosionInsumos);
			
			respuesta.getBody().addValor("explosionInsumos", explosionInsumos);
			respuesta.getBody().addValor("detalles", detalles);
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar la Explosion de Insumos", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("No indico la Explosion de Insumos o esta vacia");
		}
		
		return respuesta;
	}

	private void bo_explosionInsumosEstatus(Long idExplosionInsumos) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idExplosionInsumos == null || idExplosionInsumos <= 0L)
				return;
			
			target = idExplosionInsumos.toString();
			
			msgTopic = new MensajeTopic(TopicEventosGP.INSUMOS_ESTATUS, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/GP:INSUMOS_ESTATUS\n\n" + comando + "\n\n", e);
		}
	}
	
	private Respuesta leerEXCEL(byte[] fileSrc, HashMap<String, String> layoutReference) throws Exception {
		Respuesta respuesta = new Respuesta();
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		InsumoRow itemInsumo = null;
		int indexRow = 0;	
		TipoInsumo tipoInsumo = TipoInsumo.Ninguno;
		int tipoNivel = 0;
		
		try {
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			this.listInsumos = new ArrayList<InsumoRow>();
			this.layoutReference = layoutReference; 
			this.iniciaCategoria = true;
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				itemInsumo = new InsumoRow();
				row = rowIterator.next();
				
				if (! validaDatos(row)) {
					tipoInsumo = determinaTipoInsumo(row, tipoInsumo, tipoNivel);
					continue;
				}

				// TIPO DE INSUMO
				itemInsumo.setTipo(tipoInsumo.ordinal());

				// CODIGO o SECCION
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
				itemInsumo.setClave(celda.getStringCellValue().trim().toUpperCase());
				
				// DESCRIPCION
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("DESCRIPCION")));
				itemInsumo.setDescripcion(celda.getStringCellValue().trim().toUpperCase());
				
				// UNIDAD DE MEDIDA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("UM")));
				itemInsumo.setUnidad(celda.getStringCellValue().trim().toUpperCase());

				// FAMILIA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("FAMILIA")));
				if (celda != null && ! "".equals(celda.getStringCellValue().trim()))
					itemInsumo.setFamilia(celda.getStringCellValue().trim().toUpperCase());
				
				// CANTIDAD
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CANTIDAD")));
				itemInsumo.setCantidad(celda.getNumericCellValue());
				
				// PRECIO UNITARIO
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PU")));
				itemInsumo.setCostoUnitario(celda.getNumericCellValue());
				
				// IMPORTE
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("IMPORTE")));
				itemInsumo.setMonto(celda.getNumericCellValue());
				
				// PORCENTAJE
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PORCENTAJE")));
				if (celda != null && celda.getCellType() == Cell.CELL_TYPE_NUMERIC)
					itemInsumo.setPorcentaje(celda.getNumericCellValue());
				
				// MONEDA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("MONEDA")));
				if (celda != null && ! "".equals(celda.getStringCellValue().trim()))
					itemInsumo.setMoneda(celda.getStringCellValue().trim().toUpperCase());
				
				// Añadimos el item a la lista
				this.listInsumos.add(itemInsumo);
			}
		} catch (Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, 1L);
			if (e.getMessage().contains("{NO_UNIDAD}"))
				respuesta.getErrores().setDescError("Error en formato de Explosion de Insumos.\n\nFalta informacion para algunos productos:\n" + e.getMessage().replace("{NO_UNIDAD}", ""));
			else
				respuesta.getErrores().setDescError("Error en formato de Explosion de Insumos");
			log.error("Error al leer el formato de Explosion de Insumos.  Comprobo hasta el renglon " + (indexRow + 1), e);
		}
		
		return respuesta;
	}
	
	private Respuesta comprobarInsumos() {
		Respuesta resp = new Respuesta();
		InsumoRow current = null;
		
		try {
			this.listMateriales = new ArrayList<InsumosDetallesExt>();
			this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
			this.listHerramientas = new ArrayList<InsumosDetallesExt>();
			this.listEquipos = new ArrayList<InsumosDetallesExt>();
			this.listOtros = new ArrayList<InsumosDetallesExt>();
			this.listEquipos = new ArrayList<InsumosDetallesExt>();
			this.listNoEncontrados = new ArrayList<InsumoRow>();
			
			for (InsumoRow var : this.listInsumos) {
				current = var;
				this.detalleInsumo = getInsumoDetalleFromInsumoRow(var);
				if (this.detalleInsumo == null) {
					this.listNoEncontrados.add(var);

					// Generamos nuestro detalle: solo para referencia
					this.detalleInsumo = new InsumosDetallesExt();
					this.detalleInsumo.setNombreProducto(var.getClave() + " - " + var.getDescripcion());
					this.detalleInsumo.setCantidad(var.getCantidad());
					this.detalleInsumo.setPrecioUnitario(var.getCostoUnitario());
					this.detalleInsumo.setMonto(new BigDecimal(var.getMonto()));
					this.detalleInsumo.setPorcentaje(var.getPorcentaje());
					this.detalleInsumo.setTipo(var.getTipo());
				} 
				
				if(var.getTipo() == TipoInsumo.Material.ordinal())
					this.listMateriales.add(this.detalleInsumo);
				else if(var.getTipo() == TipoInsumo.ManoDeObra.ordinal())
					this.listManoDeObra.add(this.detalleInsumo);
				else if(var.getTipo() == TipoInsumo.Herramienta.ordinal())
					this.listHerramientas.add(this.detalleInsumo);
				else if(var.getTipo() == TipoInsumo.Equipo.ordinal())
					this.listEquipos.add(this.detalleInsumo);
				else 
					this.listOtros.add(this.detalleInsumo);
			}
		} catch (Exception e) {
			log.error("Error comprobando la Explosion de Insumos en Logica_GestionProyectos.InsumosFac.comprobarInsumos con: " + current.toString(), e);
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			resp.getErrores().setDescError("Error comprobando la Explosion de Insumos");
		}
		
		return resp;
	}
	
	@SuppressWarnings("unused")
	private HashMap<String,String> procesaLayout(HashMap<String,String> layout) {
		HashMap<String,String> resultado = new HashMap<String, String>();
		String[] sections = null;
		String[] parts = null;
		String seccion = "";
		String column = "";
		//---------------------------------
		Pattern pattern = null;
		Matcher matcher = null;
		String expregRangoVertical = "(^[1-9]+$)|(^[a-zA-Z]{1,2}$)|(^([a-zA-Z]{1,2})([\\\\{]{1})([\\w_$,]*)([\\\\}]{1})([\\\\:]?)([\\w_$]*)$)";
		String expCelda = "(^[a-zA-Z]{1,2}$)|(^[a-zA-Z]{1,2}[1-9]*$)";
		int groupMatch = 0;
		
		for (Entry<String,String> rangoItem : layout.entrySet()) {
			sections = rangoItem.getValue().split("\\|");
			seccion = sections[0];
			
			// Validamos rango vertical y lo procesamos
			pattern = Pattern.compile(expregRangoVertical);
			matcher = pattern.matcher(seccion);
			if (! matcher.find())
				return null;
			
			if (matcher.group(1) != null && matcher.group(1).equals(matcher.group(0))) {
				
			} else if (matcher.group(2) != null && matcher.group(2).equals(matcher.group(0))) {
				
			} else if (matcher.group(3) != null && matcher.group(3).equals(matcher.group(0))) {
				
			} else {
				
			}
			
			
			if (seccion.contains("{")) { // Rango vertical
				parts = seccion.split("\\:");
				seccion = parts[0];
				column = seccion.substring(0, seccion.indexOf("{"));
				seccion = seccion.substring(seccion.indexOf("{")).replace("{", "").replace("}", "");
				parts = seccion.split("\\,");
				
			}
		}
		
		return resultado;
	}
	
	private InsumosDetallesExt getInsumoDetalleFromInsumoRow(InsumoRow value) {
		InsumosDetallesExt result = new InsumosDetallesExt();
		List<ConValores> listUnidades = null;
		Producto pojoProducto = null;

		this.ifzProductos.setInfoSesion(this.infoSesion);
		pojoProducto = this.ifzProductos.findByClave(value.getClave());
		if (pojoProducto == null)
			return null; 
		
		if (pojoProducto.getUnidadMedida() <= 0) {
			try {
				if (this.grupoUnidades == null) 
					this.grupoUnidades = this.ifzGrupos.findByName("SYS_UNIDAD_MEDIDA");
				
				if (this.grupoUnidades != null) {
					// Localizamos la UNIDAD DE MEDIDA
					listUnidades = this.ifzConValores.findByProperty("valor", value.getUnidad(), this.grupoUnidades, 0);
					if (listUnidades != null && ! listUnidades.isEmpty()) {
						// Actualizamos UNIDAD DE MEDIDA en el producto
						pojoProducto.setUnidadMedida(listUnidades.get(0).getId());
						pojoProducto.setDescUnidadMedida(listUnidades.get(0).getDescripcion());
						this.ifzProductos.update(pojoProducto);
					}
				}
			} catch (Exception e) {
				log.error("ERROR INTERNO - No pude actualizar la Unidad de Medida", e);
			}
		}
		
		result.setIdProducto(this.ifzProductos.convertir(pojoProducto));
		result.setNombreProducto(value.getDescripcion());
		result.setCantidad(value.getCantidad());
		result.setPrecioUnitario(value.getCostoUnitario());
		result.setMonto(new BigDecimal(value.getMonto()));
		result.setPorcentaje(value.getPorcentaje());
		result.setTipo(value.getTipo());
		
		result.setCreadoPor(this.infoSesion.getAcceso().getId());
		result.setFechaCreacion(Calendar.getInstance().getTime());
		result.setModificadoPor(this.infoSesion.getAcceso().getId());
		result.setFechaModificacion(Calendar.getInstance().getTime());
		
		return result;
	}	

	private int getColumnIndex(String cellHeader) {
		return CellReference.convertColStringToIndex(cellHeader);
	}
	
	private boolean validaDatos(Row row) throws Exception {
		HashMap<String, String> excluyentes = new HashMap<String, String>();
		FormulaEvaluator eval = null;
		Cell celda = null;
		CellValue cellValue = null;
		String cadena = "";
		
		excluyentes.put("MATERIALES", "TOTAL_DE_MATERIALES");
		excluyentes.put("MANO_DE_OBRA", "TOTAL_DE_MANO_DE_OBRA");
		excluyentes.put("HERRAMIENTA", "HERRAMIENTA");
		excluyentes.put("EQUIPO", "TOTAL_EQUIPO");
		excluyentes.put("AUXILIARES", "TOTAL_AUXILIARES");
		eval = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				
		// Validacion de codigo
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
		if (celda == null) 
			return false;
		cadena = celda.getStringCellValue().trim().toUpperCase();
		if ("".equals(cadena.trim()))
			return false; 
		if (excluyentes.containsKey(cadena.trim().replace(" ", "_").toUpperCase()))
			return false; 
		
		// Validacion de descripcion
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("DESCRIPCION")));
		if (celda == null) 
			return false;
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim()))
			return false;
		
		// Validacion de unidad de medida
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("UM")));
		if (celda == null) {
			celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
			cadena = celda.getStringCellValue().trim();
			throw new Exception("{NO_UNIDAD}El producto " + cadena + " no tiene asignada UNIDAD DE MEDIDA");
		}
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim())) {
			celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
			cadena = celda.getStringCellValue().trim();
			throw new Exception("{NO_UNIDAD}El producto " + cadena + " no tiene asignada UNIDAD DE MEDIDA");
		}
		
		/*
		 * NO SE VALIDAN LOS DEMAS CAMPOS PARA QUE LO DE POR BUENO Y ROMPA EL FLUJO CUANDO
		 * INTENTE RECUPERAR UN DATO Y ESTE INCORRECTO: FORMATO, VACIO, ETC. 
		*/
		
		// CANTIDAD
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CANTIDAD")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;

		// PRECIO UNITARIO
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PU")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;
		
		// IMPORTE
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("IMPORTE")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean validarSuministro(Long idExplosionInsumos) {
		List<Object> nativeResult = null;
		String queryString = "";
		//-------------------------------------------------
		double resultados = 0;
		
		try {
			queryString += "select coalesce(sum(b.cantidad_surtida), 0) as suministrado ";
			queryString += "from insumos_presupuesto a inner join insumos_detalles b on b.id_insumo = a.id ";
			queryString += "where a.id = :idExplosionInsumos and b.tipo <= 1 ";
			queryString = queryString.replace(":idExplosionInsumos", idExplosionInsumos.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty()) 
				resultados = ((Double) nativeResult.get(0)).doubleValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el Suministro de la Explosion de Insumos " + idExplosionInsumos, e);
			resultados = 0;
		}
		
		return (resultados > 0);
	}
	
	@SuppressWarnings("unchecked")
	private boolean validarOrdenesCompra(Long idObra) {
		List<Object> nativeResult = null;
		String queryString = "";
		//-------------------------------------------------
		int resultados = 0;
		
		try {
			queryString += "select count(id) as ordenes from orden_compra ";
			queryString += "where id_cotizacion in (select id from cotizacion where id_obra = :idObra and id_requisicion <= 0 and estatus <> 1) and estatus <> 1 ";
			queryString = queryString.replace(":idObra", idObra.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty()) 
				resultados = ((BigInteger) nativeResult.get(0)).intValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar las Ordenes de Compra generadas para la Explosion de Insumos de la Obra " + idObra, e);
			resultados = 0;
		}
		
		return (resultados > 0);
	}

	@SuppressWarnings("unchecked")
	private boolean validarCotizaciones(Long idObra) {
		List<Object> nativeResult = null;
		String queryString = "";
		//-------------------------------------------------
		int resultados = 0;
		
		try {
			queryString += "select count(id) as cotizaciones from cotizacion ";
			queryString += "where id_obra = :idObra and id_requisicion <= 0 and estatus <> 1 ";
			queryString = queryString.replace(":idObra", idObra.toString());
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty()) 
				resultados = ((BigInteger) nativeResult.get(0)).intValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar las Cotizaciones generadas para la Explosion de Insumos de la Obra " + idObra, e);
			resultados = 0;
		}
		
		return (resultados > 0);
	}

	private TipoInsumo determinaTipoInsumo(Row row, TipoInsumo tipoActual, int tipoNivel) {
		HashMap<String, TipoInsumo> map = new HashMap<String, TipoInsumo>();
		Cell celda = null;
		String cadena = "";
		
		map.put("MATERIALES", TipoInsumo.Material);
		map.put("TOTAL_DE_MATERIALES", TipoInsumo.Material);
		map.put("MANO_DE_OBRA", TipoInsumo.ManoDeObra);
		map.put("TOTAL_DE_MANO_DE_OBRA", TipoInsumo.ManoDeObra);
		map.put("HERRAMIENTA", TipoInsumo.Herramienta);
		map.put("TOTAL_HERRAMIENTA", TipoInsumo.Herramienta);
		map.put("EQUIPO", TipoInsumo.Equipo);
		map.put("TOTAL_EQUIPO", TipoInsumo.Equipo);
		
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
		if (celda == null) {
			if (this.iniciaCategoria && tipoActual == TipoInsumo.Ninguno) {
				this.iniciaCategoria = false;
				tipoActual = TipoInsumo.Material;
			}
			return tipoActual;
		}
		
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim()))
			return tipoActual;
		
		if (this.iniciaCategoria && tipoActual == TipoInsumo.Ninguno) {
			this.iniciaCategoria = false;
			tipoActual = TipoInsumo.Material;
			return tipoActual;
		}
		
		cadena = cadena.trim().toUpperCase().replace(" ", "_");
		if (map.containsKey(cadena))
			tipoActual = map.get(cadena); 
		else
			tipoActual = TipoInsumo.Otros;
		
		if (cadena.contains("TOTAL_"))
			this.iniciaCategoria = true;
		
		return tipoActual;
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

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSION |    FECHA   | 			 AUTOR 			 | DESCRIPCI\D3N 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.1	| 03/04/2017 | Javier Tirado 			 | Modifico el metodo leerExcel. Lee el archivo en base a un layout.
 *    2.1	| 03/04/2017 | Javier Tirado 			 | Añado los metodos convertir. Convierte el pojo de insumos a extendido y viceversa
 */