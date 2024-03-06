package net.giro.backorder.ordenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

public class BackOrderOrdenes implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BackOrderOrdenes.class);
	
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
	private CellStyle sCantidad = null;
	private CellStyle sMoneda = null;
	private CellStyle sNormalLeft = null;
	private CellStyle sNormalCenter = null;
	private CellStyle sNormalCenterNoBorder = null;
	private CellStyle sFirma = null;
	private CellStyle sPorcentaje = null;
	private CellStyle sPorcentajeCenter = null;
	private List<String> etiquetas;
	//private boolean baseComparativa;
    
    public BackOrderOrdenes() {
    	this.conn = null;

    	this.etiquetas = new ArrayList<String>();
    	this.etiquetas.add("SUBTOTAL");
    	this.etiquetas.add("IVA");
    	this.etiquetas.add("TOTAL");
    	this.etiquetas.add("MARGEN");
    	this.etiquetas.add("FLETE");
    	this.etiquetas.add("TIEMPO ENTREGA");
    }
    
    public BackOrderOrdenes(HashMap<String, String> params) {
    	this();
    	this.params = params;
    }

    
    public int generarReporte() throws Exception {
    	//SimpleDateFormat formatter = null;
    	FileOutputStream fileOut = null;
    	HSSFWorkbook libro = null;
    	HSSFSheet hoja = null;
        HSSFRow row = null;
        HSSFCell celda = null;
        ResultSet rsMain = null; 		// productos
        ResultSet rsFacturas = null; 	// facturas (Entradas a almacen)
    	String salida = "";
        int currentRow = 0;
        int noColumna = 0;
        boolean tieneFacturas = false;
        int numReg = 0;
        int colFacturas = 0;
    	
    	try {
    		if (this.params == null || this.params.isEmpty()) {
    			this.errorCode = 1;
    			this.errorDesc = "No se indicaron parametros.";
    			throw new Exception (this.errorCode + " - " + this.errorDesc);
    		}

            if (! this.getConnection())
            	throw new Exception(this.errorCode + " - " + this.errorDesc);

            libro = new HSSFWorkbook();
            hoja = libro.createSheet("BACKORDER_OC-" + this.params.get("idOrdenCompra"));
            
            // Inicializamos
            currentRow = 0; // BASE 0
            noColumna = 0;  // BASE 0
            generaCellStyles(libro);

            // Recuperamos datos
            rsMain = getOrdenCompra();
            rsFacturas = getFacturas();
            
            if (! rsMain.next()) {
            	log.info("No se pudo generar el reporte con los parametros indicados");
            	return 1;
            }
            
            row = hoja.createRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Contiene");
    		celda.setCellStyle(this.sHeaderTotal);
    		
    		noColumna = noColumna + 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("BACKORDER: ORDEN DE COMPRA");
    		celda.setCellStyle(this.sHeader);
    		
    		noColumna = noColumna + 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("PLAN DE CALIDAD V-01");
    		celda.setCellStyle(this.sHeader);
    		
    		noColumna = noColumna + 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 2)));
    		celda.setCellValue("");

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Orden de Compra");
    		celda.setCellStyle(this.sHeaderTotal);
    		
    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue(rsMain.getString("folio"));
    		celda.setCellStyle(this.sNormalLeft);
    		
    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Formato");
    		celda.setCellStyle(this.sHeader);
    		
    		noColumna += 1;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("F-CO-02/REV01");
    		celda.setCellStyle(this.sHeader);
    		
    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Fecha");
    		celda.setCellStyle(this.sHeaderTotal);
    		
    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue(rsMain.getString("fecha"));
    		celda.setCellStyle(this.sNormalLeft);
    		
    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Fecha Emision");
    		celda.setCellStyle(this.sHeader);
    		
    		noColumna += 1;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue(rsMain.getString("fecha_emision"));
    		celda.setCellStyle(this.sNormalCenter);
    		
    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Obra");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("id_obra") + " - " + rsMain.getString("obra"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Empresa");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("proveedor") + " (" + rsMain.getString("rfc") + ")");
    		celda.setCellStyle(this.sNormalLeft);
    		
    		noColumna += 4;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow + 3, noColumna, (noColumna + 2)));
    		celda.setCellValue("5 de Febrero s/n Local 12\nEsq. I. Ramirez, La Paz B.C.S.\nrfc: DIS060704QLA\nTel/Fax: 124 1500");
    		celda.setCellStyle(this.sNormalCenter);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Atencion");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("contacto"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Correo");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("correo"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Tel y Fax");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("telefono"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Solicita");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("solicita"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Comprador");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 3)));
    		celda.setCellValue(rsMain.getString("comprador"));
    		celda.setCellStyle(this.sNormalLeft);

    		// --------------------------------------------------------------------------------------------
    		currentRow += 2;
    		noColumna = 0;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("No.");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Codigo");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Descripcion");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Unidad");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Cantidad");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Precio Unitario");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("Importe");
    		celda.setCellStyle(this.sHeader);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("Surtido");
    		celda.setCellStyle(this.sHeader);

    		// FACTURAS
            if (rsFacturas.next()) {
            	tieneFacturas = true;
            	noColumna += 2;
            	
            	do {
            		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
            		celda.setCellValue(rsFacturas.getString("folio_factura"));
            		celda.setCellStyle(this.sHeader);
            		noColumna++;
            		colFacturas++;
    			} while (rsFacturas.next());
            }

            do {
            	// PRODUCTOS
        		// --------------------------------------------------------------------------------------------
        		currentRow++;
        		noColumna = 0;
        		numReg++;
        		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(numReg);
        		celda.setCellStyle(this.sNormalCenter);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsMain.getString("codigo"));
        		celda.setCellStyle(this.sNormalCenter);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsMain.getString("descripcion"));
        		celda.setCellStyle(this.sNormalLeft);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
        		celda.setCellValue(rsMain.getString("unidad_medida"));
        		celda.setCellStyle(this.sNormalCenter);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsMain.getDouble("cantidad"));
        		celda.setCellStyle(this.sCantidad);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsMain.getDouble("precio_unitario"));
        		celda.setCellStyle(this.sNumero);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsMain.getDouble("importe"));
        		celda.setCellStyle(this.sNumero);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellValue(rsMain.getDouble("surtido"));
        		celda.setCellStyle(this.sCantidad);

        		noColumna++;
        		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
        		celda.setCellFormula("H" + (currentRow + 1) + "/" + "E" + (currentRow + 1));
        		celda.setCellStyle(this.sPorcentaje);
        		
        		// SUMINISTRADO EN FACTURA
        		if (tieneFacturas && rsFacturas.first()) {
                	do {
                		noColumna++;
                		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
                		if (rsMain.getString("id_producto").equals(rsFacturas.getString("id_producto")))
                    		celda.setCellValue(rsFacturas.getDouble("cantidad_surtida"));
                		else 
	                		celda.setCellValue("");
                		celda.setCellStyle(this.sCantidad);
        			} while (rsFacturas.next());
                }
            } while (rsMain.next());
            
            if (! rsMain.first()) {
            	return 1;
            }

    		// --------------------------------------------------------------------------------------------
    		currentRow += 2;
    		noColumna = 1;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("FIRMAS AUTORIZADAS");
    		celda.setCellStyle(this.sHeader);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("ANTICIPO");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellFormula(rsMain.getString("anticipo") + "/G" + (currentRow + 3));
    		celda.setCellStyle(this.sPorcentajeCenter);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("SUBTOTAL");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue(rsMain.getDouble("subtotal"));
    		celda.setCellStyle(this.sMoneda);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 1;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, (currentRow + 3), noColumna, (noColumna + 1)));
    		celda.setCellValue("FIRMA");
    		celda.setCellStyle(this.sFirma);

    		noColumna += 2;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("MONEDA");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue(rsMain.getString("moneda_abr"));
    		celda.setCellStyle(this.sNormalCenter);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("IVA (" + rsMain.getString("porcentaje_iva") + " %)");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue(rsMain.getDouble("iva"));
    		celda.setCellStyle(this.sMoneda);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 3;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, noColumna));
    		celda.setCellValue("T. CAMBIO");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue(rsMain.getDouble("tipo_cambio"));
    		celda.setCellStyle(this.sCantidad);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("TOTAL");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue(rsMain.getDouble("total"));
    		celda.setCellStyle(this.sMoneda);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 3;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, noColumna));
    		celda.setCellValue("PLAZO");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue(rsMain.getDouble("plazo"));
    		celda.setCellStyle(this.sCantidad);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("DESCUENTO");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellValue(rsMain.getDouble("descuento"));
    		celda.setCellStyle(this.sMoneda);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 3;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, noColumna));
    		celda.setCellValue("T. ENTREGA");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue(rsMain.getDouble("tiempo_entrega"));
    		celda.setCellStyle(this.sCantidad);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		celda.setCellValue("GRAN TOTAL");
    		celda.setCellStyle(this.sHeaderTotal);

    		noColumna++;
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_NUMERIC);
    		celda.setCellFormula("G" + (currentRow -1) + "-G" + currentRow);
    		celda.setCellStyle(this.sMoneda);

    		// --------------------------------------------------------------------------------------------
    		currentRow += 2;
    		noColumna = 1;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, currentRow, noColumna, (noColumna + 1)));
    		celda.setCellValue("LUGAR DE ENTREGA");
    		celda.setCellStyle(this.sHeader);

    		// --------------------------------------------------------------------------------------------
    		currentRow++;
    		noColumna = 1;
    		row = (hoja.getRow(currentRow) == null) ? hoja.createRow(currentRow) : hoja.getRow(currentRow);
    		celda = row.createCell(noColumna, HSSFCell.CELL_TYPE_STRING);
    		hoja.addMergedRegion(new CellRangeAddress(currentRow, (currentRow + 3), noColumna, (noColumna + 1)));
    		celda.setCellValue(rsMain.getString("lugar_entrega"));
    		celda.setCellStyle(this.sNormalCenter);
            
            // LOGO
            if (this.params.containsKey("ruta_img"))
            	agregarImagen(libro, hoja, this.params.get("ruta_img"));
            
            // Ajustamos columnas
            hoja.autoSizeColumn(getCellReference("A"), true);
            hoja.autoSizeColumn(getCellReference("B"), true);
            hoja.autoSizeColumn(getCellReference("C"), true);
            hoja.autoSizeColumn(getCellReference("D"), true);
            hoja.autoSizeColumn(getCellReference("E"), true);
            hoja.autoSizeColumn(getCellReference("F"), true);
            hoja.autoSizeColumn(getCellReference("G"), true);
            hoja.autoSizeColumn(getCellReference("H"), true);
            hoja.autoSizeColumn(getCellReference("I"), true);
            if (colFacturas > 0) {
	            colFacturas = getCellReference("I") + colFacturas; // mas columnas default BASE-ZERO
				for (int indexCell = getCellReference("J"); indexCell < colFacturas; indexCell++)
					hoja.autoSizeColumn(getCellReference(getCellReference(indexCell,-1)));
			}
            
    		// GUARDAMOS
    		if (this.params.containsKey("salida"))
    			salida = this.params.get("salida");
    		else if (this.params.containsKey("SALIDA"))
    			salida = this.params.get("SALIDA");
    		else
    			salida = "salida";
    		salida += (! salida.contains(".xls") ? ".xls" : "");
    		
    		fileOut = new FileOutputStream(salida);
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

    // ---------------------------------------------------------------------------------------------
    // TALENT
    // ---------------------------------------------------------------------------------------------
    
    public static void main(String[] args) throws Exception {
    	BackOrderOrdenes comp = new BackOrderOrdenes();
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
    // CONSULTAS y METODOS
    // ---------------------------------------------------------------------------------------------
    
    private ResultSet getOrdenCompra() throws Exception {
		try {
			// Generamos consulta
			this.strQuery = "SELECT oc.id AS id_orden_compra"
						+ ", det.id AS id_orden_compra_detalle"
						+ ", oc.folio"
						+ ", DATE(oc.fecha) AS fecha"
						+ ", oc.id_proveedor"
						+ ", oc.anticipo"
						+ ", oc.tipo_cambio"
						+ ", oc.plazo"
						+ ", oc.tiempo_entrega"
						+ ", oc.lugar_entrega"
						+ ", o.id_obra"
						+ ", o.nombre AS obra"
						+ ", CASE cot.tipo_persona_proveedor WHEN 'N' THEN nprv.af ELSE prv.ag END AS proveedor"
						+ ", cto.ax AS correo"
						+ ", CASE cot.tipo_persona_proveedor WHEN 'N' THEN nprv.ah ELSE prv.aw END AS telefono"
						+ ", CASE cot.tipo_persona_proveedor WHEN 'N' THEN nprv.ar ELSE prv.am END AS rfc"
						+ ", cto.ag AS contacto"
						+ ", sta.nombre AS solicita"
						+ ", com.nombre AS comprador"
						+ ", pro.id AS id_producto"
						+ ", pro.clave AS codigo"
						+ ", pro.descripcion"
						+ ", CASE COALESCE(med.aa,0) WHEN 0 THEN '' ELSE med.af END AS unidad_medida"
						+ ", det.cantidad"
						+ ", det.precio_unitario"
						+ ", det.importe"
						+ ", oc.subtotal"
						+ ", oc.iva"
						+ ", oc.total"
						+ ", (SELECT B.ai FROM d7729f32ba7 A INNER JOIN b761110ccfe B ON B.af = A.aa WHERE A.af='VALOR_IVA') AS porcentaje_iva"
						+ ", usu.ag AS autorizo"
						+ ", pro.unidad_medida AS id_unidad_medida"
						+ ", CURRENT_DATE AS fecha_emision"
						+ ", oc.id_moneda"
						+ ", mo.abreviacion AS moneda_abr"
						+ ", mo.nombre || ' (' || mo.abreviacion || ')' AS moneda"
						+ ", 0.00 AS descuento"
						+ ", COALESCE((SELECT COALESCE(SUM(cantidad), 0) "
									+ "FROM almacen_movimientos y INNER JOIN movimientos_detalle z ON z.id_almacen_movimiento = y.id "
									+ "WHERE y.id_orden_compra = oc.id AND z.id_producto = det.id_producto), 0) AS surtido "
					+ "FROM orden_compra oc "
						+ "INNER JOIN orden_compra_detalle det ON det.id_orden_compra = oc.id "
						+ "INNER JOIN cotizacion cot ON cot.id = oc.id_cotizacion "
						+ "INNER JOIN moneda mo ON mo.clave = oc.id_moneda "
						+ "INNER JOIN obra o ON o.id_obra = oc.id_obra "
						+ "LEFT  JOIN c81498d458 prv ON prv.aa = oc.id_proveedor AND cot.tipo_persona_proveedor = 'P' "
						+ "LEFT  JOIN e769c352b7 nprv ON nprv.aa = oc.id_proveedor AND cot.tipo_persona_proveedor = 'N' "
						+ "LEFT  JOIN c81498d458 cto ON cto.aa = oc.id_contacto "
						+ "INNER JOIN producto pro ON pro.id = det.id_producto "
						+ "LEFT  JOIN de7a4d94446 med ON med.aa = pro.unidad_medida "
						+ "LEFT  JOIN dc8deac2731 usu ON usu.aa = oc.id_usuario_autorizo "
						+ "INNER JOIN c5f822917f sta ON sta.aa = oc.id_solicita "
						+ "INNER JOIN c5f822917f com ON com.aa = cot.id_comprador "
					+ "WHERE oc.id = $idOrdenCompra "
					+ "ORDER BY pro.descripcion;";
			
			// Reemplazamos parametro
			this.strQuery = this.strQuery.replace("$idOrdenCompra", this.params.get("idOrdenCompra"));

			// Ejecutamos consulta
	        return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(this.strQuery);
	    } catch(Exception e) {
			log.error("Error en BackOrderOrdenes.getOrdenCompra", e);
	    	this.errorDesc = "Error al obtener la Orden de Compra: [" + this.strQuery + "]";
	    	this.errorCode = 5;
			throw e;
		}
    }
    
    private ResultSet getFacturas() throws Exception {
		try {
			// Generamos consulta
			this.strQuery = "SELECT y.id AS id_movimiento, y.folio_factura, z.id_producto, z.cantidad AS cantidad_surtida "
					+ "FROM almacen_movimientos y INNER JOIN movimientos_detalle z ON z.id_almacen_movimiento = y.id "
					+ "WHERE y.id_orden_compra = $idOrdenCompra AND y.tipo = 0;";
			
			// Reemplazamos parametro
			this.strQuery = this.strQuery.replace("$idOrdenCompra", this.params.get("idOrdenCompra"));

			// Ejecutamos consulta
	        return this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(strQuery);
	    } catch(Exception e) {
			log.error("Error en BackOrderOrdenes.getFacturas", e);
	    	this.errorDesc = "Error al obtener la relacion de Facturas y Productos en Orden de Compra: [" + this.strQuery + "]";
	    	this.errorCode = 6;
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
    	
    	this.sCantidad = wb.createCellStyle();
    	this.sCantidad.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sCantidad.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sCantidad.setDataFormat(df.getFormat("0.00"));
    	this.sCantidad.setBorderTop(CellStyle.BORDER_THIN);
    	this.sCantidad.setBorderRight(CellStyle.BORDER_THIN);
    	this.sCantidad.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sCantidad.setBorderBottom(CellStyle.BORDER_THIN);
    	
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
    	this.sPorcentaje.setDataFormat(df.getFormat("0.## %"));
    	this.sPorcentaje.setBorderTop(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderRight(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sPorcentaje.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sPorcentajeCenter = wb.createCellStyle();
    	this.sPorcentajeCenter.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sPorcentajeCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sPorcentajeCenter.setDataFormat(df.getFormat("0.## %"));
    	this.sPorcentajeCenter.setBorderTop(CellStyle.BORDER_THIN);
    	this.sPorcentajeCenter.setBorderRight(CellStyle.BORDER_THIN);
    	this.sPorcentajeCenter.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sPorcentajeCenter.setBorderBottom(CellStyle.BORDER_THIN);
    }

	private String getCellReference(int col, int row) {
		if (col < 0) 
			return "";
		if (row == -1) 
			return CellReference.convertNumToColString(col);
		return CellReference.convertNumToColString(col) + (row + 1);
	}
    
	private int getCellReference(String col) {
		if (col == null || "".equals(col.trim()))
			return 0;
		return CellReference.convertColStringToIndex(col);
	}
    
    private void agregarImagen(HSSFWorkbook libro, HSSFSheet hoja, String rutaImg) throws Exception {
    	String imgSrc = "";
    	InputStream inputStream = null;
    	CreationHelper helper = null;
    	ClientAnchor anchor = null;
    	Drawing drawing = null;
    	Picture pict = null;
    	int pictureIdx = -1;
    	byte[] bytes = null;
    	
    	try {
        	if ("".equals(rutaImg))
        		return;
        	
        	imgSrc = rutaImg + "logo_disesa.png";
        	if (! (new File(imgSrc)).exists()) 
        		return;
        	
        	inputStream = new FileInputStream(imgSrc);
        	bytes = IOUtils.toByteArray(inputStream);
        	
        	// Adds a picture to the workbook
        	pictureIdx = libro.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_PNG);
        	// close the input stream
        	inputStream.close();

        	// Returns an object that handles instantiating concrete classes
        	helper = libro.getCreationHelper();

        	// Creates the top-level drawing patriarch.
        	drawing = hoja.createDrawingPatriarch();

        	// Create an anchor that is attached to the worksheet
        	anchor = helper.createClientAnchor();
        	// set top-left corner for the image
        	anchor.setCol1(6);
        	anchor.setRow1(0);
        	/*anchor.setDx1(0);
        	anchor.setDy1(0);*/

        	// Creates a picture
        	pict = drawing.createPicture(anchor, pictureIdx);
        	// Reset the image to the original size
        	pict.resize(0.18);
    	} catch (Exception e) {
    		log.error("Error al agregar imagen", e);
    		throw e;
    	}
    }

    @SuppressWarnings("unused")
	private String formatoMoneda(double monto) {
    	return (new DecimalFormat("###,###,##0.00")).format (monto);
    }
    
    @SuppressWarnings("unused")
	private String formatoPorcentaje(double monto) {
    	return (new DecimalFormat("0.0")).format(monto);
    }
    
    @SuppressWarnings("unused")
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
}
