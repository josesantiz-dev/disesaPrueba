package net.giro.plataforma.impresion;

import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPConnectMode;

public class Ftp {
	private static Logger log = Logger.getLogger(Ftp.class);
	
	private String hostFtp;
	private String pswFTP;
	private String userFTP;
	private Integer portFTP;
	byte[] resultadoDoc = null;
	private Long ID;
	
	public Ftp() {}
	
	public Ftp(String user, String password, String host, String port){
		this.userFTP = user;
		this.pswFTP = password;
		this.hostFtp = host;
		this.portFTP = Integer.valueOf(port);
	}
	
	
	public void setHostFtp(String hostFtp) {
		this.hostFtp = hostFtp;
	}

	public void setPswFTP(String pswFTP) {
		this.pswFTP = pswFTP;
	}
	
	public void setUserFTP(String userFTP) {
		this.userFTP = userFTP;
	}
	
	public void setPortFTP(Integer portFTP) {
		this.portFTP = portFTP;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public byte[] getResultadoDoc() {
		return resultadoDoc;
	}

	public byte[] getArchivo( String tipo, Long id) {
		FTPMessageCollector listener = new FTPMessageCollector();
		byte[] resultado = null;
		FTPClient ftp = null;
		
		try {
			// set up client
			log.info("---> FTP Initializing");
			ftp = new FTPClient();
			ftp.setRemoteHost(hostFtp);
			ftp.setRemotePort(portFTP);
			ftp.setMessageListener(listener);

			// connect
			log.info("---> FTP Connecting");
			ftp.connect();
			Thread.sleep(3000);

			// login
			log.info("---> FTP Logging in");
			ftp.login(userFTP, pswFTP);

			// set up passive ASCII transfers
			log.info("---> FTP Setting on passive mode");
			ftp.setConnectMode(FTPConnectMode.PASV);
			
			// get file
			log.info("---> FTP Getting file");
			if (tipo.equals("SALIDA")) {
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

	public boolean FTPObtenDocumento() {
		@SuppressWarnings("unused")
		String pathTmpJava;
		boolean hrFtp;

		hrFtp= false;				
		resultadoDoc= null;
		pathTmpJava= String.format("%s%s.pdf", System.getProperty("java.io.tmpdir"), ID.toString());
		//hrFtp= getArchivo("SALIDA", ID);
		return hrFtp;
	}

	public boolean FTPObtenLog() {
		@SuppressWarnings("unused")
		String pathTmpJava;
		boolean hrFtp;
		
		hrFtp= false;
		resultadoDoc= null;
		pathTmpJava= String.format("%s%s.log", System.getProperty("java.io.tmpdir"), ID.toString());
		//hrFtp= getArchivo("LOG",  ID);
		return hrFtp;
	}
}
