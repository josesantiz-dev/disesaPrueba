package net.giro.contabilidad.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class TransaccionesData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id; // NUMERIC
	private Long codigoTransaccion; // NUMERIC
	private Long idConcepto;
	private BigDecimal importe; // Numeric
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
	private String referencia1; // Varchar
	private String referencia2; // Varchar
	private Long idCanal; // NUMERIC
	private String canal; // Varchar
	private Long idSucursal; // NUMERIC
	private String sucursal; // Varchar
	private Long idUsuarioCreacion; // NUMERIC
	private String usuarioCreacion; // Varchar
	private Long idMoneda; // NUMERIC
	private String moneda; // Varchar
	private Long idFormaPago; // NUMERIC
	private Long idOperacion;
	private Long idFuncion;
	private Long poliza;
	private Long lote;
	private int contabilizado; // VARCHAR
	private Long codigoError; // NUMERIC
	private String descripcionError; // Varchar
	private long creadoPor;
	private Date fechaCreacion;
	private long modificadoPor;
	private Date fechaModificacion;

	
	public TransaccionesData() {
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}
	
	public TransaccionesData(Long id) {
		super();
		this.id = id;
		this.fechaCreacion = Calendar.getInstance().getTime();
		this.fechaModificacion = Calendar.getInstance().getTime();
	}

	public TransaccionesData(Long id, Long codigoTransaccion, Long idConcepto, BigDecimal importe, String valorLlave1,
			String valorLlave2, String valorLlave3, String valorLlave4, String valorLlave5, String valorLlave6,
			String valorLlave7, String valorLlave8, String valorLlave9, String valorLlave10, String valorLlave11,
			String valorLlave12, String valorLlave13, String valorLlave14, String valorLlave15, String valorLlave16,
			String valorLlave17, String valorLlave18, String valorLlave19, String valorLlave20, String valorLlave21,
			String valorLlave22, String valorLlave23, String valorLlave24, String valorLlave25, String valorLlave26,
			String valorLlave27, String valorLlave28, String valorLlave29, String valorLlave30, String referencia1,
			String referencia2, Long idCanal, String canal, Long idSucursal, String sucursal, Long idUsuarioCreacion,
			String usuarioCreacion, Long idMoneda, String moneda, Long idFormaPago, Long idOperacion, Long idFuncion,
			Long poliza, Long lote, int contabilizado, Long codigoError, String descripcionError, 
			long creadoPor, Date fechaCreacion, long modificadoPor, Date fechaModificacion) {
		super();
		this.id = id;
		this.codigoTransaccion = codigoTransaccion;
		this.idConcepto = idConcepto;
		this.importe = importe;
		this.valorLlave1 = valorLlave1;
		this.valorLlave2 = valorLlave2;
		this.valorLlave3 = valorLlave3;
		this.valorLlave4 = valorLlave4;
		this.valorLlave5 = valorLlave5;
		this.valorLlave6 = valorLlave6;
		this.valorLlave7 = valorLlave7;
		this.valorLlave8 = valorLlave8;
		this.valorLlave9 = valorLlave9;
		this.valorLlave10 = valorLlave10;
		this.valorLlave11 = valorLlave11;
		this.valorLlave12 = valorLlave12;
		this.valorLlave13 = valorLlave13;
		this.valorLlave14 = valorLlave14;
		this.valorLlave15 = valorLlave15;
		this.valorLlave16 = valorLlave16;
		this.valorLlave17 = valorLlave17;
		this.valorLlave18 = valorLlave18;
		this.valorLlave19 = valorLlave19;
		this.valorLlave20 = valorLlave20;
		this.valorLlave21 = valorLlave21;
		this.valorLlave22 = valorLlave22;
		this.valorLlave23 = valorLlave23;
		this.valorLlave24 = valorLlave24;
		this.valorLlave25 = valorLlave25;
		this.valorLlave26 = valorLlave26;
		this.valorLlave27 = valorLlave27;
		this.valorLlave28 = valorLlave28;
		this.valorLlave29 = valorLlave29;
		this.valorLlave30 = valorLlave30;
		this.referencia1 = referencia1;
		this.referencia2 = referencia2;
		this.idCanal = idCanal;
		this.canal = canal;
		this.idSucursal = idSucursal;
		this.sucursal = sucursal;
		this.idUsuarioCreacion = idUsuarioCreacion;
		this.usuarioCreacion = usuarioCreacion;
		this.idMoneda = idMoneda;
		this.moneda = moneda;
		this.idFormaPago = idFormaPago;
		this.idOperacion = idOperacion;
		this.idFuncion = idFuncion;
		this.poliza = poliza;
		this.lote = lote;
		this.contabilizado = contabilizado;
		this.codigoError = codigoError;
		this.descripcionError = descripcionError;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
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

	public Long getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(Long codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getValorLlave1() {
		return valorLlave1;
	}

	public void setValorLlave1(String valorLlave1) {
		this.valorLlave1 = valorLlave1;
	}

	public String getValorLlave2() {
		return valorLlave2;
	}

	public void setValorLlave2(String valorLlave2) {
		this.valorLlave2 = valorLlave2;
	}

	public String getValorLlave3() {
		return valorLlave3;
	}

	public void setValorLlave3(String valorLlave3) {
		this.valorLlave3 = valorLlave3;
	}
	
	public String getValorLlave4() {
		return valorLlave4;
	}

	public void setValorLlave4(String valorLlave4) {
		this.valorLlave4 = valorLlave4;
	}

	public String getValorLlave5() {
		return valorLlave5;
	}

	public void setValorLlave5(String valorLlave5) {
		this.valorLlave5 = valorLlave5;
	}

	public String getValorLlave6() {
		return valorLlave6;
	}

	public void setValorLlave6(String valorLlave6) {
		this.valorLlave6 = valorLlave6;
	}

	public String getValorLlave7() {
		return valorLlave7;
	}

	public void setValorLlave7(String valorLlave7) {
		this.valorLlave7 = valorLlave7;
	}

	public String getValorLlave8() {
		return valorLlave8;
	}

	public void setValorLlave8(String valorLlave8) {
		this.valorLlave8 = valorLlave8;
	}

	public String getValorLlave9() {
		return valorLlave9;
	}

	public void setValorLlave9(String valorLlave9) {
		this.valorLlave9 = valorLlave9;
	}

	public String getValorLlave10() {
		return valorLlave10;
	}

	public void setValorLlave10(String valorLlave10) {
		this.valorLlave10 = valorLlave10;
	}

	public String getValorLlave11() {
		return valorLlave11;
	}

	public void setValorLlave11(String valorLlave11) {
		this.valorLlave11 = valorLlave11;
	}

	public String getValorLlave12() {
		return valorLlave12;
	}

	public void setValorLlave12(String valorLlave12) {
		this.valorLlave12 = valorLlave12;
	}

	public String getValorLlave13() {
		return valorLlave13;
	}

	public void setValorLlave13(String valorLlave13) {
		this.valorLlave13 = valorLlave13;
	}

	public String getValorLlave14() {
		return valorLlave14;
	}

	public void setValorLlave14(String valorLlave14) {
		this.valorLlave14 = valorLlave14;
	}

	public String getValorLlave15() {
		return valorLlave15;
	}

	public void setValorLlave15(String valorLlave15) {
		this.valorLlave15 = valorLlave15;
	}

	public String getValorLlave16() {
		return valorLlave16;
	}

	public void setValorLlave16(String valorLlave16) {
		this.valorLlave16 = valorLlave16;
	}

	public String getValorLlave17() {
		return valorLlave17;
	}

	public void setValorLlave17(String valorLlave17) {
		this.valorLlave17 = valorLlave17;
	}

	public String getValorLlave18() {
		return valorLlave18;
	}

	public void setValorLlave18(String valorLlave18) {
		this.valorLlave18 = valorLlave18;
	}

	public String getValorLlave19() {
		return valorLlave19;
	}

	public void setValorLlave19(String valorLlave19) {
		this.valorLlave19 = valorLlave19;
	}

	public String getValorLlave20() {
		return valorLlave20;
	}

	public void setValorLlave20(String valorLlave20) {
		this.valorLlave20 = valorLlave20;
	}

	public String getValorLlave21() {
		return valorLlave21;
	}

	public void setValorLlave21(String valorLlave21) {
		this.valorLlave21 = valorLlave21;
	}

	public String getValorLlave22() {
		return valorLlave22;
	}

	public void setValorLlave22(String valorLlave22) {
		this.valorLlave22 = valorLlave22;
	}

	public String getValorLlave23() {
		return valorLlave23;
	}

	public void setValorLlave23(String valorLlave23) {
		this.valorLlave23 = valorLlave23;
	}

	public String getValorLlave24() {
		return valorLlave24;
	}

	public void setValorLlave24(String valorLlave24) {
		this.valorLlave24 = valorLlave24;
	}

	public String getValorLlave25() {
		return valorLlave25;
	}

	public void setValorLlave25(String valorLlave25) {
		this.valorLlave25 = valorLlave25;
	}

	public String getValorLlave26() {
		return valorLlave26;
	}

	public void setValorLlave26(String valorLlave26) {
		this.valorLlave26 = valorLlave26;
	}

	public String getValorLlave27() {
		return valorLlave27;
	}

	public void setValorLlave27(String valorLlave27) {
		this.valorLlave27 = valorLlave27;
	}

	public String getValorLlave28() {
		return valorLlave28;
	}

	public void setValorLlave28(String valorLlave28) {
		this.valorLlave28 = valorLlave28;
	}

	public String getValorLlave29() {
		return valorLlave29;
	}

	public void setValorLlave29(String valorLlave29) {
		this.valorLlave29 = valorLlave29;
	}

	public String getValorLlave30() {
		return valorLlave30;
	}

	public void setValorLlave30(String valorLlave30) {
		this.valorLlave30 = valorLlave30;
	}

	public String getReferencia1() {
		return referencia1;
	}

	public void setReferencia1(String referencia1) {
		this.referencia1 = referencia1;
	}

	public String getReferencia2() {
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public Long getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(Long idCanal) {
		this.idCanal = idCanal;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public Long getIdUsuarioCreacion() {
		return idUsuarioCreacion;
	}

	public void setIdUsuarioCreacion(Long idUsuarioCreacion) {
		this.idUsuarioCreacion = idUsuarioCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public Long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(Long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Long getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(Long codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public int getContabilizado() {
		return contabilizado;
	}

	public void setContabilizado(int contabilizado) {
		this.contabilizado = contabilizado;
	}

	public Long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(Long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public Long getPoliza() {
		return poliza;
	}

	public void setPoliza(Long poliza) {
		this.poliza = poliza;
	}

	public Long getLote() {
		return lote;
	}

	public void setLote(Long lote) {
		this.lote = lote;
	}

	public Long getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(Long idOperacion) {
		this.idOperacion = idOperacion;
	}

	public Long getIdFuncion() {
		return idFuncion;
	}

	public void setIdFuncion(Long idFuncion) {
		this.idFuncion = idFuncion;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2016-11-09 | Javier Tirado 	| Modifico el tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor, ModificadoPor, etc).
 */