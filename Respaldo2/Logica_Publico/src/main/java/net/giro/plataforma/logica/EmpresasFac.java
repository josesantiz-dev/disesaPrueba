package net.giro.plataforma.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.dao.EmpresaDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.MonedaTYGExt;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.respuesta.Respuesta;
import net.giro.publico.util.Errores;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

import org.apache.log4j.Logger;

@Stateless
public class EmpresasFac implements EmpresasRem{
	private static Logger log = Logger.getLogger(EmpresasFac.class);
	
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private EmpresaDAO ifzEmpresas;
	private ConValoresDAO ifzConValores;
	private MonedaDAO ifzMonedas;
	private static String orderBy;
	private String modulo = "PUBLICO";
	
	
	public EmpresasFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		ifzEmpresas 	= (EmpresaDAO) ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
    		ifzConValores 	= (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		ifzMonedas 		= (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto , no se pudo crear", e);
    		ctx = null;
		}
	}

	
	@Override
	public void orderBy(String orderBy) { EmpresasFac.orderBy = orderBy; }

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Empresa entity) throws ExcepConstraint {
		try {
			return this.ifzEmpresas.save(entity, null);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.save(Empresa)", e);
			throw e;
		}
	}

	@Override
	public void update(Empresa entity) throws ExcepConstraint {
		try {
			this.ifzEmpresas.update(entity);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.update(Empresa)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzEmpresas.delete(entityId);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Empresa findById(Long id) {
		try {
			return this.ifzEmpresas.findById(id);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findById(id)", e);
			throw e;
		}
	}
	
	@Override
	public List<Empresa> findAll() throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findAll();
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findAll()", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Empresa> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Empresa> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Empresa> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Empresa> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Empresa> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpresas.orderBy(orderBy);
			return this.ifzEmpresas.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en MODULO.EmpresaFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	public List<Empresa> findByPropertyLike(String propertyName, String value) {
		try {
			return ifzEmpresas.findLikeColumnName(propertyName, value);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Respuesta buscarDomicilios(HashMap<String, String> params) {
		Respuesta respuesta = new Respuesta();
		List<ConValores> listValores = null;
		
		try {
			listValores = ifzConValores.findByGrupoNombreByParams("SYS_DOMICILIOS", params);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listValores", listValores);
		} catch (Exception e) {
			log.error("Error en EmpresasFac.buscarDomicilios", e);
			throw e;
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarEmpresas(String tipo, String valor) {
		Respuesta respuesta = new Respuesta();
		List<Empresa> listEmpresas = null;
		
		try {
			ifzEmpresas.orderBy(orderBy);
			listEmpresas = ifzEmpresas.findLikeProperty(tipo, valor, 0);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEmpresas", listEmpresas);
		} catch (Exception e) {
			log.error("Error en EmpresasFac.buscarEmpresas", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_EMPRESAS);
			respuesta.setBody(null);
		} finally {
			orderBy = null;
		}

		return respuesta;
	}
	
	@Override
	public Respuesta cargarMonedas(){
		Respuesta respuesta = new Respuesta();
		List<MonedaTYGExt> listMonedasExt = new ArrayList<MonedaTYGExt>();
		List<Moneda> listMonedas = null;
		
		try {
			listMonedas = ifzMonedas.findAll();
			for (Moneda pojoMoneda : listMonedas) {
				listMonedasExt.add(convertMonedaToMonedaTYGExt(pojoMoneda));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMonedas", listMonedasExt);
		} catch (Exception e) {
			log.error("Error en EmpresasFac.cargarMonedas");
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_MONEDAS);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarEmpresa(Empresa pojoEmpresa) {
		Respuesta respuesta = new Respuesta();
		
		try {
			ifzEmpresas.delete(pojoEmpresa);
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			log.error("Error en EmpresasFac.eliminarEmpresa");
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_EMPRESAS);
			respuesta.setBody(null);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta guardarEmpresa(Empresa pojoEmpresa){
		Respuesta respuesta = new Respuesta();
		String domicilio = "";
		
		try {
			if (pojoEmpresa.getDomicilio1() != null && !"".equals(pojoEmpresa.getDescDomicilio1().trim())){
				pojoEmpresa.setDomicilio1(ifzConValores.findById(pojoEmpresa.getDomicilio1().getId()));
				domicilio += pojoEmpresa.getDomicilio1().getAtributo1() + " " + pojoEmpresa.getDescDomicilio1() + " ";
			}
			
			if (pojoEmpresa.getDomicilio2() != null && !"".equals(pojoEmpresa.getDescDomicilio2().trim())){
				pojoEmpresa.setDomicilio2(ifzConValores.findById(pojoEmpresa.getDomicilio2().getId()));
				domicilio += pojoEmpresa.getDomicilio2().getAtributo1() + " " + pojoEmpresa.getDescDomicilio2() + " ";
			}
			
			if (pojoEmpresa.getDomicilio3() != null && !"".equals(pojoEmpresa.getDescDomicilio3().trim())){
				pojoEmpresa.setDomicilio3(ifzConValores.findById(pojoEmpresa.getDomicilio3().getId()));
				domicilio += pojoEmpresa.getDomicilio3().getAtributo1() + " " + pojoEmpresa.getDescDomicilio3() + " ";
			}
			
			if (pojoEmpresa.getDomicilio4() != null && !"".equals(pojoEmpresa.getDescDomicilio4().trim())){
				pojoEmpresa.setDomicilio4(ifzConValores.findById(pojoEmpresa.getDomicilio4().getId()));
				domicilio += pojoEmpresa.getDomicilio4().getAtributo1() + " " + pojoEmpresa.getDescDomicilio4() + " ";
			}
			
			if (pojoEmpresa.getDomicilio5() != null && !"".equals(pojoEmpresa.getDescDomicilio5().trim())){
				pojoEmpresa.setDomicilio5(ifzConValores.findById(pojoEmpresa.getDomicilio5().getId()));
				domicilio += pojoEmpresa.getDomicilio5().getAtributo1() + " " + pojoEmpresa.getDescDomicilio5() + " ";
			}
			
			if (domicilio.length() > 0)
				domicilio = domicilio.substring(0, domicilio.length() - 1);
			
			pojoEmpresa.setDomicilio(domicilio);
			
			if (pojoEmpresa.getSerieComplementoPago() == null || "".equals(pojoEmpresa.getSerieComplementoPago().trim()))
				pojoEmpresa.setSerieComplementoPago("P");
			
			if (pojoEmpresa.getId() == null || pojoEmpresa.getId() == 0L){
				pojoEmpresa.setCreadoPor(infoSesion.getAcceso().getId());
				pojoEmpresa.setFechaCreacion(Calendar.getInstance().getTime());
				pojoEmpresa.setModificadoPor(infoSesion.getAcceso().getId());
				pojoEmpresa.setFechaModificacion(Calendar.getInstance().getTime());
				
				pojoEmpresa.setId(ifzEmpresas.save(pojoEmpresa, null));
			} else {
				pojoEmpresa.setModificadoPor(infoSesion.getAcceso().getId());
				pojoEmpresa.setFechaModificacion(Calendar.getInstance().getTime());
				ifzEmpresas.update(pojoEmpresa);
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoEmpresa", pojoEmpresa);
		} catch (Exception e) {
			log.error("Error en EmpresasFac.guardarEmpresa", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_EMPRESA);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	private MonedaTYGExt convertMonedaToMonedaTYGExt(Moneda pojoMoneda) throws Exception {
		MonedaTYGExt pojoMonedaExt = new MonedaTYGExt();
		
		try {
			pojoMonedaExt.setId(pojoMoneda.getId());
			pojoMonedaExt.setCreadoPor(pojoMoneda.getCreadoPor());
			pojoMonedaExt.setFechaCreacion(pojoMoneda.getFechaCreacion());
			pojoMonedaExt.setModificadoPor(pojoMoneda.getModificadoPor());
			pojoMonedaExt.setFechaModificacion(pojoMoneda.getFechaModificacion());
			pojoMonedaExt.setIdShf(pojoMoneda.getIdShf());
			pojoMonedaExt.setNombre(pojoMoneda.getNombre());
			pojoMonedaExt.setAbreviacion(pojoMoneda.getAbreviacion());
			
			return pojoMonedaExt;
		} catch (Exception e) {
			log.error("Error en EmpresasFac.convertMonedaToMonedaTYGExt");
			throw e;
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Modificacion de EmpresaFac