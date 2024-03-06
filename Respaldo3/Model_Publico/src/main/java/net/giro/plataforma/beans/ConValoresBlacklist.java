package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * con_valores_blacklist
 * @author javaz
 */
public class ConValoresBlacklist implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private ConValores idConValores;
	private ConGrupoValores idGrupo;
	private long idEmpresa;
	private long creadoPor;
	private Date fechaCreacion;
	
	public ConValoresBlacklist() {
		this.fechaCreacion = Calendar.getInstance().getTime();
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
	
	public ConValores getIdConValores() {
		return idConValores;
	}
	
	public void setIdConValores(ConValores idConValores) {
		this.idConValores = idConValores;
	}
	
	public ConGrupoValores getIdGrupo() {
		return idGrupo;
	}
	
	public void setIdGrupo(ConGrupoValores idGrupo) {
		this.idGrupo = idGrupo;
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2019-08-14 | Javier Tirado 	| Creacion de Entity.
 */
