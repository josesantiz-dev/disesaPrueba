/**
 * DgieWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mx.org.banxico.dgie.ws;

public interface DgieWS extends javax.xml.rpc.Service {
	public java.lang.String getDgieWSPortAddress();

	public mx.org.banxico.dgie.ws.DgieWSPort getDgieWSPort() throws Exception;

	public mx.org.banxico.dgie.ws.DgieWSPort getDgieWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
