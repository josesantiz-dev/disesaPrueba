package net.giro.adp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * obra_satics
 * 
 * @author javaz
 *
 */
public class TipoSub implements Serializable {
	private static final long serialVersionUID = 1L;

	private String clave;
	private String descripcion;
	private int estatus;

	public TipoSub() {
		this.clave = "";
		this.descripcion = "";
	}

	public TipoSub(String clave) {
		this();
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
}

/*
 * -----------------------------------------------------------------------------
 * ----------------------------------- HISTORIAL DE MODIFICACIONES
 * -----------------------------------------------------------------------------
 * ----------------------------------- VER | FECHA | AUTOR | DESCRIPCION
 * -----------------------------------------------------------------------------
 * ----------------------------------- 2.2 | 23/05/2016 | Javier Tirado |
 * Creando la clase ObraSatics 1.2 | 2016-11-09 | Javier Tirado | Modifico el
 * tipo de dato a LONG en propiedades que referencien ID de Usuario (creadoPor,
 * ModificadoPor, etc).
 */