package net.giro.ne.logica;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Regiones;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.dao.EmpresaDAO;
import net.giro.ne.dao.RegionesDAO;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;
import net.giro.publico.util.Errores;
import net.giro.respuesta.Respuesta;

@Stateless
public class SucursalesFac implements SucursalesRem {
	private static Logger log = Logger.getLogger(SucursalesFac.class);
	private InfoSesion infoSesion;
	@Resource
	private SucursalDAO ifzSucursales;
	private ConValoresDAO ifzConValores;
	private ColoniaDAO ifzColonias;
	private EmpresaDAO ifzEmpresas;
	private RegionesDAO ifzRegiones;
	private LocalidadDAO ifzLocalidades;
	private MunicipioDAO ifzMunicipios;
	private EstadoDAO ifzEstados;	
	private String modulo = "PUBLICO";
	
	public SucursalesFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzSucursales 	= (SucursalDAO) ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
			this.ifzConValores 	= (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			this.ifzColonias 	= (ColoniaDAO) ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
			this.ifzEmpresas 	= (EmpresaDAO) ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
			this.ifzRegiones 	= (RegionesDAO) ctx.lookup("ejb:/Model_Publico//RegionesImp!net.giro.ne.dao.RegionesDAO");
			this.ifzLocalidades = (LocalidadDAO) ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzMunicipios 	= (MunicipioDAO) ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
			this.ifzEstados 	= (EstadoDAO) ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto Logica_Publico.SucursalesFac, no se pudo crear ", e);
    	}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Sucursal entity) throws Exception {
		try {
			return ifzSucursales.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en SucursalesFac.save(Sucursal entity)", e);
			throw e;
		}
	}

	@Override
	public List<Sucursal> saveOrUpdateList(List<Sucursal> entities) throws Exception {
		try {
			return ifzSucursales.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en SucursalesFac.saveOrUpdateList(List<Sucursal> entities)", e);
			throw e;
		}
	}

	@Override
	public Respuesta salvar(Sucursal sucursal){
		Respuesta respuesta = new Respuesta();
		String res = "";
		
		try {
			if (sucursal.getColonia() == null) {
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_COLONIA);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (sucursal.getDescDomicilio1() != null && !"".equals(sucursal.getDescDomicilio1().trim()) && sucursal.getDomicilio1() != null)
				res += sucursal.getDomicilio1().getAtributo1() + " " + sucursal.getDescDomicilio1() + " ";
			if (sucursal.getDescDomicilio2() != null && !"".equals(sucursal.getDescDomicilio2().trim()) && sucursal.getDomicilio2() != null)
				res += sucursal.getDomicilio2().getAtributo1() + " " + sucursal.getDescDomicilio2() + " ";
			if (sucursal.getDescDomicilio3() != null && !"".equals(sucursal.getDescDomicilio3().trim()) && sucursal.getDomicilio3() != null)
				res += sucursal.getDomicilio3().getAtributo1() + " " + sucursal.getDescDomicilio3() + " ";
			if (sucursal.getDescDomicilio4() != null && !"".equals(sucursal.getDescDomicilio4().trim()) && sucursal.getDomicilio4() != null)
				res += sucursal.getDomicilio4().getAtributo1() + " " + sucursal.getDescDomicilio4() + " ";
			if (sucursal.getDescDomicilio5() != null && !"".equals(sucursal.getDescDomicilio5().trim()) && sucursal.getDomicilio5() != null)
				res += sucursal.getDomicilio5().getAtributo1() + " " + sucursal.getDescDomicilio5() + " ";
			sucursal.setDomicilio(res);
			
			if (sucursal.getId() == 0) {
				sucursal.setCreadoPor(infoSesion.getAcceso().getId());
				sucursal.setFechaCreacion(Calendar.getInstance().getTime());
				sucursal.setModificadoPor(infoSesion.getAcceso().getId());
				sucursal.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos la sucursal
				sucursal.setId(ifzSucursales.save(sucursal, getIdEmpresa()));
			} else {
				// Actualizamos la sucursal
				sucursal.setModificadoPor(infoSesion.getAcceso().getId());
				sucursal.setFechaModificacion(Calendar.getInstance().getTime());
				ifzSucursales.update(sucursal);
			}
			
			// Asignamos folio de facturacion y actualizamos la sucursal si corresponde 
			if (sucursal.getSerie() != null && ! "".equals(sucursal.getSerie())) {
				long folio = this.comprobarSecuenciaFolioFacturacion(sucursal);
				if (folio == 0)
					folio = this.getFolioFacturacion(sucursal);
				sucursal.setFolio(folio);
				ifzSucursales.update(sucursal);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoSucursal", sucursal);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.salvar", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_SUCURSAL);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	public void eliminar(Sucursal sucursal) throws Exception {
		try {
			ifzSucursales.delete(sucursal);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.eliminar(Sucursal sucursal)", e);
			throw e;
		}
	}
	
	public Sucursal findById(Long id) {
		try {
			return ifzSucursales.findById(id);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.findById(Long id)", e);
			throw e;
		}
	}
	
	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws Exception {
		try {
			return ifzSucursales.findLikePropiedad(propiedad, valor, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en SucursalesFac.findLikePropiedad(String propiedad, String valor)", e);
			throw e;
		}
	}
	
	public List<Sucursal> findLikeColumnName(String propiedad, String valor) {
		try {
			return findLikePropiedad(propiedad, valor);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.findLikePropiedad", e);
		}
		return null;
	}
	
	@Override
	public Respuesta buscarColonias(Localidad pojoLocalidad, String nombre){
		Respuesta respuesta = new Respuesta();
		try{
			List<Colonia> listColonias = ifzColonias.findByPropertyLikeValor("localidad", pojoLocalidad, "nombre", nombre);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listColonias", listColonias);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_COLONIAS);
			respuesta.setBody(null);
			log.error("Error en SucursalesFac.buscarColonias", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarLocalidades(Municipio pojoMunicipio, String valor) {
		Respuesta respuesta = new Respuesta();
		List<Localidad> listLocalidades = null;
				
		try{
			listLocalidades = ifzLocalidades.findByPropertyLikeValor("municipio", pojoMunicipio, "nombre", valor);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listLocalidades", listLocalidades);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_LOCALIDADES);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.buscarLocalidades", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre){
		Respuesta respuesta = new Respuesta();
		List<Municipio> listMunicipios = null; 
		
		try {
			listMunicipios = ifzMunicipios.findByPropertyLikeValor("estado", pojoEstado, "nombre", nombre);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMunicipios", listMunicipios);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.buscarMunicipios", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MUNICIPIOS);
			respuesta.setBody(null);
		}
		return respuesta;
	}

	@Override
	public List<Sucursal> buscarSucursales(String tipoBusqueda, String valorBusqueda) throws Exception {
		return ifzSucursales.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}

	@Override
	public List<ConValores> buscarDomicilios(HashMap<String, String> params) throws Exception {
		try {
			return ifzConValores.findByGrupoNombreByParams("SYS_DOMICILIOS", params);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.buscarMunicipios", e);
			throw e;
		}
	}

	@Override
	public List<Colonia> buscarColonia(String nombreColonia) throws Exception {
		return this.ifzColonias.findLikeColumnName("nombre", nombreColonia);
	}

	@Override
	public List<Empresa> buscarEmpresas(String busquedaEmpresa) throws Exception {
		return ifzEmpresas.findLikeColumnName("empresa", busquedaEmpresa);
	}

	@Override
	public List<Regiones> buscarRegiones(String busquedaRegiones) throws Exception {
		return ifzRegiones.findLikeColumnName("region", busquedaRegiones);
	}
	
	@Override
	public Respuesta cargarEstados(){
		Respuesta respuesta = new Respuesta();
		try{
			List<Estado> listEstados = ifzEstados.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstados);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_ESTADOS);
			respuesta.setBody(null);
			log.error("Error en SucursalesFac.cargarEstados");
		}
		return respuesta;
	}

	@Override
	public List<Sucursal> findAll() throws Exception {
		try {
			return this.findAll(null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Sucursal> findAll(String orderBy) throws Exception {
		try {
			return ifzSucursales.findAll(orderBy, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta folioFacturacion(Sucursal entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		long folio = 0;

		if (entity == null || entity.getId() <= 0) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FOLIO_FACTURACION);
			respuesta.getErrores().setCodigoError(-1);
			respuesta.getErrores().setDescError("Debe indicar una sucursal existente");
			respuesta.setBody(null);
			return respuesta;
		}
		
		if (entity.getSerie() == null || "".equals(entity.getSerie())){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FOLIO_FACTURACION);
			respuesta.getErrores().setCodigoError(-2);
			respuesta.getErrores().setDescError("La sucursal seleccionada no tiene serie asignada");
			respuesta.setBody(null);
			return respuesta;
		}
		
		// Asignamos folio y actualizamos sucursal
		folio = this.getFolioFacturacion(entity);
		entity.setFolio(folio);
		this.ifzSucursales.update(entity);
		
		respuesta.getErrores().setCodigoError(0L);
		respuesta.getBody().addValor("folioFacturacion", folio);
		respuesta.setReferencia(String.valueOf(folio));
		
		return respuesta;
	}

	// ----------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	private long comprobarSecuenciaFolioFacturacion(Sucursal entity) throws Exception {
		NQueryFac query = new NQueryFac();
		List<Object> lista = null;
		Iterator<Object> it = null;
		BigInteger val = BigInteger.ZERO;
		String secuencia = "seq_sucursal_folio_facturacion_";
		String queryString = "";
		
		try {
			secuencia += entity.getId() + "_serie_" + entity.getSerie().trim().toLowerCase();
			
			// Comprobamos la secuencia
			queryString = "select 1 from pg_class where relkind = 'S'  and relname = '" + secuencia + "'";
			lista = query.findNativeQuery(queryString);
			if (lista != null && ! lista.isEmpty()) {
				// Si existe, obtener el valor actual.
				queryString = "SELECT last_value FROM " + secuencia;
				lista = query.findNativeQuery(queryString);
				if (lista == null || lista.isEmpty())
					return 0;
				
				it = lista.iterator();
				while (it.hasNext()) {
					val = (BigInteger) it.next();
					val = (val != null) ? val : BigInteger.ZERO;
					return val.longValue();
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Publico.SucursalesFac.comprobarFolioFacturacion(Sucursal entity)", e);
		}
		
		return 0L;
	}

	@SuppressWarnings("unchecked")
	private long getFolioFacturacion(Sucursal entity) throws Exception {
		NQueryFac query = new NQueryFac();
		List<Object> lista = null;
		Iterator<Object> it = null;
		BigInteger val = BigInteger.ZERO;
		String secuencia = "seq_sucursal_folio_facturacion_";
		String queryString = "";
		long folio = 0;
		
		try {
			secuencia += entity.getId() + "_serie_" + entity.getSerie().trim().toLowerCase();
			
			// Si no existe creamos la secuencia
			queryString = "select 1 from pg_class where relkind = 'S'  and relname = '" + secuencia + "'";
			lista = query.findNativeQuery(queryString);
			if (lista == null || lista.isEmpty()) {
				queryString = "CREATE SEQUENCE " +  secuencia  + " INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;";
				query.ejecutaAccion(queryString);
				folio = 0;
			}
			
			if (folio <= 0) {
				queryString = "select nextval('" + secuencia + " ') AS folio ";
				lista = query.findNativeQuery(queryString);
				it = lista.iterator();
				while (it.hasNext()) {
					val = (BigInteger) it.next();
					val = (val != null) ? val : BigInteger.ZERO;
					folio = val.longValue();
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_Publico.SucursalesFac.folioFacturacion(Sucursal entity)", e);
			throw e;
		}
		
		return folio;
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}

// HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//   1.0   | 2016-10-19 | Javier Tirado      | Añadimos los metodos para generar el folio de facturacion