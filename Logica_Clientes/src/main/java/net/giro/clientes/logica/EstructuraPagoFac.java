package net.giro.clientes.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.CtasBancoExt;
import net.giro.clientes.beans.EstructuraImportacionCuenta;
import net.giro.clientes.beans.EstructuraImportacionCuentaExt;
import net.giro.clientes.dao.EstructuraImportacionCuentaDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.dao.CuentasBancariasDAO;
import net.giro.util.clientes.Errores;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

@Stateless
public class EstructuraPagoFac implements EstructuraPagoRem {
	private static Logger log = Logger.getLogger(EstructuraPagoFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	@Resource
	private SessionContext sctx;
	private String modulo;
	private EstructuraImportacionCuentaDAO ifzEstructuraImportacion;
	private CuentasBancariasDAO ifzCtasBancos;

	public EstructuraPagoFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);

			infoSesion = new InfoSesion();

			modulo = "CLIENTES";

			ifzEstructuraImportacion = (EstructuraImportacionCuentaDAO) ctx.lookup("ejb:/Model_Clientes//EstructuraImportacionCuentaImp!net.giro.clientes.dao.EstructuraImportacionCuentaDAO");
			ifzCtasBancos  =  (CuentasBancariasDAO)ctx.lookup("ejb:/Model_TYG//CuentasBancariasImp!net.giro.tyg.dao.CuentasBancariasDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear EstructuraPagoFac", e);
			ctx = null;
		}
	}

	@Override
	public Respuesta cargarEstructurasImportacion() {
		Respuesta respuesta = new Respuesta();
		try {
			List<EstructuraImportacionCuenta> listEstructuras = ifzEstructuraImportacion.findAll();
			
			List<EstructuraImportacionCuentaExt> listEstructurasExt = new ArrayList<EstructuraImportacionCuentaExt>();
			
			for(EstructuraImportacionCuenta pojoEstructuraImportacion : listEstructuras){
				listEstructurasExt.add(convertEstructuraImportacionToEstructuraImportacionExt(pojoEstructuraImportacion));
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstructuras", listEstructurasExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_ESTRUCTURA_IMPORTACION);
			respuesta.setBody(null);
			log.error("Error en EstructuraPagoFac.cargarEstructurasImportacion", e);
		}
		return respuesta;
	}

	@Override
	public Respuesta eliminarEstructuraImportacion(EstructuraImportacionCuentaExt pojoEstructuraImportacionExt) {
		Respuesta respuesta = new Respuesta();
		try {
			EstructuraImportacionCuenta pojoEstructuraImportacion = convertEstructuraImportacionExtToEstructuraImportacion(pojoEstructuraImportacionExt);
			
			ifzEstructuraImportacion.delete(pojoEstructuraImportacion.getId());

			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo,
					Errores.ERROR_ELIMINAR_ESTRUCTURA_IMPORTACION_PAGO);
			respuesta.setBody(null);
			log.error("Error en EstructuraPagoFac.eliminarEstructuraImportacion", e);
		}
		return respuesta;
	}

	@Override
	public Respuesta guardarEstructuraImportacion(EstructuraImportacionCuentaExt pojoEstructuraImportacionExt) {
		Respuesta respuesta = new Respuesta();
		try {
			EstructuraImportacionCuenta pojoEstructuraImportacion = convertEstructuraImportacionExtToEstructuraImportacion(pojoEstructuraImportacionExt);
			
			if(pojoEstructuraImportacion.getSeparador().length() > 1){
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LONGITUD_SEPARADOR);
				respuesta.setBody(null);
				return respuesta;
			}
			
			List<EstructuraImportacionCuenta> listCuentas = ifzEstructuraImportacion.findByColumnName("idCuenta", pojoEstructuraImportacion.getIdCuenta());
			if(listCuentas.size() > 0 && listCuentas.get(0).getId() != pojoEstructuraImportacionExt.getId()){
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ESTRUCTURA_DEFINIDA);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if(pojoEstructuraImportacion.getPosicionFecha() > 0){
				if(pojoEstructuraImportacion.getFormatoFecha().length() <= 0){
					respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FORMATO_FECHA_REQUERIDO);
					respuesta.setBody(null);
					return respuesta;
				}
			}
			
			if(pojoEstructuraImportacion.getPosicionIdentificador() > 0){
				if(pojoEstructuraImportacion.getValorIdentificador().length() <= 0){
					respuesta.getErrores().addCodigo(modulo, Errores.ERROR_VALOR_IDENTIFICADOR_REQUERIDO);
					respuesta.setBody(null);
					return respuesta;
				}
			}
			
			if(pojoEstructuraImportacion.getSeparador().length() <= 0){
				if(pojoEstructuraImportacion.getPosicionReferencia() > 0){
					if(pojoEstructuraImportacion.getLongitudReferencia() <= 0){
						respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LONGITUD_REFERENCIA_REQUERIDO);
						respuesta.setBody(null);
						return respuesta;
					}
				}
				
				if(pojoEstructuraImportacion.getPosicionMonto() > 0){
					if(pojoEstructuraImportacion.getLongitudMonto() <= 0){
						respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LONGITUD_MONTO_REQUERIDO);
						respuesta.setBody(null);
						return respuesta;
					}
				}
				
				if(pojoEstructuraImportacion.getPosicionFecha() > 0){
					if(pojoEstructuraImportacion.getLongitudFecha() <= 0){
						respuesta.getErrores().addCodigo(modulo, Errores.ERROR_LONGITUD_FECHA_REQUERIDO);
						respuesta.setBody(null);
						return respuesta;
					}
				}
			}
			
			//if(pojoEstructuraImportacion.getSeparador().length())
			
			pojoEstructuraImportacion.setModificadoPor(infoSesion.getAcceso().getId());
			pojoEstructuraImportacion.setFechaModificacion(Calendar.getInstance().getTime());

			if (pojoEstructuraImportacion.getId() == 0L) {
				pojoEstructuraImportacion.setCreadoPor(infoSesion.getAcceso().getId());
				pojoEstructuraImportacion.setFechaCreacion(Calendar.getInstance().getTime());

				pojoEstructuraImportacion.setId(ifzEstructuraImportacion.save(pojoEstructuraImportacion, null));
			} else
				ifzEstructuraImportacion.update(pojoEstructuraImportacion);

			pojoEstructuraImportacionExt = convertEstructuraImportacionToEstructuraImportacionExt(pojoEstructuraImportacion);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoEstructuraImportacion", pojoEstructuraImportacionExt);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ELIMINAR_ESTRUCTURA_IMPORTACION_PAGO);
			respuesta.setBody(null);
			log.error("Error en EstructuraPagoFac.guardarEstructuraImportacion", e);
		}
		return respuesta;
	}

	@Override
	public Respuesta listarCtasBancos() {
		Respuesta respuesta = new Respuesta();
		try {
			List<CtasBancoExt> listCuentas = new ArrayList<CtasBancoExt>();
			List<CuentaBancaria> listTmp;

			listTmp = ifzCtasBancos.findAll();

			for (CuentaBancaria o : listTmp) {
				CtasBancoExt cuenta = new CtasBancoExt();

				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(cuenta, o);

				cuenta.setInstitucionBancaria(o.getInstitucionBancaria());

				listCuentas.add(cuenta);
			}

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listCuentas", listCuentas);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo,
					Errores.ERROR_LISTAR_BANCOS);
			respuesta.setBody(null);
			log.error("Error en ContratosFac.listarBancos", e);
		}
		return respuesta;
	}
	
	private EstructuraImportacionCuentaExt convertEstructuraImportacionToEstructuraImportacionExt(EstructuraImportacionCuenta pojoEstructuraImportacion) throws Exception{
		EstructuraImportacionCuentaExt pojoEstructuraImportacionExt = new EstructuraImportacionCuentaExt();
		
		pojoEstructuraImportacionExt.setId(pojoEstructuraImportacion.getId());
		pojoEstructuraImportacionExt.setIdCuenta(convertCtasBancoToCtasBancoExt(pojoEstructuraImportacion.getIdCuenta()));
		pojoEstructuraImportacionExt.setValorIdentificador(pojoEstructuraImportacion.getValorIdentificador());
		pojoEstructuraImportacionExt.setPosicionIdentificador(pojoEstructuraImportacion.getPosicionIdentificador());
		pojoEstructuraImportacionExt.setLongitudIdentificador(pojoEstructuraImportacion.getLongitudIdentificador());
		pojoEstructuraImportacionExt.setPosicionReferencia(pojoEstructuraImportacion.getPosicionReferencia());
		pojoEstructuraImportacionExt.setLongitudReferencia(pojoEstructuraImportacion.getLongitudReferencia());
		pojoEstructuraImportacionExt.setPosicionMonto(pojoEstructuraImportacion.getPosicionMonto());
		pojoEstructuraImportacionExt.setLongitudMonto(pojoEstructuraImportacion.getLongitudMonto());
		pojoEstructuraImportacionExt.setFormatoFecha(pojoEstructuraImportacion.getFormatoFecha());
		pojoEstructuraImportacionExt.setPosicionFecha(pojoEstructuraImportacion.getPosicionFecha());
		pojoEstructuraImportacionExt.setLongitudFecha(pojoEstructuraImportacion.getLongitudFecha());
		pojoEstructuraImportacionExt.setSeparador(pojoEstructuraImportacion.getSeparador());
		pojoEstructuraImportacionExt.setCreadoPor(pojoEstructuraImportacion.getCreadoPor());
		pojoEstructuraImportacionExt.setFechaCreacion(pojoEstructuraImportacion.getFechaCreacion());
		pojoEstructuraImportacionExt.setModificadoPor(pojoEstructuraImportacion.getModificadoPor());
		pojoEstructuraImportacionExt.setFechaModificacion(pojoEstructuraImportacion.getFechaModificacion());
		pojoEstructuraImportacionExt.setPosicionCabecera(pojoEstructuraImportacion.getPosicionCabecera());
		pojoEstructuraImportacionExt.setPosicionResumen(pojoEstructuraImportacion.getPosicionResumen());
		
		return pojoEstructuraImportacionExt;
	}
	
	private EstructuraImportacionCuenta convertEstructuraImportacionExtToEstructuraImportacion(EstructuraImportacionCuentaExt pojoEstructuraImportacionExt){
		EstructuraImportacionCuenta pojoEstructuraImportacion = new EstructuraImportacionCuenta();
		
		pojoEstructuraImportacion.setId(pojoEstructuraImportacionExt.getId());
		pojoEstructuraImportacion.setIdCuenta(pojoEstructuraImportacionExt.getIdCuenta().getId());
		pojoEstructuraImportacion.setValorIdentificador(pojoEstructuraImportacionExt.getValorIdentificador());
		pojoEstructuraImportacion.setPosicionIdentificador(pojoEstructuraImportacionExt.getPosicionIdentificador());
		pojoEstructuraImportacion.setLongitudIdentificador(pojoEstructuraImportacionExt.getLongitudIdentificador());
		pojoEstructuraImportacion.setPosicionReferencia(pojoEstructuraImportacionExt.getPosicionReferencia());
		pojoEstructuraImportacion.setLongitudReferencia(pojoEstructuraImportacionExt.getLongitudReferencia());
		pojoEstructuraImportacion.setPosicionMonto(pojoEstructuraImportacionExt.getPosicionMonto());
		pojoEstructuraImportacion.setLongitudMonto(pojoEstructuraImportacionExt.getLongitudMonto());
		pojoEstructuraImportacion.setFormatoFecha(pojoEstructuraImportacionExt.getFormatoFecha());
		pojoEstructuraImportacion.setPosicionFecha(pojoEstructuraImportacionExt.getPosicionFecha());
		pojoEstructuraImportacion.setLongitudFecha(pojoEstructuraImportacionExt.getLongitudFecha());
		pojoEstructuraImportacion.setSeparador(pojoEstructuraImportacionExt.getSeparador());
		pojoEstructuraImportacion.setCreadoPor(pojoEstructuraImportacionExt.getCreadoPor());
		pojoEstructuraImportacion.setFechaCreacion(pojoEstructuraImportacionExt.getFechaCreacion());
		pojoEstructuraImportacion.setModificadoPor(pojoEstructuraImportacionExt.getModificadoPor());
		pojoEstructuraImportacion.setFechaModificacion(pojoEstructuraImportacionExt.getFechaModificacion());
		pojoEstructuraImportacion.setPosicionCabecera(pojoEstructuraImportacionExt.getPosicionCabecera());
		pojoEstructuraImportacion.setPosicionResumen(pojoEstructuraImportacionExt.getPosicionResumen());
		
		return pojoEstructuraImportacion;
	}
	
	private CtasBancoExt convertCtasBancoToCtasBancoExt(long idCtasBanco) throws Exception{
		CtasBancoExt pojoCtasBancoExt = new CtasBancoExt();
		try{
			CuentaBancaria pojoCtasBanco = ifzCtasBancos.findById(idCtasBanco);
			
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(pojoCtasBancoExt, pojoCtasBanco);

			pojoCtasBancoExt.setInstitucionBancaria(pojoCtasBanco.getInstitucionBancaria());
		} catch (Exception e) {
			throw e;
		}
		return pojoCtasBancoExt;
	}

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

}
