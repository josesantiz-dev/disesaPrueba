package net.giro.cxp.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.dao.PagosGastosDetImpuestosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PagosGastosDetImpuestosFac implements PagosGastosDetImpuestosRem {
	private static Logger log = Logger.getLogger(PagosGastosDetImpuestosFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private PagosGastosDetImpuestosDAO ifzPagosGastosDetImpuestos;
	private ConvertExt convertidor;
	//public static final String VALOR = "valor";

	
	public PagosGastosDetImpuestosFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		this.ctx = new InitialContext(p);
    		this.ifzPagosGastosDetImpuestos = (PagosGastosDetImpuestosDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImpuestosImp!net.giro.cxp.dao.PagosGastosDetImpuestosDAO");
    		
    		this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear PagosGastosDetImpuestosFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(PagosGastosDetImpuestos entity) throws Exception {
		try {
			return this.ifzPagosGastosDetImpuestos.save(entity, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetImpuestos> saveOrUpdateList(List<PagosGastosDetImpuestos> entities) throws Exception {
		try {
			return this.ifzPagosGastosDetImpuestos.saveOrUpdateList(entities, null);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void update(PagosGastosDetImpuestos entity) throws Exception {
		try {
			this.ifzPagosGastosDetImpuestos.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDetImpuestos update(PagosGastosDetImpuestosExt entityExt) throws Exception {
		try {
			PagosGastosDetImpuestos entity = this.convertidor.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(entityExt);
			this.ifzPagosGastosDetImpuestos.update(entity);
			return entity;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDetImpuestos entity) throws Exception {
		try {
			this.ifzPagosGastosDetImpuestos.delete(entity.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(PagosGastosDetImpuestosExt entityExt) throws Exception {
		try {
			this.ifzPagosGastosDetImpuestos.delete(entityExt.getId());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public PagosGastosDetImpuestos findById(Long id) {
		try {
			return this.ifzPagosGastosDetImpuestos.findById(id);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetImpuestos> findByProperty(String propertyName,final Object value) {
		try {
			return this.ifzPagosGastosDetImpuestos.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetImpuestos> findAll() {
		try {
			return this.ifzPagosGastosDetImpuestos.findAll();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max){
		try {
			return this.ifzPagosGastosDetImpuestos.findLikePojoCompleto(value, max);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetImpuestosExt> findLikePojoCompletoExt(PagosGastosDetExt entityExt, int max) throws Exception{
		List<PagosGastosDetImpuestosExt> listaExt = new ArrayList<PagosGastosDetImpuestosExt>();
		
		try {
			List<PagosGastosDetImpuestos> lista = this.ifzPagosGastosDetImpuestos.findLikePojoCompleto(this.convertidor.PagosGastosDetExtToPagosGastosDet(entityExt), max);
			for (PagosGastosDetImpuestos pagosGastosDetImpuestos : lista)
				listaExt.add(this.convertidor.PagosGastosDetImpuestosToPagosGastosDetImpuestosExt(pagosGastosDetImpuestos));
		} catch (Exception re) {
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value,int max){
		try {
			return this.ifzPagosGastosDetImpuestos.findImptos2DetGtos(value, max);
		} catch (Exception re) {
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------

	@Override
	public Long save(PagosGastosDetImpuestosExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(entityExt));
		} catch (Exception re) {
			throw re;
		}
	}
}
