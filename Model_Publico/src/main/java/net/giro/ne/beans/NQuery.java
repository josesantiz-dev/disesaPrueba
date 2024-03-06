package net.giro.ne.beans;

public class NQuery implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Double valor;
	
	public NQuery(){};
	
	public NQuery(Integer id, Double valor){
		super();
		this.id = id;
		this.valor = valor;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
