package net.giro.sesion;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import com.google.gson.Gson;

public class Cliente {
    private String url = "jdbc:postgresql://192.168.11.244:5432/25sep13";
    private String driver = "org.postgresql.Driver";
    private String usuario = "apps";
    private String password = "apps";
    
    
	public Cliente() throws Exception {
		Socket socket = new Socket("192.168.0.11",1099);
		//talend(socket);
	
		/*
		 Connection conn = getConexion();
		 
		ResultSet rs = conn.createStatement().executeQuery(
					"select folioconsulta,'1' " + 
					" from consultas_circulo " +
					" where buro='' and folioconsulta > 190648 " + 
					"  limit 1");
		while (rs.next()) {
			job(socket,rs.getString(2),rs.getString(1));
		}
		*/
		//job(socket,"1","190650");
		/* rs = conn.createStatement().executeQuery(
		 
				"select folioconsulta,'2' " + 
				" from consultas_circulo " +
				" where  buro='B' and folioconsulta > 194641 " + 
				" limit 100");
		while (rs.next()) {
			job(socket,rs.getString(2),rs.getString(1));
		}
		*/
		//rs.close();
		//conn.close();
		//procesosDelUsuario(socket);
		//procesos(socket);
		opciones(socket);
		socket.close();
	}

    
	public void opciones(Socket socket) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		
		map.put("tipo", "020300");
		map.put("usuario", "admin");
		map.put("role", "Administrators");
		map.put("delUsuario", "admin");
		map.put("delRole", "Administrators");
		
		Gson gson = new Gson();
		String comando = gson.toJson(map);
		
		socket.getOutputStream().write(comando.getBytes());
		
		byte[] b = new byte[4096];
		socket.getInputStream().read(b);
		System.out.println(new String(b));
		
	}

	public void procesos(Socket socket) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		
		map.put("tipo", "020200");
		map.put("usuario", "admin");
		map.put("role", "Administrators");
		
		Gson gson = new Gson();
		String comando = gson.toJson(map);
		
		socket.getOutputStream().write(comando.getBytes());
		
		byte[] b = new byte[4096];
		socket.getInputStream().read(b);
		System.out.println(new String(b));
		
	}

	public void procesosDelUsuario(Socket socket) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		
		map.put("tipo", "020100");
		map.put("usuario", "admin");
		map.put("role", "Administrators");
		
		map.put("delUsuario", "admin");
		Gson gson = new Gson();
		String comando = gson.toJson(map);
		
		socket.getOutputStream().write(comando.getBytes());
		
		byte[] b = new byte[4096];
		socket.getInputStream().read(b);
		System.out.println(new String(b));
		
	}

	public void talend(Socket socket) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		
		map.put("tipo", "000100");
		map.put("idAplicacion", "3");
		map.put("idPrograma", "3");
		map.put("usuario", "admin");
		map.put("role", "Administrators");

		map.put( "parametros", parametros);
		
		programacion.put("empieza", "2013-10-11 12:32");
		/*
		programacion.put("repetir", "m");
		programacion.put("cada", "2");
		//programacion.put("diasSemana", "L,M"); 
		programacion.put("finaliza", "V");
		programacion.put("despues", "3");
		*/
		map.put("programacion", programacion);
		
		Gson gson = new Gson();
		String comando = gson.toJson(map);
		
		socket.getOutputStream().write(comando.getBytes());
		
		byte[] b = new byte[4096];
		socket.getInputStream().read(b);
		System.out.println(new String(b));
		
	}
	
	public void job(Socket socket, String prog, String folio) throws Exception {
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		
		map.put("tipo", "000100");
		map.put("idAplicacion", "3");
		map.put("idPrograma", prog);
		map.put("usuario", "admin");
		map.put("role", "Administrators");

		parametros.put("pfolioconsulta", folio);
		//parametros.put("SUBREPORT_DIR", "C:\\desarrollo\\wsReportServer\\credito\\reportes\\");
		map.put( "parametros", parametros);
		
		//programacion.put("empieza", "2013-10-10 10:32");
		//programacion.put("repetir", "m");
		//programacion.put("cada", "2");
		//programacion.put("diasSemana", "L,M"); 
		//programacion.put("finaliza", "V"); V o F
		//programacion.put("despues", "3"); numero o fecha
		map.put("programacion", programacion);
		
		Gson gson = new Gson();
		String comando = gson.toJson(map);
		
		socket.getOutputStream().write(comando.getBytes());
		
		byte[] b = new byte[4096];
		socket.getInputStream().read(b);
		System.out.println(new String(b));
		
	}

	@SuppressWarnings("unused")
	private Connection getConexion() {
		Connection conn = null;
    	try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,usuario,password);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return conn;		
    }
	

	public static void main(String[] args) throws Exception {
		new Cliente();
		System.exit(0);
	}
}
