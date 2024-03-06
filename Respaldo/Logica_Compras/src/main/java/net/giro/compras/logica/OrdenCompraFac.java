package net.giro.compras.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.dao.OrdenCompraDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.logica.MonedaRem;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Stateless
public class OrdenCompraFac implements OrdenCompraRem {
	private static Logger log = Logger.getLogger(OrdenCompraFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private OrdenCompraDAO ifzOrdenCompras;
	private MonedaRem ifzMonedas;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Long estatus;
	
	public OrdenCompraFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
            
			this.ifzOrdenCompras = (OrdenCompraDAO) this.ctx.lookup("ejb:/Model_Compras//OrdenCompraImp!net.giro.compras.dao.OrdenCompraDAO");
			this.ifzMonedas = (MonedaRem) this.ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
            
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSecion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { this.convertidor.setMostrarSystemOut(value); }

	@Override
	public void OrderBy(String orderBy) { OrdenCompraFac.orderBy = orderBy; }
	
	@Override
	public void estatus(Long estatus) { OrdenCompraFac.estatus = estatus; }

	@Override
	public Long save(OrdenCompra entity) throws ExcepConstraint {
		try {
			return this.ifzOrdenCompras.save(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.save(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	public Long save(OrdenCompraExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.OrdenCompraExtToOrdenCompra(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.save(OrdenCompraExt)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompra entity) throws ExcepConstraint {
		try {
			this.ifzOrdenCompras.update(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.update(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.OrdenCompraExtToOrdenCompra(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.update(OrdenCompraExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzOrdenCompras.delete(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra findById(Long id) {
		try {
			return this.ifzOrdenCompras.findById(id);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraExt findExtById(Long id) throws Exception {
		try {
			OrdenCompra entity = this.findById(id);
			return this.convertidor.OrdenCompraToOrdenCompraExt(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	
	@Override
	public List<OrdenCompraExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findNoCompletas(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findNoCompletas(propertyName, value, max)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@Override
	public List<OrdenCompraExt> findNoCompletasExt(String propertyName, Object value, int max) throws Exception {
		try {
			List<OrdenCompra> lista = this.ifzOrdenCompras.findNoCompletas(propertyName, value, max);
			List<OrdenCompraExt> listaExt = new ArrayList<>();
			
			for(OrdenCompra var: lista){
				listaExt.add( this.convertidor.OrdenCompraToOrdenCompraExt(var) );
			}
			
			return listaExt;
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findNoCompletas(propertyName, value, max)", e);
			throw e;
		}
	}

	
	@Override
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findInProperty(columnName, values);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findInProperty(columnName, values)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraExt> findExtInProperty(String columnName, List<Object> values) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findInProperty(columnName, values);
			if (lista != null) {
				for(OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenCompraFac.findExtInProperty(columnName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompraExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<Moneda> findMonedas() throws Exception {
		try {
			return this.ifzMonedas.findAll();
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findMonedas(columnName, values)", e);
			throw e;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzOrdenCompras.findConsecutivoByProveedor(idProveedor);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findConsecutivoByProveedor(idProveedor)", e);
			throw e;
		}
	}
	
	public Respuesta procesarArchivo(byte[] fileSrc, HashMap<String, String> cellReference) throws Exception {
		Respuesta res = new Respuesta();
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		FormulaEvaluator eval;
		Iterator<Row> rowIterator = null;
		Iterator<Cell> cellIterator = null;
		Row row = null;
		Cell celda = null;
		int initProductos = -1;
		int rowIndex = 0;
		int celIndex = 0;
		String codigo  = "";
		String refCell = "";
		String valProds = null;
		String[] sections = null;
		String[] labels = null;
		String[] cols = null;
		double cantidad = 0;
		double pu = 0;
		boolean terminaCarga = false;
		HashMap<String, Object> encabezados = new HashMap<String, Object>();
		HashMap<String, String> productos = new HashMap<String, String>(); 
		HashMap<String, String> producto = new HashMap<String, String>(); 
		HashMap<String, HashMap<String, String>> sin_procesar = null; 
		
		try {
			// Leemos archivo
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			eval = workbook.getCreationHelper().createFormulaEvaluator();
			rowIterator = sheet.iterator();
			
			if (cellReference.containsKey("PRODUCTOS")) {
				valProds = cellReference.get("PRODUCTOS");
				sections = valProds.split("\\|");
				
				initProductos = Integer.parseInt(sections[0]);
				labels = sections[1].split("\\,");
				cols = sections[2].split("\\,");
				
				// Correcion de inicio de productos por indice base 0
				initProductos = initProductos - 1;
			}
						
			// iteramos las filas
			rowIndex = -1;
			for (rowIndex = 0; rowIterator.hasNext();) {
				row = rowIterator.next();
				cellIterator = row.cellIterator();
				rowIndex = row.getRowNum();
				
				if (terminaCarga)
					break;
				
				// ENCABEZADO
				if (rowIndex < initProductos) { 
					// iteramos las columnas
					for (celIndex = 0; cellIterator.hasNext();) {
						celda = cellIterator.next();
						celIndex = celda.getColumnIndex();
	
						// Validamos celda
						if (Cell.CELL_TYPE_BLANK == celda.getCellType()) continue;
						if (Cell.CELL_TYPE_STRING == celda.getCellType() && "".equals(celda.getStringCellValue())) continue;
						
						// Validacion para encabezados
						refCell = getCellReference(celIndex, rowIndex);
						if (! cellReference.containsKey(refCell)) 
							continue;
						
						switch(celda.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								encabezados.put(cellReference.get(refCell), celda.getNumericCellValue());
								break;
								
							case Cell.CELL_TYPE_FORMULA:
								CellValue cellValue = eval.evaluate(celda);
								encabezados.put(cellReference.get(refCell), ((cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValue.getNumberValue() : cellValue.getStringValue().trim()));
								break;
								
							case Cell.CELL_TYPE_BOOLEAN:
								encabezados.put(cellReference.get(refCell), celda.getBooleanCellValue());
								break;
								
							default:
								encabezados.put(cellReference.get(refCell), celda.getStringCellValue().trim());
								break;
						}
					}
				} 

				// PRODUCTOS
				if (rowIndex >= initProductos) {  
					codigo = "";
					cantidad = 0;
					pu = 0;

					// Iteracion de columnas de productos
					producto = new HashMap<String, String>();
					for(int i = 0; i < cols.length; i++) {
						if (cols[i].contains(":")) {
							// Si contiene ':' significa que el valor puede estar en una o varias columnas ocultas, 
							// tomamos la primera visible o la ultima de la lista si no hay ninguna visible
							for (int x = 0; x < cols[i].split("\\:").length; x++) {
								celIndex = CellReference.convertColStringToIndex((cols[i].split("\\:"))[x]);
								if (! sheet.isColumnHidden(celIndex)) 
									break;
							}
						} else {
							celIndex = CellReference.convertColStringToIndex(cols[i]);
						}
						 
						celda = row.getCell(celIndex);
						if (celda == null) continue;
						switch (celda.getCellType()) {
							case Cell.CELL_TYPE_STRING: 
								producto.put(labels[i], celda.getStringCellValue().trim()); 
								break;
							case Cell.CELL_TYPE_NUMERIC:
								producto.put(labels[i], getStringValue(celda.getNumericCellValue()));
								break;
							default:
								break;
						}
					}
					
					// Si son del mismo tamaño, el producto tiene todos las datos
					if (producto.size() == cols.length) {
						codigo = producto.get("CODIGO");
						cantidad = (new BigDecimal(producto.get("CANTIDAD"))).doubleValue();
						pu = (new BigDecimal(producto.get("PRECIO"))).doubleValue();
						if (! "".equals(codigo))
							productos.put(codigo, cantidad + "|" + pu);
					} else if (producto.size() != cols.length && producto.size() > 0) {
						// el producto no se puede procesar
						if (sin_procesar == null)
							sin_procesar = new HashMap<String, HashMap<String,String>>();
						// "C" + row.getRowNum() + 1 :: Correccion indice base 0
						sin_procesar.put("C" + (row.getRowNum() + 1), producto);
					} else {
						// renglon vacio, terminamos carga
						terminaCarga = true;
					}

					producto = null;
				} 
			}
			
			res.getBody().addValor("encabezados", encabezados);
			res.getBody().addValor("productos", productos);
			res.getBody().addValor("sin_procesar", sin_procesar);
		} catch (Exception e) {
			res.setBody(null);
			res.getErrores().addCodigo("COMPRAS", 1L);
			res.getErrores().setDescError("Estructura o datos no son correctos");
			log.error("Error en Logica_Compras.OrdenCompraFac.procesarArchivo", e);
		} 
		
		return res;
	}
	
	private String getCellReference(int col, int row) {
		return (new CellReference(row, col)).formatAsString().replace("$", "");
	}
	
	private String getStringValue(double value) {
		NumberFormat nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		return nf.format(value).replace(",", "");
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.2	| 29/04/2017 | Javier Tirado 			 | Corrigo el metodo procesarArchivo. Mayor precision con la referencia de celdas
 */