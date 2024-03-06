package net.giro.cargas.documentos.impresion;

import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FTPConnectMode;

public class Ftp 
{
	private static Logger log = Logger.getLogger(Ftp.class);
	
	private String hostFtp;
	private String pswFTP;
	private String userFTP;
	private Integer portFTP;
	byte[] resultadoDoc = null;


	private Long ID;		
	
	public Ftp(){
		
	}
	
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

	public byte[] getArchivo( String tipo, Long id) 
	{
		FTPClient ftp = null;
		byte[] resultado = null;
		

		try {
			log.info("SocketPoolImp::getArchivo >> Inicializando FTP");
			// set up client
			ftp = new FTPClient();
			ftp.setRemoteHost(hostFtp);
			ftp.setRemotePort(portFTP);
			FTPMessageCollector listener = new FTPMessageCollector();
			ftp.setMessageListener(listener);

			// connect
			log.info(" Connecting");
			ftp.connect();
			Thread.sleep(3000);

			// login
			log.info("Logging in");
			ftp.login(userFTP, pswFTP);

			// set up passive ASCII transfers
			log.debug("Setting up passive, ASCII transfers");
			ftp.setConnectMode(FTPConnectMode.PASV);
			log.info("Getting file");

			if (tipo.equals("SALIDA")) {
				ftp.setType(FTPTransferType.BINARY);
				//ftp.get(local,"salida " + id);
				resultado = ftp.get("salida " + id);
			} 
			else {
				ftp.setType(FTPTransferType.ASCII);
				//ftp.get(local,"log " + id);
				resultado = ftp.get("log " + id);				
			}

			// Shut down client
			log.info("Quitting client");
			ftp.quit();

			String messages = listener.getLog();
			log.debug("Listener log:");
			log.debug(messages);

			log.info("ftp complete");

		} 
		catch (Exception e) {			
			log.error(String.format("Ftp::getArchivo Exception: %s", e.toString() ) );
			return null;
		}
		return resultado;
	}

	public  boolean  FTPObtenDocumento() 
	{
		@SuppressWarnings("unused")
		String pathTmpJava;
		boolean hrFtp;

		hrFtp= false;				
		resultadoDoc= null;
		pathTmpJava= String.format("%s%s.pdf", System.getProperty("java.io.tmpdir"), ID.toString());		
		//hrFtp= getArchivo("SALIDA", ID);
		return hrFtp;
	}

	public  boolean  FTPObtenLog() 
	{
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
