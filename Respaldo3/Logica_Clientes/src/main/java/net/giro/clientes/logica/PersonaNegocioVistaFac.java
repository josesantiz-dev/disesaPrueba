package net.giro.clientes.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.PersonaNegocioVista;
import net.giro.clientes.dao.PersonaNegocioVistaDAO;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class PersonaNegocioVistaFac implements PersonaNegocioVistaRem {
	private static Logger log = Logger.getLogger(PersonaNegocioVistaFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private PersonaNegocioVistaDAO ifzPersonaNegocioVista;
	
	public PersonaNegocioVistaFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzPersonaNegocioVista = (PersonaNegocioVistaDAO) this.ctx.lookup("ejb:/Model_Clientes//PersonaNegocioVistaImp!net.giro.clientes.dao.PersonaNegocioVistaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_CuentasPorPagar.PersonaNegocioVistaFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public PersonaNegocioVista findById(Long id) {
		try {
			return this.ifzPersonaNegocioVista.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findAll(Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findAll(estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findAll(estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findByProperty(String propertyName, Object value, Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findByProperty(propertyName, value, estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findByProperty(propertyName, value, estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findByProperties(HashMap<String, Object> params, Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findByProperties(params, estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findByProperties(params, estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findLikeProperty(String propertyName, Object value, Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findLikeProperty(propertyName, value, estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findLikeProperty(propertyName, value, estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findLikeProperties(HashMap<String, String> params, Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findLikeProperties(params, estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findLikeProperties(params, estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findInProperty(String columnName, List<Object> values, Long estatus, int limite) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findInProperty(columnName, values, estatus, limite);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findInProperty(columnName, values, estatus, limite)", e);
			throw e;
		}
	}

	@Override
	public PersonaNegocioVista findById(Long id, String tipo) {
		try {
			return this.ifzPersonaNegocioVista.findById(id, tipo);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findById(Long id, String tipo)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> busquedaDinamica(String nombre) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.busquedaDinamica(nombre);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.busquedaDinamica(nombre)", e);
			throw e;
		}
	}

	@Override
	public List<PersonaNegocioVista> findLike(String nombre, String tipo) throws Exception {
		try {
			return this.ifzPersonaNegocioVista.findLike(nombre, tipo);
		} catch (Exception e) {
			log.error("error en Logica_CuentasPorPagar.PersonaNegocioVistaFac.findLike(String nombre, String tipo)", e);
			throw e;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/04/2017 | Javier Tirado	| Creacion de PersonaNegocioVistaFac