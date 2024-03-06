package net.giro.comun;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;

public class ExcepConstraint extends Exception {
	private static final long serialVersionUID = 5010487257755308944L;
	private Object pojo;
	
	public ExcepConstraint(String llave){
		super(Errores.get(llave));
	}
	
	public ExcepConstraint(String llave, Object pojo){
		super(Errores.get(llave));
		this.pojo = pojo;
	}
	 
	public ExcepConstraint(String llave, Object pojo, Object ... args){
		super(Errores.get(llave, args));
		this.pojo = pojo;
	}
	 
	public Object getPojo() {
		return pojo;
	}
	
	public String getPojoAsString() {
		return ReflectionToStringBuilder.toString(pojo, StandardToStringStyle.MULTI_LINE_STYLE);
	}
}
