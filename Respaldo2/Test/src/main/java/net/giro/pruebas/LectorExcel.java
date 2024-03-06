package net.giro.pruebas;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/* ---------------------
 * --  DEPENDENCIAS   --
 * ---------------------
 * org.apache.poi 3.13 (poi, poi-ooxml, poi-ooxml-schemas)
 * ---------------------
 * -- TIPOS DE CELDAS --
 * ---------------------
 * 0 CELL_TYPE_NUMERIC
 * 1 CELL_TYPE_STRING
 * 2 CELL_TYPE_FORMULA
 * 3 CELL_TYPE_BLANK
 * 4 CELL_TYPE_BOOLEAN
 * 5 CELL_TYPE_ERROR
 */

public class LectorExcel {
	private HashMap<String, String> mapReference;
	private byte[] source;
		
	public LectorExcel() {
		this(null, null);
	}
	
	public LectorExcel(byte[] source) {
		this(source, null);
	}
	
	public LectorExcel(HashMap<String, String> mapReference) {
		this(null, mapReference);
	}

	public LectorExcel(byte[] source, HashMap<String, String> mapReference) {
		this.mapReference = mapReference;
		this.source = source;
		
		if (this.mapReference == null)
			this.mapReference = new HashMap<String, String>();
	}

	public HashMap<String, String> getMapReference() {
		return mapReference;
	}

	public void setMapReference(HashMap<String, String> mapReference) {
		this.mapReference = mapReference;
	}

	public byte[] getSource() {
		return source;
	}

	public void setSource(byte[] source) {
		this.source = source;
	}
	
	public void leer() {
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet;
		Iterator<Row> rowIterator;
		FormulaEvaluator eval;
		SimpleDateFormat formatter;
		Row row = null;
		Cell celda = null;
		int indexRow = 0;
		int indexCel = 0;
		
		try {
			if (this.source == null)
				return;
			
			file = new ByteArrayInputStream(this.source);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			eval = workbook.getCreationHelper().createFormulaEvaluator();
			rowIterator = sheet.iterator();
			
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				row = rowIterator.next();
				if (! this.mapReference.isEmpty())
					indexCel = CellReference.convertColStringToIndex(this.mapReference.get("1"));
				celda = row.getCell(indexCel);
				
				if (celda == null || celda.getCellType() == Cell.CELL_TYPE_BLANK)
					continue;
				
				switch(celda.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.println(CellReference.convertNumToColString(celda.getColumnIndex()) + (indexRow + 1) + ": " + celda.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(celda)) {
						formatter = new SimpleDateFormat("dd-MM-yyyy");
						System.out.println(CellReference.convertNumToColString(celda.getColumnIndex()) + (indexRow + 1) + ": " + formatter.format(celda.getDateCellValue()));
					} else {
						System.out.println(CellReference.convertNumToColString(celda.getColumnIndex()) + (indexRow + 1) + ": " + celda.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_FORMULA:
					CellValue cellValue = eval.evaluate(celda);
					switch(cellValue.getCellType()) {
		            case Cell.CELL_TYPE_NUMERIC:
		            	System.out.println(CellReference.convertNumToColString(celda.getColumnIndex()) + (indexRow + 1) + ": " + celda.getNumericCellValue());
						break;
		            case Cell.CELL_TYPE_STRING:
		            	System.out.println(CellReference.convertNumToColString(celda.getColumnIndex()) + (indexRow + 1) + ": " + celda.getStringCellValue());
						break;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void leer(byte[] source) {
		this.source = source;
		leer();
	}
}
