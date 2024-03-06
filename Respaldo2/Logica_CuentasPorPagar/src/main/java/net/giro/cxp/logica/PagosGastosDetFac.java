package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.giro.cargas.documentos.logica.FacturasRem;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.dao.PagosGastosDetDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Stateless
public class PagosGastosDetFac implements PagosGastosDetRem {
	private static Log log = LogFactory.getLog(PagosGastosDetFac.class);
	private InfoSesion infoSesion;
	private PagosGastosDetDAO ifzPagosGastosDet;
	private PagosGastosDetImpuestosRem ifzImptos;
	private FacturasRem	ifzFacturas;
	private ConvertExt convertidor;
	private InitialContext ctx;
	// property constants
	public static final String PROVEEDOR_ID = "proveedorId";
	public static final String REFERENCIA = "referencia";
	public static final String SUBTOTAL = "subtotal";
	public static final String IVA = "iva";
	public static final String CREADO_POR = "creadoPor";
	public static final String MODIFICADO_POR = "modificadoPor";
	public static final String OBSERVACIONES = "observaciones";
	public static final String CONCEPTO_ID = "conceptoId";

	
	public PagosGastosDetFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzPagosGastosDet = (PagosGastosDetDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImp!net.giro.cxp.dao.PagosGastosDetDAO");
    		this.ifzImptos = (PagosGastosDetImpuestosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
    		this.ifzFacturas = (FacturasRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//FacturasFac!net.giro.cargas.documentos.logica.FacturasRem");
    		this.convertidor = new ConvertExt();
    	} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear PagosGastosDetFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(PagosGastosDetExt entityExt) throws Exception {
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			return this.ifzPagosGastosDet.save(entity, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Long save(PagosGastosDet entity) throws ExcepConstraint {
		try {
			return this.ifzPagosGastosDet.save(entity, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> saveOrUpdateList(List<PagosGastosDet> entities) throws Exception {
		try {
			return this.ifzPagosGastosDet.saveOrUpdateList(entities, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDet entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDet.delete(entity.getId());;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDetExt entityExt) throws Exception {
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			this.ifzPagosGastosDet.delete(entity.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(PagosGastosDetExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(PagosGastosDet entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDet.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDet findById(Long id) {
		try {
			return this.ifzPagosGastosDet.findById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByProperty(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDet.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDet.findByPropertyALL(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findAll() {
		try {
			return this.ifzPagosGastosDet.findAll();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findLikePojoCompleto(Object entity, int max) {
		try {
			return this.ifzPagosGastosDet.findLikePojoCompleto(entity, max);
		} catch (Exception re ) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetExt> findLikePojoCompletoExt(Object entity, int max) throws Exception {
		List<PagosGastosDetExt> listaExt = new ArrayList<PagosGastosDetExt>();
		
		try {
			List<PagosGastosDet> lista = this.ifzPagosGastosDet.findLikePojoCompleto(entity, max);
			if (lista != null && ! lista.isEmpty()) {
				for (PagosGastosDet pagosGastosDet : lista)
					listaExt.add(this.convertidor.PagosGastosDetToPagosGastosDetExt(pagosGastosDet));
			}
		} catch (Exception re ) {
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public String eliminaDetGastos(PagosGastosDet value) throws Exception {
		List<PagosGastosDetImpuestos> listImptos = null;
		
		try {
			this.ifzImptos.setInfoSesion(this.infoSesion);
			listImptos = ifzImptos.findImptos2DetGtos(value, 0);
			if (listImptos != null && ! listImptos.isEmpty()){
				for(PagosGastosDetImpuestos var: listImptos)
					ifzImptos.delete(var);
			}
			
			this.ifzPagosGastosDet.delete(value);
			return "OK";
		} catch (Exception re ) {
			log.error("Error en metodo EliminaDetGastos" + re);
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findPagoProvisionado(String propertyName, Object value, String sucursales){
		try {
			return this.ifzPagosGastosDet.findPagoProvisionado(propertyName, value, sucursales);
		} catch (Exception re ) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max){
		try {
			return this.ifzPagosGastosDet.findDetGtos2MovtoCtas(value, max);
		} catch (Exception re ) {
			throw re;
		}
	}

	@Override
	public Double saldoRepCajaChica(Object value){
		try{
			return this.ifzPagosGastosDet.saldoRepCajaChica(value);
		}catch(Exception re){
			throw re;
		}
	}

	@Override
	public Respuesta analizaFactura(byte[] fileSrc) throws Exception {
		//net.giro.cargas.documentos.respuesta.Respuesta resp = null;
		Respuesta respuesta = new Respuesta();
		String stepTrace = "";
		long errorCode = 0L;
		
		try {
			stepTrace += "|setting-infoSesion";
			/*if (this.infoSesion != null)
				this.ifzFacturas.setInfoSesion(convertInfoSesion());*/
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			stepTrace += "|send-analizarXML";
			respuesta = this.ifzFacturas.analizarXML(fileSrc);
			errorCode = respuesta.getErrores().getCodigoError(); 
			stepTrace += "|trace-analizarXML{" + respuesta.getBody().getValor("stepTrace").toString() + "}";
			if (errorCode != 0L) {
				log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura)\n\nTRACE\n" + stepTrace + "\n\n ERROR: " + respuesta.getErrores().getDescError());
				respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
				//respuesta.getErrores().setCodigoError(resp.getErrores().getCodigoError());
				//respuesta.getErrores().setDescError(resp.getErrores().getDescError());
				//respuesta.getBody().addValor("idComprobante", resp.getBody().getValor("idComprobante")); 
				respuesta.getBody().addValor("stepTrace", stepTrace);
				return respuesta;
			}

			stepTrace += "|setting-respuesta";
			respuesta.getErrores().setCodigoError(0L);
			/*respuesta.getBody().addValor("pojoComprobante", resp.getBody().getValor("pojoComprobante"));
			respuesta.getBody().addValor("pojoComprobacionFactura", resp.getBody().getValor("pojoComprobacionFactura"));
			respuesta.getBody().addValor("idComprobante", resp.getBody().getValor("idComprobante")); 
			respuesta.getBody().addValor("comprobanteFactura", resp.getBody().getValor("comprobanteFactura")); 
			respuesta.getBody().addValor("comprobanteSerie", resp.getBody().getValor("comprobanteSerie")); 
			respuesta.getBody().addValor("comprobanteFolio", resp.getBody().getValor("comprobanteFolio")); 
			respuesta.getBody().addValor("comprobanteRfc", resp.getBody().getValor("comprobanteRfc")); 
			respuesta.getBody().addValor("comprobanteRazonSocial", resp.getBody().getValor("comprobanteRazonSocial")); 
			respuesta.getBody().addValor("comprobanteRazonSocialRfc", resp.getBody().getValor("comprobanteRazonSocialRfc")); 
			respuesta.getBody().addValor("comprobanteTotal", resp.getBody().getValor("comprobanteTotal")); */
			respuesta.getBody().addValor("stepTrace", stepTrace);
			stepTrace += "|passing-pojoAcuse";
			//respuesta.getBody().addValor("pojoAcuse", resp.getBody().getValor("pojoAcuse"));
			respuesta.getBody().addValor("stepTrace", stepTrace);
			log.info("Proceso completo.\n\nTRACE\n" + stepTrace + "\n\n");
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura()\n\nTRACE\n" + stepTrace + "\n\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError("Ocurrio un problema al Analizar el XML");
			respuesta.getBody().addValor("stepTrace", stepTrace);
		} 
		
		return respuesta;
	}

	@Override
	public Respuesta cargaFacturaXML(byte[] fileSrc, Long idComprobacion) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<PagosGastosDet> listDetalles = null;
		long idComprobante = 0L;
		String stepTrace = "";

		try {
			if (idComprobacion == null)
				idComprobacion = 0L;

			/*if (this.infoSesion != null) 
				this.ifzFacturas.setInfoSesion(convertInfoSesion());*/
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			respuesta = analizaFactura(fileSrc);
			if (respuesta.getErrores().getCodigoError() == 6L) {
				stepTrace = (String) respuesta.getBody().getValor("stepTrace");
				// Recupero el ID de la carga previa
				idComprobante = (long) respuesta.getBody().getValor("idComprobante");
				stepTrace += "|id-xml-previo-" + idComprobante;
				
				// Comprobamos si ID esta siendo usado en algun detalle de las comprobaciones
				stepTrace += "|verificar-comprobaciones";
				listDetalles = this.ifzPagosGastosDet.findByProperty("idXml", idComprobante);
				if (listDetalles == null || listDetalles.isEmpty()) {
					// Elimino la carga previa porque no se esta usando
					stepTrace += "|sin-comprobaciones-eliminando";
					this.ifzFacturas.deleteXML(idComprobante);

					// re-lanzo la carga de XML
					respuesta = analizaFactura(fileSrc);
					stepTrace += "|cargaXml{" + respuesta.getBody().getValor("stepTrace").toString() + "}";
				} else {
					// Si esta siendo usada, por lo tanto, verifico si es la misma comprobacion que este editanto si corresponde
					stepTrace += "|ciclo-comprobaciones-NOT_IMPLEMENTED_YET";
					log.error("NOT_IMPLEMENTED_YET", new NotImplementedException("Verificar XML a traves de distintas Comprobaciones"));
					
					/*if (listDetalles != null && ! listDetalles.isEmpty() && idComprobacion > 0L) {
						for (PagosGastosDet var : listDetalles) {
							if (var.getIdXml().longValue() != idComprobacion) {
								// do nothing
								break;
							}
						}
					}*/
				}
				
				respuesta.getBody().addValor("stepTrace", stepTrace);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura()\n\nTRACE\n" + stepTrace + "\n\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al Analizar el XML");
			respuesta.getBody().addValor("stepTrace", stepTrace);
		} 
		
		return respuesta;
	}
	
	@Override
	public void eliminarFactura(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion.getUltimaModificacion(), this.infoSesion.getAcceso().getId());
			this.ifzFacturas.deleteXML(idFactura);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.eliminarFactura. Exception: ", e);
		}
	}
	
	@Override
	public String getFacturaProperty(long idFactura, String property) {
		String resultado = "";
		
		try {
			this.ifzImptos.setInfoSesion(this.infoSesion);
			resultado = this.ifzFacturas.getProperty(idFactura, property);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.getFacturaProperty. Exception: ", e);
			resultado = "";
		}
		
		return resultado;
	}
	
	@Override
	public void setFacturaProperty(long idFactura, String property, String value) throws Exception {
		try {
			this.ifzImptos.setInfoSesion(this.infoSesion);
			this.ifzFacturas.setProperty(idFactura, property, value);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.setFacturaProperty. Exception: ", e);
		}
	}

	@Override
	public PagosGastosDetExt convertir(PagosGastosDet entity) throws Exception {
		return this.convertir(entity);
	}
	
	@Override
	public PagosGastosDet convertir(PagosGastosDetExt entityExt) throws Exception {		
		return this.convertir(entityExt);
	}

	@Override
	public List<PagosGastosDet> findByPagosGastos(PagosGastos entity, int limite) throws Exception {
		try {
			return this.ifzPagosGastosDet.findByPagosGastos(entity, limite);
		} catch (Exception re ) {
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------
	// EXTENDIDO
	// --------------------------------------------------------------------------------------
	
	@Override
	public List<PagosGastosDetExt> findByPagosGastosExt(PagosGastos entity, int limite) throws Exception {
		List<PagosGastosDetExt> listaExt = new ArrayList<PagosGastosDetExt>();
		
		try {
			List<PagosGastosDet> lista = this.findByPagosGastos(entity, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (PagosGastosDet pagosGastosDet : lista)
					listaExt.add(this.convertidor.PagosGastosDetToPagosGastosDetExt(pagosGastosDet));
			}
		} catch (Exception re ) {
			throw re;
		}
		
		return listaExt;
	}
	
	@Override
	public List<PagosGastosDetExt> findByPagosGastosExt(PagosGastosExt entityExt, int limite) throws Exception {
		List<PagosGastosDetExt> listaExt = new ArrayList<PagosGastosDetExt>();
		
		try {
			PagosGastos entity = this.convertidor.PagosGastosExtToPagosGastos(entityExt);
			List<PagosGastosDet> lista = this.findByPagosGastos(entity, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (PagosGastosDet pagosGastosDet : lista)
					listaExt.add(this.convertidor.PagosGastosDetToPagosGastosDetExt(pagosGastosDet));
			}
		} catch (Exception re ) {
			throw re;
		}
		
		return listaExt;
	}
}
