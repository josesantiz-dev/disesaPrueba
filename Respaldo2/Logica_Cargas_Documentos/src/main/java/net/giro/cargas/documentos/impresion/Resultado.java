package net.giro.cargas.documentos.impresion;

import java.io.Serializable;

public class Resultado  implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long ID;
	private Long codigo;
	private String error_descr;
	
	public Resultado() 
	{
		ID= 0L;
		codigo= 0L;
		error_descr= "";
	}
	
	public Long getID() {
		return ID;
	}
	
	public void setID(Long iD) {
		ID = iD;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public String getError_descr() {
		return error_descr;
	}
	
	public void setError_descr(String error_descr) {
		this.error_descr = error_descr;
	}
	
}

/** !Resultado.java */


