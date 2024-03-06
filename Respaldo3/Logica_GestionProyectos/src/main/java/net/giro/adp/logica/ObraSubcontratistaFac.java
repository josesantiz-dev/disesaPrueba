package net.giro.adp.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.CFDIRow;
import net.giro.adp.beans.ObraSubcontratista;
import net.giro.adp.beans.ObraSubcontratistaExt;
import net.giro.adp.beans.ObraSubcontratistaImpuestosExt;
import net.giro.adp.dao.ObraSubcontratistaDAO;
import net.giro.cargas.documentos.logica.ComprobanteCFDIRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Stateless
public class ObraSubcontratistaFac implements ObraSubcontratistaRem {
	private static Logger log = Logger.getLogger(ObraSubcontratistaFac.class);
	private InfoSesion infoSesion;
	private ObraSubcontratistaDAO ifzBase;
	private ObraSubcontratistaImpuestosRem ifzImpuestos;
	private ComprobanteCFDIRem ifzCFDI;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private ConvertExt convertidor;

	public ObraSubcontratistaFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ObraSubcontratistaDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraSubcontratistaImp!net.giro.adp.dao.ObraSubcontratistaDAO");
			this.ifzImpuestos = (ObraSubcontratistaImpuestosRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraSubcontratistaImpuestosFac!net.giro.adp.logica.ObraSubcontratistaImpuestosRem");
			this.ifzCFDI = (ComprobanteCFDIRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobanteCFDIFac!net.giro.cargas.documentos.logica.ComprobanteCFDIRem");
            this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
            this.ifzPersonas = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setMostrarSystemOut(false);
			this.convertidor.setFrom(this.getClass().getCanonicalName());
		} catch(Exception e) {
			log.error("Ocurrio un problema al instanciar el EJB", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public ObraSubcontratista save(ObraSubcontratista entity) throws Exception {
		long idObraSubcontratista = 0L;
		
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			if (entity.getId() != null && entity.getId() > 0L) {
				this.ifzBase.update(entity);
				return entity;
			}
			
			idObraSubcontratista = this.ifzBase.save(entity, getCodigoEmpresa());
			entity.setId(idObraSubcontratista);
			return entity;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratista> saveOrUpdateList(List<ObraSubcontratista> entities) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idObraSubcontratista) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return;
			}
			
			this.ifzBase.delete(idObraSubcontratista);
		} catch (Exception e) {
			control("Ocurrio un problema al eliminar: delete(idObraSubcontratista)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratista> findAll(long idObra, long idEmpleado, String orderBy) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.findAll(idObra, idEmpleado, orderBy);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findAll(idObra, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratista> findLikeProperty(String propertyName, Object value, long idObra, long idEmpleado, String orderBy, int limite) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.findLikeProperty(propertyName, value, idObra, idEmpleado, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findLikeProperty(propertyName, value, idObra, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratista> findByProperty(String propertyName, Object value, long idObra, long idEmpleado, String orderBy, int limite) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.findByProperty(propertyName, value, idObra,  idEmpleado, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findByProperty(propertyName, value, idObra, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Respuesta procesarCFDI(byte[] fileSrc, String fileName, String originalFileName) throws Exception {
		Respuesta respuesta = null;
		CFDIRow row = null;
		HashMap<String, Object> cfdiValores = null;
		List<Negocio> negocios = null;
		List<Persona> personas = null;
		Long idComprobante = 0L;
		Long idEmisor = 0L;
		String emisorRfc = "";
		String emisor = "";

		try {
			respuesta = new Respuesta();
			fileName = (fileName != null && ! "".equals(fileName.trim()) ? fileName.trim() : "NO-NAME");
			originalFileName = (originalFileName != null && ! "".equals(originalFileName.trim()) ? originalFileName.trim() : "CXP-GP");
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				respuesta.getErrores().addCodigo("GP", 1L);
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se puede comprobar la sesion de Usuario");
				return respuesta;
			}
			
			if (fileSrc == null || fileSrc.length <= 0) {
				control("No indico el XML");
				respuesta.getErrores().addCodigo("GP", 1L);
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico el XML");
				return respuesta;
			}
			
			this.ifzCFDI.setInfoSesion(this.infoSesion);
			respuesta = this.ifzCFDI.importarXML(fileSrc, fileName);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control("Ocurrio un problema al importar/procesar el CFDI indicado");
				return respuesta;
			}

			cfdiValores = (HashMap<String, Object>) respuesta.getBody().getValor("cfdi_map");
			if (cfdiValores == null || cfdiValores.isEmpty()) {
				control("No indico el XML");
				respuesta.getErrores().addCodigo("GP", 1L);
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No indico el XML");
				return respuesta;
			}

			idComprobante = (cfdiValores.get("idComprobante") != null ? (Long) cfdiValores.get("idComprobante") : 0L);
			emisor = cfdiValores.get("emisor").toString();
			emisorRfc = cfdiValores.get("emisorRFC").toString();
			this.ifzNegocios.setInfoSesion(this.infoSesion);
			negocios = this.ifzNegocios.findLikeProperty("rfc", emisorRfc, 0);
			if (negocios != null && ! negocios.isEmpty()) 
				idEmisor = negocios.get(0).getId();
			if (idEmisor != null && idEmisor <= 0L) {
				this.ifzPersonas.setInfoSesion(this.infoSesion);
				personas = this.ifzPersonas.findLikeProperty("rfc", emisorRfc, 0);
				if (personas != null && ! personas.isEmpty()) 
					idEmisor = personas.get(0).getId();
			}

			row = new CFDIRow();
			row.setIdComprobante(idComprobante);
			row.setExpresionImpresa(cfdiValores.get("expresionImpresa").toString());
			row.setFactura(cfdiValores.get("factura").toString());
			row.setUuid(cfdiValores.get("uuid").toString());
			row.setIdEmisor(idEmisor);
			row.setEmisor(emisor);
			row.setEmisorRfc(emisorRfc);
			row.setReceptor(cfdiValores.get("receptor").toString());
			row.setReceptorRfc(cfdiValores.get("receptorRFC").toString());
			row.setConcepto(cfdiValores.get("concepto1").toString());
			row.setMoneda(cfdiValores.get("moneda").toString());
			row.setTipoCambio((BigDecimal) cfdiValores.get("tipoCambio"));
			row.setSubtotal((BigDecimal) cfdiValores.get("subtotal"));
			row.setImpuestos((BigDecimal) cfdiValores.get("impuestos"));
			row.setRetenciones((BigDecimal) cfdiValores.get("retenciones"));
			row.setTotal((BigDecimal) cfdiValores.get("total"));
			row.setTotalPesos((BigDecimal) cfdiValores.get("totalPesos"));
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			respuesta.getBody().addValor("cfdi", row);
		} catch (Exception e) {
			control("Ocurrio un problema en procesarCFDI\n Exception:\n", e);
			respuesta.getErrores().addCodigo("GP", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar el CFDI (XML) indicado");
		} finally {
			respuesta.getBody().addValor("pojoAcuse", null);
			respuesta.getBody().addValor("pojoComprobante", null);
			respuesta.getBody().addValor("pojoComprobacionFactura", null);
		}
		
		return respuesta;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public ObraSubcontratistaExt save(ObraSubcontratistaExt extendido) throws Exception {
		List<ObraSubcontratistaImpuestosExt> impuestos = null;
		ObraSubcontratista entity = null;
		
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			entity = this.convertidor.getPojo(extendido);
			entity = this.save(entity);
			extendido.setId(entity.getId());
			extendido.setGuardable(false);
			extendido.setNuevo(false);
			
			impuestos = extendido.getListImpuestos();
			if (impuestos != null && ! impuestos.isEmpty()) {
				for (ObraSubcontratistaImpuestosExt impuesto : impuestos)
					impuesto.setIdObraSubcontratista(entity);
				this.ifzImpuestos.setInfoSesion(this.infoSesion);
				impuestos = this.ifzImpuestos.saveOrUpdateListExt(impuestos);
				extendido.setListImpuestos(impuestos);
			}
			
			return extendido;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: save(extendido)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaExt> saveOrUpdateListExt(List<ObraSubcontratistaExt> extendidos) throws Exception {
		List<ObraSubcontratistaExt> guardados = null;

		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			guardados = new ArrayList<ObraSubcontratistaExt>();
			for (ObraSubcontratistaExt extendido : extendidos) 
				guardados.add(this.save(extendido));
			return guardados;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaExt> findAllExt(long idObra, long idEmpleado, String orderBy) throws Exception {
		List<ObraSubcontratistaExt> extendidos = null;
		List<ObraSubcontratista> entities = null;
		ObraSubcontratistaExt extendido = null;
		List<ObraSubcontratistaImpuestosExt> impuestos = null;
		
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			entities = this.ifzBase.findAll(idObra, idEmpleado, orderBy);
			entities = (entities != null ? entities : new ArrayList<ObraSubcontratista>());
			for (ObraSubcontratista entity : entities) {
				extendido = this.convertidor.getExtendido(entity);
				extendido.setGuardable(false);
				this.ifzImpuestos.setInfoSesion(this.infoSesion);
				impuestos = this.ifzImpuestos.findAllExt(extendido.getId(), "");
				if (impuestos != null && ! impuestos.isEmpty())
					extendido.setListImpuestos(impuestos);
				// -------------------------------------------------------------------------------------
				extendidos = (extendidos != null ? extendidos : new ArrayList<ObraSubcontratistaExt>());
				extendidos.add(extendido);
			}
			return extendidos;
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findAllExt(idObra, orderBy)", e);
			throw e;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	
	private void control(String mensaje) {
		control(mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		log.error(this.infoSesion.getAcceso().getUsuario().getUsuario() + " - " + mensaje, throwable);
	}

	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
