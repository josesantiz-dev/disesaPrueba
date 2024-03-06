package net.giro.clientes.plataforma.seguridad;

public class Login  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private long empresaId;
	private String usuario;
	private String password;
	private String terminal;
	private String ipTerminal;
	private long canalId;
	private long aplicacionId;
	 
	public long getEmpresaId() {
		return empresaId;
	}
	public void setEmpresaId(long empresaId) {
		this.empresaId = empresaId;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getIpTerminal() {
		return ipTerminal;
	}
	public void setIpTerminal(String ipTerminal) {
		this.ipTerminal = ipTerminal;
	}
	public long getCanalId() {
		return canalId;
	}
	public void setCanalId(long canalId) {
		this.canalId = canalId;
	}
	public long getAplicacionId() {
		return aplicacionId;
	}
	public void setAplicacionId(long aplicacionId) {
		this.aplicacionId = aplicacionId;
	}

}
