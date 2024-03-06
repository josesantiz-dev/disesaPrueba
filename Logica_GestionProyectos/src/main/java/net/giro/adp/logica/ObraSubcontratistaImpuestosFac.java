package net.giro.adp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraSubcontratistaImpuestos;
import net.giro.adp.beans.ObraSubcontratistaImpuestosExt;
import net.giro.adp.dao.ObraSubcontratistaImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraSubcontratistaImpuestosFac implements ObraSubcontratistaImpuestosRem {
	private static Logger log = Logger.getLogger(ObraSubcontratistaImpuestosFac.class);
	private InfoSesion infoSesion;
	private ObraSubcontratistaImpuestosDAO ifzBase;
	private ConvertExt convertidor;

	public ObraSubcontratistaImpuestosFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try{
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ObraSubcontratistaImpuestosDAO) ctx.lookup("ejb:/Model_GestionProyectos//ObraSubcontratistaImpuestosImp!net.giro.adp.dao.ObraSubcontratistaImpuestosDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setMostrarSystemOut(false);
			this.convertidor.setFrom(this.getClass().getCanonicalName());
		} catch(Exception e) {
			control("Ocurrio un problema al intanciar el EJB " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public long save(ObraSubcontratistaImpuestos entity) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return 0L;
			}
			
			return this.ifzBase.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: save(entity)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaImpuestos> saveOrUpdateList(List<ObraSubcontratistaImpuestos> entities) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaImpuestos> findAll(long idObraSubcontratista, String orderBy) throws Exception {
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			return this.ifzBase.findAll(idObraSubcontratista, orderBy);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findAll(idObraSubcontratista, orderBy)", e);
			throw e;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public ObraSubcontratistaImpuestosExt save(ObraSubcontratistaImpuestosExt extendido) throws Exception {
		ObraSubcontratistaImpuestos entity = null;
		long idSubcontratistaImpuestos = 0L;
		
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			entity = this.convertidor.getPojo(extendido);
			idSubcontratistaImpuestos = this.save(entity);
			entity.setId(idSubcontratistaImpuestos);
			extendido.setId(idSubcontratistaImpuestos);
			return extendido;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: save(extendido)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaImpuestosExt> saveOrUpdateListExt(List<ObraSubcontratistaImpuestosExt> extendidos) throws Exception {
		List<ObraSubcontratistaImpuestos> entities = null;

		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			entities = new ArrayList<ObraSubcontratistaImpuestos>();
			for (ObraSubcontratistaImpuestosExt extendido : extendidos) 
				entities.add(this.convertidor.getPojo(extendido));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (ObraSubcontratistaImpuestos entity : entities) 
				extendidos.add(this.convertidor.getExtendido(entity));
			return extendidos;
		} catch (Exception e) {
			control("Ocurrio un problema al guardar: saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSubcontratistaImpuestosExt> findAllExt(long idObraSubcontratista, String orderBy) throws Exception {
		List<ObraSubcontratistaImpuestosExt> extendidos = null;
		List<ObraSubcontratistaImpuestos> entities = null;
		
		try {
			if (this.infoSesion == null) {
				control("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				return null;
			}
			
			if (idObraSubcontratista <= 0L)
				return null;
			entities = this.ifzBase.findAll(idObraSubcontratista, orderBy);
			entities = (entities != null ? entities : new ArrayList<ObraSubcontratistaImpuestos>());
			
			extendidos = new ArrayList<ObraSubcontratistaImpuestosExt>();
			for (ObraSubcontratistaImpuestos subcontratista : entities)
				extendidos.add(this.convertidor.getExtendido(subcontratista));
			return extendidos;
		} catch (Exception e) {
			control("Ocurrio un problema al consultar: findAllExt(idObraSubcontratista, orderBy)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// -----------------------------------------------------------------------------------------------------------------

	private void control(String mensaje) {
		control(mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		log.error(this.infoSesion.getAcceso().getUsuario().getUsuario() + " - " + mensaje, throwable);
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
