package net.giro.tyg.logica;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.dao.EmpresaDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.GastoDeCobranza;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
//import net.giro.tyg.admon.Seguimiento;
import net.giro.tyg.admon.SucursalBancaria;
import net.giro.tyg.admon.Tasas;
import net.giro.tyg.admon.TiposTasas;
import net.giro.tyg.admon.ValoresTasas;
import net.giro.tyg.dao.CatBancosDAO;
import net.giro.tyg.dao.CtasBancariasDAO;
import net.giro.tyg.dao.FormasPagosDAO;
import net.giro.tyg.dao.GastosCobranzaDAO;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;
//import net.giro.tyg.dao.SeguimientoDAO;
import net.giro.tyg.dao.SucursalBancariaDAO;
import net.giro.tyg.dao.TasasDAO;
import net.giro.tyg.dao.TiposTasasDAO;
import net.giro.tyg.dao.ValoresTasasDAO;
import net.giro.util.tyg.Errores;


@Stateless
public class TygFacade implements TygRem {
	private static Logger log = Logger.getLogger(TygFacade.class);
	private InfoSesion infoSesion;
	private InitialContext ctx = null;
	@Resource
    
	private SessionContext sctx;
	private MonedaDAO ifzImoneda;
	private CatBancosDAO ifzIbancos;
	private CtasBancariasDAO ifzIctasBanco;
	private EmpresaDAO ifzEmpresas;
	private SucursalBancariaDAO ifzIsucursalBancaria;

	private EstadoDAO ifzEstado;
	private MonedasValoresDAO ifzIMonedaValor;
	private TasasDAO ifzITasas;
	private TiposTasasDAO ifzITipoTasas;
	private ValoresTasasDAO ifzIValoresTasas;
	private FormasPagosDAO ifzIFormasPagos;
	private GastosCobranzaDAO ifzIGastoDeCobranza;
	//private SeguimientoDAO ifzISeguimientos;
	
	public TygFacade(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");            
            ctx = new InitialContext(p);
       
    		ifzImoneda =  (MonedaDAO)ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
    		ifzIbancos =  (CatBancosDAO)ctx.lookup("ejb:/Model_TYG//CatBancosImp!net.giro.tyg.dao.CatBancosDAO");
    		ifzIctasBanco  =  (CtasBancariasDAO)ctx.lookup("ejb:/Model_TYG//CtasBancariasImp!net.giro.tyg.dao.CtasBancariasDAO");
    		ifzIsucursalBancaria =  (SucursalBancariaDAO)ctx.lookup("ejb:/Model_TYG//SucursalBancariaImp!net.giro.tyg.dao.SucursalBancariaDAO");
    		ifzEmpresas =  (EmpresaDAO)ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
    		ifzEstado=(EstadoDAO)ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
    		ifzIMonedaValor=(MonedasValoresDAO)ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
    		ifzITasas=(TasasDAO)ctx.lookup("ejb:/Model_TYG//TasasImp!net.giro.tyg.dao.TasasDAO");
    		ifzITipoTasas=(TiposTasasDAO)ctx.lookup("ejb:/Model_TYG//TiposTasasImp!net.giro.tyg.dao.TiposTasasDAO");
    		ifzIValoresTasas=(ValoresTasasDAO)ctx.lookup("ejb:/Model_TYG//ValoresTasasImp!net.giro.tyg.dao.ValoresTasasDAO");
    		ifzIFormasPagos=(FormasPagosDAO)ctx.lookup("ejb:/Model_TYG//FormasPagosImp!net.giro.tyg.dao.FormasPagosDAO");
    		ifzIGastoDeCobranza=(GastosCobranzaDAO)ctx.lookup("ejb:/Model_TYG//GastosCobrazaImp!net.giro.tyg.dao.GastosCobranzaDAO");
    		
			} catch(Exception e) {
    		ctx = null;
    		}	
		}	
	

	@Override
	public Respuesta salvar(Moneda moneda){
		Respuesta respuesta = new Respuesta();
		try{
			moneda.setModificadoPor(infoSesion.getAcceso().getId());
			moneda.setFechaModificacion(Calendar.getInstance().getTime());
			if(moneda.getId() > 0){
				this.ifzImoneda.update(moneda);
			}else{
				moneda.setCreadoPor(infoSesion.getAcceso().getId());
				moneda.setFechaCreacion(Calendar.getInstance().getTime());
				moneda.setId(this.ifzImoneda.save(moneda));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMoneda", moneda);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_MONEDA);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar para Moneda", e);
		}
		
		return respuesta;
	}
	
	
	@Override
	public long salvar(GastoDeCobranza gastodecobranza) throws ExcepConstraint {
		if(gastodecobranza.getId() > 0){
			this.ifzIGastoDeCobranza.update(gastodecobranza);
		}else{
			return this.ifzIGastoDeCobranza.save(gastodecobranza) ;
		}
		return gastodecobranza.getId();
	}
	
	@Override
	public Respuesta salvar(CatBancos catbancos){
		Respuesta respuesta = new Respuesta();
		
		try {
			catbancos.setModificadoPor(infoSesion.getAcceso().getId());
			catbancos.setFechaModificacion(Calendar.getInstance().getTime());
			if(catbancos.getId() > 0){
				this.ifzIbancos.update(catbancos);
			}else{
				catbancos.setCreadoPor(infoSesion.getAcceso().getId());
				catbancos.setFechaCreacion(Calendar.getInstance().getTime());
				catbancos.setId(this.ifzIbancos.save(catbancos));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoBanco", catbancos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar de CatBancos", e);
		}
		
		return respuesta;
	}
	
	
	@Override	
	public Respuesta salvar(CtasBanco ctasBanco){
		Respuesta respuesta = new Respuesta();
		
		try{
			ctasBanco.setModificadoPor(infoSesion.getAcceso().getId());
			ctasBanco.setFechaModificacion(Calendar.getInstance().getTime());
			if(ctasBanco.getId() > 0){
				this.ifzIctasBanco.update(ctasBanco);
			}else{
				ctasBanco.setCreadoPor(infoSesion.getAcceso().getId());
				ctasBanco.setFechaCreacion(Calendar.getInstance().getTime());
				ctasBanco.setId(this.ifzIctasBanco.save(ctasBanco));
			}
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoCuentaBanco", ctasBanco);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_CUENTA_BANCO);
			respuesta.setBody(null);
			log.error("Error TygFacade.salvar para CtasBanco", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(Tasas tasas){
		Respuesta respuesta = new Respuesta();
		try{
			tasas.setModificadoPor(infoSesion.getAcceso().getId());
			tasas.setFechaModificacion(Calendar.getInstance().getTime());
			if(tasas.getId() > 0)
				this.ifzITasas.update(tasas);
			else{
				tasas.setCreadoPor(infoSesion.getAcceso().getId());
				tasas.setFechaCreacion(Calendar.getInstance().getTime());
				tasas.setId(this.ifzITasas.save(tasas));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoTasas", tasas);
		} catch(Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(TiposTasas tipotasas){
		Respuesta respuesta = new Respuesta();
		try{
			tipotasas.setModificadoPor(infoSesion.getAcceso().getId());
			tipotasas.setFechaModificacion(Calendar.getInstance().getTime());
			if(tipotasas.getId() > 0){
				this.ifzITipoTasas.update(tipotasas);
			}else{
				tipotasas.setCreadoPor(infoSesion.getAcceso().getId());
				tipotasas.setFechaCreacion(Calendar.getInstance().getTime());
				tipotasas.setId(this.ifzITipoTasas.save(tipotasas));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoTiposTasas", tipotasas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_TIPOS_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar para TiposTasas", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(ValoresTasas valorestasas){
		Respuesta respuesta = new Respuesta();
		try{
			valorestasas.setModificadoPor(infoSesion.getAcceso().getId());
			valorestasas.setFechaModificacion(Calendar.getInstance().getTime());
			if(valorestasas.getId() > 0){
				this.ifzIValoresTasas.update(valorestasas);
			}else{
				valorestasas.setCreadoPor(infoSesion.getAcceso().getId());
				valorestasas.setFechaCreacion(Calendar.getInstance().getTime());
				valorestasas.setId(this.ifzIValoresTasas.save(valorestasas));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoValoresTasas", valorestasas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_TASAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar para ValoresTasas", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta salvar(FormasPagos formaspagos) {
		Respuesta respuesta = new Respuesta();
		try{
			formaspagos.setModificadoPor(infoSesion.getAcceso().getId());
			formaspagos.setFechaModificacion(Calendar.getInstance().getTime());
			if(formaspagos.getId() > 0){
				this.ifzIFormasPagos.update(formaspagos);
			}else{
				formaspagos.setCreadoPor(infoSesion.getAcceso().getId());
				formaspagos.setFechaCreacion(Calendar.getInstance().getTime());
				formaspagos.setId(this.ifzIFormasPagos.save(formaspagos));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoFormasPagos", formaspagos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_FORMAS_PAGO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar para FormasPagos", e);
		}
		
		return respuesta;
	}
	@Override
	public Respuesta buscarCtasBancos(String tipoBusqueda, String valorBusqueda){
		List<CtasBanco> listaCuentaBancos;
		Respuesta respuesta = new Respuesta();
		try{
			listaCuentaBancos = ifzIctasBanco.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listCuentaBancos", listaCuentaBancos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_CUENTA_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarCtasBancos", e);
		}
		return respuesta;
	}
	
	@Override
	public List<GastoDeCobranza> buscarGastosDeCobranza(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint{
		return ifzIGastoDeCobranza.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
/*
  	public List<Seguimiento> buscarSeguimientos(String tipoBusqueda, String valorBusqueda, String valorStatus) throws ExcepConstraint{
 
		return ifzISeguimientos.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
*/	
	@Override
	public Respuesta buscarMoneda(String tipoBusqueda, String valorBusqueda) {
		Respuesta respuesta = new Respuesta();
		try{
			List<Moneda> listMonedas = ifzImoneda.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMonedas", listMonedas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_MONEDA);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarMoneda", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarBanco(String tipoBusqueda, String valorBusqueda) {
		Respuesta respuesta = new Respuesta();
		List<CatBancos> listBancos;
		
		try {
			listBancos = ifzIbancos.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listBancos", listBancos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarBanco", e);
		}
		
		return respuesta;
	}
	@Override
	public Respuesta buscarTasas(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<Tasas> listTasas =  ifzITasas.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listTasas", listTasas);
		} catch(Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarTasas", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarTipoTasas(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<TiposTasas> listTiposTasas = ifzITipoTasas.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listTiposTasas", listTiposTasas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_TIPOS_TASAS);
			respuesta.setBody(null);
			log.error("Error en buscarTipoTasas", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarValoresTasas(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<ValoresTasas> listValoresTasas = ifzIValoresTasas.findLikePropiedad(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listValoresTasas", listValoresTasas);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_TASAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarValoresTasas", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarFormasPagos(String tipoBusqueda, String valorBusqueda) {
		Respuesta respuesta = new Respuesta();
		try {
			List<FormasPagos> listFormasPago = ifzIFormasPagos.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listFormasPago", listFormasPago);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_FORMAS_PAGO);
			respuesta.setBody(null);
			log.error("Error al buscar formas de pagos", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarBancos(CatBancos catbancos) {
		Respuesta respuesta = new Respuesta();
		
		try{
			ifzIbancos.delete(catbancos.getId());
			
			respuesta.getErrores().setCodigoError(0);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarBancos", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarCtasBancarias(CtasBanco ctasbanco) {
		Respuesta respuesta = new Respuesta();
		try {
			ifzIctasBanco.delete(ctasbanco.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_CUENTA_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarCtasBancarias", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarFormasPago(FormasPagos formaspagos){
		Respuesta respuesta = new Respuesta();
		try{
			ifzIFormasPagos.delete(formaspagos.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_FORMAS_PAGO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarFormasPago", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarTasas(Tasas tasas){
		Respuesta respuesta = new Respuesta();
		try{
			ifzITasas.delete(tasas.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarTasas", e);
		}
		return respuesta;
	}
	
	@Override
	public void eliminarGastosDeCobranza(GastoDeCobranza gastosdecobranza) throws ExcepConstraint {
		ifzIGastoDeCobranza.delete(gastosdecobranza.getId());
		
	}
	
	@Override
	public Respuesta eliminarValoresTasas(ValoresTasas valorestasas){
		Respuesta respuesta = new Respuesta();
		try{
			ifzIValoresTasas.delete(valorestasas.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_TASAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarValoresTasas", e);
		}
		
		return respuesta;
	}
	@Override
	public Respuesta autocompletarMoneda(){
		Respuesta respuesta = new Respuesta();
		try {
			List<Moneda> listMonedas = ifzImoneda.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMonedas", listMonedas);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_MONEDA);
			respuesta.setBody(null);
			log.error("Error en TygFacade.autocompletarMoneda", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta autoCargarTasas(){
		Respuesta respuesta = new Respuesta();
		try{
			List<Tasas> listTasas =  ifzITasas.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listTasas", listTasas);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.autoCargarTasas", e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta eliminarTipoTasas(TiposTasas tipotasas){
		Respuesta respuesta = new Respuesta();
		try{
			ifzITipoTasas.delete(tipotasas.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_TIPOS_TASAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarTipoTasas", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta autoacompletarBanco() {
		Respuesta respuesta = new Respuesta();
		try {
			
			List<CatBancos> listBancos = ifzIbancos.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listBancos", listBancos);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_BANCO);
			respuesta.setBody(null);
			log.error("Error en TygFacade.autoacompletarBanco", e);
		}
		return respuesta;
	}
	@Override
	public Respuesta autoacompletarEmpresa(){
		Respuesta respuesta = new Respuesta();
		try {
			List<Empresa> listEmpresas = ifzEmpresas.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEmpresas", listEmpresas);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_EMPRESA);
			respuesta.setBody(null);
			log.error("Error en TygFacade.autoacompletarEmpresa", e);
		}
		
		return respuesta;
	}

	@Override
	public Respuesta autoacompletarSucursal(){
		Respuesta respuesta = new Respuesta();
		try{
			List<SucursalBancaria> listSucursales = ifzIsucursalBancaria.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSucursales", listSucursales);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_SUCURSAL);
			respuesta.setBody(null);
			log.error("Error en autoacompletarSucursal", e);
		}
		
		return respuesta;
	}

	@Override
	public Respuesta buscarSucursal(String tipoBusqueda, String valorBusqueda){
		Respuesta respuesta = new Respuesta();
		try{
			List<SucursalBancaria> listSucursalesBancarias = ifzIsucursalBancaria.findLikeColumnName(tipoBusqueda, valorBusqueda);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listSucursalesBancarias", listSucursalesBancarias);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_SUCURSALES_BANCARIAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarSucursal", e);
		}
		
		return respuesta;
	}

	
	@Override	
	public Respuesta salvar(SucursalBancaria sucursalbancaria) {
		Respuesta respuesta = new Respuesta();
		try{
			sucursalbancaria.setModificadoPor(infoSesion.getAcceso().getId());
			sucursalbancaria.setFechaModificacion(Calendar.getInstance().getTime());
			if(sucursalbancaria.getId() > 0){
				this.ifzIsucursalBancaria.update(sucursalbancaria);
			}else{
				sucursalbancaria.setCreadoPor(infoSesion.getAcceso().getId());
				sucursalbancaria.setFechaCreacion(Calendar.getInstance().getTime());
				sucursalbancaria.setId(this.ifzIsucursalBancaria.save(sucursalbancaria));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoSucursalBancaria", sucursalbancaria);
		} catch (Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_SUCURSALES_BANCARIAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar para SucursalBancaria", e);
		}
		
		return respuesta;
	}
	

	@Override
	public Respuesta salvar(MonedasValores monedavalores){
		Respuesta respuesta = new Respuesta();
		try{
			monedavalores.setModificadoPor(infoSesion.getAcceso().getId());
			monedavalores.setFechaModificacion(Calendar.getInstance().getTime());
			if(monedavalores.getId() > 0){
				this.ifzIMonedaValor.update(monedavalores);
			}else{
				monedavalores.setCreadoPor(infoSesion.getAcceso().getId());
				monedavalores.setFechaCreacion(Calendar.getInstance().getTime());
				monedavalores.setId(this.ifzIMonedaValor.save(monedavalores));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMonedasValores", monedavalores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_GUARDAR_MONEDAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.salvar de MonedasValores", e);
		}
		
		return respuesta;
	}
	
	
	@Override
	public Respuesta eliminarSucursalBancaria(SucursalBancaria sucursalbancaria){
		Respuesta respuesta = new Respuesta();
		try{
			ifzIsucursalBancaria.delete(sucursalbancaria.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_SUCURSALES_BANCARIAS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.eliminarSucursalBancaria", e);
		}
		
		return respuesta;
	}
	

	
//	@Override
//	public List<Estado> autoacompletarEstado()throws ExcepConstraint{
//		return ifzEstado.findAll();
//	}


	@Override
	public Respuesta autoacompletarEstado() {
		Respuesta respuesta = new Respuesta();
		try{
			List<Estado> listEstados = ifzEstado.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstados);
		} catch(Exception e){
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_ESTADOS);
			respuesta.setBody(null);
			log.error("Error en TygFacade.autoacompletarEstado", e);
		}
		return respuesta;
	}
	
	public List<ConValores> buscarEstatus(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint{
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		//List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		/*List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_COMO_ENTERO");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		*/
		return listaConValores;
	}
	
	@Override
	public List<MonedasValores> autoacompletarMonedaValor()throws ExcepConstraint{
		return ifzIMonedaValor.findAll();


	}
	
	

	@Override
	public Respuesta buscarMonedaV(Long monedaBase, Long monedaDestino){
		Respuesta respuesta = new Respuesta();
		try{
			List<MonedasValores> listMonedasValores = ifzIMonedaValor.findByBaseDestino(monedaBase, monedaDestino);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMonedasValores", listMonedasValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_BUSCAR_MONEDAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarMonedaV", e);
		}
		
		return respuesta;
	}
	
	
	@Override
	public Respuesta eliminarMonedaValor(MonedasValores monedavalores){
		Respuesta respuesta = new Respuesta();
		try{
			ifzIMonedaValor.delete(monedavalores.getId());
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_ELIMINAR_MONEDAS_VALORES);
			log.error("Error en TygFacade.eliminarMonedaValor", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta listarMonedasValores(){
		Respuesta respuesta = new Respuesta();
		try{
			List<MonedasValores> listMonedasValores = ifzIMonedaValor.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMonedasValores", listMonedasValores);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("TYG", Errores.ERROR_LISTAR_MONEDAS_VALORES);
			respuesta.setBody(null);
			log.error("Error en TygFacade.buscarMonedaV", e);
		}
		
		return respuesta;
	}

	@Override
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

}
	

	
	
