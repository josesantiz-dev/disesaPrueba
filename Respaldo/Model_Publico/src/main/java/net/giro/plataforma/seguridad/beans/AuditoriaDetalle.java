package net.giro.plataforma.seguridad.beans;

public class AuditoriaDetalle implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	net.giro.plataforma.seguridad.beans.Auditoria auditoria;
	java.lang.String propiedad;
	java.lang.String valorAnterior;
	java.lang.String valorNuevo;
	long version;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public net.giro.plataforma.seguridad.beans.Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(net.giro.plataforma.seguridad.beans.Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	public java.lang.String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(java.lang.String propiedad) {
		this.propiedad = propiedad;
	}

	public java.lang.String getValorAnterior() {
		return valorAnterior;
	}

	public void setValorAnterior(java.lang.String valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public java.lang.String getValorNuevo() {
		return valorNuevo;
	}

	public void setValorNuevo(java.lang.String valorNuevo) {
		this.valorNuevo = valorNuevo;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
