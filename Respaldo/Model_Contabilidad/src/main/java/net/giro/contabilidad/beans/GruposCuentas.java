package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GruposCuentas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private Grupos idGrupo;
	private Long idCuenta; // NUMERIC
	private String noCuenta; // Varchar
	private Long valorLlave1; // Varchar
	private Long valorLlave2; // Varchar
	private Long valorLlave3; // Varchar
	private Long valorLlave4; // Varchar
	private Long valorLlave5; // Varchar
	private Long valorLlave6; // Varchar
	private Long valorLlave7; // Varchar
	private Long valorLlave8; // Varchar
	private Long valorLlave9; // Varchar
	private Long valorLlave10; // Varchar
	private Long valorLlave11; // Varchar
	private Long valorLlave12; // Varchar
	private Long valorLlave13; // Varchar
	private Long valorLlave14; // Varchar
	private Long valorLlave15; // Varchar
	private Long valorLlave16; // Varchar
	private Long valorLlave17; // Varchar
	private Long valorLlave18; // Varchar
	private Long valorLlave19; // Varchar
	private Long valorLlave20; // Varchar
	private Long valorLlave21; // Varchar
	private Long valorLlave22; // Varchar
	private Long valorLlave23; // Varchar
	private Long valorLlave24; // Varchar
	private Long valorLlave25; // Varchar
	private Long valorLlave26; // Varchar
	private Long valorLlave27; // Varchar
	private Long valorLlave28; // Varchar
	private Long valorLlave29; // Varchar
	private Long valorLlave30; // Varchar
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	
	private HashMap<Long, String> valorLlaveDescripcion;


	public GruposCuentas() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.valorLlaveDescripcion = new HashMap<Long, String>();
	}
	
	public GruposCuentas(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
		this.valorLlaveDescripcion = new HashMap<Long, String>();
	}
	
	public GruposCuentas(Long id, Grupos idGrupo, Long idCuenta, String noCuenta, 
			Long valorLlave1, Long valorLlave2, Long valorLlave3, Long valorLlave30,
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.idGrupo = idGrupo;
		this.valorLlave1 = valorLlave1;
		this.valorLlave2 = valorLlave2;
		this.valorLlave3 = valorLlave3;
		this.valorLlave30 = valorLlave30;
		this.idCuenta = idCuenta;
		this.noCuenta = noCuenta;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
		this.valorLlaveDescripcion = new HashMap<Long, String>();
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

	public Grupos getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Grupos idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getNoCuenta() {
		return noCuenta;
	}

	public void setNoCuenta(String noCuenta) {
		this.noCuenta = noCuenta;
	}

	public Long getValorLlave1() {
		return valorLlave1;
	}

	public void setValorLlave1(Long valorLlave1) {
		this.valorLlave1 = valorLlave1;
	}

	public Long getValorLlave2() {
		return valorLlave2;
	}

	public void setValorLlave2(Long valorLlave2) {
		this.valorLlave2 = valorLlave2;
	}

	public Long getValorLlave3() {
		return valorLlave3;
	}

	public void setValorLlave3(Long valorLlave3) {
		this.valorLlave3 = valorLlave3;
	}

	public Long getValorLlave4() {
		return valorLlave4;
	}

	public void setValorLlave4(Long valorLlave4) {
		this.valorLlave4 = valorLlave4;
	}

	public Long getValorLlave5() {
		return valorLlave5;
	}

	public void setValorLlave5(Long valorLlave5) {
		this.valorLlave5 = valorLlave5;
	}

	public Long getValorLlave6() {
		return valorLlave6;
	}

	public void setValorLlave6(Long valorLlave6) {
		this.valorLlave6 = valorLlave6;
	}

	public Long getValorLlave7() {
		return valorLlave7;
	}

	public void setValorLlave7(Long valorLlave7) {
		this.valorLlave7 = valorLlave7;
	}

	public Long getValorLlave8() {
		return valorLlave8;
	}

	public void setValorLlave8(Long valorLlave8) {
		this.valorLlave8 = valorLlave8;
	}

	public Long getValorLlave9() {
		return valorLlave9;
	}

	public void setValorLlave9(Long valorLlave9) {
		this.valorLlave9 = valorLlave9;
	}

	public Long getValorLlave10() {
		return valorLlave10;
	}

	public void setValorLlave10(Long valorLlave10) {
		this.valorLlave10 = valorLlave10;
	}

	public Long getValorLlave11() {
		return valorLlave11;
	}

	public void setValorLlave11(Long valorLlave11) {
		this.valorLlave11 = valorLlave11;
	}

	public Long getValorLlave12() {
		return valorLlave12;
	}

	public void setValorLlave12(Long valorLlave12) {
		this.valorLlave12 = valorLlave12;
	}

	public Long getValorLlave13() {
		return valorLlave13;
	}

	public void setValorLlave13(Long valorLlave13) {
		this.valorLlave13 = valorLlave13;
	}

	public Long getValorLlave14() {
		return valorLlave14;
	}

	public void setValorLlave14(Long valorLlave14) {
		this.valorLlave14 = valorLlave14;
	}

	public Long getValorLlave15() {
		return valorLlave15;
	}

	public void setValorLlave15(Long valorLlave15) {
		this.valorLlave15 = valorLlave15;
	}

	public Long getValorLlave16() {
		return valorLlave16;
	}

	public void setValorLlave16(Long valorLlave16) {
		this.valorLlave16 = valorLlave16;
	}

	public Long getValorLlave17() {
		return valorLlave17;
	}

	public void setValorLlave17(Long valorLlave17) {
		this.valorLlave17 = valorLlave17;
	}

	public Long getValorLlave18() {
		return valorLlave18;
	}

	public void setValorLlave18(Long valorLlave18) {
		this.valorLlave18 = valorLlave18;
	}

	public Long getValorLlave19() {
		return valorLlave19;
	}

	public void setValorLlave19(Long valorLlave19) {
		this.valorLlave19 = valorLlave19;
	}

	public Long getValorLlave20() {
		return valorLlave20;
	}

	public void setValorLlave20(Long valorLlave20) {
		this.valorLlave20 = valorLlave20;
	}

	public Long getValorLlave21() {
		return valorLlave21;
	}

	public void setValorLlave21(Long valorLlave21) {
		this.valorLlave21 = valorLlave21;
	}

	public Long getValorLlave22() {
		return valorLlave22;
	}

	public void setValorLlave22(Long valorLlave22) {
		this.valorLlave22 = valorLlave22;
	}

	public Long getValorLlave23() {
		return valorLlave23;
	}

	public void setValorLlave23(Long valorLlave23) {
		this.valorLlave23 = valorLlave23;
	}

	public Long getValorLlave24() {
		return valorLlave24;
	}

	public void setValorLlave24(Long valorLlave24) {
		this.valorLlave24 = valorLlave24;
	}

	public Long getValorLlave25() {
		return valorLlave25;
	}

	public void setValorLlave25(Long valorLlave25) {
		this.valorLlave25 = valorLlave25;
	}

	public Long getValorLlave26() {
		return valorLlave26;
	}

	public void setValorLlave26(Long valorLlave26) {
		this.valorLlave26 = valorLlave26;
	}

	public Long getValorLlave27() {
		return valorLlave27;
	}

	public void setValorLlave27(Long valorLlave27) {
		this.valorLlave27 = valorLlave27;
	}

	public Long getValorLlave28() {
		return valorLlave28;
	}

	public void setValorLlave28(Long valorLlave28) {
		this.valorLlave28 = valorLlave28;
	}

	public Long getValorLlave29() {
		return valorLlave29;
	}

	public void setValorLlave29(Long valorLlave29) {
		this.valorLlave29 = valorLlave29;
	}

	public Long getValorLlave30() {
		return valorLlave30;
	}

	public void setValorLlave30(Long valorLlave30) {
		this.valorLlave30 = valorLlave30;
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

	public String getValorLlaveDescripcion(long valor) {
		return this.valorLlaveDescripcion.get(valor);
	}

	public void addValorLlaveDescripcion(long valor, String descripcion) {
		this.valorLlaveDescripcion.put(valor, descripcion);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */