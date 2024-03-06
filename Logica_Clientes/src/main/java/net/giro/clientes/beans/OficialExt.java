package net.giro.clientes.beans;

import java.util.Date;

import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.rh.admon.beans.Empleado;


public class OficialExt implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
		
		long id;
		long creadoPor;
		long modificadoPor;
		java.util.Date fechaCreacion;
		java.util.Date fechaModificacion;
		Usuario usuarioId;
		Empleado empleado;
		
		
		public OficialExt(){
			
		}


		public OficialExt(long id, long creadoPor, long modificadoPor,
				Date fechaCreacion, Date fechaModificacion, Usuario usuarioId,
				Empleado empleado) {
			this.id = id;
			this.creadoPor = creadoPor;
			this.modificadoPor = modificadoPor;
			this.fechaCreacion = fechaCreacion;
			this.fechaModificacion = fechaModificacion;
			this.usuarioId = usuarioId;
			this.empleado = empleado;
		}


		public long getId() {
			return id;
		}


		public void setId(long id) {
			this.id = id;
		}


		public long getCreadoPor() {
			return creadoPor;
		}


		public void setCreadoPor(long creadoPor) {
			this.creadoPor = creadoPor;
		}


		public long getModificadoPor() {
			return modificadoPor;
		}


		public void setModificadoPor(long modificadoPor) {
			this.modificadoPor = modificadoPor;
		}


		public java.util.Date getFechaCreacion() {
			return fechaCreacion;
		}


		public void setFechaCreacion(java.util.Date fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}


		public java.util.Date getFechaModificacion() {
			return fechaModificacion;
		}


		public void setFechaModificacion(java.util.Date fechaModificacion) {
			this.fechaModificacion = fechaModificacion;
		}


		public Usuario getUsuarioId() {
			return usuarioId;
		}


		public void setUsuarioId(Usuario usuarioId) {
			this.usuarioId = usuarioId;
		}


		public Empleado getEmpleado() {
			return empleado;
		}


		public void setEmpleado(Empleado empleado) {
			this.empleado = empleado;
		}
		
		
	}
