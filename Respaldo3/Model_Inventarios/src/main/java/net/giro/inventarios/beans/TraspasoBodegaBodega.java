package net.giro.inventarios.beans;

import java.io.Serializable;

/**
 * traspaso_bodega_bodega
 * @author javaz
 *
 */
public class TraspasoBodegaBodega implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long idTraspaso;
	private long idObraOrigen;
	private long idObraDestino;
	private int estatus; // ESTATUS: 0 - Sin generar, 1 - generado
	
	public TraspasoBodegaBodega() {}

	public TraspasoBodegaBodega(long idTraspaso, long idObraOrigen, long idObraDestino) {
		this();
		this.idTraspaso = idTraspaso;
		this.idObraOrigen = idObraOrigen;
		this.idObraDestino = idObraDestino;
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

	public long getIdTraspaso() {
		return idTraspaso;
	}

	public void setIdTraspaso(long idTraspaso) {
		this.idTraspaso = idTraspaso;
	}

	public long getIdObraOrigen() {
		return idObraOrigen;
	}

	public void setIdObraOrigen(long idObraOrigen) {
		this.idObraOrigen = idObraOrigen;
	}

	public long getIdObraDestino() {
		return idObraDestino;
	}

	public void setIdObraDestino(long idObraDestino) {
		this.idObraDestino = idObraDestino;
	}

	/**
	 * ESTATUS: 0 - Sin generar, 1 - generado
	 * @return
	 */
	public int getEstatus() {
		return estatus;
	}

	/**
	 * ESTATUS: 0 - Sin generar, 1 - generado
	 * @param estatus
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
}
