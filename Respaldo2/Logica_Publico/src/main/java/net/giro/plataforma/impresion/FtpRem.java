package net.giro.plataforma.impresion;

import javax.ejb.Remote;

@Remote
public interface FtpRem {
	public void setInfo(String host, String user, String password, String port);

	/**
	 * Metodo para recuperar archivo del servidor
	 * @param fileName Nombre del archivo a obtener. Debe contener ruta completa y extension.
	 * @return Contenido del archivo en bytes
	 */
	public byte[] getArchivo(String fileName);

	/**
	 * Metodo para almacenar un archivo en el servidor
	 * @param fileSource Contenido del archivo a almacenar
	 * @param fileName Nombre del archivo. Debe contener ruta completa y extension.
	 * @return True si pudo almacenar el archivo, de lo contrario, False.
	 */
	public boolean putArchivo(byte[] fileSource, String fileName);
	
	public boolean delArchivo(String fileName);

	/**
	 * Metodo para recuperar archivo del servidor por medio de FTP
	 * @param fileName Nombre del archivo a obtener. Debe contener ruta completa y extension.
	 * @return Contenido del archivo en bytes
	 */
	public byte[] getFtpArchivo(String fileName);

	/**
	 * Metodo para almacenar un archivo en el servidor por FTP
	 * @param fileSource Contenido del archivo a almacenar
	 * @param fileName Nombre del archivo. Debe contener ruta completa y extension.
	 * @return True si pudo almacenar el archivo, de lo contrario, False.
	 */
	public boolean putFtpArchivo(byte[] fileSource, String fileName);
	
	public boolean delFtpArchivo(String fileName);
}
