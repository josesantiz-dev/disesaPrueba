package net.giro.cargas.documentos.logica;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import mx.gob.sat.cfdi.v33.Comprobante;
import mx.gob.sat.cfdi.v33.complementos.nomina12.Nomina;
import net.giro.cargas.documentos.beans.ComprobacionNomina;
import net.giro.cargas.documentos.dao.ComprobacionNominaDAO;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Stateless
public class ComprobacionNominaFac implements ComprobacionNominaRem {
	private static Logger log = Logger.getLogger(ComprobacionNominaFac.class);
	private InfoSesion infoSesion;
	// ----------------------------------------------------
	private ComprobacionNominaDAO ifzBase;

	public ComprobacionNominaFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try { 
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ComprobacionNominaDAO) ctx.lookup("ejb:/Model_Cargas_Documentos//ComprobacionNominaImp!net.giro.cargas.documentos.dao.ComprobacionNominaDAO");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ComprobacionNomina save(ComprobacionNomina entity) throws Exception {
		long idComprobacionNomina = 0L;
		
		try {
			if (entity != null) {
				// Actualizamos entity
				if (entity.getId() != null && entity.getId() > 0L) {
					this.ifzBase.update(entity);
					return entity;
				}
				
				// Comprobamos y recuperamos entity
				idComprobacionNomina = comprobarCFDI(entity.getExpresionImpresa());
				if (idComprobacionNomina <= 0L) 
					idComprobacionNomina = this.ifzBase.save(entity, getCodigoEmpresa());
				entity = this.findById(idComprobacionNomina);
			}
			
			return entity;
		} catch (Exception e) {
			log.error("Ocurrio un problema en save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ComprobacionNomina> saveOrUpdateList(List<ComprobacionNomina> entities) throws Exception {
		try {
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema en saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cancelar(long idComprobacionNomina) throws Exception {
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteCFDI(long idComprobacionNomina) throws Exception {
		try {
			this.ifzBase.delete(idComprobacionNomina);
		} catch (Exception e) {
			log.error("Ocurrio un problema en deleteCFDI(idComprobacionNomina)", e);
			throw e;
		}
	}

	@Override
	public ComprobacionNomina findById(long idComprobacionNomina) throws Exception {
		try {
			return this.ifzBase.findById(idComprobacionNomina);
		} catch (Exception e) {
			log.error("Ocurrio un problema en findById(idComprobacionNomina)", e);
			throw e;
		}
	}

	@Override
	public Respuesta comprobarEstatusSAT(long idComprobacionNomina) throws Exception {
		return null;
	}

	@Override
	public Respuesta importarCFDI(byte[] fileSrc) throws Exception {
		Respuesta respuesta = null;
		ComprobacionNomina comprobacion = null;
		// ------------------------------------------------------------
		String strXML = "";
		Nomina cfdi = null;
		Serializer serializer = null;

		try {
			respuesta = new Respuesta();
			serializer = new Persister();
			strXML = readFile(fileSrc);
			fileSrc = aMinusculas(strXML);
			cfdi = serializer.read(Nomina.class, new ByteArrayInputStream(fileSrc), false);
			//cfdi.setSerie(setDefaultValue(cfdi.getSerie(), "TMP"));
			//cfdi.setFolio(setDefaultValue(cfdi.getFolio(), "00000"));
			//cfdi.getEmisor().setNombre(setDefaultValue(cfdi.getEmisor().getNombre(), ""));
			// Generamos comprobacion
			comprobacion = toComprobacionPago(cfdi);
			comprobacion = this.save(comprobacion);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			respuesta.getBody().addValor("cfdi", cfdi); 
			respuesta.getBody().addValor("comprobacion", comprobacion); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al Importar el CFDI", e);
			respuesta.getErrores().addCodigo("CARGAS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));
		} 

		return null;
	}

	@Override
	public Respuesta importarCFDI(Comprobante comprobante) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------
	
	private long comprobarCFDI(String expresionImpresa) {
		return 0L;
	}
	
	private ComprobacionNomina toComprobacionPago(Nomina cfdi) {
		ComprobacionNomina comprobacion = null;
		//SimpleDateFormat dtFormat = null;
		//String expresionImpresa = "";

		try {
			//dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//expresionImpresa = "?re=" + cfdi.getEmisor().getRfc() + "&rr=" + cfdi.getReceptor().getRfc() + "&tt=" + cfdi.getTotal() + "&id=" + cfdi.getComplemento().getTimbreFiscalDigital().getUUID();
			comprobacion = new ComprobacionNomina();
			/*comprobacion.setEstatus(0);
			comprobacion.setDescripcion("");
			comprobacion.setExpresionImpresa(expresionImpresa);
			// ---------------------------------------------------------------------------------------------------------------------------------
			comprobacion.setSerie(cfdi.getSerie());
			comprobacion.setFolio(cfdi.getFolio());
			comprobacion.setFecha(dtFormat.parse(cfdi.getFecha().replace("T", " ")));
			comprobacion.setFechaTimbrado(dtFormat.parse(cfdi.getComplemento().getTimbreFiscalDigital().getFechaTimbrado().replace("T", " ")));
			comprobacion.setFacturaFolioFiscal(cfdi.getComplemento().getTimbreFiscalDigital().getUUID());
			comprobacion.setPagoFecha(dtFormat.parse(cfdi.getFecha().replace("T", " ")));
			comprobacion.setPagoFormaPago(cfdi.getFormaPago());
			comprobacion.setPagoMoneda(cfdi.getMoneda());
			comprobacion.setPagoMonto(getMonto(cfdi.getTotal()));
			comprobacion.setPagoNumOperacion("");
			// ---------------------------------------------------------------------------------------------------------------------------------
			comprobacion.setEmisor(cfdi.getEmisor().getNombre());
			comprobacion.setEmisorRfc(cfdi.getEmisor().getRfc());
			comprobacion.setEmisorRegimen("");
			// ---------------------------------------------------------------------------------------------------------------------------------
			comprobacion.setReceptor(cfdi.getReceptor().getNombre());
			comprobacion.setReceptorRfc(cfdi.getReceptor().getRfc());
			comprobacion.setReceptorUsoCfdi("");
			// ---------------------------------------------------------------------------------------------------------------------------------
			comprobacion.setFacturaFolioFiscal("");
			comprobacion.setFacturaSerie("");
			comprobacion.setFacturaFolio("");
			comprobacion.setFacturaParcialidad("");
			comprobacion.setFacturaPagado(getMonto(cfdi.getTotal()));
			comprobacion.setFacturaSaldoAnterior(getMonto(cfdi.getTotal()));
			comprobacion.setFacturaSaldoInsoluto(getMonto(cfdi.getTotal()));
			comprobacion.setFacturaMetodoPago("");
			comprobacion.setFacturaMoneda("");*/
			// ---------------------------------------------------------------------------------------------------------------------------------
			comprobacion.setIdEmpresa(getIdEmpresa());
			comprobacion.setCreadoPor(this.infoSesion.getAcceso().getId());
			comprobacion.setFechaCreacion(Calendar.getInstance().getTime());
			comprobacion.setModificadoPor(this.infoSesion.getAcceso().getId());
			comprobacion.setFechaModificacion(Calendar.getInstance().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return comprobacion;
	}

	/*private String setDefaultValue(String value, String defaultValue) {
		defaultValue = (defaultValue != null && ! "".equals(defaultValue.trim()) ? defaultValue.trim() : "");
		value = (value != null && ! "".equals(value.trim()) ? value.trim() : defaultValue);
		return value;
	}
	
	private BigDecimal getMonto(String value) {
		value = (value != null && ! "".equals(value.trim()) ? value.trim() : "0");
		return new BigDecimal(value);
	}*/
	
	private String readFile(byte[] archivoSrc) throws Exception {
		return readFile(archivoSrc, null);
	}
	
	private String readFile(byte[] archivoSrc, String charsetName) throws Exception {
		InputStream stream = null;
		BufferedReader br = null;
		StringBuilder sb = null;
		String line = "";
		String fileString = "";
		
		try {
			sb = new StringBuilder();
			stream = new ByteArrayInputStream(archivoSrc);
			charsetName = (charsetName != null && ! "".equals(charsetName.trim()) ? charsetName.trim().toUpperCase() : "UTF-8");
			br = new BufferedReader(new InputStreamReader(stream, charsetName));
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			
			fileString = sb.toString();
			if (fileString.charAt(0) != '<')
				fileString = fileString.substring(1, fileString.length() - 1);
			return fileString;
		} catch (Exception e) {
			log.error("Ocurrio un problema en readFile", e);
			throw e;
		}
	}

	private byte[] aMinusculas(String xml) throws IOException, URISyntaxException, TransformerException {
		ByteArrayOutputStream bf = null;
		TransformerFactory factory = null;
		Transformer transformer = null;
		Source xslt = null;
		Source text = null;
		
		try {
			factory = TransformerFactory.newInstance();
			bf = new ByteArrayOutputStream();
			text = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
			xslt = new StreamSource(this.getClass().getResourceAsStream("/net/giro/cargas/documentos/beans/min1.xslt"));
			transformer = factory.newTransformer(xslt);
			transformer.transform(text, new StreamResult(bf));
			return bf.toByteArray();
		} catch (Exception e) {
			log.error("Ocurrio un problema al minificar el XML", e);
		}
		
		return xml.getBytes();
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
