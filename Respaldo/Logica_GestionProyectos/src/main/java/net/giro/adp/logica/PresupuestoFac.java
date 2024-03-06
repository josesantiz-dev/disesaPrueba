package net.giro.adp.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoExt;
import net.giro.adp.beans.PresupuestoRow;
import net.giro.adp.dao.PresupuestoDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Stateless
public class PresupuestoFac implements PresupuestoRem {
	private static Logger log = Logger.getLogger(PresupuestoFac.class);	
	InitialContext ctx;
	private PresupuestoDAO ifzPresupuesto;
	private ConvertExt convertidor;
	
	public PresupuestoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            
            this.ifzPresupuesto = (PresupuestoDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//PresupuestoImp!net.giro.adp.dao.PresupuestoDAO");
            
           this.convertidor = new ConvertExt();
           this.convertidor.setFrom("PresupuestoFac");
           this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Presupuesto", e);
			ctx = null;
		}
	}

	public Long save(Presupuesto entity) throws ExcepConstraint {
		try {
			return this.ifzPresupuesto.save(entity);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.save(entity)", e);
			throw e;
		}
	}

	public Long save(PresupuestoExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.PresupuestoExtToPresupuesto(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.save(entityExt)", e);
			throw e;
		}
	}

	public void update(Presupuesto entity) throws ExcepConstraint {
		try {
			this.ifzPresupuesto.update(entity);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.update(entity)", e);
			throw e;
		}
	}

	public void update(PresupuestoExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.PresupuestoExtToPresupuesto(entityExt));
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.update(entityExt)", e);
			throw e;
		}
	}

	public void delete(Long id) throws ExcepConstraint {
		try {
			this.ifzPresupuesto.delete(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.delete(id)", e);
			throw e;
		}
	}

	public List<Presupuesto> findAll() {
		try {
			return this.ifzPresupuesto.findAll();
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.findAll()", e);
			throw e;
		}
	}

	public Presupuesto findById(Long id) {
		try {
			return this.ifzPresupuesto.findById(id);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.findById(id)", e);
			throw e;
		}
	}

	public PresupuestoExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.PresupuestoToPresupuestoExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.findByIdExt(id)", e);
			throw e;
		}
	}

	public List<Presupuesto> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuesto.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	public List<PresupuestoExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoExt> listaExt = new ArrayList<PresupuestoExt>();
		
		try {
			List<Presupuesto> lista = this.findByProperty(propertyName, value, max);
			for(Presupuesto var : lista)
				listaExt.add(this.convertidor.PresupuestoToPresupuestoExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	public List<Presupuesto> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzPresupuesto.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en PresupuestoFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}
	
	public List<PresupuestoExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<PresupuestoExt> listaExt = new ArrayList<PresupuestoExt>();
		
		try {
			List<Presupuesto> lista = this.findLikeProperty(propertyName, value, max);
			for(Presupuesto var : lista)
				listaExt.add(this.convertidor.PresupuestoToPresupuestoExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout) throws Exception {
		Respuesta respuesta = new Respuesta();
		PresupuestoRow ppt = null;
		List<PresupuestoRow> procesados = new ArrayList<PresupuestoRow>();
		List<PresupuestoRow> no_procesados = new ArrayList<PresupuestoRow>();
		LinkedHashMap<String, String> concepto = new LinkedHashMap<String, String>();
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Iterator<Cell> cellIterator = null;
		DataFormatter defaultFormat = new DataFormatter();
		FormulaEvaluator fEval = null;
		Row row = null;
		Cell celda = null;
		int sheetIndex = 0;
		int rowIndex = 0;
		int colIndex = 0;
		String[] splitted = null;
		String[] tipos = null;
		String refCell = "";
		String valString = "";
		int conceptoNumero = 0;
		
		try {
			// Leemos archivo
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			fEval = workbook.getCreationHelper().createFormulaEvaluator();
			
			for (sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				if (workbook.isSheetHidden(sheetIndex)) 
					continue;

				conceptoNumero = 0;
				sheet = workbook.getSheetAt(sheetIndex);
				rowIterator = sheet.iterator();
				
				// Nuevo PPT
				ppt = new PresupuestoRow(sheetIndex + 1, sheet.getSheetName());
				
				// iteramos las filas
				while (rowIterator.hasNext()) {
					row = rowIterator.next();
					rowIndex = row.getRowNum();
					cellIterator = row.cellIterator();
					
					// Iteramos las columnas
					while(cellIterator.hasNext()) {
						celda = cellIterator.next();
						colIndex = celda.getColumnIndex();
						refCell = getCellReference(rowIndex, colIndex);
						
						if (layout.containsKey(refCell)) {
							valString = defaultFormat.formatCellValue(celda, fEval);
							
							// ENCABEZADOS
							switch (layout.get(refCell)) {
								case "OBRA": ppt.setIdObra(getConvertStringToLong(valString)); continue;
								case "TEMA": ppt.setTema(valString); continue;
								case "RUTA": ppt.setRuta(valString); continue;
								default: break;
							}
							
							// CONCEPTOS
							refCell = layout.get(refCell);
							if (! refCell.contains(":"))
								continue;

							tipos = null;
							if (refCell.contains("|")) {
								splitted = refCell.trim().split("\\|");
								refCell = splitted[1];
								tipos = refCell.trim().split("\\:");
								refCell = splitted[0];
							}
							
							splitted = refCell.trim().split("\\:");
							concepto = new LinkedHashMap<String, String>();
							for (int i = 0; i < splitted.length; i++) {
								colIndex = CellReference.convertColStringToIndex(splitted[i]);
								celda = row.getCell(colIndex);
								celda = fEval.evaluateInCell(celda);
								valString = defaultFormat.formatCellValue(celda, fEval);
								if (tipos != null)
									valString = aplicaTipo(valString, tipos[i]);
								concepto.put(splitted[i], valString);
							}
						}
					} // fin cols

					if (concepto != null && ! concepto.isEmpty()) {
						conceptoNumero += 1;
						switch (conceptoNumero) {
							case  1: ppt.setConcepto01(concepto); break;
							case  2: ppt.setConcepto02(concepto); break;
							case  3: ppt.setConcepto03(concepto); break;
							case  4: ppt.setConcepto04(concepto); break;
							case  5: ppt.setConcepto05(concepto); break;
							case  6: ppt.setConcepto06(concepto); break;
							case  7: ppt.setConcepto07(concepto); break;
							case  8: ppt.setConcepto08(concepto); break;
							case  9: ppt.setConcepto09(concepto); break;
							case 10: ppt.setConcepto10(concepto); break;
							case 11: ppt.setConcepto11(concepto); break;
							case 12: ppt.setConcepto12(concepto); break;
							case 13: ppt.setConcepto13(concepto); break;
							case 14: ppt.setConcepto14(concepto); break;
							case 15: ppt.setConcepto15(concepto); break;
							case 16: ppt.setConcepto16(concepto); break;
							default: break;
						}
						
						concepto = null;
					}
				} // fin rows
				
				/*if (ppt != null) {
					if (ppt.registroCompleto())
						procesados.add(ppt);
					else
						no_procesados.add(ppt);
					ppt = null;
				}*/
				if (ppt != null)
					procesados.add(ppt);
				ppt = null;
			} // fin hojas
			
			respuesta.getBody().addValor("procesados", procesados);
			respuesta.getBody().addValor("no_procesados", no_procesados);
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.PresupuestoFac.procesarLayout", e);
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo("GP", 1L);
			
			if (celda != null && Cell.CELL_TYPE_FORMULA == celda.getCellType())
				respuesta.getErrores().setDescError("Estructura o datos no son correctos. Comprueba que no tiene enlaces externos.");
			else
				respuesta.getErrores().setDescError("Estructura o datos no son correctos");
		}
		
		return respuesta;
	}
	
	public Presupuesto convertir(PresupuestoExt pojoTarget) throws Exception {
		try {
			return this.convertir(pojoTarget);
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.PrecupuestoFac.convertir. No se pudo convertir el pojo extendido en el entity Presupuesto", e);
			throw e;
		}
	}
	
	public PresupuestoExt convertir(Presupuesto pojoTarget) throws Exception {
		try {
			return this.convertir(pojoTarget);
		} catch (Exception e) {
			log.error("Error en Logica_GestionProyectos.PrecupuestoFac.convertir. No se pudo extender el entity Presupuesto", e);
			throw e;
		}
	}
	
	private String getCellReference(int row, int col) {
		return (new CellReference(row, col)).formatAsString().replace("$", "");
	}
	
	private Long getConvertStringToLong(String value) {
		return new BigDecimal(getNumeric(value)).longValue();
	}
	
	private String getNumeric(String value) {
		return getNumeric(value, true);
	}
	
	private String getNumeric(String value, boolean incluyeDecimales) {
		DecimalFormat df = new DecimalFormat("#");
		String permitidos = "1234567890";
		String[] splitted = null;
		
		if (incluyeDecimales) {
			df.applyPattern("#.00");
			permitidos += ".";
		}
		
		if (value == null || "".equals(value.trim()))
			return "";
		
		splitted = value.trim().split("");
		value = "";
		for (int i = 0; i < splitted.length; i++) {
			if ("".equals(splitted[i])) continue;
			if (permitidos.contains(splitted[i]))
				value += splitted[i];
		}
		
		if ("".equals(value.trim())) return "";
		value = df.format(new BigDecimal(value));
		
		return value;
	}
	
	private String aplicaTipo(String value, String tipo) {
		tipo = tipo.trim().toUpperCase();
		switch (tipo) {
			case "NUMERIC": 
			case "DECIMAL": value = getNumeric(value, true); break;
			case "ENTERO": value = getNumeric(value, false); break;
			default: break;
		}
		
		return value;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-15 | Javier Tirado 	| Implemento metodos para extender el entity Presupuesto y viceversa.
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implemento metodo procesarLayoutPPT
 */