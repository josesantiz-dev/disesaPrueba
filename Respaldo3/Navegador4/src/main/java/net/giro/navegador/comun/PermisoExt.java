package net.giro.navegador.comun;

import java.io.Serializable;

public class PermisoExt extends Permiso implements Serializable {
	private static final long serialVersionUID = 1L;
	private Permiso base;
	private Long idReferencia;
	
	public PermisoExt(Permiso base, long idReferencia) {
		this.base = base;
		this.idReferencia = idReferencia;
	}
	
	public Permiso getBase() {
		return this.base;
	}

	public long getIdReferencia() {
		return this.idReferencia;
	}

	@Override
	public boolean getConsultar() {
		return this.base.getConsultar();
	}

	public boolean escribir(long idReferencia) {
		if (this.idReferencia == -1)
			return this.base.getEditar();
		return (this.idReferencia > 0L && this.idReferencia == idReferencia) ? this.base.getEditar() : false;
	}

	public boolean borrar(long idReferencia) {
		if (this.idReferencia == -1)
			return this.base.getEditar();
		return (this.idReferencia > 0L && this.idReferencia == idReferencia) ? this.base.getBorrar() : false;
	}
	
	@Override
	public int getValue() {
		return  this.base.getValue();
	}

	@Override
	public String toString() {
		return (this.idReferencia > 0L ? this.idReferencia.toString() + ":" : "") + this.base.toString();
	}
}
