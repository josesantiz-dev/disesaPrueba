package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoRow;
import net.giro.rh.admon.dao.EmpleadoDAO;

@Stateless
public class EmpleadoFac implements EmpleadoRem {
	private static Logger log = Logger.getLogger(EmpleadoFac.class);
	private InfoSesion infoSesion;
	private EmpleadoDAO ifzEmpleado;
	private EmpleadoContratoRem ifzContratos;
	private ConvertExt convertidor;
	private NQueryRem ifzQuery;

	public EmpleadoFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environment);
            this.ifzEmpleado = (EmpleadoDAO) ctx.lookup("ejb:/Model_RecHum//EmpleadoImp!net.giro.rh.admon.dao.EmpleadoDAO");
            this.ifzContratos = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
            this.convertidor = new ConvertExt(); 
            this.convertidor.setFrom("EmpleadoFac");
            this.convertidor.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear EmpleadoFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Empleado entity) throws Exception {
		try {
			if (entity.getClave() == null || "".equals(entity.getClave().trim()))
				entity.setClave(this.generaClaveEmpleado(entity.getCategoriaDescripcion()));
			return this.ifzEmpleado.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Empleado> saveOrUpdateList(List<Empleado> entities) throws Exception {
		try {
			for (Empleado entity : entities) {
				if (entity.getClave() == null || "".equals(entity.getClave().trim()))
					entity.setClave(this.generaClaveEmpleado(entity.getCategoriaDescripcion()));
			}
			return this.ifzEmpleado.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Empleado entity) throws Exception {
		try {
			if (entity.getClave() == null || "".equals(entity.getClave().trim()))
				entity.setClave(this.generaClaveEmpleado(entity.getCategoriaDescripcion()));
			this.ifzEmpleado.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Empleado entity) throws Exception {
		try {
			this.ifzEmpleado.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Empleado findById(long idEmpleado) {
		try {
			return this.ifzEmpleado.findById(idEmpleado);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public Empleado findByNss(String nssEmpleado) throws Exception {
		List<Empleado> empleados = null;
		
		try {
			empleados = this.findByProperty("numeroSeguridadSocial", nssEmpleado, true, false, "model.estatus, model.id desc", 0);
			if (empleados == null || empleados.isEmpty())
				return null;
			return empleados.get(0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Empleado> findAll(boolean incluyeBajas, boolean soloSistema, String orderBy) {
		try {
			return this.ifzEmpleado.findAll(incluyeBajas, soloSistema, getIdEmpresa(), orderBy);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Empleado> findAll(List<Long> listaEmpleados) throws Exception {
		try {
			return this.findAll(listaEmpleados, false, false, "");
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Empleado> findAll(List<Long> listaEmpleados, boolean incluyeBajas, boolean soloSistema, String orderBy) throws Exception {
		try {
			return this.ifzEmpleado.findAll(listaEmpleados, incluyeBajas, soloSistema, orderBy);
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<Empleado> findAll() {
		try {
			return this.findAll(false, false, "");
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Empleado> findAllActivos() {
		try {
			return this.findAll(false, false, "");
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Empleado> findAllPuesto(Long idPuesto) throws Exception {
		List<Long> puestos = null;
		
		try {
			if (idPuesto == null || idPuesto <= 0L)
				return this.findAll();
			puestos = new ArrayList<Long>();
			puestos.add(idPuesto);
			return this.findAllPuesto(puestos);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar consultar los Empleados del Puesto indicado: " + idPuesto, e);
			throw e;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findAllPuesto(List<Long> idPuestos) throws Exception {
		List<Long> empleados = null;
		String puestos = "";
		// ------------------------
		List<Object> rows = null;
		String queryString = "";
		
		try {
			if (idPuestos == null || idPuestos.isEmpty())
				return this.findAll();
			
			for (Long puesto : idPuestos)
				puestos += ((! "".equals(puestos.trim())) ? "," : "") + puesto.toString();
			
			queryString = "select a.aa from c5f822917f a inner join bb72d4c5be b on b.aa = a.ak inner join fecc606324 c on c.aa = b.af where c.aa in (:puestos) and a.estatus = 0 ";
			queryString = queryString.replace(":puestos", puestos);
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return this.findAll();
			empleados = new ArrayList<Long>();
			for (Object row : rows) 
				empleados.add(((BigDecimal) row).longValue());
			return this.findAll(empleados, false, false, "");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar consultar los Empleados con los Puestos indicados", e);
			throw e;
		} 
	}

	@Override
	public List<Empleado> findAllByNss(String nssEmpleado) throws Exception {
		try {
			return this.findByProperty("numeroSeguridadSocial", nssEmpleado, true, false, "model.id desc", 0);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Empleado> findByProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception {
		try {
			return this.ifzEmpleado.findByProperty(propertyName, value, incluyeBajas, soloSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Empleado> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, false, false, "", limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzEmpleado.findByProperties(params, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoFac.findByProperties(params)", e);
			throw e;
		}
	}

	@Override
	public List<Empleado> findLike(String value, String orderBy, int estatus, int limite) throws Exception {
		try {
			return this.findLike(value, (estatus > 0), false, orderBy, limite);
		} catch (Exception re) {
			log.error("Error en EmpleadoFac.findLike(value, orderBy, estatus, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Empleado> findLike(String value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception {
		try {
			value = value.trim().replace(" ", "%");
			return this.ifzEmpleado.findLike(value, incluyeBajas, soloSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Error en EmpleadoFac.findLike(value, incluyeBajas, incluyeSistema, orderBy, limite)", re);
			throw re;
		}
	}

	@Override
	public List<Empleado> findLikeProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), incluyeBajas, soloSistema, orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = value.toString().trim().replace(" ", "%");
			return this.ifzEmpleado.findLikeProperty(propertyName, value, incluyeBajas, soloSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en EmpleadoFac.findLikeProperty(propertyName, value, incluyeBajas, incluyeSistema, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, false, false, null, limite);
		} catch (Exception e) {
			log.error("Error en EmpleadoFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLikeSemanales(String value, String orderBy) throws Exception {
		List<Long> empleados = new ArrayList<Long>();
		// --------------------------
		List<BigDecimal> rows = null;
		String queryString = "";
		
		try {
			if (value == null)
				value = "";
			value = value.trim().replace(" ", "%");
			queryString += "select a.aa from c5f822917f a inner join empleado_contrato b on b.id_empleado = a.aa and b.estatus = 0 ";
			queryString += "where b.peridiocidad_pago = 10000816 and (lower(trim(a.nombre)) like lower('%:value%') or cast(a.aa as varchar) like lower('%:value%')) ";
			queryString = queryString.replace(":value", value);
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty()) 
				return new ArrayList<Empleado>();
			for (BigDecimal item : rows) 
				empleados.add(item.longValue());
			return this.findAll(empleados, false, false, orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar recuperar los Empleados Semanales", re);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLikeQuincenales(String value, String orderBy) throws Exception {
		List<Long> empleados = new ArrayList<Long>();
		// --------------------------
		List<BigDecimal> rows = null;
		String queryString = "";
		
		try {
			if (value == null)
				value = "";
			value = value.trim().replace(" ", "%");
			queryString += "select a.aa from c5f822917f a inner join empleado_contrato b on b.id_empleado = a.aa and b.estatus = 0 ";
			queryString += "where b.peridiocidad_pago = 94 and (lower(trim(a.nombre)) like lower('%:value%') or cast(a.aa as varchar) like lower('%:value%')) ";
			queryString = queryString.replace(":value", value);
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty()) 
				return new ArrayList<Empleado>();
			for (BigDecimal item : rows) 
				empleados.add(item.longValue());
			return this.findAll(empleados, false, false, orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar recuperar los Empleados Quincenales", re);
			throw re;
		}
	}

	@Override
	public boolean comprobarSemanal(long idEmpleado) throws Exception {
		EmpleadoContrato contrato = null;
		
		try {
			// Periodo Semanal
			contrato = this.comprobarPeriodicidad(idEmpleado, 10000816L);
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
				return false;
			return (contrato.getEstatus() == 0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar la Periodicidad Semanal del Empleado indicado (Contrato)", e);
			throw e;
		}
	}

	@Override
	public boolean comprobarQuincenal(long idEmpleado) throws Exception {
		EmpleadoContrato contrato = null;
		
		try {
			// Periodo Quincenal
			contrato = this.comprobarPeriodicidad(idEmpleado, 94L);
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
				return false;
			return (contrato.getEstatus() == 0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar la Periodicidad Quincenal del Empleado indicado (Contrato)", e);
			throw e;
		}
	}

	@Override
	public boolean findEmpleadoRepetido(long idPersona) {
		List<Empleado> lista = null;
		
		try {
			lista = this.ifzEmpleado.findEmpleadoRepetido(idPersona, getIdEmpresa());
			if (lista == null)
				lista = new ArrayList<Empleado>();
			return lista.size() > 0;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public boolean validarEmpleado(long idPersona) {
		List<Empleado> lista = null;
		
		try {
			lista = this.ifzEmpleado.findEmpleadoRepetido(idPersona, getIdEmpresa());
			if (lista != null && ! lista.isEmpty()) {
				for (Empleado item : lista) {
					if (item.getEstatus() != 0)
						continue;
					return false;
				}
			}
		} catch (Exception re) {
			throw re;
		}
		
		return true;
	}

	@Override
	public Empleado comprobarBajaPrevia(long idPersona) {
		List<Empleado> lista = null;
		
		try {
			lista = this.ifzEmpleado.findEmpleadoRepetido(idPersona, getIdEmpresa());
			if (lista != null && ! lista.isEmpty()) {
				for (Empleado item : lista) {
					if (item.getEstatus() == 2)
						return item;
				}
			}
		} catch (Exception re) {
			throw re;
		}
		
		return null;
	}

	@Override
	public List<Empleado> findEmpleadosAlmacenes(List<Long> puestosPermitidos) {
		try {
			return this.ifzEmpleado.findEmpleadosAlmacenes(puestosPermitidos, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout, List<String> listRequeridos) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<EmpleadoRow> listaEmpleados = new ArrayList<EmpleadoRow>();
		List<EmpleadoRow> listaErrores = new ArrayList<EmpleadoRow>();
		LinkedHashMap<String, String> listMensajes = new LinkedHashMap<String, String>();
		EmpleadoRow empleado = null;
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		int registros = 0;
		
		try {
			// Leemos archivo
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			// iteramos las filas
			while (rowIterator.hasNext()) {
				// Nuevo registro
				row = rowIterator.next();
				empleado = new EmpleadoRow();
				
				// PREVENIMOS FILA SIN INFORMACION
				if (validaFilaVacia(row, layout)) 
					continue;
				
				// PREVENIMOS FILA DE ENCABEZADOS
				if (validaEncabezados(row, layout.get("ENCABEZADOS"))) 
					continue;
				
				// RECUPERAMOS INFORMACION DE LA FILA Y LA AÑADIMOS A SU LISTA CORRESPONDIENTE
				if (validaRow(row, layout, listRequeridos, empleado, listMensajes)) 
					listaEmpleados.add(empleado);
				else
					listaErrores.add(empleado);
				registros += 1;
			}

			// Devolvemos respuesta con resultados
			respuesta.getBody().addValor("registros", registros);
			respuesta.getBody().addValor("procesados", listaEmpleados);
			respuesta.getBody().addValor("sin_procesar", listaErrores);
			respuesta.getBody().addValor("mensajes", listMensajes);
		} catch (Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo("RECHUM", 1L);
			log.error("Error en Logica_RecHum.Empleadofac.procesarLayoutEmpleados", e);
		}
		
		return respuesta;
	}

	@Override
	public String generaClaveEmpleado(String categoria) throws Exception {
		String clave = "";
		
		try {
			/*if (categoria == null || "".equals(categoria.trim()))
				categoria = "OPERACIONES";
			log.info("---- -- ----> generando secuencia de clave");
			clave = this.ifzEmpleado.generaClaveEmpleado(categoria);
			log.info("---- -- ----> Clave::" + clave);
			log.info("---- -- ---- ---- ---- ---- ---- ---- ---- ---- ----");*/
			if (categoria == null || "".equals(categoria.trim()))
				categoria = "OPERACIONES";
			categoria = categoria.trim().toUpperCase();
			clave = (categoria.equals("OPERACIONES") ? "DO" : "DA");
			clave = this.genClave(clave);
			return clave;
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public Empleado convertir(EmpleadoExt entity) {
		return this.convertidor.EmpleadoExtToEmpleado(entity);
	}

	@Override
	public EmpleadoExt convertir(Empleado entity) {
		return this.convertidor.EmpleadoToEmpleadoExt(entity);
	}

	// -----------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------
	
	@Override
	public Long save(EmpleadoExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoExtToEmpleado(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoExt extendido) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoExtToEmpleado(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoExt findByIdExt(long idEmpleado) {
		try {
			return this.convertidor.EmpleadoToEmpleadoExt(this.findById(idEmpleado)); 
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public EmpleadoExt findByNssExt(String nssEmpleado) throws Exception {
		try {
			return this.convertidor.EmpleadoToEmpleadoExt(this.findByNss(nssEmpleado)); 
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoExt> findAllExt() {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findAll();
			if (entities == null || entities.isEmpty())
				return extendidos;
			for(Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<EmpleadoExt> findAllPuestoExt(long idPuesto) throws Exception {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findAllPuesto(idPuesto);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<EmpleadoExt> findAllPuestoExt(List<Long> idPuestos) throws Exception {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findAllPuesto(idPuestos);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for (Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<EmpleadoExt> findExtLikeNombreEmpleado(String nombreEmpleado) throws Exception {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findLikeProperty("nombre", nombreEmpleado, false, false, "", 0);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for(Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<EmpleadoExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findLikeProperty(propertyName, value, limite);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for(Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<EmpleadoExt> findExtEmpleadosAlmacenes(List<Long> puestosPermitidos) {
		List<EmpleadoExt> extendidos = new ArrayList<EmpleadoExt>();
		List<Empleado> entities = null;
		
		try {
			entities = this.findEmpleadosAlmacenes(puestosPermitidos);
			if (entities == null || entities.isEmpty())
				return extendidos;
			for(Empleado entity : entities)
				extendidos.add(this.convertidor.EmpleadoToEmpleadoExt(entity));
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------
	
	private EmpleadoContrato comprobarPeriodicidad(long idEmpleado, long idPeriodicidad) throws Exception {
		EmpleadoContrato contrato = null;
		
		try {
			contrato = this.ifzContratos.findContrato(idEmpleado);
			if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L)
				return null;
			return (idPeriodicidad == contrato.getPeriodicidadPago()) ? contrato : null;
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar la Periodicidad del Empleado indicado (Contrato)", e);
			throw e;
		}
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
	
	@SuppressWarnings("unchecked")
	private String genClave(String categoria) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			queryString += "select cast(max(cast(replace(replace(trim(model.clave), substring(trim(model.clave) from '...'), ''), '-', '') as integer)) + 1 as varchar) as numero ";
			queryString += "from c5f822917f model where upper(model.clave) like upper(':categoria-%') ";
			queryString = queryString.replace(":categoria", categoria);
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar una clave de Empleado para la Categoria indicada: " + categoria, e);
		} finally {
			if (resultado != null && ! "".equals(resultado.trim()))
				resultado = categoria + "-" + String.format("%04d",Integer.parseInt(resultado));
		}
		
		return resultado;
	}
	
	private boolean validaFilaVacia(Row row, HashMap<String, String> layout) {
		int breakForInfo = 0;
		Cell celda = null;
		
		// CLAVE
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CLAVE")));
		if (cellIsNullOrEmpty(celda)) breakForInfo += 1;
		
		// APELLIDO_PATERNO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("APELLIDO_PATERNO")));
		if (cellIsNullOrEmpty(celda)) breakForInfo += 1;
		
		// PRIMER_NOMBRE
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("PRIMER_NOMBRE")));
		if (cellIsNullOrEmpty(celda)) breakForInfo += 1;
		
		return (breakForInfo > 0);
	}

	private boolean validaEncabezados(Row row, String layoutReference) {
		int indexEncabezado = -1;
		String[] valores = null;
		
		layoutReference = layoutReference.trim();
		if (layoutReference == null || "".equals(layoutReference))
			return false;
		
		if (layoutReference.contains(",")) {
			valores = layoutReference.split(",");
			layoutReference = valores[valores.length - 1];
		}
		
		try {
			indexEncabezado = Integer.valueOf(layoutReference) - 1;
			indexEncabezado = (indexEncabezado < 0) ? 0 : indexEncabezado;
		} catch (Exception e) {
			log.warn("---> Ocurrio un problema al intentar obtener el indice de los encabezados", e);
			return false;
		}
		
		if (row.getRowNum() <= indexEncabezado)
			return true;
		
		return false;
	}
	
	private boolean validaRow(Row row, HashMap<String, String> layout, List<String> listRequeridos, EmpleadoRow empleado, LinkedHashMap<String, String> listMensajes) {
		Cell celda = null;
		Object valor = null;
		boolean valido = true;
		String clave = "";
		String noInfo = "";

		// CLAVE
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CLAVE")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setClave((String) valor);
		clave = (String) valor;
		if (valor == null && listRequeridos.contains("CLAVE")) {
			log.error("No se puede obtener la ID(NUMERO INTERNO) del registro " + row.getRowNum() + 1);
			clave = String.valueOf(row.getRowNum() + 1);
			noInfo = "CLAVE";
			valido = false;
		}

		// CONSECUTIVO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CONSECUTIVO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Double.class.getName(), null);
		empleado.setConsecutivo((Double) valor);
		if (valor == null && listRequeridos.contains("CONSECUTIVO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "CONSECUTIVO";
			valido = false;
		}

		// APELLIDO_PATERNO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("APELLIDO_PATERNO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setApellidoPaterno((String) valor);
		if (valor == null && listRequeridos.contains("APELLIDO_PATERNO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "APELLIDO_PATERNO";
			valido = false;
		}

		// APELLIDO_MATERNO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("APELLIDO_MATERNO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setApellidoMaterno((String) valor);
		if (valor == null && listRequeridos.contains("APELLIDO_MATERNO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "APELLIDO_MATERNO";
			valido = false;
		}

		// PRIMER_NOMBRE
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("PRIMER_NOMBRE")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setPrimerNombre((String) valor);
		if (valor == null && listRequeridos.contains("PRIMER_NOMBRE")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "PRIMER_NOMBRE";
			valido = false;
		}

		// SEGUNDO_NOMBRE
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("SEGUNDO_NOMBRE")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setSegundoNombre((String) valor);
		if (valor == null && listRequeridos.contains("SEGUNDO_NOMBRE")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "SEGUNDO_NOMBRE";
			valido = false;
		}

		// RFC
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("RFC")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setRfc(getRfc((String) valor));
		if (valor == null && listRequeridos.contains("RFC")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "RFC";
			valido = false;
		}

		// NUMERO_CUENTA
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("NUMERO_CUENTA")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setNumeroCuenta(soloNumeros((String) valor));
		if (valor == null && listRequeridos.contains("NUMERO_CUENTA")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "NUMERO_CUENTA";
			valido = false;
		}

		// BANCO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("BANCO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setBanco((String) valor);
		if (valor == null && listRequeridos.contains("BANCO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "BANCO";
			valido = false;
		}

		// SUCURSAL
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("SUCURSAL")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setSucursal((String) valor);
		if (valor == null && listRequeridos.contains("SUCURSAL")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "SUCURSAL";
			valido = false;
		}

		// AREA
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("AREA")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setArea((String) valor);
		if (valor == null && listRequeridos.contains("AREA")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "AREA";
			valido = false;
		}

		// PUESTO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("PUESTO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setPuesto((String) valor);
		if (valor == null && listRequeridos.contains("PUESTO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "PUESTO";
			valido = false;
		}

		// CATEGORIA
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CATEGORIA")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setCategoria((String) valor);
		if (valor == null && listRequeridos.contains("CATEGORIA")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "CATEGORIA";
			valido = false;
		}

		// NSS
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("NSS")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setNss(getNss(soloNumeros((String) valor)));
		if (valor == null && listRequeridos.contains("NSS")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "NSS";
			valido = false;
		}

		// FECHA_INGRESO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("FECHA_INGRESO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Date.class.getName(), null);
		empleado.setFechaIngreso((Date) valor);
		if (valor == null && listRequeridos.contains("FECHA_INGRESO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "FECHA_INGRESO";
			valido = false;
		}

		// FECHA_TERMINO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("FECHA_TERMINO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Date.class.getName(), null);
		empleado.setFechaTermino((Date) valor);
		if (valor == null && listRequeridos.contains("FECHA_TERMINO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "FECHA_TERMINO";
			valido = false;
		}

		// CENTRO_TRABAJO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CENTRO_TRABAJO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setCentroTrabajo((String) valor);
		if (valor == null && listRequeridos.contains("CENTRO_TRABAJO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "CENTRO_TRABAJO";
			valido = false;
		}

		// PERIODICIDAD_PAGO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("PERIODICIDAD_PAGO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setPeriodicidadPago(soloNumeroEntero((String) valor));
		if (valor == null && listRequeridos.contains("PERIODICIDAD_PAGO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "PERIODICIDAD_PAGO";
			valido = false;
		}

		// DIA_DESCANSO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("DIA_DESCANSO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setDiaDescanso((String) valor);
		if (valor == null && listRequeridos.contains("DIA_DESCANSO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "DIA_DESCANSO";
			valido = false;
		}

		// SUELDO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("SUELDO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Double.class.getName(), null);
		empleado.setSueldo((Double) valor);
		if (valor == null && listRequeridos.contains("SUELDO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "SUELDO";
			valido = false;
		}

		// SUELDO_HORA
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("SUELDO_HORA")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Double.class.getName(), null);
		empleado.setSueldoHora((Double) valor);
		if (valor == null && listRequeridos.contains("SUELDO_HORA")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "SUELDO_HORA";
			valido = false;
		}

		// MODALIDAD_PAGO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("MODALIDAD_PAGO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setModalidadPago((String) valor);
		if (valor == null && listRequeridos.contains("MODALIDAD_PAGO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "MODALIDAD_PAGO";
			valido = false;
		}

		// DESCUENTO_INFONAVIT
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("DESCUENTO_INFONAVIT")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Double.class.getName(), 0D);
		empleado.setDescuentoInfonavit((Double) valor);
		if (valor == null && listRequeridos.contains("DESCUENTO_INFONAVIT")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "DESCUENTO_INFONAVIT";
			valido = false;
		}

		// CONTRATO_TIPO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("CONTRATO_TIPO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setContratoTipo((String) valor);
		if (valor == null && listRequeridos.contains("CONTRATO_TIPO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "CONTRATO_TIPO";
			valido = false;
		}

		// TIPO_HORARIO
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("TIPO_HORARIO")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, String.class.getName(), null);
		empleado.setHorarioTipo((String) valor);
		if (valor == null && listRequeridos.contains("TIPO_HORARIO")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "TIPO_HORARIO";
			valido = false;
		}

		// SUELDO_HORA_EXTRA
		celda = row.getCell(CellReference.convertColStringToIndex(layout.get("SUELDO_HORA_EXTRA")));
		valor = cellIsNullOrEmptyOrTypeInvalid(celda, Double.class.getName(), null);
		empleado.setSueldoHoraExtra((Double) valor);
		if (valor == null && listRequeridos.contains("SUELDO_HORA_EXTRA")) {
			noInfo += (! "".equals(noInfo.trim()) ? "," : "") + "SUELDO_HORA_EXTRA";
			valido = false;
		}

		listMensajes.put(clave, "OK");
		if (! "".equals(noInfo))
			listMensajes.put(clave, noInfo);
		
		return valido;
	}
	
	private boolean cellIsNullOrEmpty(Cell celda) {
		boolean resultado = false;
		DataFormatter defaultFormat = new DataFormatter();
		FormulaEvaluator fEval = null;
		String valStr = "";
		
		if (celda == null) 
			return true;
		
		if (Cell.CELL_TYPE_BLANK == celda.getCellType() || Cell.CELL_TYPE_ERROR == celda.getCellType()) 
			return true;
		
		try { 
			fEval = celda.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			valStr = defaultFormat.formatCellValue(celda, fEval);
			resultado = (valStr == null || "".equals(valStr.trim()));
		} catch (Exception e) { 
			resultado = true; 
		}
		
		return resultado;
	}
	
	private Object cellIsNullOrEmptyOrTypeInvalid(Cell celda, String tipoDato, Object defaultValue) {
		String caracteresValidos = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚabcdefghijklmnñopqrstuvwxyzáéíóú0123456789.-,'\"_()[]&=/*-+%$#¡\\!¿? ";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DataFormatter defaultFormat = new DataFormatter();
		FormulaEvaluator fEval = null;
		String[] splitted = null;
		BigDecimal valNumber = BigDecimal.ZERO;
		String valString = "";
		Date valDate = null;
		
		// Comprobamos que la cwelda no este vacia
		if (cellIsNullOrEmpty(celda)) 
			return defaultValue;
		
		try {
			fEval = celda.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			valString = defaultFormat.formatCellValue(celda, fEval);
			
			if (valString == null || "".equals(valString.trim()))
				return defaultValue;
			
			if ("NO APLICA".equals(valString.trim()) || valString.trim().contains("NO APLICA"))
				return defaultValue;
			
			// Filtramos: Solo caracteres permitidos
			splitted = valString.trim().split("");
			valString = "";
			for (int i = 0; i < splitted.length; i++) {
				if ("".equals(splitted[i])) continue;
				if (caracteresValidos.contains(splitted[i]))
					valString += splitted[i];
			}
			
			if (valString == null || "".equals(valString))
				return defaultValue;
			
			// Validamos respuesta: respondemos con el tipo solicitado
			if (String.class.getName().equals(tipoDato)) {
				if (validaCeldaToNumber(celda)) 
					valString = soloNumeros(valString.trim());
				return valString.trim();
			} else if (Double.class.getName().equals(tipoDato)) {
				if (validaCeldaToNumber(celda)) 
					valString = soloNumeros(valString.trim());
				valNumber = new BigDecimal(soloNumeros(valString.trim()));
				return (valNumber == null) ? defaultValue : valNumber.doubleValue();
			} else if (Date.class.getName().equals(tipoDato)) {
				valDate = dateFormat.parse(valString.trim());
				return (valDate == null) ? defaultValue : valDate;
			}
		} catch (Exception e) {
			log.error("Ocurrio un Error en Logica_RecHum.EmpleadoFac.cellIsNullOrEmptyOrTypeInvalid", e);
			return defaultValue;
		}
		
		return defaultValue;
	}
	
	private boolean validaCeldaToNumber(Cell celda) {
		DataFormatter defaultFormat = new DataFormatter();
		FormulaEvaluator fEval = null;
		CellValue val = null;
		
		if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return true;
		
		defaultFormat.formatCellValue(celda, fEval);
		if (celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			fEval = celda.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			val = fEval.evaluate(celda);
			return (val.getCellType() == Cell.CELL_TYPE_NUMERIC);
		} 

		return (celda.getCellType() == Cell.CELL_TYPE_NUMERIC);
	}
	
	private String soloNumeros(String value) {
		String permitidos = "0123456789.-";
		String[] splitted = null;
		
		if (value == null || "".equals(value.trim()))
			return "0";
		
		splitted = value.trim().split("");
		value = "";
		for (int i = 0; i < splitted.length; i++) {
			if ("".equals(splitted[i])) continue;
			if (permitidos.contains(splitted[i]))
				value += splitted[i];
		}
		
		return value;
	}
	
	private String soloNumeroEntero(String value) {
		DecimalFormat df = new DecimalFormat("#");
		String permitidos = "123456789.0";
		String[] splitted = null;
		
		if (value == null || "".equals(value.trim()))
			return "";
		
		splitted = value.trim().split("");
		value = "";
		for (int i = 0; i < splitted.length; i++) {
			if ("".equals(splitted[i])) continue;
			if (permitidos.contains(splitted[i]))
				value += splitted[i];
		}
		
		if ("".equals(value.trim()))
			return "";
		value = df.format(new BigDecimal(value));
		
		return value;
	}
	
	private String getRfc(String value) {
		String permitidos = "ABCDEFGHIJLKMNÑOPQRSTUVWXYZ0123456789";
		String[] splitted = null;
		
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.toUpperCase().trim();
		splitted = value.trim().split("");
		value = "";
		
		for (int i = 0; i < splitted.length; i++) {
			if ("".equals(splitted[i])) continue;
			if (permitidos.contains(splitted[i]))
				value += splitted[i];
		}
		
		return value;
	}
	
	private String getNss(String value) {
		DecimalFormat df = new DecimalFormat("00000000000");
		
		if (value == null || "".equals(value.trim()))
			return "";
		
		if (value.length() < 11)
			value = df.format(new BigDecimal(value));
		
		return value;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Implemento disponibilidad del convertidor
 *  2.2 | 2017-05-16 | Javier Tirado 	| Implemento procesamiento de layout de Empleados (carga por lotes)
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implementos los metodos findByProperties y findLikeProperties
 */