package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.dao.FacturaDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturaDetalleFac implements FacturaDetalleRem {
	private static Logger log = Logger.getLogger(FacturaDetalleFac.class);	
	private InitialContext ctx;	
	private FacturaDetalleDAO ifzFacturaDetalle;
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	
	
	public FacturaDetalleFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzFacturaDetalle = (FacturaDetalleDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaDetalleImp!net.giro.cxc.dao.FacturaDetalleDAO");
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear FacturaFac", e);
			this.ctx = null;
		}
	}

	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(FacturaDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzFacturaDetalle.save(entity, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities) throws Exception {
		try {
			return this.ifzFacturaDetalle.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzFacturaDetalle.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzFacturaDetalle.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalle findById(Long id) {
		try {
			return this.ifzFacturaDetalle.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzFacturaDetalle.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<FacturaDetalle> findAll() {
		try {
			return this.ifzFacturaDetalle.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName, long value){
		try{
			return this.findByProperty(propertyName, value);
		}catch(Exception re){
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByFactura(Long idFactura) throws Exception {
		try {
			return this.ifzFacturaDetalle.findByProperty("idFactura", idFactura);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByFactura(Factura idFactura) throws Exception {
		try {
			return this.findByFactura(idFactura.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findByFactura(FacturaExt idFactura) throws Exception {
		try {
			return this.findByFactura(idFactura.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public FacturaDetalleExt convertir(FacturaDetalle entity) throws Exception {
		try {
			return this.convertidor.FacturaDetalleToFacturaDetalleExt(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public FacturaDetalle convertir(FacturaDetalleExt entity) throws Exception {
		try {
			return this.convertidor.FacturaDetalleExtToFacturaDetalle(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public Long save(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.delete(this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaDetalleExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.FacturaDetalleExtToFacturaDetalle(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleExt> findByPropertyPojoCompletoExt(String propertyName, long value) throws Exception{
		List<FacturaDetalleExt> listaExt = new ArrayList<FacturaDetalleExt>();
		
		try {
			List<FacturaDetalle> lista = this.ifzFacturaDetalle.findByPropertyPojoCompleto(propertyName, value);
			for (FacturaDetalle fd : lista) {
				listaExt.add(this.convertidor.FacturaDetalleToFacturaDetalleExt( fd  ) );
			}
		} catch (Exception re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<FacturaDetalleExt> findExtByFactura(Long idFactura) throws Exception {
		List<FacturaDetalleExt> listaExt = new ArrayList<FacturaDetalleExt>();
		
		try {
			List<FacturaDetalle> lista = this.findByFactura(idFactura);
			for (FacturaDetalle fd : lista) {
				listaExt.add(this.convertidor.FacturaDetalleToFacturaDetalleExt( fd  ) );
			}
		} catch (Exception re) {
			throw re;
		}

		return listaExt;
	}
	
	@Override
	public List<FacturaDetalleExt> findExtByFactura(Factura idFactura) throws Exception {
		try {
			return this.findExtByFactura(idFactura.getId());
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<FacturaDetalleExt> findExtByFactura(FacturaExt idFactura) throws Exception {
		try {
			return this.findExtByFactura(idFactura.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.1 | 2017-04-25 | Javier Tirado 	| Quito los mensajes a consola y añado la anotacion Override a los metodos
 */