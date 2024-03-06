package net.giro.plataforma.impresion;

import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;

@Stateless
public class FtpFac implements FtpRem {
	private static  Logger log =  Logger.getLogger(FtpFac.class);
	@SuppressWarnings("unused")
	private InitialContext ctx = null;
	@Resource
	private SessionContext sctx;

	private String host = "127.0.0.1";
	private String user = "guess";
	private String password = "guess";
	private String port = "22";

	public FtpFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		} catch (Exception e){

		}
	}

	@Override
	public void setInfo(String host, String user, String password, String port){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	@Override
	public byte[] getArchivo(String archivo) {
		FTPClient ftp = null;
		byte[] resultFile = null;
		FTPMessageCollector listener = new FTPMessageCollector();
		
		try {
			ftp = new FTPClient();
			ftp.setRemoteHost(host);
			ftp.setRemotePort(Integer.valueOf(port));
			ftp.setMessageListener(listener);
			ftp.connect();
	
			ftp.login(user, password);
	
			// set up passive ASCII transfers
			ftp.setConnectMode(FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.BINARY);
	
			resultFile = ftp.get(archivo);
	
			// Shut down client
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
	public boolean putArchivo(byte[] archivo, String nombre){
		FTPClient ftp = null;
		FTPMessageCollector listener = new FTPMessageCollector();
		
		try {
			ftp = new FTPClient();
			ftp.setRemoteHost(host);
			ftp.setRemotePort(Integer.valueOf(port));
			ftp.setMessageListener(listener);
			ftp.connect();
	
			ftp.login(user, password);
	
			// set up passive ASCII transfers
			ftp.setConnectMode(FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.BINARY);
			ftp.put(archivo, nombre);
	
			// Shut down client
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
}
