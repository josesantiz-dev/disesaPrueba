package net.giro.cargas.documentos.respuesta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RespuestaError implements Serializable{
	
	public static final String CARGAS_DOCUMENTOS = "CARGAS_DOCUMENTOS";
	
	private static final long serialVersionUID = 1L;
	
	private long codigoError;
	private String descError;
	private List<RespuestaError>	errores;
	
	public RespuestaError(){
		codigoError = 0L;
		errores = new ArrayList<RespuestaError>();
	}
	
	public RespuestaError(Long id,String descError){
		this.codigoError = id;
		this.descError =descError;
	}

	public void addCodigo(String modulo, long id){
		if(errores == null){
			errores = new ArrayList<RespuestaError>();
		}
		codigoError = 1l;
		
		if(RespuestaError.CARGAS_DOCUMENTOS.equals(modulo)){
			descError = Errores.descError.get(id);
		}
		
		errores.add(new RespuestaError(id,descError));
	}
	
	public void addCodigo(String modulo, long id, Object ... args){
		if(errores == null){
			errores = new ArrayList<RespuestaError>();
		}
		codigoError = 1l;
				
		if(RespuestaError.CARGAS_DOCUMENTOS.equals(modulo)){
			descError = String.format(Errores.descError.get(id), args);
		}
		errores.add(new RespuestaError(id,descError));
	}

	public long getCodigoError() {
		return codigoError;
	}
	
	public Long obtenerUltimoError(){
    	if(errores == null || errores.isEmpty())
    		return null;
    	
    	return errores.get(errores.size() -1).codigoError;
    }

	public List<RespuestaError> getErrores() {
		return errores;
	}

	public void setErrores(List<RespuestaError> errores) {
		this.errores = errores;
	}

	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}

	public void setCodigoError(long codigoError) {
		this.codigoError = codigoError;
	}
	
}
