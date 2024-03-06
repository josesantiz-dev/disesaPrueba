package net.giro.cxp.logica;

import java.math.BigDecimal;
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
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GastosImpuestoFac implements GastosImpuestoRem {
	private static Logger log = Logger.getLogger(GastosImpuestoFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private GastosImpuestoDAO ifzGastosImpuesto;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public GastosImpuestoFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);
    		this.ifzGastosImpuesto = (GastosImpuestoDAO) this.ctx.lookup("ejb:/Model_CuentasPorPagar//GastosImpuestoImp!net.giro.cxp.dao.GastosImpuestoDAO");
    		this.ifzQuery = (NQueryRem) this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
    		this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear GastosImpuestoFac", e);
			this.ctx = null;
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
	public GastosImpuesto findById(Long idGastosImpuesto) {
		try {
			return this.ifzGastosImpuesto.findById(idGastosImpuesto);
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
	public List<GastosImpuesto> findAll(long idGasto, String orderBy) {
		try {
			return this.ifzGastosImpuesto.findAll(idGasto, orderBy);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> findAllByImpuesto(long idImpuesto, String orderBy) {
		try {
			return this.ifzGastosImpuesto.findAllByImpuesto(idImpuesto, orderBy);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			return this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
		} catch (Exception re) {
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
		GastosImpuesto entity = null;
		
		try {
			entity = this.convertidor.GastosImpuestoExtToGastosImpuesto(entityExt);
			this.ifzGastosImpuesto.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<GastosImpuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<GastosImpuestoExt> listaExt = new ArrayList<GastosImpuestoExt>();
		List<GastosImpuesto> lista = null;
		GastosImpuestoExt pojoAux = null;
		
		try {
			lista = this.ifzGastosImpuesto.findByPropertyPojoCompleto(propertyName, tipo, value);
			for (GastosImpuesto gastosImpuesto : lista) {
				pojoAux = this.convertidor.GastosImpuestoToGastosImpuestoExt(gastosImpuesto);
				if (tipo == null || "".equals(tipo.trim())) {
					listaExt.add(pojoAux);
					continue;
				} 
				
				if (tipo.trim().toLowerCase().equals(pojoAux.getImpuestoId().getTipoCuenta().trim().toLowerCase())) 
					listaExt.add(pojoAux);
			}
		} catch (Exception re) {
			throw re;
		}

		return listaExt;
	}

	@Override
	public List<GastosImpuestoExt> findAllExt(long idGasto, String orderBy) {
		List<GastosImpuestoExt> extendidos = new ArrayList<GastosImpuestoExt>();
		List<GastosImpuesto> entities = null;
		List<Long> gastosImpuestos = null;
		
		try {
			gastosImpuestos = listadoImpuestos(idGasto);
			entities = this.ifzGastosImpuesto.findAllById(gastosImpuestos); // this.findAll(idGasto, orderBy);
			if (entities != null && ! entities.isEmpty()) {
				for (GastosImpuesto entity : entities) 
					extendidos.add(this.convertidor.GastosImpuestoToGastosImpuestoExt(entity));
			}
		} catch (Exception re) {
			throw re;
		}

		return extendidos;
	}

	@Override
	public List<GastosImpuestoExt> findAllByImpuestoExt(long idImpuesto, String orderBy) {
		List<GastosImpuestoExt> extendidos = new ArrayList<GastosImpuestoExt>();
		List<GastosImpuesto> entities = null;
		
		try {
			entities = this.findAllByImpuesto(idImpuesto, orderBy);
			if (entities != null && ! entities.isEmpty()) {
				for (GastosImpuesto entity : entities) 
					extendidos.add(this.convertidor.GastosImpuestoToGastosImpuestoExt(entity));
			}
		} catch (Exception re) {
			throw re;
		}

		return extendidos;
	}

	// --------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	private List<Long> listadoImpuestos(Long idGasto) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		List<Long> resultado = null;
		
		try {
			if (idGasto == null || idGasto <= 0L)
				return null;
			queryString = "select a.gastos_impuesto_id from gastos_impuesto a inner join de7a4d94446 b on b.aa = a.valor_id_impuesto where a.valor_id_gasto = :idGasto order by case b.af when '0000' then 'zzzz' else b.af end, b.atributo4, b.ah desc ";
			queryString = queryString.replace(":idGasto", idGasto.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return null;
			
			resultado = new ArrayList<Long>();
			for (Object row : rows)
				resultado.add(((BigDecimal) row).longValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el listado Impuestos del Gasto indicado: " + idGasto, e);
			return null;
		}
		
		return resultado;
	}
	
}
