package net.giro.plataforma.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * topic_estatus
 * @author javaz
 *
 */
public class TopicEstatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private String evento;
	private String target;
	private String referencia;
	private String referenciaExtra;
	private String atributos;
	private String comando;
	private int manual;
	private Date fechaManual;
	private long propietario;
	private long idEmpresa;
	private int estatus;
	private String descripcion;
	private Date fechaCreado;
	private Date fechaEjecutado;
	
	public TopicEstatus() {
		this.nombre = "";
		this.evento = "";
		this.target = "";
		this.referencia = "";
		this.atributos = "";
		this.comando = "";
		this.descripcion = "";
		this.fechaCreado = Calendar.getInstance().getTime();
		this.estatus = 0;
	}
	
	public TopicEstatus(String nombre, String evento, String comando) {
		this();
		this.nombre = nombre;
		this.evento = evento;
		this.comando = comando;
		this.descripcion = "Sin Ejecucion";
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		nombre = (nombre != null && ! "".equals(nombre.trim()) ? nombre.trim() : "");
		this.nombre = nombre;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		evento = (evento != null && ! "".equals(evento.trim()) ? evento.trim() : "");
		this.evento = evento;
	}

	public String getTarget() {
		/*if (this.target == null || "".equals(this.target.trim()))
			this.target = extraerTarget();*/
		target = (target == null ? "" : target);
		return target;
	}

	public void setTarget(String target) {
		target = (target == null || "".equals(target.trim()) ? "" : target);
		this.target = target;
	}

	public String getReferencia() {
		/*if (this.referencia == null || "".equals(this.referencia.trim()))
			this.referencia = extraerReferencia();*/
		referencia = (referencia == null ? "" : referencia);
		return referencia;
	}

	public void setReferencia(String referencia) {
		referencia = (referencia == null || "".equals(referencia.trim()) ? "" : referencia);
		this.referencia = referencia;
	}

	public String getReferenciaExtra() {
		referenciaExtra = (referenciaExtra == null ? "" : referenciaExtra);
		return referenciaExtra;
	}

	public void setReferenciaExtra(String referenciaExtra) {
		referenciaExtra = (referenciaExtra == null || "".equals(referenciaExtra.trim()) ? "" : referenciaExtra);
		this.referenciaExtra = referenciaExtra;
	}

	public String getAtributos() {
		/*if (this.atributos == null || "".equals(this.atributos.trim()))
			this.atributos = extraerAtributos();*/
		atributos = (atributos == null ? "" : atributos);
		return atributos;
	}

	public void setAtributos(String atributos) {
		atributos = (atributos == null || "".equals(atributos.trim()) ? "" : atributos);
		this.atributos = atributos;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public int getManual() {
		return manual;
	}

	public void setManual(int manual) {
		this.manual = manual;
	}

	public Date getFechaManual() {
		return fechaManual;
	}

	public void setFechaManual(Date fechaManual) {
		this.fechaManual = fechaManual;
	}

	public long getPropietario() {
		return propietario;
	}

	public void setPropietario(long propietario) {
		this.propietario = propietario;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCreado() {
		return fechaCreado;
	}

	public void setFechaCreado(Date fechaCreado) {
		this.fechaCreado = fechaCreado;
	}

	public Date getFechaEjecutado() {
		return fechaEjecutado;
	}

	public void setFechaEjecutado(Date fechaEjecutado) {
		this.fechaEjecutado = fechaEjecutado;
	}

	// ----------------------------------------------------------------------------------------------
	// EXTENDIDAS
	// ----------------------------------------------------------------------------------------------
	
	public String getAtributosAux() {
		if (this.atributos.length() > 36)
			return this.atributos.substring(0, 36) + "...";
		return this.atributos;
	}

	public void populateData() {
		if (this.comando == null || "".equals(this.comando.trim()))
			return;
		this.target = extraerTarget();
		this.referencia = extraerReferencia();
		this.referenciaExtra = extraerReferenciaExtra();
		this.atributos = extraerAtributos();
	}
	
	private String extraerTarget() {
		String target = "";
		
		if (this.comando == null || "".equals(this.comando.trim()))
			return target;
		target = this.comando.replace("\"", "");
		if (! target.contains("target:"))
			return target;
		
		target = target.substring(target.indexOf("target:"));
		target = target.substring(0, target.indexOf(",")).replace("target:", "");
		target = (target != null && ! "".equals(target.trim()) ? target.trim() : "");

		return target;
	}

	private String extraerReferencia() {
		String referencia = "";
		
		if (this.comando == null || "".equals(this.comando.trim()))
			return referencia;
		referencia = this.comando.replace("\"", "");
		if (! referencia.contains("referencia:"))
			return referencia;
		
		referencia = referencia.substring(referencia.indexOf("referencia:"));
		referencia = referencia.substring(0, referencia.indexOf(",")).replace("referencia:", "");
		referencia = (referencia != null && ! "".equals(referencia.trim()) ? referencia.trim() : "");
		
		return referencia;
	}

	private String extraerReferenciaExtra() {
		String referenciaExtra = "";
		
		if (this.comando == null || "".equals(this.comando.trim()))
			return referencia;
		referenciaExtra = this.comando.replace("\"", "");
		if (! referenciaExtra.contains("referenciaExtra:"))
			return referenciaExtra;
		
		referenciaExtra = referenciaExtra.substring(referenciaExtra.indexOf("referenciaExtra:"));
		referenciaExtra = referenciaExtra.substring(0, referenciaExtra.indexOf(",")).replace("referenciaExtra:", "");
		referenciaExtra = (referenciaExtra != null && ! "".equals(referenciaExtra.trim()) ? referenciaExtra.trim() : "");
		
		return referenciaExtra;
	}

	private String extraerAtributos() {
		String atributos = "";
		
		if (this.comando == null || "".equals(this.comando.trim()))
			return atributos;
		atributos = this.comando.replace("\"", "");
		if (! atributos.contains("atributos:"))
			return atributos;
		
		atributos = atributos.substring(atributos.indexOf("atributos:")).replace("atributos:", "");
		if (atributos.startsWith("[{"))
			atributos = atributos.substring(0, atributos.indexOf("}]") + 1);
		else if (atributos.startsWith("{["))
			atributos = atributos.substring(0, atributos.indexOf("]}") + 1);
		else if (atributos.startsWith("["))
			atributos = atributos.substring(0, atributos.indexOf("]") + 1);
		else if (atributos.startsWith("{"))
			atributos = atributos.substring(0, atributos.indexOf("}") + 1);
		else
			atributos = atributos.substring(0, atributos.indexOf(","));
		atributos = (atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "");
		
		return atributos;
	}
}
