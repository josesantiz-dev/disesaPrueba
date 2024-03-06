package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.giro.cargas.documentos.logica.FacturasRem;
import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.dao.PagosGastosDetDAO;
import net.giro.plataforma.InfoSesion;

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
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(PagosGastosDetExt entityExt) throws Exception {
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			return this.ifzPagosGastosDet.save(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Long save(PagosGastosDet entity) throws ExcepConstraint {
		try {
			return this.ifzPagosGastosDet.save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDet entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDet.delete(entity.getId());;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDetExt entityExt) throws Exception {
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			this.ifzPagosGastosDet.delete(entity.getId());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDet update(PagosGastosDetExt entityExt) throws Exception {
		try {
			PagosGastosDet entity = this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt);
			return this.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDet update(PagosGastosDet entity) throws ExcepConstraint {
		try {
			this.ifzPagosGastosDet.update(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDet findById(Long id) {
		try {
			return this.ifzPagosGastosDet.findById(id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByProperty(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDet.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByPropertyALL(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDet.findByPropertyALL(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findAll() {
		try {
			return this.ifzPagosGastosDet.findAll();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findLikePojoCompleto(Object entity, int max) {
		try {
			return this.ifzPagosGastosDet.findLikePojoCompleto(entity, max);
		} catch (RuntimeException re ) {
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
		} catch (RuntimeException re ) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findDetGtos2MovtoCtas(Object value,int max){
		try {
			return this.ifzPagosGastosDet.findDetGtos2MovtoCtas(value, max);
		} catch (RuntimeException re ) {
			throw re;
		}
	}

	@Override
	public Double saldoRepCajaChica(Object value){
		try{
			return this.ifzPagosGastosDet.saldoRepCajaChica(value);
		}catch(RuntimeException re){
			throw re;
		}
	}

	@Override
	public Long analizaFactura(byte[] fileSrc) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion.getUltimaModificacion(), this.infoSesion.getAcceso().getId());
			net.giro.cargas.documentos.respuesta.Respuesta resp = this.ifzFacturas.analizarXML(fileSrc);
			long errorCode = resp.getErrores().obtenerUltimoError(); 
			if (errorCode != 0L && errorCode != 6L) {
				log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura. ERROR: " + resp.getErrores().obtenerUltimoError());
				return null;
			}

			return (long) resp.getBody().getValor("idComprobante");
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura. Exception: ", e);
			throw e;
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
		} catch (RuntimeException re ) {
			throw re;
		}
	}

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
