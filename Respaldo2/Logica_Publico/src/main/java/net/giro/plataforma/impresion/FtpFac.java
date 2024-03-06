package net.giro.plataforma.impresion;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;

@Stateless
public class FtpFac implements FtpRem {
	private static Logger log = Logger.getLogger(FtpFac.class);
	private String host;
	private String user;
	private String password;
	private String port;

	
	public FtpFac() {
		this.host = "127.0.0.1";
		this.user = "guess";
		this.password = "guess";
		this.port = "22";
	}

	
	@Override
	public void setInfo(String host, String user, String password, String port){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	@Override
	public byte[] getArchivo(String fileName) {
		byte[] data = null;
		File file = null;
		Path path = null;
		
		try {
			// Validamos archivo
			file = new File(fileName);
			if (file.isDirectory()) {
				log.info("404 - La ruta no contiene el archivo para descarga.");
				return null;
			}
				
			if (! file.exists()) {
				log.info("404 - El archivo indicado no existe");
				return null;
			}
			
			if (! file.isFile()) {
				log.info("404 - El archivo indicado no es valido");
				return null;
			}
			
			// Recuperamos contenido
			path = Paths.get(fileName);
			data = Files.readAllBytes(path);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener el archivo indicado", e);
		}
		
		return data;
	}
	
	@Override
	public boolean putArchivo(byte[] fileSource, String fileName) {
		FileOutputStream stream = null;
		File file = null;
		
		try {
			file = new File(fileName);
			stream = new FileOutputStream(file);
			stream.write(fileSource);
			stream.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar almacenar el archivo indicado", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delArchivo(String fileName) {
		File file = null;
		
		try {
			file = new File(fileName);
			return (file.exists()) ? file.delete() : true;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar borrar el archivo indicado", e);
			throw e;
		}
	}

	@Override
	public byte[] getFtpArchivo(String fileName) {
		FTPMessageCollector listener = new FTPMessageCollector();
		byte[] resultFile = null;
		FTPClient ftp = null;
		
		try {
			ftp = new FTPClient();
			ftp.setRemoteHost(host);
			ftp.setRemotePort(Integer.valueOf(port));
			ftp.setMessageListener(listener);
			
			// Conectando
			log.info("Conectando ... ");
			ftp.connect();

			// Autenticando
			log.info("Autenticando ... ");
			ftp.login(user, password);
	
			// set up passive ASCII transfers
			log.info("Estableciendo modo PASIVO ... ");
			ftp.setConnectMode(FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.BINARY);

			// recuperando
			log.info("Obteniendo ... ");
			resultFile = ftp.get(fileName);
	
			// Shut down client
			log.info("Finalizando ... ");
			ftp.quit();
		} catch (Exception e) {
			log.error("ftp failed", e);
			try {
				String messages = listener.getLog();
				log.error(messages);
			} catch (Exception e2) {
				log.error("No fue posible obtener el log del ftp");
			}
		}
		return resultFile;
	}

	@Override
	public boolean putFtpArchivo(byte[] fileSource, String fileName) {
		FTPMessageCollector listener = new FTPMessageCollector();
		FTPClient ftp = null;
		
		try {
			ftp = new FTPClient();
			ftp.setRemoteHost(host);
			ftp.setRemotePort(Integer.valueOf(port));
			ftp.setMessageListener(listener);
			
			// Conectando
			log.info("Conectando ... ");
			ftp.connect();

			// Autenticando
			log.info("Autenticando ... ");
			ftp.login(user, password);
	
			// set up passive ASCII transfers
			log.info("Estableciendo modo PASIVO ... ");
			ftp.setConnectMode(FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.BINARY);

			// recuperando
			log.info("Obteniendo ... ");
			ftp.put(fileSource, fileName);
	
			// Shut down client
			log.info("Finalizando ... ");
			ftp.quit();
		} catch (Exception e) {
			log.error("ftp send failed", e);
			try {
				String messages = listener.getLog();
				log.error(messages);
			} catch (Exception e2) {
				log.error("No fue posible obtener el log del ftp");
			}
			
			return false;
		}
		return true;
	}

	@Override
	public boolean delFtpArchivo(String fileName) {
		throw new java.lang.UnsupportedOperationException("Not supported yet.");
	}
}
