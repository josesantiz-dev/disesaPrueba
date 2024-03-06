package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * grupos
 * @author javaz
 *
 */
public class Grupos implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // NUMERIC
	private String descripcion; // VARCHAR
	private Llaves llave1;
	private Llaves llave2;
	private Llaves llave3;
	private Llaves llave4;
	private Llaves llave5;
	private Llaves llave6;
	private Llaves llave7;
	private Llaves llave8;
	private Llaves llave9;
	private Llaves llave10;
	private Llaves llave11;
	private Llaves llave12;
	private Llaves llave13;
	private Llaves llave14;
	private Llaves llave15;
	private Llaves llave16;
	private Llaves llave17;
	private Llaves llave18;
	private Llaves llave19;
	private Llaves llave20;
	private Llaves llave21;
	private Llaves llave22;
	private Llaves llave23;
	private Llaves llave24;
	private Llaves llave25;
	private Llaves llave26;
	private Llaves llave27;
	private Llaves llave28;
	private Llaves llave29;
	private Llaves llave30;
	private long idEmpresa;
	private int estatus;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	public Grupos() {
		this.descripcion = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Llaves getLlave1() {
		return llave1;
	}

	public void setLlave1(Llaves llave1) {
		this.llave1 = llave1;
	}

	public Llaves getLlave2() {
		return llave2;
	}

	public void setLlave2(Llaves llave2) {
		this.llave2 = llave2;
	}

	public Llaves getLlave3() {
		return llave3;
	}

	public void setLlave3(Llaves llave3) {
		this.llave3 = llave3;
	}

	public Llaves getLlave4() {
		return llave4;
	}

	public void setLlave4(Llaves llave4) {
		this.llave4 = llave4;
	}

	public Llaves getLlave5() {
		return llave5;
	}

	public void setLlave5(Llaves llave5) {
		this.llave5 = llave5;
	}

	public Llaves getLlave6() {
		return llave6;
	}

	public void setLlave6(Llaves llave6) {
		this.llave6 = llave6;
	}

	public Llaves getLlave7() {
		return llave7;
	}

	public void setLlave7(Llaves llave7) {
		this.llave7 = llave7;
	}

	public Llaves getLlave8() {
		return llave8;
	}

	public void setLlave8(Llaves llave8) {
		this.llave8 = llave8;
	}

	public Llaves getLlave9() {
		return llave9;
	}

	public void setLlave9(Llaves llave9) {
		this.llave9 = llave9;
	}

	public Llaves getLlave10() {
		return llave10;
	}

	public void setLlave10(Llaves llave10) {
		this.llave10 = llave10;
	}

	public Llaves getLlave11() {
		return llave11;
	}

	public void setLlave11(Llaves llave11) {
		this.llave11 = llave11;
	}

	public Llaves getLlave12() {
		return llave12;
	}

	public void setLlave12(Llaves llave12) {
		this.llave12 = llave12;
	}

	public Llaves getLlave13() {
		return llave13;
	}

	public void setLlave13(Llaves llave13) {
		this.llave13 = llave13;
	}

	public Llaves getLlave14() {
		return llave14;
	}

	public void setLlave14(Llaves llave14) {
		this.llave14 = llave14;
	}

	public Llaves getLlave15() {
		return llave15;
	}

	public void setLlave15(Llaves llave15) {
		this.llave15 = llave15;
	}

	public Llaves getLlave16() {
		return llave16;
	}

	public void setLlave16(Llaves llave16) {
		this.llave16 = llave16;
	}

	public Llaves getLlave17() {
		return llave17;
	}

	public void setLlave17(Llaves llave17) {
		this.llave17 = llave17;
	}

	public Llaves getLlave18() {
		return llave18;
	}

	public void setLlave18(Llaves llave18) {
		this.llave18 = llave18;
	}

	public Llaves getLlave19() {
		return llave19;
	}

	public void setLlave19(Llaves llave19) {
		this.llave19 = llave19;
	}

	public Llaves getLlave20() {
		return llave20;
	}

	public void setLlave20(Llaves llave20) {
		this.llave20 = llave20;
	}

	public Llaves getLlave21() {
		return llave21;
	}

	public void setLlave21(Llaves llave21) {
		this.llave21 = llave21;
	}

	public Llaves getLlave22() {
		return llave22;
	}

	public void setLlave22(Llaves llave22) {
		this.llave22 = llave22;
	}

	public Llaves getLlave23() {
		return llave23;
	}

	public void setLlave23(Llaves llave23) {
		this.llave23 = llave23;
	}

	public Llaves getLlave24() {
		return llave24;
	}

	public void setLlave24(Llaves llave24) {
		this.llave24 = llave24;
	}

	public Llaves getLlave25() {
		return llave25;
	}

	public void setLlave25(Llaves llave25) {
		this.llave25 = llave25;
	}

	public Llaves getLlave26() {
		return llave26;
	}

	public void setLlave26(Llaves llave26) {
		this.llave26 = llave26;
	}

	public Llaves getLlave27() {
		return llave27;
	}

	public void setLlave27(Llaves llave27) {
		this.llave27 = llave27;
	}

	public Llaves getLlave28() {
		return llave28;
	}

	public void setLlave28(Llaves llave28) {
		this.llave28 = llave28;
	}

	public Llaves getLlave29() {
		return llave29;
	}

	public void setLlave29(Llaves llave29) {
		this.llave29 = llave29;
	}

	public Llaves getLlave30() {
		return llave30;
	}

	public void setLlave30(Llaves llave30) {
		this.llave30 = llave30;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public long getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(long creadoPor) {
		this.creadoPor = creadoPor;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public long getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	// ---------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------

	public String getLlaves() {
		String llaves = "";

		if (this.llave1 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave1.getDescripcion();
		if (this.llave2 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave2.getDescripcion();
		if (this.llave3 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave3.getDescripcion();
		if (this.llave4 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave4.getDescripcion();
		if (this.llave5 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave5.getDescripcion();
		if (this.llave6 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave6.getDescripcion();
		if (this.llave7 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave7.getDescripcion();
		if (this.llave8 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave8.getDescripcion();
		if (this.llave9 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave9.getDescripcion();
		if (this.llave10 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave10.getDescripcion();
		if (this.llave11 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave11.getDescripcion();
		if (this.llave12 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave12.getDescripcion();
		if (this.llave13 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave13.getDescripcion();
		if (this.llave14 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave14.getDescripcion();
		if (this.llave15 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave15.getDescripcion();
		if (this.llave16 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave16.getDescripcion();
		if (this.llave17 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave17.getDescripcion();
		if (this.llave18 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave18.getDescripcion();
		if (this.llave19 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave19.getDescripcion();
		if (this.llave20 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave20.getDescripcion();
		if (this.llave21 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave21.getDescripcion();
		if (this.llave22 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave22.getDescripcion();
		if (this.llave23 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave23.getDescripcion();
		if (this.llave24 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave24.getDescripcion();
		if (this.llave25 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave25.getDescripcion();
		if (this.llave26 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave26.getDescripcion();
		if (this.llave27 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave27.getDescripcion();
		if (this.llave28 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave28.getDescripcion();
		if (this.llave29 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave29.getDescripcion();
		if (this.llave30 != null)
			llaves += (! "".equals(llaves.trim()) ? ", " : "") + this.llave30.getDescripcion();

		return llaves;
	}
	
	public void setLlaves(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */