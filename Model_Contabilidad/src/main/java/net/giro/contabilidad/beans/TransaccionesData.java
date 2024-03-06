package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * transacciones_data
 * @author javaz
 */
public class TransaccionesData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; // NUMERIC
	private long codigoError; // NUMERIC
	private String descripcionError; // Varchar
	private long idMensajeTransaccion;
	private long codigoTransaccion; // NUMERIC
	private long idOperacion;
	private long idConcepto;
	private String concepto;
	private BigDecimal importe; // Numeric
	private String referencia1; // Varchar
	private String referencia2; // Varchar
	private long idSucursal; // NUMERIC
	private String sucursal; // Varchar
	private long idMoneda; // NUMERIC
	private String moneda; // Varchar
	private long idFormaPago; // NUMERIC
	private String formaPago; // NUMERIC
	private String valorLlave1; // Varchar
	private String valorLlave2; // Varchar
	private String valorLlave3; // Varchar
	private String valorLlave4; // Varchar
	private String valorLlave5; // Varchar
	private String valorLlave6; // Varchar
	private String valorLlave7; // Varchar
	private String valorLlave8; // Varchar
	private String valorLlave9; // Varchar
	private String valorLlave10; // Varchar
	private String valorLlave11; // Varchar
	private String valorLlave12; // Varchar
	private String valorLlave13; // Varchar
	private String valorLlave14; // Varchar
	private String valorLlave15; // Varchar
	private String valorLlave16; // Varchar
	private String valorLlave17; // Varchar
	private String valorLlave18; // Varchar
	private String valorLlave19; // Varchar
	private String valorLlave20; // Varchar
	private String valorLlave21; // Varchar
	private String valorLlave22; // Varchar
	private String valorLlave23; // Varchar
	private String valorLlave24; // Varchar
	private String valorLlave25; // Varchar
	private String valorLlave26; // Varchar
	private String valorLlave27; // Varchar
	private String valorLlave28; // Varchar
	private String valorLlave29; // Varchar
	private String valorLlave30; // Varchar
	private Long idUsuarioCreacion; // NUMERIC
	private String usuarioCreacion; // Varchar
	private Long idCanal; // NUMERIC
	private String canal; // Varchar
	private Long idFuncion;
	private long poliza;
	private long lote;
	private int contabilizado; // VARCHAR
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	public TransaccionesData() {
		this.importe = BigDecimal.ZERO;
		this.referencia1 = "";
		this.referencia2 = "";
		this.canal = "";
		this.sucursal = "";
		this.usuarioCreacion = "";
		this.moneda = "";
		this.descripcionError = "";
		this.concepto = "";
		this.formaPago = "";
		this.valorLlave1 = "";
		this.valorLlave2 = "";
		this.valorLlave3 = "";
		this.valorLlave4 = "";
		this.valorLlave5 = "";
		this.valorLlave6 = "";
		this.valorLlave7 = "";
		this.valorLlave8 = "";
		this.valorLlave9 = "";
		this.valorLlave10 = "";
		this.valorLlave11 = "";
		this.valorLlave12 = "";
		this.valorLlave13 = "";
		this.valorLlave14 = "";
		this.valorLlave15 = "";
		this.valorLlave16 = "";
		this.valorLlave17 = "";
		this.valorLlave18 = "";
		this.valorLlave19 = "";
		this.valorLlave20 = "";
		this.valorLlave21 = "";
		this.valorLlave22 = "";
		this.valorLlave23 = "";
		this.valorLlave24 = "";
		this.valorLlave25 = "";
		this.valorLlave26 = "";
		this.valorLlave27 = "";
		this.valorLlave28 = "";
		this.valorLlave29 = "";
		this.valorLlave30 = "";
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

	public long getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(long codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		if (descripcionError == null || "".equals(descripcionError.trim()))
			descripcionError = "";
		this.descripcionError = descripcionError;
	}

	public Long getIdMensajeTransaccion() {
		return idMensajeTransaccion;
	}

	public void setIdMensajeTransaccion(Long idMensajeTransaccion) {
		if (idMensajeTransaccion == null)
			idMensajeTransaccion = 0L;
		this.idMensajeTransaccion = idMensajeTransaccion;
	}

	public Long getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(Long codigoTransaccion) {
		if (codigoTransaccion == null)
			codigoTransaccion = 0L;
		this.codigoTransaccion = codigoTransaccion;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		if (idConcepto == null)
			idConcepto = 0L;
		this.idConcepto = idConcepto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		if (concepto == null || "".equals(concepto.trim()))
			concepto = "";
		this.concepto = concepto;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getReferencia1() {
		return referencia1;
	}

	public void setReferencia1(String referencia1) {
		if (referencia1 == null || "".equals(referencia1.trim()))
			referencia1 = "";
		this.referencia1 = referencia1;
	}

	public String getReferencia2() {
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		if (referencia2 == null || "".equals(referencia2.trim()))
			referencia2 = "";
		this.referencia2 = referencia2;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		if (idSucursal == null)
			idSucursal = 0L;
		this.idSucursal = idSucursal;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		if (sucursal == null || "".equals(sucursal.trim()))
			sucursal = "";
		this.sucursal = sucursal;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		if (idMoneda == null)
			idMoneda = 0L;
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		if (moneda == null || "".equals(moneda.trim()))
			moneda = "";
		this.moneda = moneda;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		if (idFormaPago == null)
			idFormaPago = 0L;
		this.idFormaPago = idFormaPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		if (formaPago == null || "".equals(formaPago.trim()))
			formaPago = "";
		this.formaPago = formaPago;
	}

	public Long getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(Long idOperacion) {
		if (idOperacion == null)
			idOperacion = 0L;
		this.idOperacion = idOperacion;
	}

	public String getValorLlave1() {
		return valorLlave1;
	}

	public void setValorLlave1(String valorLlave1) {
		if (valorLlave1 == null || "".equals(valorLlave1.trim()))
			valorLlave1 = "";
		this.valorLlave1 = valorLlave1;
	}

	public String getValorLlave2() {
		return valorLlave2;
	}

	public void setValorLlave2(String valorLlave2) {
		if (valorLlave2 == null || "".equals(valorLlave2.trim()))
			valorLlave2 = "";
		this.valorLlave2 = valorLlave2;
	}

	public String getValorLlave3() {
		return valorLlave3;
	}

	public void setValorLlave3(String valorLlave3) {
		if (valorLlave3 == null || "".equals(valorLlave3.trim()))
			valorLlave3 = "";
		this.valorLlave3 = valorLlave3;
	}

	public String getValorLlave4() {
		return valorLlave4;
	}

	public void setValorLlave4(String valorLlave4) {
		if (valorLlave4 == null || "".equals(valorLlave4.trim()))
			valorLlave4 = "";
		this.valorLlave4 = valorLlave4;
	}

	public String getValorLlave5() {
		return valorLlave5;
	}

	public void setValorLlave5(String valorLlave5) {
		if (valorLlave5 == null || "".equals(valorLlave5.trim()))
			valorLlave5 = "";
		this.valorLlave5 = valorLlave5;
	}

	public String getValorLlave6() {
		return valorLlave6;
	}

	public void setValorLlave6(String valorLlave6) {
		if (valorLlave6 == null || "".equals(valorLlave6.trim()))
			valorLlave6 = "";
		this.valorLlave6 = valorLlave6;
	}

	public String getValorLlave7() {
		return valorLlave7;
	}

	public void setValorLlave7(String valorLlave7) {
		if (valorLlave7 == null || "".equals(valorLlave7.trim()))
			valorLlave7 = "";
		this.valorLlave7 = valorLlave7;
	}

	public String getValorLlave8() {
		return valorLlave8;
	}

	public void setValorLlave8(String valorLlave8) {
		if (valorLlave8 == null || "".equals(valorLlave8.trim()))
			valorLlave8 = "";
		this.valorLlave8 = valorLlave8;
	}

	public String getValorLlave9() {
		return valorLlave9;
	}

	public void setValorLlave9(String valorLlave9) {
		if (valorLlave9 == null || "".equals(valorLlave9.trim()))
			valorLlave9 = "";
		this.valorLlave9 = valorLlave9;
	}

	public String getValorLlave10() {
		return valorLlave10;
	}

	public void setValorLlave10(String valorLlave10) {
		if (valorLlave10 == null || "".equals(valorLlave10.trim()))
			valorLlave10 = "";
		this.valorLlave10 = valorLlave10;
	}

	public String getValorLlave11() {
		return valorLlave11;
	}

	public void setValorLlave11(String valorLlave11) {
		if (valorLlave11 == null || "".equals(valorLlave11.trim()))
			valorLlave11 = "";
		this.valorLlave11 = valorLlave11;
	}

	public String getValorLlave12() {
		return valorLlave12;
	}

	public void setValorLlave12(String valorLlave12) {
		if (valorLlave12 == null || "".equals(valorLlave12.trim()))
			valorLlave12 = "";
		this.valorLlave12 = valorLlave12;
	}

	public String getValorLlave13() {
		return valorLlave13;
	}

	public void setValorLlave13(String valorLlave13) {
		if (valorLlave13 == null || "".equals(valorLlave13.trim()))
			valorLlave13 = "";
		this.valorLlave13 = valorLlave13;
	}

	public String getValorLlave14() {
		return valorLlave14;
	}

	public void setValorLlave14(String valorLlave14) {
		if (valorLlave14 == null || "".equals(valorLlave14.trim()))
			valorLlave14 = "";
		this.valorLlave14 = valorLlave14;
	}

	public String getValorLlave15() {
		return valorLlave15;
	}

	public void setValorLlave15(String valorLlave15) {
		if (valorLlave15 == null || "".equals(valorLlave15.trim()))
			valorLlave15 = "";
		this.valorLlave15 = valorLlave15;
	}

	public String getValorLlave16() {
		return valorLlave16;
	}

	public void setValorLlave16(String valorLlave16) {
		if (valorLlave16 == null || "".equals(valorLlave16.trim()))
			valorLlave16 = "";
		this.valorLlave16 = valorLlave16;
	}

	public String getValorLlave17() {
		return valorLlave17;
	}

	public void setValorLlave17(String valorLlave17) {
		if (valorLlave17 == null || "".equals(valorLlave17.trim()))
			valorLlave17 = "";
		this.valorLlave17 = valorLlave17;
	}

	public String getValorLlave18() {
		return valorLlave18;
	}

	public void setValorLlave18(String valorLlave18) {
		if (valorLlave18 == null || "".equals(valorLlave18.trim()))
			valorLlave18 = "";
		this.valorLlave18 = valorLlave18;
	}

	public String getValorLlave19() {
		return valorLlave19;
	}

	public void setValorLlave19(String valorLlave19) {
		if (valorLlave19 == null || "".equals(valorLlave19.trim()))
			valorLlave19 = "";
		this.valorLlave19 = valorLlave19;
	}

	public String getValorLlave20() {
		return valorLlave20;
	}

	public void setValorLlave20(String valorLlave20) {
		if (valorLlave20 == null || "".equals(valorLlave20.trim()))
			valorLlave20 = "";
		this.valorLlave20 = valorLlave20;
	}

	public String getValorLlave21() {
		return valorLlave21;
	}

	public void setValorLlave21(String valorLlave21) {
		if (valorLlave21 == null || "".equals(valorLlave21.trim()))
			valorLlave21 = "";
		this.valorLlave21 = valorLlave21;
	}

	public String getValorLlave22() {
		return valorLlave22;
	}

	public void setValorLlave22(String valorLlave22) {
		if (valorLlave22 == null || "".equals(valorLlave22.trim()))
			valorLlave22 = "";
		this.valorLlave22 = valorLlave22;
	}

	public String getValorLlave23() {
		return valorLlave23;
	}

	public void setValorLlave23(String valorLlave23) {
		if (valorLlave23 == null || "".equals(valorLlave23.trim()))
			valorLlave23 = "";
		this.valorLlave23 = valorLlave23;
	}

	public String getValorLlave24() {
		return valorLlave24;
	}

	public void setValorLlave24(String valorLlave24) {
		if (valorLlave24 == null || "".equals(valorLlave24.trim()))
			valorLlave24 = "";
		this.valorLlave24 = valorLlave24;
	}

	public String getValorLlave25() {
		return valorLlave25;
	}

	public void setValorLlave25(String valorLlave25) {
		if (valorLlave25 == null || "".equals(valorLlave25.trim()))
			valorLlave25 = "";
		this.valorLlave25 = valorLlave25;
	}

	public String getValorLlave26() {
		return valorLlave26;
	}

	public void setValorLlave26(String valorLlave26) {
		if (valorLlave26 == null || "".equals(valorLlave26.trim()))
			valorLlave26 = "";
		this.valorLlave26 = valorLlave26;
	}

	public String getValorLlave27() {
		return valorLlave27;
	}

	public void setValorLlave27(String valorLlave27) {
		if (valorLlave27 == null || "".equals(valorLlave27.trim()))
			valorLlave27 = "";
		this.valorLlave27 = valorLlave27;
	}

	public String getValorLlave28() {
		return valorLlave28;
	}

	public void setValorLlave28(String valorLlave28) {
		if (valorLlave28 == null || "".equals(valorLlave28.trim()))
			valorLlave28 = "";
		this.valorLlave28 = valorLlave28;
	}

	public String getValorLlave29() {
		return valorLlave29;
	}

	public void setValorLlave29(String valorLlave29) {
		if (valorLlave29 == null || "".equals(valorLlave29.trim()))
			valorLlave29 = "";
		this.valorLlave29 = valorLlave29;
	}

	public String getValorLlave30() {
		return valorLlave30;
	}

	public void setValorLlave30(String valorLlave30) {
		if (valorLlave30 == null || "".equals(valorLlave30.trim()))
			valorLlave30 = "";
		this.valorLlave30 = valorLlave30;
	}

	public Long getIdUsuarioCreacion() {
		return idUsuarioCreacion;
	}

	public void setIdUsuarioCreacion(Long idUsuarioCreacion) {
		if (idUsuarioCreacion == null)
			idUsuarioCreacion = 0L;
		this.idUsuarioCreacion = idUsuarioCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		if (usuarioCreacion == null || "".equals(usuarioCreacion.trim()))
			usuarioCreacion = "";
		this.usuarioCreacion = usuarioCreacion;
	}

	public Long getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(Long idCanal) {
		if (idCanal == null)
			idCanal = 0L;
		this.idCanal = idCanal;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		if (canal == null || "".equals(canal.trim()))
			canal = "";
		this.canal = canal;
	}

	public Long getIdFuncion() {
		return idFuncion;
	}

	public void setIdFuncion(Long idFuncion) {
		if (idFuncion == null)
			idFuncion = 0L;
		this.idFuncion = idFuncion;
	}

	public long getPoliza() {
		return poliza;
	}

	public void setPoliza(long poliza) {
		this.poliza = poliza;
	}

	public long getLote() {
		return lote;
	}

	public void setLote(long lote) {
		this.lote = lote;
	}

	public int getContabilizado() {
		return contabilizado;
	}

	public void setContabilizado(int contabilizado) {
		this.contabilizado = contabilizado;
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

	// ---------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------

	public String getLlavesValores() {
		String valores = "";

		if (this.valorLlave1 != null && ! "".equals(this.valorLlave1.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L1 - " + this.valorLlave1;
		if (this.valorLlave2 != null && ! "".equals(this.valorLlave2.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L2 - " + this.valorLlave2;
		if (this.valorLlave3 != null && ! "".equals(this.valorLlave3.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L3 - " + this.valorLlave3;
		if (this.valorLlave4 != null && ! "".equals(this.valorLlave4.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L4 - " + this.valorLlave4;
		if (this.valorLlave5 != null && ! "".equals(this.valorLlave5.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L5 - " + this.valorLlave5;
		if (this.valorLlave6 != null && ! "".equals(this.valorLlave6.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L6 - " + this.valorLlave6;
		if (this.valorLlave7 != null && ! "".equals(this.valorLlave7.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L7 - " + this.valorLlave7;
		if (this.valorLlave8 != null && ! "".equals(this.valorLlave8.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L8 - " + this.valorLlave8;
		if (this.valorLlave9 != null && ! "".equals(this.valorLlave9.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + " L9 - " + this.valorLlave9;
		if (this.valorLlave10 != null && ! "".equals(this.valorLlave10.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L10 - " + this.valorLlave10;
		if (this.valorLlave11 != null && ! "".equals(this.valorLlave11.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L11 - " + this.valorLlave11;
		if (this.valorLlave12 != null && ! "".equals(this.valorLlave12.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L12 - " + this.valorLlave12;
		if (this.valorLlave13 != null && ! "".equals(this.valorLlave13.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L13 - " + this.valorLlave13;
		if (this.valorLlave14 != null && ! "".equals(this.valorLlave14.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L14 - " + this.valorLlave14;
		if (this.valorLlave15 != null && ! "".equals(this.valorLlave15.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L15 - " + this.valorLlave15;
		if (this.valorLlave16 != null && ! "".equals(this.valorLlave16.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L16 - " + this.valorLlave16;
		if (this.valorLlave17 != null && ! "".equals(this.valorLlave17.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L17 - " + this.valorLlave17;
		if (this.valorLlave18 != null && ! "".equals(this.valorLlave18.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L18 - " + this.valorLlave18;
		if (this.valorLlave19 != null && ! "".equals(this.valorLlave19.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L19 - " + this.valorLlave19;
		if (this.valorLlave20 != null && ! "".equals(this.valorLlave20.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L20 - " + this.valorLlave20;
		if (this.valorLlave21 != null && ! "".equals(this.valorLlave21.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L21 - " + this.valorLlave21;
		if (this.valorLlave22 != null && ! "".equals(this.valorLlave22.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L22 - " + this.valorLlave22;
		if (this.valorLlave23 != null && ! "".equals(this.valorLlave23.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L23 - " + this.valorLlave23;
		if (this.valorLlave24 != null && ! "".equals(this.valorLlave24.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L24 - " + this.valorLlave24;
		if (this.valorLlave25 != null && ! "".equals(this.valorLlave25.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L25 - " + this.valorLlave25;
		if (this.valorLlave26 != null && ! "".equals(this.valorLlave26.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L26 - " + this.valorLlave26;
		if (this.valorLlave27 != null && ! "".equals(this.valorLlave27.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L27 - " + this.valorLlave27;
		if (this.valorLlave28 != null && ! "".equals(this.valorLlave28.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L28 - " + this.valorLlave28;
		if (this.valorLlave29 != null && ! "".equals(this.valorLlave29.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L29 - " + this.valorLlave29;
		if (this.valorLlave30 != null && ! "".equals(this.valorLlave30.trim()))
			valores += (! "".equals(valores.trim()) ? ", " : "") + "L30 - " + this.valorLlave30;

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