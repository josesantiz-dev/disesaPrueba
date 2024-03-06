package net.giro.cxc.logica;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.contabilidad.beans.MensajeTransaccion;
import net.giro.contabilidad.logica.MensajeTransaccionRem;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.dao.FacturaPagosDAO;
import net.giro.cxc.fe.v33.CMetodoPago;
import net.giro.cxc.fe.v33.CMoneda;
import net.giro.cxc.fe.v33.CTipoDeComprobante;
import net.giro.cxc.fe.v33.CUsoCFDI;
import net.giro.cxc.fe.v33.Comprobante;
import net.giro.cxc.fe.v33.Comprobante.Complemento;
import net.giro.cxc.fe.v33.Comprobante.Conceptos;
import net.giro.cxc.fe.v33.Comprobante.Conceptos.Concepto;
import net.giro.cxc.fe.v33.Comprobante.Emisor;
import net.giro.cxc.fe.v33.Comprobante.Receptor;
import net.giro.cxc.fe.v33.complemento.pagos.DoctoRelacionado;
import net.giro.cxc.fe.v33.complemento.pagos.Pago;
import net.giro.cxc.fe.v33.complemento.pagos.Pagos;
/*import net.giro.cxc.fe.documentos.Complemento;
import net.giro.cxc.fe.documentos.Comprobante;
import net.giro.cxc.fe.documentos.Concepto;
import net.giro.cxc.fe.documentos.DoctoRelacionado;
import net.giro.cxc.fe.documentos.Emisor;
import net.giro.cxc.fe.documentos.Pago10;
import net.giro.cxc.fe.documentos.Pagos10;
import net.giro.cxc.fe.documentos.Receptor;*/
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.ne.logica.NQueryFac;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.EmpresaCertificadoRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;

@Stateless
public class FacturaPagosFac implements FacturaPagosRem {
	private static Logger log = Logger.getLogger(FacturaPagosFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private FacturaPagosDAO ifzFacturaPagos;
	private FacturaRem ifzFacturas;
	private EmpresaCertificadoRem ifzEmpCertificado;
	private CuentasBancariasRem ifzCtas;
	private ObraDAO ifzObras;
	private MensajeTransaccionRem ifzMsgTransaccion;
	private ConvertExt convertidor;
	
	
 	public FacturaPagosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
            this.ctx = new InitialContext(p);
            this.ifzFacturas 	 	= (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
            this.ifzFacturaPagos 	= (FacturaPagosDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaPagosImp!net.giro.cxc.dao.FacturaPagosDAO");
            this.ifzCtas 			= (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
            this.ifzObras 			= (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
            this.ifzMsgTransaccion 	= (MensajeTransaccionRem) this.ctx.lookup("ejb:/Logica_Contabilidad//MensajeTransaccionFac!net.giro.contabilidad.logica.MensajeTransaccionRem");
            this.ifzEmpCertificado 	= (EmpresaCertificadoRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresaCertificadoFac!net.giro.plataforma.logica.EmpresaCertificadoRem");
            
            this.convertidor = new ConvertExt();
            this.convertidor.setFrom("FacturaPagosFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaPagosFac", e);
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
	public long save(FacturaPagos entity) throws Exception {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.save(entity);
		} catch (Exception re) {	
			log.error("Error en el método save", re);
			throw re;
		}
	}

	@Override
	public void update(FacturaPagos entity) throws Exception {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			this.ifzFacturaPagos.update(entity);
		} catch (Exception re) {	
			log.error("Error en el método update", re);
			throw re;
		}
	}

	@Override
	public void delete(FacturaPagos entity) throws Exception {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			this.ifzFacturaPagos.delete(entity.getId());
		} catch (Exception re) {	
			log.error("Error en el método delete", re);
			throw re;
		}
	}

	@Override
	public FacturaPagos findById(long id) {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.findById(id);
		} catch (Exception re) {	
			log.error("Error en el método findById", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findAll() {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.findAll();
		} catch (Exception re) {	
			log.error("Error en el método findAll", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findByProperty(String propertyName, Object value) {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.findByProperty(propertyName, value, 0);
		} catch (Exception re) {	
			log.error("Error en el método safindByPropertyve", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findLikeProperty(String propertyName, String value) {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.findLikeProperty(propertyName, value, 0);
		} catch (Exception re) {	
			log.error("Error en el método findLikeProperty", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagos> findByFactura(Long idFactura) throws Exception {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.ifzFacturaPagos.findByFactura(idFactura); 
		} catch (Exception e) {
			log.error("Error en el método findByFactura(Long idFactura)", e);
			throw e;
		}
	}

	@Override
	public List<FacturaPagos> findByFactura(Factura idFactura) throws Exception {
		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			return this.findByFactura(idFactura.getId()); 
		} catch (Exception e) {
			log.error("Error en el método findByFactura(Factura idFactura)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findLiquidado(Long idFactura) throws Exception {
		BigDecimal liquidado = BigDecimal.ZERO;
		List<FacturaPagos> pagado = null;
		

		try {
			this.ifzFacturaPagos.setEmpresa(getIdEmpresa());
			pagado = this.ifzFacturaPagos.findByFactura(idFactura); 
			if (pagado != null && ! pagado.isEmpty()) {
				for (FacturaPagos pago : pagado)
					liquidado = liquidado.add(pago.getMonto());
			}
			
			return liquidado;
		} catch (Exception e) {
			log.error("Error en el método findByFactura(Long idFactura)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findLiquidado(Factura idFactura) throws Exception {
		try {
			return this.findLiquidado(idFactura.getId()); 
		} catch (Exception e) {
			log.error("Error en el método findByFactura(Factura idFactura)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(Long entityId) throws Exception {
		try {
			return this.enviarTransaccion(this.findByIdExt(entityId));
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entityId, idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public Respuesta enviarTransaccion(FacturaPagos entity) throws Exception {
		try {
			return this.enviarTransaccion(this.convertidor.FacturaPagosToFacturaPagosExt(entity));
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorCobrar.FacturaPagosFac.enviarTransaccion(entity, idEmpresa)", e);
			throw e;
		}
	}
	
	@Override
	public int findParcialidad(long idFactura) throws Exception {
		List<FacturaPagos> pagos = null;
		int parcialidad = 0;
		
		if (idFactura > 0L) {
			pagos = this.findByFactura(idFactura);
			if (pagos != null && ! pagos.isEmpty()) 
				parcialidad = pagos.size();
		}
		
		return parcialidad + 1;
	}

	@Override
	public Comprobante generarComprobante(long idFacturaPago) throws Exception {
		FacturaExt factura = null;
		FacturaPagosExt pago = null;
		EmpresaCertificado empCertificado = null;
		String serieComplementoPago = "";
		String folioComplementoPago = "";
		double pagado = 0;
		boolean actualizarPago = false;
		// --------------------------------------
		Comprobante comprobante = null;
		Emisor emisor = null;
		Receptor receptor = null;
		Conceptos conceptos = null;
		Concepto concepto = null;
		Complemento complemento = null;
		Pagos pagos10 = null;
		Pago pago10 = null;
		DoctoRelacionado doctoRelacionado = null;
		// --------------------------------------
		XMLGregorianCalendar date = null;
		DateFormat format = null;
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		
		try {
			if (idFacturaPago <= 0L)
				return comprobante;

			format = new SimpleDateFormat(FORMATER);
			pago = this.findByIdExt(idFacturaPago);
			factura = pago.getIdFactura();
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(getIdEmpresa());
			pagado = this.findLiquidado(factura).doubleValue();
			
			serieComplementoPago = pago.getSerie();
			if (pago.getSerie() == null || "".equals(pago.getSerie().trim())) {
				serieComplementoPago = factura.getIdEmpresa().getSerieComplementoPago();
				pago.setSerie(serieComplementoPago);
				actualizarPago = true;
			}
			
			folioComplementoPago = pago.getFolio();
			if (pago.getFolio() == null || "".equals(pago.getFolio().trim())) {
				folioComplementoPago = this.folioComplementoPago(factura.getIdEmpresa().getId(), serieComplementoPago);
				pago.setFolio(folioComplementoPago);
				actualizarPago = true;
			}
			
			if (serieComplementoPago == null || "".equals(serieComplementoPago)) {
				log.error("No se puede timbrar el pago, No ha sido configurado ninguna serie para el complemento de pago");
				return null;
			}
			
			if (actualizarPago) 
				this.update(pago);
			
			emisor = new Emisor();
			emisor.setRfc(factura.getIdEmpresa().getRfc());
			emisor.setNombre(factura.getIdEmpresa().getEmpresa());
			emisor.setRegimenFiscal(factura.getIdEmpresa().getCodigoRegimenFiscal());
			
			receptor = new Receptor();
			receptor.setRfc(factura.getRfc());
			receptor.setNombre(factura.getCliente());
			receptor.setUsoCFDI(CUsoCFDI.P_01); // Por Definir
			
			concepto = new Concepto();
			concepto.setCantidad(new BigDecimal(1.0));
			concepto.setClaveProdServ("84111506");
			concepto.setClaveUnidad("ACT");
			concepto.setDescripcion("Pago");
			concepto.setImporte(BigDecimal.ZERO);
			concepto.setValorUnitario(BigDecimal.ZERO);
			
			conceptos = new Conceptos();
			conceptos.getConcepto().add(concepto);
			
			doctoRelacionado = new DoctoRelacionado();
			doctoRelacionado.setIdDocumento(factura.getUuid());
			doctoRelacionado.setMonedaDR(CMoneda.fromValue(factura.getAbreviaturaMoneda()));
			doctoRelacionado.setMetodoDePagoDR(CMetodoPago.fromValue(factura.getIdMetodoPago().getValor()));
			doctoRelacionado.setSerie(factura.getSerie());
			doctoRelacionado.setFolio(factura.getFolio());
			if (! "MXN".equals(factura.getAbreviaturaMoneda()))
				doctoRelacionado.setTipoCambioDR(formatoNumerico(factura.getTipoCambio()));
			doctoRelacionado.setNumParcialidad(BigInteger.valueOf(pago.getParcialidad()));
			doctoRelacionado.setImpSaldoAnt(formatoNumerico(pago.getSaldoAnterior()));
			doctoRelacionado.setImpSaldoInsoluto(formatoNumerico(pago.getSaldoInsoluto()));
			if (pagado > 0)
				doctoRelacionado.setImpPagado(formatoNumerico(pagado));
			
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(Calendar.getInstance().getTime()));
			
			pago10 = new Pago();
			pago10.setFechaPago(date);
			pago10.setFormaDePagoP(pago.getIdFormaPago().getClaveSat());
			pago10.setMonedaP(CMoneda.fromValue(pago.getIdMoneda().getAbreviacion()));
			pago10.setMonto(formatoNumerico(pago.getMonto()));
			if (! "MXN".equals(pago.getIdMoneda().getAbreviacion()))
				pago10.setTipoCambioP(formatoNumerico(pago.getTipoCambio()));
			pago10.setNumOperacion(pago.getReferenciaFormaPago());
			if ("01".equals(pago.getIdFormaPago().getClaveSat()))
				pago10.setNumOperacion("01");
			pago10.getDoctoRelacionado().add(doctoRelacionado);
			
			pagos10 = new Pagos();
			pagos10.setVersion("1.0");
			pagos10.getPago().add(pago10);
			
			complemento = new Complemento();
			complemento.getAny().add(pagos10);
			
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(factura.getFechaEmision()));
			
			comprobante = new Comprobante();
			comprobante.setVersion("3.3");
			comprobante.setTipoDeComprobante(CTipoDeComprobante.P);
			comprobante.setFecha(date);
			comprobante.setCertificado(new String(Base64.encodeBase64(empCertificado.getCertificadoData())));
			comprobante.setLugarExpedicion(factura.getIdSucursal().getColonia().getCp());
			comprobante.setNoCertificado(empCertificado.getNoCertificado());
			comprobante.setSello("");
			comprobante.setSerie(serieComplementoPago); 
			comprobante.setFolio(folioComplementoPago); 
			comprobante.setMoneda(CMoneda.XXX);
			comprobante.setSubTotal(BigDecimal.ZERO);
			comprobante.setTotal(BigDecimal.ZERO);
			comprobante.setEmisor(emisor);
			comprobante.setReceptor(receptor);
			comprobante.setConceptos(conceptos);
			comprobante.getComplemento().add(complemento);
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.generarComprobante(long idFacturaPago)", e);
			throw e;
		}
		
		return comprobante;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long folioComplementoPago(String folioComplementoPago) throws Exception {
		NQueryFac query = null;
		List<Object> lista = null;
		Iterator<Object> it = null;
		BigInteger val = null;
		String secuencia = "";
		String queryString = "";
		long folio = 0;
		
		try {
			if (folioComplementoPago == null || "".equals(folioComplementoPago))
				return 0L;
			
			secuencia += "seq_comprobante_pago_serie_" + folioComplementoPago.trim().toLowerCase();
			queryString = "select 1 from pg_class where relkind = 'S' and relname = '" + secuencia + "'";
			query = new NQueryFac();
			lista = query.findNativeQuery(queryString);
			if (lista == null || lista.isEmpty()) {
				queryString = "CREATE SEQUENCE " + secuencia + " INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;";
				query.ejecutaAccion(queryString);
				folio = 0;
			}
			
			if (folio <= 0) {
				queryString = "select nextval('" + secuencia + " ') AS folio ";
				lista = query.findNativeQuery(queryString);
				it = lista.iterator();
				while (it.hasNext()) {
					val = (BigInteger) it.next();
					folio = val.longValue();
				}
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaPagosFac.folioComplementoPago(String folioComplementoPago)", e);
			throw e;
		}
		
		return folio;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long folioActualComplementoPago(String folioComplementoPago) throws Exception {
		NQueryFac query = null;
		List<Object> lista = null;
		Iterator<Object> it = null;
		BigInteger val = null;
		String secuencia = "";
		String queryString = "";
		long folio = 0;
		
		try {
			if (folioComplementoPago == null || "".equals(folioComplementoPago))
				return -1L;
			
			secuencia += "seq_comprobante_pago_serie_" + folioComplementoPago.trim().toLowerCase();
			queryString = "select 1 from pg_class where relkind = 'S' and relname = '" + secuencia + "'";
			query = new NQueryFac();
			lista = query.findNativeQuery(queryString);
			if (lista == null || lista.isEmpty()) 
				return 0L;
			
			queryString = "select currval('" + secuencia + " ') AS folio ";
			lista = query.findNativeQuery(queryString);
			it = lista.iterator();
			while (it.hasNext()) {
				val = (BigInteger) it.next();
				folio = val.longValue();
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaPagosFac.folioComplementoPago(String folioComplementoPago)", e);
			folio = 0L;
		}
		
		return folio;
	}

	@Override
	public String folioComplementoPago(long idEmpresa, String folioComplementoPago) throws Exception {
		String secuencia = "";
		long folio = 0;
		
		try {
			folio = this.folioComplementoPago(folioComplementoPago);
			if (folio <= 0L)
				return "";
			if (idEmpresa > 10000000)
				idEmpresa = idEmpresa - 10000000;
			secuencia = idEmpresa + (new DecimalFormat("#00000")).format(folio);
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorCobrar.FacturaPagosFac.folioComplementoPago(long idEmpresa, String folioComplementoPago)", e);
			throw e;
		}
		
		return secuencia;
	}

	@Override
	public FacturaPagos convertir(FacturaPagosExt pojoTarget) throws Exception {
		return this.convertidor.FacturaPagosExtToFacturaPagos(pojoTarget);
	}

	@Override
	public FacturaPagosExt convertir(FacturaPagos pojoTarget) throws Exception {
		return this.convertidor.FacturaPagosToFacturaPagosExt(pojoTarget);
	}
	
	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	@Override
	public long save(FacturaPagosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.FacturaPagosExtToFacturaPagos(entityExt));
		} catch (Exception re) {	
			log.error("Error en el método save", re);
			throw re;
		}
	}
	
	@Override
	public void update(FacturaPagosExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.FacturaPagosExtToFacturaPagos(entityExt));
		} catch (Exception re) {	
			log.error("Error en el método update", re);
			throw re;
		}
	}
	
	@Override
	public void delete(FacturaPagosExt entity) throws Exception {
		try {
			this.delete(this.convertidor.FacturaPagosExtToFacturaPagos(entity));
		} catch (Exception re) {	
			log.error("Error en el método delete", re);
			throw re;
		}
	}

	@Override
	public FacturaPagosExt findByIdExt(long id) throws Exception {
		try {
			return this.convertidor.FacturaPagosToFacturaPagosExt(this.findById(id));
		} catch (Exception re) {	
			log.error("Error en el método findByIdExt", re);
			throw re;
		}
	}

	@Override
	public List<FacturaPagosExt> findByPropertyExt(String propertyName, Object value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		List<FacturaPagos> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaPagos var : lista)
					listaExt.add(this.convertidor.FacturaPagosToFacturaPagosExt(var));
			}
		} catch (Exception e) {
			log.error("Error en el método findByPropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<FacturaPagosExt> findLikePropertyExt(String propertyName, String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		List<FacturaPagos> lista = null;
		
		try {
			lista = this.findLikeProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaPagos var : lista)
					listaExt.add(this.convertidor.FacturaPagosToFacturaPagosExt(var));
			}
		} catch (Exception e) {
			log.error("Error en el método findLikePropertyExt", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<FacturaPagosExt> findLikeFolioFactura(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		List<FacturaPagos> listPagos = null;
		List<Factura> listaFacturas = null;
		List<Long> values = null;
		
		try {
			listaFacturas = this.ifzFacturas.findLikeProperty("fac.folioFactura", value);
			if (listaFacturas != null && ! listaFacturas.isEmpty()) {
				values = new ArrayList<Long>();
				for (Factura var : listaFacturas)
					values.add(var.getId());
				
				listPagos = this.ifzFacturaPagos.findInProperty("idFactura", values, 0);
				for (FacturaPagos var : listPagos)
					listaExt.add(this.convertidor.FacturaPagosToFacturaPagosExt(var));
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<FacturaPagos> findByFactura(FacturaExt idFactura) throws Exception {
		try {
			return this.findByFactura(idFactura.getId()); 
		} catch (Exception e) {
			log.error("Error en el método findByFactura(FacturaExt idFactura)", e);
			throw e;
		}
	}

	@Override
	public BigDecimal findLiquidado(FacturaExt idFactura) throws Exception {
		try {
			return this.findLiquidado(idFactura.getId()); 
		} catch (Exception e) {
			log.error("Error en el método findLiquidado(FacturaExt idFactura)", e);
			throw e;
		}
	}

	@Override
	public List<FacturaPagosExt> findLikeBeneficiario(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		List<FacturaPagos> lista = null;
		
		try {
			lista = this.findLikeProperty("beneficiario", value);
			if (lista != null && ! lista.isEmpty()) {
				for (FacturaPagos var : lista)
					listaExt.add(this.convertidor.FacturaPagosToFacturaPagosExt(var));
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<FacturaPagosExt> findLikeCuentaBancaria(String value) throws Exception {
		List<FacturaPagosExt> listaExt = new ArrayList<FacturaPagosExt>();
		List<FacturaPagos> listPagos = null;
		List<CuentaBancaria> listaCuentas = null;
		List<Long> values = null;
		
		try {
			this.ifzCtas.setInfoSesion(this.infoSesion);
			listaCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", value);
			if (! listaCuentas.isEmpty()) {
				values = new ArrayList<Long>();
				for (CuentaBancaria var : listaCuentas)
					values.add(var.getId());
				
				listPagos = this.ifzFacturaPagos.findInProperty("idCuentaDeposito", values, 0);
				for (FacturaPagos var : listPagos) 
					listaExt.add(this.convertidor.FacturaPagosToFacturaPagosExt(var));
			}
		} catch (Exception e) {
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public Respuesta enviarTransaccion(FacturaPagosExt entity) throws Exception {
		Respuesta respuesta = new Respuesta();
		MensajeTransaccion msg = null;
		Long idMensaje = 0L;
		Long idTransaccion = 0L;
		Long idFormaPago = 0L;
		Long idMoneda = 0L;
		Long idCliente = 0L;
		String descMoneda = "";
		String referencia = "";
		String nombreCliente = "";
		String tipoPersona = "";
		String tracking = "";
		Obra pojoObra = null;
		
		try {
			if (entity == null) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico Factura");
				return respuesta;
			}

			pojoObra = this.ifzObras.findById(entity.getIdFactura().getIdObra().getId());
			if (pojoObra == null) {
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo obtener los datos del Cliente");
				return respuesta;
			}
			
			idCliente = pojoObra.getIdCliente();
			nombreCliente = pojoObra.getNombreCliente();
			tipoPersona = pojoObra.getTipoCliente();
			idMoneda = entity.getIdCuentaDeposito().getMoneda().getId();
			descMoneda = entity.getIdCuentaDeposito().getMoneda().getNombre();
			
			// Determinamos Transaccion
			if ("PPD".equals(entity.getIdFactura().getIdMetodoPago().getValor())) {
				// Transaccion 1032: PAGO DE CLIENTES - VENTAS DE CREDITO
				tracking = "Transaccion 1032 disparada desde FacturaPagosFac. Pago " + entity.getId() + " a Factura " + entity.getIdFactura().getFolioFactura() + " (" + entity.getIdFactura().getId() + ")";
				idTransaccion = 1032L; 
			} else {
				// Transaccion 1013: PAGO DE CLIENTES - VENTAS DE CONTADO
				tracking = "Transaccion 1013 disparada desde FacturaPagosFac. Pago " + entity.getId() + " a Factura " + entity.getIdFactura().getFolioFactura() + " (" + entity.getIdFactura().getId() + ")";
				idTransaccion = 1013L; 
			}

			// Forma de Pago y Referencia
			idFormaPago = entity.getIdFormaPago().getId();
			referencia = entity.getReferenciaFormaPago();

			msg = new MensajeTransaccion();
			msg.setIdTransaccion(idTransaccion);
			msg.setIdOperacion(entity.getId());
			msg.setIdMoneda(idMoneda);
			msg.setDescripcionMoneda(descMoneda);
			msg.setImporte(entity.getMonto());
			msg.setIdPersonaReferencia(idCliente);
			msg.setNombrePersonaReferencia(nombreCliente);
			msg.setTipoPersona(tipoPersona);
			msg.setReferencia("");
			msg.setIdFormaPago(idFormaPago);
			msg.setReferenciaFormaPago(referencia);
			msg.setIdUsuarioCreacionRegistro(entity.getCreadoPor());
			msg.setIdSucursal(entity.getIdFactura().getIdSucursal().getId());
			msg.setCreadoPor(entity.getCreadoPor());
			msg.setFechaCreacion(entity.getFechaCreacion());
			msg.setAnuladoPor(0L);
			msg.setFechaAnulacion(null);
			msg.setEstatus(0);
			msg.setFechaRegistro(entity.getIdFactura().getFechaEmision());
			msg.setIdEmpresa(getIdEmpresa());
			
			// Registramos el mensaje
			this.ifzMsgTransaccion.setInfoSesion(this.infoSesion);
			idMensaje = this.ifzMsgTransaccion.save(msg);
			log.info("Tracking ID " + idMensaje + " : " + tracking);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("mensaje", idMensaje);
			respuesta.getErrores().setDescError("Factura Provisionada");
		} catch (Exception e) {
			log.error("Error Logica_CuentasPorPagar.PagosGastosFac.enviarTransaccion(PagosGastosEntity, idEmpresa)", e);
			respuesta.getErrores().setCodigoError(-1L);
			respuesta.getErrores().setDescError("Error. No se pudo provisionar la factura");
			respuesta.getBody().addValor("exception", e);
		} 
		
		return respuesta;
	}

	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
	
	private BigDecimal formatoNumerico(double value) {
		return formatoNumerico(new BigDecimal(value));
	}
	
	private BigDecimal formatoNumerico(BigDecimal value) {
		DecimalFormat formatter = new DecimalFormat("#0.00####");
		BigDecimal resultado = BigDecimal.ZERO;
		
		if (value == null)
			return resultado;
		resultado = new BigDecimal(formatter.format(value));
		
		return resultado;
	}
}
