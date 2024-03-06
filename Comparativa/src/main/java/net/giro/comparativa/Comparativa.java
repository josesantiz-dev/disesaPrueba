package net.giro.comparativa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

public class Comparativa implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Comparativa.class);
	private Connection conn;
	private HashMap<String, String> params = null;
	private String strQuery = "";
	private int errorCode = 0;
	private String errorDesc = "";
	// Formato de celdas
	private CellStyle sTitulo = null;
	private CellStyle sHeader = null;
	private CellStyle sHeaderWraptext = null;
	private CellStyle sHeaderTotal = null;
	private CellStyle sNumero = null;
	private CellStyle sMoneda = null;
	private CellStyle sNormalLeft = null;
	private CellStyle sNormalCenter = null;
	private CellStyle sNormalCenterNoBorder = null;
	private CellStyle sFirma = null;
	private CellStyle sPorcentaje = null;
	private List<String> etiquetas;
	private boolean baseComparativa;
    
    public Comparativa() {
    	this.conn = null;
    	this.etiquetas = new ArrayList<String>();
    	this.etiquetas.add("SUBTOTAL");
    	this.etiquetas.add("IVA");
    	this.etiquetas.add("TOTAL");
    	this.etiquetas.add("MARGEN");
    	this.etiquetas.add("FLETE");
    	this.etiquetas.add("TIEMPO ENTREGA");
    }
    
    public Comparativa(HashMap<String, String> params) {
    	this();
    	this.params = params;
    }

    // ---------------------------------------------------------------------------------------------
    // TALENT
    // ---------------------------------------------------------------------------------------------
    
    public static void main(String[] args) throws Exception {
    	Comparativa comp = new Comparativa();
    	int exitCode = comp.runJobInTOS(args);
    	System.exit(exitCode);
    }
    
    public String[][] runJob(String[] args) {
    	String[][] bufferValue = null;
    	int exitCode = 0;
    	
    	try {
    		exitCode = runJobInTOS(args);
    		bufferValue = new String[][] { { Integer.toString(exitCode) } };
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return bufferValue;
    }
    
    public int runJobInTOS(String[] args) throws Exception {
		String[] splitted = null;
		String arg = "";
		
    	try {
    		BasicConfigurator.configure();
    		if (this.params == null)
    			this.params = new HashMap<String, String>();
    		this.params.clear();
    		
    		// Generamos los parametros de acuerdo a los argumentos
    		for (String p : args) {
    			arg = (p.contains("--context_param")) ? p.substring(15).trim() : p.trim();
    			splitted = arg.split("=");
    			if (splitted.length >= 2)
    				this.params.put(splitted[0].trim(), splitted[1].trim());
    		}
    		
    		// Pasamos parametros hacia consola
    		log.info(this.params.toString());
    	    return this.generarReporte();
    	} catch (Exception e) {
    		log.error("Error en runJobInTOS :: ", e);
    		e.printStackTrace();
    	}
    	
    	return this.errorCode;
    }
    
    // ---------------------------------------------------------------------------------------------
    // END TALENT
    // ---------------------------------------------------------------------------------------------
    
    public int generarReporte() throws Exception {
    	HSSFWorkbook libro = null;
    	HSSFSheet hoja = null;
        HSSFRow row = null;
        HSSFCell celda = null;
        ResultSet rsProductos = null; 		// titulo | productos
        ResultSet rsProveedores = null; 	// proveedores
        ResultSet rsProvProductos = null; 	// proveedoresProductos
        ResultSet rsPPTObra = null; 		// DATOS DE PPT
    	String directorObra = "";
    	String refCellTotalDISESA = "";
    	String salida = "";
    	String porIndirectos = "";
    	String porUtilidad = "";
    	String comentario = "";
		double porcentajeIva = 0;
		double cantidad = 0;
		double precio = 0;
		//double importe = 0;
		double porcentaje = 0;
    	int initProds = 0;
    	int endProds = 0;
        int currentRow = 0;
        int noColumna = 0;
        // --------------------------
		String etiqueta = "";
		String iniRange = "";
		String endRange = "";
    	
    	try {
    		if (this.params == null || this.params.isEmpty()) {
    			this.errorCode = 1;
    			this.errorDesc = "No se indicaron parametros.";
    			throw new Exception (this.errorCode + " - " + this.errorDesc);
    		}

            if (! this.getConnection())
            	throw new Exception(this.errorCode + " - " + this.errorDesc);
            
            this.baseComparativa = esBaseComparativa();
            log.info("Reporte basado en " + (this.baseComparativa ? "Comparativa: "  + this.params.get("idComparativa") : "Obra: "  + this.params.get("idObra")));

            libro = new HSSFWorkbook();
            hoja = libro.createSheet("Comparativa-" + (this.baseComparativa ? this.params.get("idComparativa") : this.params.get("idObra")));
            
            // Generamos estilos de celdas
            generaCellStyles(libro);
            
            // Titulo
            currentRow = 1;
            noColumna = 3;
            
            rsProductos = getTitulo();
            if (rsProductos.next()) {
            	SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy"); 
            	String desc = "";
            	Date fecha = null;
            	
            	// Director de obra
            	directorObra = rsProductos.getString("director");
            	if ("".equals(directorObra))
            		directorObra = "NOMBRE";
            	
            	// Familia
            	desc = rsProductos.getString("familia");
            	if (! "".equals(desc))
            		desc = " FAMILIA " + desc;
            	
                row = hoja.createRow(currentRow);
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
        		celda.setCellValue("COMPARATIVA" + desc);
        		celda.setCellStyle(sTitulo);
        		currentRow++;
        		
        		// OBRA
            	desc = rsProductos.getString("obra");
            	if ("".equals(desc))
            		desc = "SIN OBRA";
                row = hoja.createRow(currentRow);
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
        		celda.setCellValue(desc);
        		celda.setCellStyle(sTitulo);
        		currentRow++;
        		
        		// Moneda
        		desc = rsProductos.getString("moneda");
            	if ("".equals(desc))
            		desc = "PESOS (MXN)";
                row = hoja.createRow(currentRow);
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
        		celda.setCellValue(desc);
        		celda.setCellStyle(sNormalCenterNoBorder);
        		
        		// Fecha de emision
            	fecha = rsProductos.getDate("fecha");
            	desc = formatter.format(fecha).toUpperCase();
        		celda = row.createCell((noColumna + 2), HSSFCell.CELL_TYPE_STRING);
        		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, (noColumna + 2), (noColumna + 3)));
        		celda.setCellValue(desc);
        		celda.setCellStyle(sNormalCenterNoBorder);
            } else {
            	log.info("No se pudo generar el reporte con los parametros indicados");
            	return 1;
            }
            
            // Agregamos la imagen si corresponde
            if (this.params.containsKey("ruta_img"))
            	agregarImagen(libro, hoja, this.params.get("ruta_img"));
            
            // Generamos encabezados
            // -----------------------------------------------------------------------
            currentRow = 5;
            row = hoja.createRow(currentRow);
            row.setHeight((short) (row.getHeight() * 2));
			//row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
            noColumna = 0;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue("DISESA");
    		celda.setCellStyle(sHeader);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 6)));
    		comentario = "Explosion de Insumos";
    		addComment(celda, comentario);
    		noColumna = noColumna + 7;
            
            // Proveedores
            // -----------------------------------------------------------------------
            rsProveedores = getProveedores();
            while(rsProveedores.next()) {
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsProveedores.getString("id_proveedor") + " - " + rsProveedores.getString("proveedor"));
        		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 2)));
        		celda.setCellStyle(sHeaderWraptext);
        		comentario = rsProveedores.getString("folio");
        		addComment(celda, comentario);
        		noColumna = noColumna + 3;
            }
            
            // Etiquetas 
            // -----------------------------------------------------------------------
            noColumna = 0;
            currentRow++;
            row = hoja.createRow(currentRow);
            
            // Numero de registro (No.) 
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("No.");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Codigo
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("CODIGO");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Descripcion
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("DESCRIPCION");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Unidad de Media
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("U.M.");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Cantidad
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("CANTIDAD");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Precio Unitario
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("P.U.");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Importe
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("IMPORTE");
    		celda.setCellStyle(sHeader);
    		noColumna++;
    		
    		// Etiquetas de proveedores
    		if(rsProveedores.first()) {
    			do {
            		// Cantidad
            		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
            		celda.setCellValue("CANTIDAD");
            		celda.setCellStyle(sHeader);
            		noColumna++;
            		
            		// Precio Unitario
            		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
            		celda.setCellValue("P.U.");
            		celda.setCellStyle(sHeader);
            		noColumna++;
            		
            		// Importe
            		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
            		celda.setCellValue("IMPORTE");
            		celda.setCellStyle(sHeader);
            		noColumna++;
    			} while (rsProveedores.next());
    		}
    		
    		// Recuperamos productos
            // -----------------------------------------------------------------------
    		rsProductos = getProductos();
    		
    		// Recuperamos proveedores productos
            // -----------------------------------------------------------------------
            rsProvProductos = getProveedoresProductos();
            
            currentRow++;
            initProds = currentRow;
            row = hoja.createRow(currentRow);

            // Productos
            // -----------------------------------------------------------------------
            int numeroRegistro = 1;
            while (rsProductos.next()) {
        		row = hoja.createRow(currentRow);
        		noColumna = 0;
        		
        		if (porcentajeIva <= 0)
        			porcentajeIva = rsProductos.getDouble("por_iva");
        		
        		// Numero de registro (No.) 
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(numeroRegistro);
        		celda.setCellStyle(sNormalCenter);
        		noColumna++;
        		
        		// Codigo
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsProductos.getString("codigo"));
        		celda.setCellStyle(sNormalCenter);
        		addComment(celda, rsProductos.getString("id_producto"));
        		noColumna++;
        		
        		// Descripcion
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsProductos.getString("producto"));
        		celda.setCellStyle(sNormalLeft);
        		noColumna++;
        		
        		// Unidad de Media
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsProductos.getString("unidad_medida"));
        		celda.setCellStyle(sNormalCenter);
        		noColumna++;
        		
        		// Cantidad
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsProductos.getDouble("cantidad"));
        		celda.setCellStyle(sNumero);
        		noColumna++;
        		
        		// Precio Unitario
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsProductos.getDouble("pu_explosion"));
        		celda.setCellStyle(sMoneda);
        		noColumna++;
        		
        		// Importe
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_FORMULA);
        		celda.setCellFormula(CellReference.convertNumToColString(celda.getColumnIndex() - 2) + (celda.getRowIndex() + 1) + "*" + CellReference.convertNumToColString(celda.getColumnIndex() - 1) + (celda.getRowIndex() + 1));
        		celda.setCellStyle(sMoneda);
        		noColumna++;
        		
        		// Proveedores
                // -----------------------------------------------------------------------
        		if(rsProveedores.first()) {
    				do {
            			cantidad = 0;
            			precio = 0;
            			//importe = 0;

            			// proveedores productos
            			if (rsProvProductos.first()) {
							// Buscamos el producto en todos los proveedores. Si lo encuentra, almaceno los datos en las variables
	    					do {
	    						if (rsProvProductos.getString("id_proveedor").equals(rsProveedores.getString("id_proveedor")) && rsProvProductos.getString("id_producto").equals(rsProductos.getString("id_producto"))) {
	    							cantidad = rsProvProductos.getDouble("prov_cantidad");
	    							precio = rsProvProductos.getDouble("prov_pu");
	    	                		break;
	    						}
	    					} while (rsProvProductos.next());
            			}

						// Cantidad
                		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
                		celda.setCellValue(cantidad);
                		celda.setCellStyle(sNumero);
                		noColumna++;
                		
                		// Precio Unitario
                		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
                		celda.setCellValue(precio);
                		celda.setCellStyle(sMoneda);
                		noColumna++;
                		
                		// Importe
                		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
                		celda.setCellFormula(CellReference.convertNumToColString(celda.getColumnIndex() - 2) + (celda.getRowIndex() + 1) + "*" + CellReference.convertNumToColString(celda.getColumnIndex() - 1) + (celda.getRowIndex() + 1));
                		celda.setCellStyle(sMoneda);
                		noColumna++;
    				} while(rsProveedores.next());
        		}
        		
        		currentRow++;
        		numeroRegistro++;
            }

            endProds = currentRow - 1;
            currentRow++;
            
            // Subtotal DISESA
            // -----------------------------------------------------------------------
    		int rowIndex = currentRow;
    		int cellIndex = 5;
    		
    		rsPPTObra = getDataFromPPT01();
    		if (rsPPTObra.first()) {
    			porIndirectos = "";
    			porUtilidad = "";
    			
    			do {
    				porcentaje = rsPPTObra.getDouble("porcentaje");
    				if ("".equals(porIndirectos))
    					porIndirectos = formatoPorcentaje(porcentaje);
    				else if ("".equals(porUtilidad))
    					porUtilidad = formatoPorcentaje(porcentaje);
    				else
    					break;
    			} while (rsPPTObra.next());
    		}

    		// SUBTOTAL
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("SUBTOTAL:");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
			iniRange = this.getCellReference(cellIndex, initProds);
			endRange = this.getCellReference(cellIndex, endProds);
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;

    		// IND. 18%
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("IND. (" + porIndirectos + " %):");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("(" + this.getCellReference(cellIndex, rowIndex - 1) + "*" + porIndirectos + ")/100");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;
    		
    		// SUBTOTAL 1
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("SUB1:");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
			iniRange = this.getCellReference(cellIndex, rowIndex - 2);
			endRange = this.getCellReference(cellIndex, rowIndex - 1);
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;

    		// UTILIDAD 10%
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);     
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("UTILIDAD (" + porUtilidad + " %):");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("(" + this.getCellReference(cellIndex, rowIndex - 1) + "*" + porUtilidad + ")/100");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;
    		
    		// SUBTOTAL 2
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);     
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("SUB2:");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
			iniRange = this.getCellReference(cellIndex, rowIndex - 2);
			endRange = this.getCellReference(cellIndex, rowIndex - 1);
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;

    		// IVA 16%
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("IVA (" + porcentajeIva + " %):");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("(" + this.getCellReference(cellIndex, rowIndex - 1) + "*" + porcentajeIva + ")/100");
    		celda.setCellStyle(sMoneda);
    		cellIndex = 5;
    		rowIndex++;
    		
    		// TOTAL 
			row = (hoja.getRow(rowIndex) == null) ? hoja.createRow(rowIndex) : hoja.getRow(rowIndex);   
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("TOTAL:");
    		celda.setCellStyle(sHeaderTotal);
    		cellIndex++;
			iniRange = this.getCellReference(cellIndex, rowIndex - 2);
			endRange = this.getCellReference(cellIndex, rowIndex - 1);
    		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
    		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
    		celda.setCellStyle(sMoneda);
    		refCellTotalDISESA = this.getCellReference(cellIndex, rowIndex);
    		cellIndex = 5;
            
            // Subtotales por proveedor
            // -----------------------------------------------------------------------
    		noColumna = 7;
    		rowIndex = currentRow;
    		cellIndex = 0;
    		
			// ETIQUETAS: SUBTOTAL, IVA, TOTAL, MARGEN, FLETE, TIEMPO DE ENTREGA
			for (int indexEtiqueta = 0; indexEtiqueta < this.etiquetas.size(); indexEtiqueta++) {
    			row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
        		etiqueta = this.etiquetas.get(indexEtiqueta);
    			cellIndex = noColumna;
    			
				// PROVEEDORES
	    		if (rsProveedores.first()) {
        			do {
                		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING);
                		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, cellIndex, (cellIndex + 1)));
                		celda.setCellStyle(sHeaderTotal);
                		
                		switch (indexEtiqueta) {
                			case 0: // SUBTOTAL
                        		celda.setCellValue(etiqueta + ":");
                        		cellIndex += 2;
        	        			iniRange = this.getCellReference(cellIndex, initProds);
        	        			endRange = this.getCellReference(cellIndex, endProds);
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
        		        		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
        		        		celda.setCellStyle(sMoneda);
                			break;
                			
                			case 1: // IVA
                        		celda.setCellValue(etiqueta + " (" + porcentajeIva + " %):");
                        		cellIndex += 2;
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
        		        		celda.setCellFormula("(" + this.getCellReference(cellIndex, currentRow - 1) + "*" + porcentajeIva + ")/100");
        		        		celda.setCellStyle(sMoneda);
            				break;
            				
            				case 2: // TOTAL
                        		celda.setCellValue(etiqueta + ":");
                        		cellIndex += 2;
        	        			iniRange = this.getCellReference(cellIndex, currentRow - 2);
        	        			endRange = this.getCellReference(cellIndex, currentRow - 1);
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
        		        		celda.setCellFormula("SUM(" + iniRange + ":" + endRange + ")");
        		        		celda.setCellStyle(sMoneda);
            				break;
            				
            				case 3: // MARGEN
                        		celda.setCellValue(etiqueta + ":");
                        		cellIndex += 2;
        	        			iniRange = refCellTotalDISESA;
        	        			endRange = this.getCellReference(cellIndex, currentRow - 1);
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA);
        		        		celda.setCellFormula("(" + iniRange + "/" + endRange + ")-1");
        		        		celda.setCellStyle(sPorcentaje);
            				break;
            				
            				case 4: // FLETE
                        		celda.setCellValue(etiqueta + ":");
                        		cellIndex += 2;
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_NUMERIC);
        		        		celda.setCellValue(rsProveedores.getDouble("flete"));
        		        		celda.setCellStyle(sMoneda);
            				break;
            				
            				case 5: // TIEMPO ENTREGA
                        		celda.setCellValue(etiqueta + ":");
                        		cellIndex += 2;
        		        		celda = row.createCell(cellIndex, HSSFCell.CELL_TYPE_NUMERIC);
        		        		celda.setCellValue(rsProveedores.getDouble("tiempo_entrega"));
        		        		celda.setCellStyle(sNumero);
            				break;
                		}
                		
    	        		cellIndex++;
        			} while (rsProveedores.next());
	    		}
	    		
	    		currentRow++;
			}

            // Firma director obra
            // -----------------------------------------------------------------------
    		currentRow = rowIndex;
    		noColumna = 1;
			row = hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("FIRMA DIRECTOR DE OBRA");
    		celda.setCellStyle(sHeader);
    		currentRow++;
    		
			row = hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, (currentRow + 4), noColumna, (noColumna + 1)));
    		celda.setCellValue("Firma");
    		celda.setCellStyle(sFirma);
    		currentRow++;
    		
    		// Guardamos el documento
    		salida = "";
    		if (this.params.containsKey("salida"))
    			salida = this.params.get("salida");
    		else if (this.params.containsKey("SALIDA"))
    			salida = this.params.get("SALIDA");
    		else
    			salida = "salida";
    		salida += (! salida.contains(".xls") ? ".xls" : "");
    		
    		FileOutputStream fileOut = new FileOutputStream(salida);
    		libro.write(fileOut);
    		fileOut.close();
    		
    		this.errorCode = 0;
    		this.errorDesc = "";
    	} catch (Exception e) {
    		log.error("Error al generar reporte de COMPARATIVA", e);
    		throw e;
    	} finally {
    		this.closeConnection();
    	}
    	
    	return this.errorCode;
    }

    private void generaCellStyles(HSSFWorkbook wb) {
    	HSSFDataFormat df = wb.createDataFormat();
    	HSSFFont hfont = wb.createFont();

    	hfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	
    	this.sTitulo = wb.createCellStyle();
    	this.sTitulo.setFont(hfont);
    	this.sTitulo.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sTitulo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	
    	this.sHeader = wb.createCellStyle();
    	this.sHeader.setFont(hfont);
    	this.sHeader.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    	this.sHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	this.sHeader.setBorderTop(CellStyle.BORDER_THIN);
    	this.sHeader.setBorderRight(CellStyle.BORDER_THIN);
    	this.sHeader.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sHeader.setBorderBottom(CellStyle.BORDER_THIN);
    	this.sHeader.setWrapText(false);
    	
    	this.sHeaderWraptext = wb.createCellStyle();
    	this.sHeaderWraptext.setFont(hfont);
    	this.sHeaderWraptext.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sHeaderWraptext.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sHeaderWraptext.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    	this.sHeaderWraptext.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	this.sHeaderWraptext.setBorderTop(CellStyle.BORDER_THIN);
    	this.sHeaderWraptext.setBorderRight(CellStyle.BORDER_THIN);
    	this.sHeaderWraptext.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sHeaderWraptext.setBorderBottom(CellStyle.BORDER_THIN);
    	this.sHeaderWraptext.setWrapText(true);

    	this.sHeaderTotal = wb.createCellStyle();
    	this.sHeaderTotal.setFont(hfont);
    	this.sHeaderTotal.setAlignment(CellStyle.ALIGN_RIGHT);
    	this.sHeaderTotal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sHeaderTotal.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    	this.sHeaderTotal.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	this.sHeaderTotal.setBorderTop(CellStyle.BORDER_THIN);
    	this.sHeaderTotal.setBorderRight(CellStyle.BORDER_THIN);
    	this.sHeaderTotal.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sHeaderTotal.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sNumero = wb.createCellStyle();
    	this.sNumero.setAlignment(CellStyle.ALIGN_RIGHT);
    	this.sNumero.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sNumero.setDataFormat(df.getFormat("0.00"));
    	this.sNumero.setBorderTop(CellStyle.BORDER_THIN);
    	this.sNumero.setBorderRight(CellStyle.BORDER_THIN);
    	this.sNumero.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sNumero.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sMoneda = wb.createCellStyle();
    	this.sMoneda.setAlignment(CellStyle.ALIGN_RIGHT);
    	this.sMoneda.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sMoneda.setDataFormat(df.getFormat("[$$-80A]#,##0.00;-[$$-80A]#,##0.00"));
    	this.sMoneda.setBorderTop(CellStyle.BORDER_THIN);
    	this.sMoneda.setBorderRight(CellStyle.BORDER_THIN);
    	this.sMoneda.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sMoneda.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sNormalLeft = wb.createCellStyle();
    	this.sNormalLeft.setAlignment(CellStyle.ALIGN_LEFT);
    	this.sNormalLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sNormalLeft.setBorderTop(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderRight(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sNormalCenter = wb.createCellStyle();
    	this.sNormalCenter.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sNormalCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sNormalCenter.setBorderTop(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderRight(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sNormalCenterNoBorder = wb.createCellStyle();
    	this.sNormalCenterNoBorder.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sNormalCenterNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	
    	this.sFirma = wb.createCellStyle();
    	this.sFirma.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sFirma.setVerticalAlignment(CellStyle.VERTICAL_TOP);
    	this.sFirma.setBorderTop(CellStyle.BORDER_THIN);
    	this.sFirma.setBorderRight(CellStyle.BORDER_THIN);
    	this.sFirma.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sFirma.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sPorcentaje = wb.createCellStyle();
    	this.sPorcentaje.setAlignment(CellStyle.ALIGN_RIGHT);
    	this.sPorcentaje.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sPorcentaje.setDataFormat(df.getFormat("0.00%"));
    	this.sPorcentaje.setBorderTop(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderRight(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderBottom(CellStyle.BORDER_THIN);
    }

    private String getCellReference(int col, int row) {
		if (col < 0) return "";
		if (row == -1) return CellReference.convertNumToColString(col);
		return CellReference.convertNumToColString(col) + (row + 1);
	}
    
    private void agregarImagen(HSSFWorkbook libro, HSSFSheet hoja, String rutaImg) throws Exception {
    	String imgSrc = "";
    	InputStream inputStream = null;
    	CreationHelper helper = null;
    	ClientAnchor anchor = null;
    	Drawing drawing = null;
    	int pictureIdx = -1;
    	byte[] bytes = null;
    	
    	try {
        	if ("".equals(rutaImg)) return;
        	imgSrc = rutaImg + "logo_disesa.png";
        	
        	if (! (new File(imgSrc)).exists()) return;
        	
        	inputStream = new FileInputStream(imgSrc);
        	bytes = IOUtils.toByteArray(inputStream);
        	
        	//Adds a picture to the workbook
        	pictureIdx = libro.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_PNG);
        	//close the input stream
        	inputStream.close();

        	//Returns an object that handles instantiating concrete classes
        	helper = libro.getCreationHelper();

        	//Creates the top-level drawing patriarch.
        	drawing = hoja.createDrawingPatriarch();

        	//Create an anchor that is attached to the worksheet
        	anchor = helper.createClientAnchor();
        	//set top-left corner for the image
        	anchor.setCol1(0);
        	anchor.setRow1(0);
        	/*anchor.setDx1(0);
        	anchor.setDy1(0);*/

        	//Creates a picture
        	Picture pict = drawing.createPicture(anchor, pictureIdx);
        	//Reset the image to the original size
        	pict.resize(0.21);
    	} catch (Exception e) {
    		log.error("Error alagregar imagen", e);
    		throw e;
    	}
    }

    // ---------------------------------------------------------------------------------------------
    // CONSULTAS
    // ---------------------------------------------------------------------------------------------
    
    private ResultSet getTitulo() throws Exception {
    	try {
    		if (this.baseComparativa) {
    			this.strQuery = "SELECT COALESCE((SELECT af FROM de7a4d94446 WHERE aa = 0), '') AS familia "
    					+ "    , ob.id_obra || ' - ' || ob.nombre AS obra "
    					+ "    , COALESCE(e.nombre, '') AS director "
    					+ "    , COALESCE((SELECT mon.nombre || ' (' || mon.abreviacion || ')' "
    					+ "                FROM cotizacion cot INNER JOIN moneda mon ON mon.clave = cot.id_moneda "
    					+ "                WHERE cot.id = (SELECT MAX(id_cotizacion) FROM comparativa_detalle WHERE id_comparativa = com.id) "
    					+ "        ), 'PESOS (MXN)') AS moneda "
    					+ "    , CURRENT_DATE AS fecha "
    					+ "FROM comparativa com "
    					+ "    INNER JOIN obra ob ON ob.id_obra = com.id_obra "
    					+ "    LEFT  JOIN c5f822917f e ON e.aa = ob.id_responsable "
    					+ "WHERE com.id = $idComparativa;";
    			this.strQuery = this.strQuery.replace("$idComparativa", this.params.get("idComparativa"));
    		} else {
				this.strQuery = "SELECT COALESCE((SELECT af FROM de7a4d94446 WHERE $idFamilia), '') AS familia "
						+ ", COALESCE((SELECT id_obra || ' - ' || nombre FROM obra WHERE $idObra), '') AS obra "
						+ ", COALESCE((SELECT e.nombre FROM obra o INNER JOIN c5f822917f e ON e.aa = o.id_responsable WHERE $idObra), '') AS director "
						+ ", '' AS moneda "
						+ ", CURRENT_DATE AS fecha; ";
		        
		        if (this.params.containsKey("idObra") && ! "".equals(this.params.get("idObra")) && ! "0".equals(this.params.get("idObra"))) 
		        	this.strQuery = this.strQuery.replace("$idObra", "id_obra = " + this.params.get("idObra"));
		        else
		        	this.strQuery = this.strQuery.replace("$idObra", "id_obra = 0");
	
	            if (this.params.containsKey("idFamilia") && ! "".equals(this.params.get("idFamilia")) && ! "0".equals(this.params.get("idFamilia"))) 
	            	this.strQuery = this.strQuery.replace("$idFamilia", "aa = " + this.params.get("idFamilia"));
	            else
	            	this.strQuery = this.strQuery.replace("$idFamilia", "aa = 0");
    		}
	        
	        return this.conn.createStatement().executeQuery(strQuery);
	    } catch(Exception e) {
	    	this.errorCode = 3;
	    	this.errorDesc = "Error al obtener datos para el titulo: [" + this.strQuery + "]";
			log.error("Error en Comparativa.getTitulo", e);
			throw e;
		}
    }

    private ResultSet getProveedores() throws Exception {
    	String where = "";
    	
		try {
			this.strQuery = "SELECT DISTINCT cot.id_proveedor "
					+ "    , CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END AS proveedor "
					+ "    , string_agg(cot.folio, ', ') AS folio "
					+ "    , MAX(cot.flete) AS flete "
					+ "    , MAX(cot.tiempo_entrega) AS tiempo_entrega "
					+ "FROM cotizacion cot "
					+ "    LEFT JOIN c81498d458 pfi ON pfi.aa = cot.id_proveedor AND cot.tipo_persona_proveedor = 'P' "
					+ "    LEFT JOIN e769c352b7 pmo ON pmo.aa = cot.id_proveedor AND cot.tipo_persona_proveedor = 'N' "
					+ "$WHERE "
					+ "GROUP BY cot.id_proveedor, CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END "
					+ "ORDER BY CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END;";
	        
			if (this.baseComparativa) {
				where = "WHERE cot.id IN (SELECT id_cotizacion FROM comparativa_detalle WHERE id_comparativa = $idComparativa) ";
				this.strQuery = this.strQuery.replace("$WHERE", where);
				this.strQuery = this.strQuery.replace("$idComparativa", this.params.get("idComparativa"));
			} else {
				where = "WHERE $idObra AND $idRequisicion "
	                    + "    AND cot.estatus = 0 "
	                    + "    AND 0 < (SELECT COUNT(id) FROM cotizacion_detalle WHERE id_cotizacion = cot.id AND estatus = 0) ";
				this.strQuery = this.strQuery.replace("$WHERE", where);
				
		        if (this.params.containsKey("idObra") && ! "".equals(this.params.get("idObra")) && ! "0".equals(this.params.get("idObra"))) 
		        	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = " + this.params.get("idObra"));
		        else
		        	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = 0");
	
	            if (this.params.containsKey("idRequisicion") && ! "".equals(this.params.get("idRequisicion")) && ! "0".equals(this.params.get("idRequisicion"))) 
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion = " + this.params.get("idRequisicion"));
	            else
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion >= 0");
			}

	        return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(strQuery);
	    } catch(Exception e) {
	    	this.errorCode = 4;
	    	this.errorDesc = "Error al obtener los proveedores: [" + this.strQuery + "]";
			log.error("Error en Comparativa.getProveedores", e);
			throw e;
		}
    }

    private ResultSet getProductos() throws Exception {
    	String where = "";
    	
    	try {
            if (this.baseComparativa) {
                this.strQuery = "SELECT DISTINCT pro.id AS id_producto "
                        + "    , pro.clave AS codigo "
                        + "    , pro.descripcion AS producto "
                        + "    , pro.familia AS id_familia "
                        + "    , fam.af AS familia "
                        + "    , pro.unidad_medida AS id_unidad_medida "
                        + "    , med.af AS unidad_medida "
                        + "    , det.cotizar as cantidad "
                        + "    , COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) AS precio_unitario "
                        + "    , fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) AS pu_explosion "
                        + "    , (COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) * det.cantidad) AS importe "
                        + "    , obtener_iva() AS por_iva "
                        + "    , CURRENT_DATE AS fecha_emision "
                        + "FROM cotizacion cot "
                        + "    INNER JOIN cotizacion_detalle det ON det.id_cotizacion = cot.id "
                        + "    INNER JOIN producto pro ON pro.id = det.id_producto "
                        + "    LEFT  JOIN insumos_presupuesto ins ON ins.id_obra = cot.id_obra AND ins.estatus = 0 "
                        + "    LEFT  JOIN de7a4d94446 fam ON fam.aa = pro.familia "
                        + "    LEFT  JOIN de7a4d94446 med ON med.aa = pro.unidad_medida "
                        + "WHERE cot.id IN (SELECT MIN(id_cotizacion) FROM comparativa_detalle WHERE id_comparativa = $idComparativa) "
						+ "    AND TRIM(pro.clave) != '' "
                        + "    AND $idFamilia "
                        + "UNION ALL "
                		+ "SELECT DISTINCT pro.id AS id_producto "
                        + "    , pro.clave AS codigo "
                        + "    , pro.descripcion AS producto "
                        + "    , pro.familia AS id_familia "
                        + "    , fam.af AS familia "
                        + "    , pro.unidad_medida AS id_unidad_medida "
                        + "    , med.af AS unidad_medida "
                        + "    , det.cotizar as cantidad "
                        + "    , COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) AS precio_unitario "
                        + "    , fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) AS pu_explosion "
                        + "    , (COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) * det.cantidad) AS importe "
                        + "    , obtener_iva() AS por_iva "
                        + "    , CURRENT_DATE AS fecha_emision "
                        + "FROM cotizacion cot "
                        + "    INNER JOIN cotizacion_detalle det ON det.id_cotizacion = cot.id "
                        + "    INNER JOIN producto pro ON pro.id = det.id_producto "
                        + "    LEFT  JOIN insumos_presupuesto ins ON ins.id_obra = cot.id_obra AND ins.estatus = 0 "
                        + "    LEFT  JOIN de7a4d94446 fam ON fam.aa = pro.familia "
                        + "    LEFT  JOIN de7a4d94446 med ON med.aa = pro.unidad_medida "
                        + "WHERE cot.id IN (SELECT id_cotizacion FROM comparativa_detalle WHERE id_comparativa = $idComparativa) "
						+ "    AND det.id_producto NOT IN (SELECT id_producto FROM cotizacion_detalle WHERE id_cotizacion = (SELECT MIN(id_cotizacion) FROM comparativa_detalle WHERE id_comparativa = $idComparativa)) "
						+ "    AND TRIM(pro.clave) != '' "
                        + "    AND $idFamilia "
                        + "ORDER BY producto;";
				/*where = "WHERE cot.id IN (SELECT MIN(id_cotizacion) FROM comparativa_detalle WHERE id_comparativa = $idComparativa) "
						+ "    AND TRIM(pro.clave) != '' "
                        + "    AND $idFamilia ";
				this.strQuery = this.strQuery.replace("$WHERE", where);*/
				this.strQuery = this.strQuery.replace("$idComparativa", this.params.get("idComparativa"));
            } else {
                this.strQuery = "SELECT DISTINCT pro.id AS id_producto "
                        + "    , pro.clave AS codigo "
                        + "    , pro.descripcion AS producto "
                        + "    , pro.familia AS id_familia "
                        + "    , fam.af AS familia "
                        + "    , pro.unidad_medida AS id_unidad_medida "
                        + "    , med.af AS unidad_medida "
                        + "    , det.cotizar as cantidad "
                        + "    , COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) AS precio_unitario "
                        + "    , (COALESCE(CASE cot.id_requisicion WHEN 0 THEN fn_getPrecioUnitarioFromInsumo(ins.id, pro.id, pro.precio_compra) ELSE 0.0 END, 0.0) * det.cantidad) AS importe "
                        + "    , obtener_iva() AS por_iva "
                        + "    , CURRENT_DATE AS fecha_emision "
                        + "FROM cotizacion cot "
                        + "    INNER JOIN cotizacion_detalle det ON det.id_cotizacion = cot.id "
                        + "    INNER JOIN producto pro ON pro.id = det.id_producto "
                        + "    LEFT  JOIN insumos_presupuesto ins ON ins.id_obra = cot.id_obra AND ins.estatus = 0 "
                        + "    LEFT  JOIN de7a4d94446 fam ON fam.aa = pro.familia "
                        + "    LEFT  JOIN de7a4d94446 med ON med.aa = pro.unidad_medida "
                        + "$WHERE "
                        + "ORDER BY pro.descripcion;";
            	where = "WHERE TRIM(pro.clave) != '' "
                        + "    AND $idObra "
                        + "    AND $idRequisicion "
                        + "    AND $idFamilia "
                        + "    AND cot.estatus = 0 "
                        + "    AND det.estatus = 0 ";
				this.strQuery = this.strQuery.replace("$WHERE", where);
            	
	            if (this.params.containsKey("idObra") && ! "".equals(this.params.get("idObra")) && ! "0".equals(this.params.get("idObra"))) 
	            	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = " + this.params.get("idObra"));
	            else
	            	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = 0");
	
	            if (this.params.containsKey("idRequisicion") && ! "".equals(this.params.get("idRequisicion")) && ! "0".equals(this.params.get("idRequisicion"))) 
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion = " + this.params.get("idRequisicion"));
	            else
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion >= 0");
            }
        	
            if (this.params.containsKey("idFamilia") && ! "".equals(this.params.get("idFamilia")) && ! "0".equals(this.params.get("idFamilia"))) 
            	this.strQuery = this.strQuery.replace("$idFamilia", "pro.familia = " + this.params.get("idFamilia"));
            else
            	this.strQuery = this.strQuery.replace("$idFamilia", "pro.familia >= 0");
            
            return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(strQuery);
    	} catch(Exception e) {
	    	this.errorCode = 5;
	    	this.errorDesc = "Error al obtener los productos: [" + this.strQuery + "]";
    		log.error("Error en Comparativa.getProductos", e);
    		throw e;
    	}
    }
    
    private ResultSet getProveedoresProductos() throws Exception {
    	String where = "";
    	
		try {
			this.strQuery = "SELECT DISTINCT (cot.id_proveedor, det.id_producto) AS id_row "
					+ "    , cot.id_proveedor "
					+ "    , det.id_producto "
					+ "    , CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END AS proveedor "
					+ "    , pro.descripcion "
					+ "    , det.cotizar AS prov_cantidad "
					+ "    , case cot.id_moneda when 10000002 then conv_usd_mxn(det.precio_unitario, cot.tipo_cambio) else det.precio_unitario end AS prov_pu "
					+ "    , case cot.id_moneda when 10000002 then conv_usd_mxn(det.importe, cot.tipo_cambio) else det.importe end AS prov_importe "
					+ "FROM cotizacion cot "
					+ "    INNER JOIN cotizacion_detalle det ON det.id_cotizacion = cot.id "
					+ "    INNER JOIN producto pro ON pro.id = det.id_producto "
					+ "    LEFT  JOIN c81498d458 pfi ON pfi.aa = cot.id_proveedor AND cot.tipo_persona_proveedor = 'P' "
					+ "    LEFT  JOIN e769c352b7 pmo ON pmo.aa = cot.id_proveedor AND cot.tipo_persona_proveedor = 'N' "
					+ "$WHERE "
					+ "GROUP BY cot.id_proveedor, det.id_producto, CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END, pro.descripcion, det.cotizar, cot.id_moneda, cot.tipo_cambio, det.precio_unitario, det.importe "
					+ "ORDER BY pro.descripcion, CASE cot.tipo_persona_proveedor WHEN 'N' THEN pmo.af ELSE pfi.ag END;";
			
			if (this.baseComparativa) {
				where = "WHERE cot.id IN (SELECT id_cotizacion FROM comparativa_detalle WHERE id_comparativa = $idComparativa) ";
				this.strQuery = this.strQuery.replace("$WHERE", where);
				this.strQuery = this.strQuery.replace("$idComparativa", this.params.get("idComparativa"));
			} else {
				where = "WHERE $idObra "
	                    + "    AND $idRequisicion "
	                    + "    AND cot.estatus = 0 "
	                    + "    AND det.estatus = 0 ";
				this.strQuery = this.strQuery.replace("$WHERE", where);
				
		        if (this.params.containsKey("idObra") && ! "".equals(this.params.get("idObra")) && ! "0".equals(this.params.get("idObra"))) 
		        	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = " + this.params.get("idObra"));
		        else
		        	this.strQuery = this.strQuery.replace("$idObra", "cot.id_obra = 0");
	
	            if (this.params.containsKey("idRequisicion") && ! "".equals(this.params.get("idRequisicion")) && ! "0".equals(this.params.get("idRequisicion"))) 
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion = " + this.params.get("idRequisicion"));
	            else
	            	this.strQuery = this.strQuery.replace("$idRequisicion", "cot.id_requisicion >= 0");
			}

	        return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(strQuery);
	    } catch(Exception e) {
	    	this.errorCode = 6;
	    	this.errorDesc = "Error al obtener la relacion de Proveedores y Productos: [" + this.strQuery + "]";
			log.error("Error en Comparativa.getProveedoresProductos", e);
			throw e;
		}
    }
    
    private ResultSet getDataFromPPT01() throws Exception {
    	String where = "";
    	
    	try {
			this.strQuery = "SELECT 8 AS id "
					+ "    , 'INDIRECTOS DE CAMPO Y OFICINA CENTRAL' AS concepto "
					+ "    , COALESCE(( "
					+ "        SELECT SUM(COALESCE(d.monto, 0.0)) "
					+ "        FROM presupuesto p INNER JOIN presupuesto_detalle d ON d.id_presupuesto = p.id "
					+ "        WHERE $idObra AND d.id_concepto_presupuesto IN (8,9)), 0.0) AS monto "
					+ "    , COALESCE(( "
					+ "        SELECT SUM(COALESCE(d.porcentaje, 0.0)) "
					+ "        FROM presupuesto p INNER JOIN presupuesto_detalle d ON d.id_presupuesto = p.id "
					+ "        WHERE $idObra AND d.id_concepto_presupuesto IN (8,9)), 0.0) AS porcentaje "
					+ "UNION ALL "
					+ "SELECT 12 AS id "
					+ "    , 'UTILIDAD' AS concepto "
					+ "    , COALESCE(( "
					+ "        SELECT COALESCE(d.monto, 0.0) "
					+ "        FROM presupuesto p INNER JOIN presupuesto_detalle d ON d.id_presupuesto = p.id "
					+ "        WHERE $idObra AND d.id_concepto_presupuesto = 12), 0.0) AS monto "
					+ "    , COALESCE(( "
					+ "        SELECT COALESCE(d.porcentaje, 0.0) "
					+ "        FROM presupuesto p INNER JOIN presupuesto_detalle d ON d.id_presupuesto = p.id "
					+ "        WHERE $idObra AND d.id_concepto_presupuesto = 12), 0.0) AS porcentaje "
					+ "ORDER BY id;";
			
			if (this.baseComparativa) {
				where = "p.id_obra = (SELECT id_obra FROM comparativa WHERE id = $idComparativa)";
				this.strQuery = this.strQuery.replace("$idObra", where);
				this.strQuery = this.strQuery.replace("$idComparativa", this.params.get("idComparativa"));
			} else {
		        if (this.params.containsKey("idObra") && ! "".equals(this.params.get("idObra")) && ! "0".equals(this.params.get("idObra"))) 
		        	this.strQuery = this.strQuery.replace("$idObra", "p.id_obra = " + this.params.get("idObra"));
		        else
		        	this.strQuery = this.strQuery.replace("$idObra", "p.id_obra = 0");
			}

	        return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(strQuery);
	    } catch(Exception e) {
	    	this.errorCode = 4;
	    	this.errorDesc = "Error al obtener los datos del PPT01: [" + this.strQuery + "]";
			log.error("Error en Comparativa.getDataFromPPT01", e);
			throw e;
		}
    }
    
    private boolean getConnection() throws Exception {
		try {
			if (this.conn == null) {
				Class.forName(this.params.get("pgDriver"));
				this.conn = DriverManager.getConnection(this.params.get("pgUrl"), this.params.get("pgUsuario") , this.params.get("pgPassword"));
			}
		} catch (Exception e) {
			this.errorCode = 2;
    		this.errorDesc = "Error al obtener la coneccion :: " + e.getMessage();
			log.error("Error al obtener la coneccion", e);
			return false;
		}
		
		return true;
	}
    
    private void closeConnection() {
		try {
			if (this.conn == null)
				return;
				
			if (! this.conn.isClosed()) 
				this.conn.close();
				
			this.conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    // ---------------------------------------------------------------------------------------------
    // END CONSULTAS
    // ---------------------------------------------------------------------------------------------
    
    @SuppressWarnings("unused")
	private String formatoMoneda(double monto) {
    	return (new DecimalFormat("###,###,##0.00")).format (monto);
    }
    
    private String formatoPorcentaje(double monto) {
    	return (new DecimalFormat("0.0")).format (monto);
    }
    
    private void addComment(HSSFCell cell, String value) {
		if (value == null || "".equals(value))
			return;
	
    	Drawing drawing = cell.getSheet().createDrawingPatriarch();
        CreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setCol2(cell.getColumnIndex() + 2);
        anchor.setRow2(cell.getRowIndex() + 1);

        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(value);
        comment.setVisible(Boolean.FALSE);
        comment.setString(str);

        cell.setCellComment(comment);
    }
    
    private boolean esBaseComparativa() {
    	if (! this.params.containsKey("idComparativa")) return false;
		if (this.params.get("idComparativa") == null || "".equals(this.params.get("idComparativa").trim())) 
			this.params.put("idComparativa", "0");
		return ((new BigDecimal(this.params.get("idComparativa").trim())).doubleValue() > 0);
    }
}
