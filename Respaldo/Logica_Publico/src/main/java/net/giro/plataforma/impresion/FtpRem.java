package net.giro.plataforma.impresion;

import javax.ejb.Remote;

@Remote
public interface FtpRem {
	public void setInfo(String host, String user, String password, String port);

	public byte[] getArchivo(String archivo);

	public boolean putArchivo(byte[] archivo, String nombre);
}
