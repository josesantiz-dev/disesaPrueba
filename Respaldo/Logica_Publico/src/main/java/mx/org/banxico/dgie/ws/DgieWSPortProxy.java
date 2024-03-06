package mx.org.banxico.dgie.ws;

public class DgieWSPortProxy implements mx.org.banxico.dgie.ws.DgieWSPort {
	private String _endpoint = null;
	private mx.org.banxico.dgie.ws.DgieWSPort dgieWSPort = null;
	
	public DgieWSPortProxy() {
		_initDgieWSPortProxy();
	}
	
	public DgieWSPortProxy(String endpoint) {
		_endpoint = endpoint;
		_initDgieWSPortProxy();
	}
	
	private void _initDgieWSPortProxy() {
		try {
			dgieWSPort = (new mx.org.banxico.dgie.ws.DgieWSLocator()).getDgieWSPort();
			if (dgieWSPort != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) dgieWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				else
					_endpoint = (String)((javax.xml.rpc.Stub) dgieWSPort)._getProperty("javax.xml.rpc.service.endpoint.address");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getEndpoint() {
		return _endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (dgieWSPort != null)
			((javax.xml.rpc.Stub) dgieWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	}
	
	public mx.org.banxico.dgie.ws.DgieWSPort getDgieWSPort() {
		if (dgieWSPort == null)
			_initDgieWSPortProxy();
		return dgieWSPort;
	}
	
	public java.lang.String reservasInternacionalesBanxico() throws java.rmi.RemoteException{
		if (dgieWSPort == null)
			_initDgieWSPortProxy();
		return dgieWSPort.reservasInternacionalesBanxico();
	}
	
	public java.lang.String tasasDeInteresBanxico() throws java.rmi.RemoteException{
		if (dgieWSPort == null)
			_initDgieWSPortProxy();
		return dgieWSPort.tasasDeInteresBanxico();
	}
	
	public java.lang.String tiposDeCambioBanxico() throws java.rmi.RemoteException{
		if (dgieWSPort == null)
			_initDgieWSPortProxy();
		return dgieWSPort.tiposDeCambioBanxico();
	}
	
	public java.lang.String udisBanxico() throws java.rmi.RemoteException{
		if (dgieWSPort == null)
			_initDgieWSPortProxy();
		return dgieWSPort.udisBanxico();
	}
}