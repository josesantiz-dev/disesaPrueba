package net.giro.clientes.beans;

import java.io.Serializable;

/**
 * a4f45868e6
 * @author javaz
 *
 */
public class EstructuraImportacionCuenta implements Serializable { 

	private static final long serialVersionUID = 1L;

	/*IDENTIFICADOR DE LA TABLA*/
	private long id;

	/*ID DE LA CUENTA BANCARIA A LA QUE PERTENECE LA ESTRUCTURA DE IMPORTACION*/
	private java.lang.Long idCuenta;

	/*CADE DE LETRAS O NUMEROS PARA EL IDENTIFICADOR DE LINEA*/
	private java.lang.String valorIdentificador;

	/*POSICION DEL ARCHIVO DONDE INICIA EL IDENTIFICADOR*/
	private java.lang.Long posicionIdentificador;

	/*LONGITUD DE CARACTERES DEL IDENTIFICADOR*/
	private java.lang.Long longitudIdentificador;

	/*POSICION INICIAL DE LA REFERENCIA EN EL ARCHIVO DE BANCO*/
	private java.lang.Long posicionReferencia;

	/*LONGITUD DE CARACTERES DE LA REFERENCIA*/
	private java.lang.Long longitudReferencia;

	/*POSICION INICIAL DEL MONTO EN EL ARCHIVO DE BANCO*/
	private java.lang.Long posicionMonto;

	/*LONGITUD DE CARACTERES PARA EL MONTO*/
	private java.lang.Long longitudMonto;

	/*FORMATO DE FECHA QUE MANEJA EL ARCHIVO DE BANCO*/
	private java.lang.String formatoFecha;

	/*POSICION INICIAL DE LA FECHA EN EL ARCHIVO DE BANCO*/
	private java.lang.Long posicionFecha;

	/*LONGITUD DE CARACTERES DE LA FECHA*/
	private java.lang.Long longitudFecha;

	/*VALOR DEL SEPARADOR EN EL ARCHIVO BANCARIO*/
	private java.lang.String separador;

	/*ID DEL USUARIO QUE CREA EL REGISTRO*/
	private java.lang.Long creadoPor;

	/*FECHA DE CREACION DEL REGISTRO*/
	private java.util.Date fechaCreacion;

	/*ID DEL USUARIO QUE MODIFICA EL REGISTRO*/
	private java.lang.Long modificadoPor;

	/*FECHA DE MODIFICACION DEL REGISTRO*/
	private java.util.Date fechaModificacion;
	
	private Long posicionCabecera;
	
	private Long posicionResumen;

	public EstructuraImportacionCuenta() {}

 	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public java.lang.Long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(java.lang.Long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public java.lang.String getValorIdentificador() {
		return valorIdentificador;
	}
	public void setValorIdentificador(java.lang.String valorIdentificador) {
		this.valorIdentificador = valorIdentificador;
	}
	public java.lang.Long getPosicionIdentificador() {
		return posicionIdentificador;
	}
	public void setPosicionIdentificador(java.lang.Long posicionIdentificador) {
		this.posicionIdentificador = posicionIdentificador;
	}
	public java.lang.Long getLongitudIdentificador() {
		return longitudIdentificador;
	}
	public void setLongitudIdentificador(java.lang.Long longitudIdentificador) {
		this.longitudIdentificador = longitudIdentificador;
	}
	public java.lang.Long getPosicionReferencia() {
		return posicionReferencia;
	}
	public void setPosicionReferencia(java.lang.Long posicionReferencia) {
		this.posicionReferencia = posicionReferencia;
	}
	public java.lang.Long getLongitudReferencia() {
		return longitudReferencia;
	}
	public void setLongitudReferencia(java.lang.Long longitudReferencia) {
		this.longitudReferencia = longitudReferencia;
	}
	public java.lang.Long getPosicionMonto() {
		return posicionMonto;
	}
	public void setPosicionMonto(java.lang.Long posicionMonto) {
		this.posicionMonto = posicionMonto;
	}
	public java.lang.Long getLongitudMonto() {
		return longitudMonto;
	}
	public void setLongitudMonto(java.lang.Long longitudMonto) {
		this.longitudMonto = longitudMonto;
	}
	public java.lang.String getFormatoFecha() {
		return formatoFecha;
	}
	public void setFormatoFecha(java.lang.String formatoFecha) {
		this.formatoFecha = formatoFecha;
	}
	public java.lang.Long getPosicionFecha() {
		return posicionFecha;
	}
	public void setPosicionFecha(java.lang.Long posicionFecha) {
		this.posicionFecha = posicionFecha;
	}
	public java.lang.Long getLongitudFecha() {
		return longitudFecha;
	}
	public void setLongitudFecha(java.lang.Long longitudFecha) {
		this.longitudFecha = longitudFecha;
	}
	public java.lang.String getSeparador() {
		return separador;
	}
	public void setSeparador(java.lang.String separador) {
		this.separador = separador;
	}
	public java.lang.Long getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(java.lang.Long creadoPor) {
		this.creadoPor = creadoPor;
	}
	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public java.lang.Long getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(java.lang.Long modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public java.util.Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(java.util.Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Long getPosicionCabecera() {
		return posicionCabecera;
	}

	public void setPosicionCabecera(Long posicionCabecera) {
		this.posicionCabecera = posicionCabecera;
	}

	public Long getPosicionResumen() {
		return posicionResumen;
	}

	public void setPosicionResumen(Long posicionResumen) {
		this.posicionResumen = posicionResumen;
	}
}
