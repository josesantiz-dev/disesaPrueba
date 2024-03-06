package net.giro;

import java.io.Serializable;

public class Modulo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String img ;
	private String modulo;
	private String url;
	
	public Modulo(String modulo, String img, String url) {
		this.modulo = modulo;
		this.img = img;
		this.url = url;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getModulo() {
		return modulo;
	}
	
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
