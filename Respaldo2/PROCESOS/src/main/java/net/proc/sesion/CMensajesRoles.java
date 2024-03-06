package net.proc.sesion;


import java.io.Serializable;
import java.util.List;

public class CMensajesRoles implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public List<CRole> roles;
	public String error;
	public String tipo;
	public String sesion;
	public String resultado;
	public String msgHost;
	public String mensaje;
	public String respuesta;
	
	public CMensajesRoles()
	{	
	}
	
}

/** !CMensajesRoles.java */



