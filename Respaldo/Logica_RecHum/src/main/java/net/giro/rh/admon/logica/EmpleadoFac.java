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

import net.giro.comun.ExcepConstraint;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoRow;
import net.giro.rh.admon.dao.EmpleadoDAO;

@Stateless
public class EmpleadoFac implements EmpleadoRem {
	private static Logger log = Logger.getLogger(EmpleadoFac.class);
	
	private InitialContext ctx;
	private EmpleadoDAO ifzEmpleado;
	private ConvertExt convertidor;
	
	
	public EmpleadoFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoImp!net.giro.rh.admon.dao.EmpleadoDAO";
            this.ifzEmpleado = (EmpleadoDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoFac", e);
			ctx = null;
		}
	}
	
	
	public Long save(Empleado entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleado.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(EmpleadoExt entityExt) throws ExcepConstraint {
		try {
			Empleado entity = this.convertidor.EmpleadoExtToEmpleado(entityExt);
			return this.ifzEmpleado.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(Empleado entity) throws ExcepConstraint {
		try {
			this.ifzEmpleado.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoExt entityExt) throws ExcepConstraint {
		try {
			Empleado entity = this.convertidor.EmpleadoExtToEmpleado(entityExt);
			this.ifzEmpleado.delete(entity.getId());
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Empleado update(Empleado entity) throws ExcepConstraint {
		try {
			this.ifzEmpleado.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Empleado update(EmpleadoExt entityExt) throws ExcepConstraint {
		try {
			Empleado entity = this.convertidor.EmpleadoExtToEmpleado(entityExt);
			this.ifzEmpleado.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Empleado findById(Long id) {
		try {
			return this.ifzEmpleado.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public EmpleadoExt findByIdExt(Long id) {
		try {
			Empleado empleado = this.findById(id);
			return  this.convertidor.EmpleadoToEmpleadoExt(empleado); //this.ifzEmpleado.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public List<Empleado> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleado.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<Empleado> findAll() {
		try {
			return this.ifzEmpleado.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<Empleado> findAllActivos() {
		try {
			return this.ifzEmpleado.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	public List<EmpleadoExt> findByEmpleado(String nombreEmpleado) {
		List<EmpleadoExt> listaExt = new ArrayList<EmpleadoExt>();
		
		try {
			List<Empleado> listaEmpleados = this.ifzEmpleado.findByEmpleado(nombreEmpleado);
			for(Empleado e: listaEmpleados){
				listaExt.add(this.convertidor.EmpleadoToEmpleadoExt(e));
			}
		} catch (RuntimeException re) {		
			throw re;
		}
		
		return listaExt;
	}
	
	public List<Empleado> findByNombreEmpleado(String nombreEmpleado) {
		try{
			return this.ifzEmpleado.findByEmpleado(nombreEmpleado);
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<EmpleadoExt> findAllExt() {
		List<EmpleadoExt> empleadosExt = new ArrayList<EmpleadoExt>();
		List<Empleado> empleados = this.findAll();
		
		for(Empleado e: empleados){
			empleadosExt.add( this.convertidor.EmpleadoToEmpleadoExt( e ) );
		}
		
		return empleadosExt;
	}
	
	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try {
			return this.ifzEmpleado.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<EmpleadoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoExt> listaExt = new ArrayList<EmpleadoExt>();
		
		try{
			List<Empleado> lista = this.ifzEmpleado.findByPropertyPojoCompleto(propertyName, tipo, value);
			for (Empleado empleado : lista) {
				EmpleadoExt pojoAux = this.convertidor.EmpleadoToEmpleadoExt(empleado); 
				if ("".equals(tipo))
					listaExt.add(pojoAux);
			}
		}catch(RuntimeException re){
			throw re;
		}

		return listaExt;
	}

	public boolean findEmpleadoRepetido(long idEmpleado) {
		List<Empleado> lista = this.ifzEmpleado.findEmpleadoRepetido(idEmpleado);
		if (lista == null)
			lista = new ArrayList<Empleado>();
		return lista.size() > 0;
	}

	@Override
	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite) {
		try {
			return this.ifzEmpleado.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("Error en EmpleadoFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
	}
	
	@Override
	public List<EmpleadoExt> findExtLikeProperty(String propertyName, Object value, int limite) {
		List<EmpleadoExt> listaExt = new ArrayList<EmpleadoExt>();
		
		try {
			List<Empleado> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(Empleado var : lista) {
					listaExt.add(this.convertidor.EmpleadoToEmpleadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en EmpleadoFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public Empleado convertir(EmpleadoExt entity) {
		return this.convertidor.EmpleadoExtToEmpleado(entity);
	}

	@Override
	public EmpleadoExt convertir(Empleado entity) {
		return this.convertidor.EmpleadoToEmpleadoExt(entity);
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
	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception {
		try {
			return this.ifzEmpleado.findByProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoFac.findByProperties(params)", e);
			throw e;
		}
	}
	
	@Override
	public List<Empleado> findLikeProperties(HashMap<String, String> params) throws Exception {
		try {
			return this.ifzEmpleado.findLikeProperties(params);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoFac.findLikeProperties(params)", e);
			throw e;
		}
	}
	
	// ------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------
	
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