package net.giro.plataforma.seguridad.beans;

/**
 * bc4cf7985d
 * @author javaz
 *
 */
public class Auditoria implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	long id;
	java.util.Date fechaCreacion;
	net.giro.plataforma.seguridad.beans.Usuario creadoPor;
	net.giro.plataforma.seguridad.beans.Acceso acceso;
	java.lang.String bean;
	java.lang.String idBean;
	long version;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.util.Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(java.util.Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public net.giro.plataforma.seguridad.beans.Usuario getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(net.giro.plataforma.seguridad.beans.Usuario creadoPor) {
		this.creadoPor = creadoPor;
	}

	public net.giro.plataforma.seguridad.beans.Acceso getAcceso() {
		return acceso;
	}

	public void setAcceso(net.giro.plataforma.seguridad.beans.Acceso acceso) {
		this.acceso = acceso;
	}

	public java.lang.String getBean() {
		return bean;
	}

	public void setBean(java.lang.String bean) {
		this.bean = bean;
	}

	public java.lang.String getIdBean() {
		return idBean;
	}

	public void setIdBean(java.lang.String idBean) {
		this.idBean = idBean;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
