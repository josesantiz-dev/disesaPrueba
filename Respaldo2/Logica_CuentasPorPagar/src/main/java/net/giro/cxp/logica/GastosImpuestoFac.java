package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.GastosImpuesto;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.dao.GastosImpuestoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GastosImpuestoFac implements GastosImpuestoRem {
	private static Logger log = Logger.getLogger(GastosImpuestoFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private GastosImpuestoDAO ifzGastosImpuesto;
	private ConvertExt convertidor;

	
	public GastosImpuestoFac(){
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(p);
    		
    		this.ifzGastosImpuesto = (GastosImpuestoDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//GastosImpuestoImp!net.giro.cxp.dao.GastosImpuestoDAO");
    		this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear GastosImpuestoFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(GastosImpuesto entity) throws Exception {
		try {
			return this.ifzGastosImpuesto.save(entity, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> saveOrUpdateList(List<GastosImpuesto> entities) throws Exception {
		try {
			return this.ifzGastosImpuesto.saveOrUpdateList(entities, null);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(GastosImpuesto entity) throws Exception {
		try {
			this.ifzGastosImpuesto.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(GastosImpuesto entity) throws Exception {
		try {
			this.ifzGastosImpuesto.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public GastosImpuesto findById(Long id) {
		try {
			return this.ifzGastosImpuesto.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> findByProperty(String propertyName,	final Object value) {
		try {
			return this.ifzGastosImpuesto.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> findAll() {
		try {
			return this.ifzGastosImpuesto.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			List<GastosImpuesto> lista = this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			return lista;
		}catch(Exception re){
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------
	// EXTENDIDO
	// --------------------------------------------------------------------------------------
	
	@Override
	public Long save(GastosImpuestoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(GastosImpuestoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(GastosImpuestoExt entityExt) throws Exception {
		try {
			GastosImpuesto entity = this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt);
			this.ifzGastosImpuesto.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<GastosImpuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<GastosImpuestoExt> listaExt = new ArrayList<GastosImpuestoExt>();
		
		try{
			List<GastosImpuesto> lista = this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			
			for (GastosImpuesto gastosImpuesto : lista) {
				GastosImpuestoExt pojoAux = this.convertidor.GastosImpuestoToGastosImpuestoExt(gastosImpuesto);
				
				if ("".equals(tipo)) {
					listaExt.add(pojoAux);
				} else {
					if (tipo.equals(pojoAux.getImpuestoId().getTipoCuenta())) {
						listaExt.add(pojoAux);
					}
				}
			}
		} catch(Exception re) {
			throw re;
		}

		return listaExt;
	}
}
