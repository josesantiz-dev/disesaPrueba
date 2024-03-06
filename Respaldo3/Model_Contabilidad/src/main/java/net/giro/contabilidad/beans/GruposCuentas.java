package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * grupos_cuentas
 * @author javaz
 */
public class GruposCuentas implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; 
	private Grupos idGrupo;
	private Long idCuenta;
	private String noCuenta;
	private Long valorLlave1;
	private Long valorLlave2;
	private Long valorLlave3;
	private Long valorLlave4;
	private Long valorLlave5;
	private Long valorLlave6;
	private Long valorLlave7;
	private Long valorLlave8;
	private Long valorLlave9;
	private Long valorLlave10;
	private Long valorLlave11;
	private Long valorLlave12;
	private Long valorLlave13;
	private Long valorLlave14;
	private Long valorLlave15;
	private Long valorLlave16;
	private Long valorLlave17;
	private Long valorLlave18;
	private Long valorLlave19;
	private Long valorLlave20;
	private Long valorLlave21;
	private Long valorLlave22;
	private Long valorLlave23;
	private Long valorLlave24;
	private Long valorLlave25;
	private Long valorLlave26;
	private Long valorLlave27;
	private Long valorLlave28;
	private Long valorLlave29;
	private Long valorLlave30;
	private String tipoPersona;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;
	// -------------------------------------------------
	private HashMap<Long, String> valorLlaveDescripcion;

	public GruposCuentas() {
		this.noCuenta = "";
		this.tipoPersona = "";
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
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

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
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

	// ---------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------

	public String getValorLlaveDescripcion(long valorLlave) {
		return this.valorLlaveDescripcion.get(valorLlave);
	}

	public void addValorLlaveDescripcion(long valorLlave, String descripcion) {
		this.valorLlaveDescripcion.put(valorLlave, descripcion);
	}

	public String getLlavesValores() {
		String valores = "";
		
		if (this.idGrupo == null)
			return valores;
		
		if (this.idGrupo.getLlave1() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave1().getDescripcion() + ": " + this.valorLlave1 + ((this.tipoPersona != null && ! "".equals(this.tipoPersona)) ? " (" + this.tipoPersona + ")" : "");
		if (this.idGrupo.getLlave2() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave2().getDescripcion() + ": " + this.valorLlave2;
		if (this.idGrupo.getLlave3() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave3().getDescripcion() + ": " + this.valorLlave3;
		if (this.idGrupo.getLlave4() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave4().getDescripcion() + ": " + this.valorLlave4;
		if (this.idGrupo.getLlave5() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave5().getDescripcion() + ": " + this.valorLlave5;
		if (this.idGrupo.getLlave6() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave6().getDescripcion() + ": " + this.valorLlave6;
		if (this.idGrupo.getLlave7() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave7().getDescripcion() + ": " + this.valorLlave7;
		if (this.idGrupo.getLlave8() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave8().getDescripcion() + ": " + this.valorLlave8;
		if (this.idGrupo.getLlave9() != null)
			valores += (! "".equals(valores.trim()) ? ", " : " ") + this.idGrupo.getLlave9().getDescripcion() + ": " + this.valorLlave9;
		if (this.idGrupo.getLlave10() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave10().getDescripcion() + ": " + this.valorLlave10;
		if (this.idGrupo.getLlave11() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave11().getDescripcion() + ": " + this.valorLlave11;
		if (this.idGrupo.getLlave12() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave12().getDescripcion() + ": " + this.valorLlave12;
		if (this.idGrupo.getLlave13() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave13().getDescripcion() + ": " + this.valorLlave13;
		if (this.idGrupo.getLlave14() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave14().getDescripcion() + ": " + this.valorLlave14;
		if (this.idGrupo.getLlave15() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave15().getDescripcion() + ": " + this.valorLlave15;
		if (this.idGrupo.getLlave16() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave16().getDescripcion() + ": " + this.valorLlave16;
		if (this.idGrupo.getLlave17() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave17().getDescripcion() + ": " + this.valorLlave17;
		if (this.idGrupo.getLlave18() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave18().getDescripcion() + ": " + this.valorLlave18;
		if (this.idGrupo.getLlave19() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave19().getDescripcion() + ": " + this.valorLlave19;
		if (this.idGrupo.getLlave20() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave20().getDescripcion() + ": " + this.valorLlave20;
		if (this.idGrupo.getLlave21() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave21().getDescripcion() + ": " + this.valorLlave21;
		if (this.idGrupo.getLlave22() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave22().getDescripcion() + ": " + this.valorLlave22;
		if (this.idGrupo.getLlave23() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave23().getDescripcion() + ": " + this.valorLlave23;
		if (this.idGrupo.getLlave24() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave24().getDescripcion() + ": " + this.valorLlave24;
		if (this.idGrupo.getLlave25() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave25().getDescripcion() + ": " + this.valorLlave25;
		if (this.idGrupo.getLlave26() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave26().getDescripcion() + ": " + this.valorLlave26;
		if (this.idGrupo.getLlave27() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave27().getDescripcion() + ": " + this.valorLlave27;
		if (this.idGrupo.getLlave28() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave28().getDescripcion() + ": " + this.valorLlave28;
		if (this.idGrupo.getLlave29() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave29().getDescripcion() + ": " + this.valorLlave29;
		if (this.idGrupo.getLlave30() != null)
			valores += (! "".equals(valores.trim()) ? ", " : "") + this.idGrupo.getLlave30().getDescripcion() + ": " + this.valorLlave30;

		return valores;
	}
	
	public void setLlavesValores(String value) {}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */