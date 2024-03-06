package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.GastosExt;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;

@Stateless
public class GastosFac implements GastosRem {
	private static Logger log = Logger.getLogger(GastosFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private ConValoresRem ifzConValores;
	private ConGrupoValoresRem ifzGpoValores;
	private ConGrupoValores pojoGrupo;
	private ConvertExt convertidor;
	
	public GastosFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);

			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("PagosGastosFac");
			this.convertidor.setMostrarSystemOut(false);
			
			this.pojoGrupo = this.ifzGpoValores.findByName("SYS_MOVGTOS");
			if (this.pojoGrupo == null || this.pojoGrupo.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_CuentasPorPagar.PagosGastosFac", e);
			this.ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public long save(GastosExt pojoGasto) throws Exception {
		ConValores pojoEntity = null;
		
		try {
			pojoEntity = this.convertidor.GastosToGastosExt(pojoGasto);
			this.ifzConValores.setInfoSesion(this.infoSesion);
			return this.ifzConValores.save(pojoEntity);
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.save()", e);
			throw e;
		}
	}

	@Override
	public void update(GastosExt pojoGasto) throws Exception {
		ConValores pojoEntity = null;
		
		try {
			pojoEntity = this.convertidor.GastosToGastosExt(pojoGasto);
			this.ifzConValores.setInfoSesion(this.infoSesion);
			this.ifzConValores.update(pojoEntity);
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.update()", e);
			throw e;
		}
	}

	@Override
	public void delete(GastosExt pojoGasto) throws Exception {
		ConValores pojoEntity = null;
		
		try {
			pojoEntity = this.convertidor.GastosToGastosExt(pojoGasto);
			this.ifzConValores.setInfoSesion(this.infoSesion);
			this.ifzConValores.delete(pojoEntity);
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.delete()", e);
			throw e;
		}
	}

	@Override
	public GastosExt findById(Long id) throws Exception {
		try {
			this.ifzConValores.setInfoSesion(this.infoSesion);
			return this.convertidor.GastosExtToGastos(this.ifzConValores.findById(id));
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.save()", e);
			throw e;
		}
	}

	@Override
	public List<GastosExt> findAll() throws Exception {
		List<GastosExt> listaResult = new ArrayList<GastosExt>();
		
		try {
			if (this.pojoGrupo == null || this.pojoGrupo.getId() <= 0L) 
				return listaResult;

			this.ifzConValores.setInfoSesion(this.infoSesion);
			List<ConValores> lista = this.ifzConValores.findAll(this.pojoGrupo);
			if (lista == null || lista.isEmpty())
				return listaResult;
			
			for (ConValores var : lista)
				listaResult.add(this.convertidor.GastosExtToGastos(var));
			return listaResult;
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public List<GastosExt> findByProperty(String propertyName, Object value, int limite) throws Exception {
		List<GastosExt> listaResult = new ArrayList<GastosExt>();
		
		try {
			if (this.pojoGrupo == null || this.pojoGrupo.getId() <= 0L) 
				return listaResult;

			this.ifzConValores.setInfoSesion(this.infoSesion);
			List<ConValores> lista = this.ifzConValores.findByProperty(propertyName, value, this.pojoGrupo, 0);
			if (lista == null || lista.isEmpty())
				return listaResult;
			
			for (ConValores var : lista)
				listaResult.add(this.convertidor.GastosExtToGastos(var));
			return listaResult;
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.findByProperty()", e);
			throw e;
		}
	}

	@Override
	public List<GastosExt> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		List<GastosExt> listaResult = new ArrayList<GastosExt>();
		
		try {
			if (this.pojoGrupo == null || this.pojoGrupo.getId() <= 0L) 
				return listaResult;

			this.ifzConValores.setInfoSesion(this.infoSesion);
			List<ConValores> lista = this.ifzConValores.findLikeProperty(propertyName, value, this.pojoGrupo, 0);
			if (lista == null || lista.isEmpty())
				return listaResult;
			
			for (ConValores var : lista)
				listaResult.add(this.convertidor.GastosExtToGastos(var));
			return listaResult;
		} catch (Exception e) {
			log.error("Error en CXP.GastosFac.findLikeProperty()", e);
			throw e;
		}
	}
}
