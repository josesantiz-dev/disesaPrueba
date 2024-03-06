package net.giro.co.beans;

import java.util.*;

/**
 * bbb4420d88
 * @author javaz
 *
 */
public class Correlativo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private CorrelativoId id;
	private long cbEmpresa;
	private Long numeroCorrelativo;
	private String descriTabla;
	private Date fechaInsercion;
	private String usuarioInsercion;
	private String terminalInsercion;
	private String ipInsercion;
	private Date fechaModificacion;
	private String usuarioModificacion;
	private String terminalModificacion;
	private String ipModificacion;

	// Constructors

	/** default constructor */
	public Correlativo() {
	}

	/** minimal constructor */
	public Correlativo(CorrelativoId id, long cbEmpresa, Long numeroCorrelativo) {
		this.id = id;
		this.cbEmpresa = cbEmpresa;
		this.numeroCorrelativo = numeroCorrelativo;
	}

	/** full constructor */
	public Correlativo(CorrelativoId id, long cbEmpresa, Long numeroCorrelativo, String descriTabla,
			Date fechaInsercion, String usuarioInsercion, String terminalInsercion, String ipInsercion,
			Date fechaModificacion, String usuarioModificacion, String terminalModificacion, String ipModificacion) {
		this.id = id;
		this.cbEmpresa = cbEmpresa;
		this.numeroCorrelativo = numeroCorrelativo;
		this.descriTabla = descriTabla;
		this.fechaInsercion = fechaInsercion;
		this.usuarioInsercion = usuarioInsercion;
		this.terminalInsercion = terminalInsercion;
		this.ipInsercion = ipInsercion;
		this.fechaModificacion = fechaModificacion;
		this.usuarioModificacion = usuarioModificacion;
		this.terminalModificacion = terminalModificacion;
		this.ipModificacion = ipModificacion;
	}

	// Property accessors
	//@EmbeddedId
	//@AttributeOverrides( {
	//		@AttributeOverride(name = "codigoEmpresa", column = @Column(name = "C_CODEMP", unique = false, nullable = false, insertable = true, updatable = true, length = 3)),
	//		@AttributeOverride(name = "nombreTabla", column = @Column(name = "C_NOMTAB", unique = false, nullable = false, insertable = true, updatable = true, length = 100)) })
	public CorrelativoId getId() {
		return this.id;
	}

	public void setId(CorrelativoId id) {
		this.id = id;
	}

	//@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	//@JoinColumn(name = "C_CODEMP", unique = false, nullable = false, insertable = false, updatable = false)
	public long getCbEmpresa() {
		return this.cbEmpresa;
	}

	public void setCbEmpresa(long cbEmpresa) {
		this.cbEmpresa = cbEmpresa;
	}

	//@Column(name = "N_NUMCOR", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getNumeroCorrelativo() {
		return this.numeroCorrelativo;
	}

	public void setNumeroCorrelativo(Long numeroCorrelativo) {
		this.numeroCorrelativo = numeroCorrelativo;
	}

	//@Column(name = "C_DESTAB", unique = false, nullable = true, insertable = true, updatable = true, length = 254)
	public String getDescriTabla() {
		return this.descriTabla;
	}

	public void setDescriTabla(String descriTabla) {
		this.descriTabla = descriTabla;
	}

	//@Temporal(TemporalType.DATE)
	//@Column(name = "A_FECINS", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getFechaInsercion() {
		return this.fechaInsercion;
	}

	public void setFechaInsercion(Date fechaInsercion) {
		this.fechaInsercion = fechaInsercion;
	}

	//@Column(name = "A_USUINS", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getUsuarioInsercion() {
		return this.usuarioInsercion;
	}

	public void setUsuarioInsercion(String usuarioInsercion) {
		this.usuarioInsercion = usuarioInsercion;
	}

	//@Column(name = "A_TERINS", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getTerminalInsercion() {
		return this.terminalInsercion;
	}

	public void setTerminalInsercion(String terminalInsercion) {
		this.terminalInsercion = terminalInsercion;
	}

	//@Column(name = "A_IPEINS", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getIpInsercion() {
		return this.ipInsercion;
	}

	public void setIpInsercion(String ipInsercion) {
		this.ipInsercion = ipInsercion;
	}

	//@Temporal(TemporalType.DATE)
	//@Column(name = "A_FECMOD", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	//@Column(name = "A_USUMOD", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	//@Column(name = "A_TERMOD", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getTerminalModificacion() {
		return this.terminalModificacion;
	}

	public void setTerminalModificacion(String terminalModificacion) {
		this.terminalModificacion = terminalModificacion;
	}

	//@Column(name = "A_IPEMOD", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}
}