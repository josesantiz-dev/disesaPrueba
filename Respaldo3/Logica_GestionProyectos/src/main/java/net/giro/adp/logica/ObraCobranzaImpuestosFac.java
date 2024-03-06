package net.giro.adp.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraCobranzaImpuestos;
import net.giro.adp.dao.ObraCobranzaImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraCobranzaImpuestosFac implements ObraCobranzaImpuestosRem {
	private static Logger log = Logger.getLogger(ObraCobranzaImpuestosFac.class);
	private InfoSesion infoSesion;
	private ObraCobranzaImpuestosDAO ifzBase;

	public ObraCobranzaImpuestosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try{
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ObraCobranzaImpuestosDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraCobranzaImpuestosImp!net.giro.adp.dao.ObraCobranzaImpuestosDAO");
		} catch(Exception e) {
			log.error("Ocurrio un problema al intanciar el EJB " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public long save(ObraCobranzaImpuestos entity) throws Exception {
		try {
			return this.ifzBase.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intanciar el EJB " + this.getClass().getCanonicalName() + " con el metodo save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranzaImpuestos> saveOrUpdateList(List<ObraCobranzaImpuestos> entities) throws Exception {
		try {
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intanciar el EJB " + this.getClass().getCanonicalName() + " con el metodo saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranzaImpuestos> findAll(long idObraCobranza, String orderBy) throws Exception {
		try {
			return this.ifzBase.findAll(idObraCobranza, orderBy);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intanciar el EJB " + this.getClass().getCanonicalName() + " con el metodo findAll(idObraCobranza, orderBy)", e);
			throw e;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------------------------

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
