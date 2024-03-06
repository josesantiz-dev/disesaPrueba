package net.proc.ftp;

import java.io.Serializable;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.util.debug.Logger;

public class FTPCliente implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FTPCliente.class);
	private String host; 
	private String user; 
	private String password; 
		
	public FTPCliente(String user, String password, String host) {
		this.user = user;
		this.password = password;
		this.host = host;
	}

    public enum Tipo { SALIDA, LOG };
	 
	public byte[] getArchivo(Tipo tipo, long id) {
		FTPMessageCollector listener = new FTPMessageCollector();
		byte[] resultado = null;
		FTPClient ftp = null;
		
		try {
			// set up client
			log.info("---> FTP Initializing");
			ftp = new FTPClient();
			ftp.setRemoteHost(host);
			ftp.setRemotePort(2021);
			ftp.setMessageListener(listener);

			// connect
			log.info("---> FTP Connecting");
			ftp.connect();
			Thread.sleep(3000);

			// login
			log.info("---> FTP Logging in");
			ftp.login(user, password);

			// set up passive ASCII transfers
			log.info("---> FTP Setting on passive mode");
			ftp.setConnectMode(FTPConnectMode.PASV);
			
			// get file
			log.info("---> FTP Getting file :: " + tipo);
			if (tipo == Tipo.SALIDA) {
				ftp.setType(FTPTransferType.BINARY);
				resultado = ftp.get("SALIDA " + id);
			} else {
				ftp.setType(FTPTransferType.ASCII);
				resultado = ftp.get("LOG " + id);				
			}
			
			if (resultado == null)
				log.info("---> FTP File not found");
			else
				log.info("---> FTP File getted");
		} catch (Exception e) {			
			log.error("---> FTP ERROR", e);
			resultado = null;
		} finally {
			// Shut down client
			log.info("---> FTP Quitting");
			try { 
				ftp.quit(); 
				log.info("---> FTP Quitted");
				log.info("---> FTP complete");
				log.info(listener.getLog());
			} catch (Exception e) {
				log.error("---> FTP Error on Quit", e);
				log.info(listener.getLog());
			}
		}
		
		return resultado;
	}	 
}