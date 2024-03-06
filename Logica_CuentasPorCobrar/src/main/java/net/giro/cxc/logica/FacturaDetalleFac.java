package net.giro.cxc.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.dao.FacturaDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class FacturaDetalleFac implements FacturaDetalleRem {
	private static Logger log = Logger.getLogger(FacturaDetalleFac.class);	
	private InitialContext ctx;	
	private FacturaDetalleDAO ifzDetalles;
	private FacturaDetalleImpuestoRem ifzImpuestos;
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	
	public FacturaDetalleFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
            this.ifzDetalles = (FacturaDetalleDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//FacturaDetalleImp!net.giro.cxc.dao.FacturaDetalleDAO");
            this.ifzImpuestos = (FacturaDetalleImpuestoRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleImpuestoFac!net.giro.cxc.logica.FacturaDetalleImpuestoRem");
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
	public Long save(FacturaDetalle entity) throws Exception {
		try {
			return this.ifzDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities) throws Exception {
		try {
			return this.ifzDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(FacturaDetalle entity) throws Exception {
		try {
			this.ifzDetalles.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Long idFacturaDetalle) throws Exception {
		try {
			if (idFacturaDetalle != null && idFacturaDetalle > 0L)
				this.ifzDetalles.delete(idFacturaDetalle);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalle findById(Long idFacturaDetalle) throws Exception {
		try {
			return this.ifzDetalles.findById(idFacturaDetalle);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> findAll(Long idFactura) {
		try {
			return this.ifzDetalles.findAll(idFactura);
		} catch (Exception re) {		
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	@Override
	public FacturaDetalleExt convertir(FacturaDetalle entity) throws Exception {
		FacturaDetalleExt extendido = null;
		
		try {
			extendido = this.convertidor.FacturaDetalleToFacturaDetalleExt(entity);
			extendido.setListImpuestos(this.ifzImpuestos.findAllExt(entity.getId(), ""));
			return extendido;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public FacturaDetalle convertir(FacturaDetalleExt extendido) throws Exception {
		try {
			return this.convertidor.FacturaDetalleExtToFacturaDetalle(extendido);
		} catch (Exception re) {
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public Long save(FacturaDetalleExt extendido) throws Exception {
		try {
			return this.save(this.convertir(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleExt> saveOrUpdateListExt(List<FacturaDetalleExt> extendidos) throws Exception {
		List<FacturaDetalleExt> backup = new ArrayList<FacturaDetalleExt>();
		List<FacturaDetalle> entities = new ArrayList<FacturaDetalle>();
		FacturaDetalleExt extendido = null;
		String key1 = "";
		String key2 = "";
		
		try {
			backup.addAll(extendidos);
			for (FacturaDetalleExt item : extendidos)
				entities.add(this.convertir(item));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (FacturaDetalle entity : entities) {
				extendido = this.convertir(entity);
				if (extendido == null)
					continue;
				
				// asignamos impuestos en base a concepto-cantidad-costo
				key1 = entity.getIdConcepto() + "-" + entity.getCantidad().doubleValue() + "-" + entity.getCosto().doubleValue();
				for (FacturaDetalleExt item : backup) {
					key2 = item.getIdConcepto().getId().longValue() + "-" + item.getCantidad().doubleValue() + "-" + item.getCosto().doubleValue();
					if (key1.equals(key2)) {
						extendido.setListImpuestos(item.getListImpuestos());
						break;
					}
				}
				
				// Añadimos extendido
				extendidos.add(extendido);
			}
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public void update(FacturaDetalleExt extendido) throws Exception {
		try {
			this.update(this.convertir(extendido));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public FacturaDetalleExt findByIdExt(Long idFacturaDetalle) throws Exception {
		try {
			return this.convertir(this.findById(idFacturaDetalle));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleExt> findAllExt(Long idFactura) throws Exception {
		List<FacturaDetalleExt> extendidos = new ArrayList<FacturaDetalleExt>();
		List<FacturaDetalle> entities = null;
		
		try {
			entities = this.findAll(idFactura);
			if (entities == null || entities.isEmpty())
				return extendidos;
			
			for (FacturaDetalle entity : entities) 
				extendidos.add(this.convertir(entity));
		} catch (Exception re) {
			throw re;
		}

		return extendidos;
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
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