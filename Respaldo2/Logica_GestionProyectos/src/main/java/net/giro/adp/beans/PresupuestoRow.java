package net.giro.adp.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class PresupuestoRow implements Serializable {
	private static final long serialVersionUID = 1L;

	private int indexSheet;
	private String sheetName;
	private Long idObra;
	private String tema;
	private String ruta;
	private LinkedHashMap<String,String> concepto01;
	private LinkedHashMap<String,String> concepto02;
	private LinkedHashMap<String,String> concepto03;
	private LinkedHashMap<String,String> concepto04;
	private LinkedHashMap<String,String> concepto05;
	private LinkedHashMap<String,String> concepto06;
	private LinkedHashMap<String,String> concepto07;
	private LinkedHashMap<String,String> concepto08;
	private LinkedHashMap<String,String> concepto09;
	private LinkedHashMap<String,String> concepto10;
	private LinkedHashMap<String,String> concepto11;
	private LinkedHashMap<String,String> concepto12;
	private LinkedHashMap<String,String> concepto13;
	private LinkedHashMap<String,String> concepto14;
	private LinkedHashMap<String,String> concepto15;
	private LinkedHashMap<String,String> concepto16;
	
	public PresupuestoRow(int indexSheet, String sheetName) {
		this.indexSheet = indexSheet;
		this.sheetName = sheetName;
		this.idObra = 0L;
		this.tema = "";
		this.ruta = "";
		this.concepto01 = new LinkedHashMap<String,String>();
		this.concepto02 = new LinkedHashMap<String,String>();
		this.concepto03 = new LinkedHashMap<String,String>();
		this.concepto04 = new LinkedHashMap<String,String>();
		this.concepto05 = new LinkedHashMap<String,String>();
		this.concepto06 = new LinkedHashMap<String,String>();
		this.concepto07 = new LinkedHashMap<String,String>();
		this.concepto08 = new LinkedHashMap<String,String>();
		this.concepto09 = new LinkedHashMap<String,String>();
		this.concepto10 = new LinkedHashMap<String,String>();
		this.concepto11 = new LinkedHashMap<String,String>();
		this.concepto12 = new LinkedHashMap<String,String>();
		this.concepto13 = new LinkedHashMap<String,String>();
		this.concepto14 = new LinkedHashMap<String,String>();
		this.concepto15 = new LinkedHashMap<String,String>();
		this.concepto16 = new LinkedHashMap<String,String>();
	}

	public int getIndexSheet() {
		return indexSheet;
	}

	public void setIndexSheet(int indexSheet) {
		this.indexSheet = indexSheet;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public Long getIdObra() {
		return idObra;
	}
	
	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}
	
	public String getTema() {
		return tema;
	}
	
	public void setTema(String tema) {
		this.tema = tema;
	}
	
	public String getRuta() {
		return ruta;
	}
	
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public LinkedHashMap<String,String> getConcepto01() {
		return concepto01;
	}

	public void setConcepto01(LinkedHashMap<String,String> concepto01) {
		this.concepto01 = concepto01;
	}

	public LinkedHashMap<String,String> getConcepto02() {
		return concepto02;
	}

	public void setConcepto02(LinkedHashMap<String,String> concepto02) {
		this.concepto02 = concepto02;
	}

	public LinkedHashMap<String,String> getConcepto03() {
		return concepto03;
	}

	public void setConcepto03(LinkedHashMap<String,String> concepto03) {
		this.concepto03 = concepto03;
	}

	public LinkedHashMap<String,String> getConcepto04() {
		return concepto04;
	}

	public void setConcepto04(LinkedHashMap<String,String> concepto04) {
		this.concepto04 = concepto04;
	}

	public LinkedHashMap<String,String> getConcepto05() {
		return concepto05;
	}

	public void setConcepto05(LinkedHashMap<String,String> concepto05) {
		this.concepto05 = concepto05;
	}

	public LinkedHashMap<String,String> getConcepto06() {
		return concepto06;
	}

	public void setConcepto06(LinkedHashMap<String,String> concepto06) {
		this.concepto06 = concepto06;
	}

	public LinkedHashMap<String,String> getConcepto07() {
		return concepto07;
	}

	public void setConcepto07(LinkedHashMap<String,String> concepto07) {
		this.concepto07 = concepto07;
	}

	public LinkedHashMap<String,String> getConcepto08() {
		return concepto08;
	}

	public void setConcepto08(LinkedHashMap<String,String> concepto08) {
		this.concepto08 = concepto08;
	}

	public LinkedHashMap<String,String> getConcepto09() {
		return concepto09;
	}

	public void setConcepto09(LinkedHashMap<String,String> concepto09) {
		this.concepto09 = concepto09;
	}

	public LinkedHashMap<String,String> getConcepto10() {
		return concepto10;
	}

	public void setConcepto10(LinkedHashMap<String,String> concepto10) {
		this.concepto10 = concepto10;
	}

	public LinkedHashMap<String,String> getConcepto11() {
		return concepto11;
	}

	public void setConcepto11(LinkedHashMap<String,String> concepto11) {
		this.concepto11 = concepto11;
	}

	public LinkedHashMap<String,String> getConcepto12() {
		return concepto12;
	}

	public void setConcepto12(LinkedHashMap<String,String> concepto12) {
		this.concepto12 = concepto12;
	}

	public LinkedHashMap<String,String> getConcepto13() {
		return concepto13;
	}

	public void setConcepto13(LinkedHashMap<String,String> concepto13) {
		this.concepto13 = concepto13;
	}

	public LinkedHashMap<String,String> getConcepto14() {
		return concepto14;
	}

	public void setConcepto14(LinkedHashMap<String,String> concepto14) {
		this.concepto14 = concepto14;
	}

	public LinkedHashMap<String,String> getConcepto15() {
		return concepto15;
	}

	public void setConcepto15(LinkedHashMap<String,String> concepto15) {
		this.concepto15 = concepto15;
	}

	public LinkedHashMap<String,String> getConcepto16() {
		return concepto16;
	}

	public void setConcepto16(LinkedHashMap<String,String> concepto16) {
		this.concepto16 = concepto16;
	}
	
	public boolean registroCompleto() {
		if (idObra == null || idObra <= 0L) return false;
		if (ruta == null || "".equals(ruta)) return false;
		//if (tema == null || "".equals(tema)) return false; // NO LIMITA LA CARGA
		
		if (concepto01 == null || concepto01.isEmpty()) return false;
		if (concepto02 == null || concepto02.isEmpty()) return false;
		if (concepto03 == null || concepto03.isEmpty()) return false;
		if (concepto04 == null || concepto04.isEmpty()) return false;
		if (concepto05 == null || concepto05.isEmpty()) return false;
		if (concepto06 == null || concepto06.isEmpty()) return false;
		if (concepto07 == null || concepto07.isEmpty()) return false;
		if (concepto08 == null || concepto08.isEmpty()) return false;
		if (concepto09 == null || concepto09.isEmpty()) return false;
		if (concepto10 == null || concepto10.isEmpty()) return false;
		if (concepto11 == null || concepto11.isEmpty()) return false;
		if (concepto12 == null || concepto12.isEmpty()) return false;
		if (concepto13 == null || concepto13.isEmpty()) return false;
		if (concepto14 == null || concepto14.isEmpty()) return false;
		if (concepto15 == null || concepto15.isEmpty()) return false;
		if (concepto16 == null || concepto16.isEmpty()) return false;
		
		return true;
	}
	
	public String informacionFaltante() {
		String resultado = "";
		
		if (this.registroCompleto())
			return "OK";

		if (idObra == null || idObra <= 0L) resultado += (! "".equals(resultado) ? "," : "") + "idObra";
		if (ruta == null || "".equals(ruta)) resultado += (! "".equals(resultado) ? "," : "") + "ruta";
		//if (tema == null || "".equals(tema)) resultado += (! "".equals(resultado) ? "," : "") + "tema";
		
		if (concepto01 == null || concepto01.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto01";
		if (concepto02 == null || concepto02.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto02";
		if (concepto03 == null || concepto03.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto03";
		if (concepto04 == null || concepto04.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto04";
		if (concepto05 == null || concepto05.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto05";
		if (concepto06 == null || concepto06.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto06";
		if (concepto07 == null || concepto07.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto07";
		if (concepto08 == null || concepto08.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto08";
		if (concepto09 == null || concepto09.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto09";
		if (concepto10 == null || concepto10.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto10";
		if (concepto11 == null || concepto11.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto11";
		if (concepto12 == null || concepto12.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto12";
		if (concepto13 == null || concepto13.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto13";
		if (concepto14 == null || concepto14.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto14";
		if (concepto15 == null || concepto15.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto15";
		if (concepto16 == null || concepto16.isEmpty()) resultado += (! "".equals(resultado) ? "," : "") + "concepto16";
		
		if ("".equals(resultado))
			resultado = "OK";
		else
			resultado = "Informacion faltante: " + resultado;
		
		return resultado;
	}
	
	public LinkedHashMap<String,String> getConceptoByIndex(int index) {
		if (index ==  0) return this.concepto01;
		if (index ==  1) return this.concepto02;
		if (index ==  2) return this.concepto03;
		if (index ==  3) return this.concepto04;
		if (index ==  4) return this.concepto05;
		if (index ==  5) return this.concepto06;
		if (index ==  6) return this.concepto07;
		if (index ==  7) return this.concepto08;
		if (index ==  8) return this.concepto09;
		if (index ==  9) return this.concepto10;
		if (index == 10) return this.concepto11;
		if (index == 11) return this.concepto12;
		if (index == 12) return this.concepto13;
		if (index == 13) return this.concepto14;
		if (index == 14) return this.concepto15;
		if (index == 15) return this.concepto16;
		return null;
	}
}
