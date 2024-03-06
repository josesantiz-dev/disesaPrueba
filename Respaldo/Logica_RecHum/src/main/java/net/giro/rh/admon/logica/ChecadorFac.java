package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorExt;
import net.giro.rh.admon.dao.ChecadorDAO;

@Stateless
public class ChecadorFac implements ChecadorRem {
	private static Logger log = Logger.getLogger(ChecadorFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ChecadorDAO ifzChecadors;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public ChecadorFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
            
			this.ifzChecadors = (ChecadorDAO) this.ctx.lookup("ejb:/Model_RecHum//ChecadorImp!net.giro.rh.admon.dao.ChecadorDAO");
            
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ChecadorFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.ChecadorFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		ChecadorFac.orderBy = orderBy;
	}

	
	@Override
	public Long save(Checador Checador) throws ExcepConstraint {
		try {
			return this.ifzChecadors.save(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(Checador)", e);
			throw e;
		}
	}

	@Override
	public Long save(ChecadorExt ChecadorExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ChecadorExtToChecador(ChecadorExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(ChecadorExt)", e);
			throw e;
		}
	}

	@Override
	public void update(Checador Checador) throws ExcepConstraint {
		try {
			this.ifzChecadors.update(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.update(Checador)", e);
			throw e;
		}
	}

	@Override
	public void update(ChecadorExt ChecadorExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ChecadorExtToChecador(ChecadorExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.update(ChecadorExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long Checador) throws ExcepConstraint {
		try {
			this.ifzChecadors.delete(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.delete(Long)", e);
			throw e;
		}
	}

	
	@Override
	public Checador findById(Long id) {
		try {
			return this.ifzChecadors.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public ChecadorExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ChecadorToChecadorExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzChecadors.orderBy(orderBy);
			return this.ifzChecadors.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<ChecadorExt> listaExt = new ArrayList<ChecadorExt>();
		
		try {
			List<Checador> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(Checador var : lista) {
					listaExt.add(this.convertidor.ChecadorToChecadorExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Checador> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzChecadors.orderBy(orderBy);
			return this.ifzChecadors.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<ChecadorExt> listaExt = new ArrayList<ChecadorExt>();
		
		try {
			List<Checador> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(Checador var : lista) {
					listaExt.add(this.convertidor.ChecadorToChecadorExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzChecadors.orderBy(orderBy);
			return this.ifzChecadors.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ChecadorExt> listaExt = new ArrayList<ChecadorExt>();
		
		try {
			List<Checador> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(Checador var : lista) {
					listaExt.add(this.convertidor.ChecadorToChecadorExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Checador> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzChecadors.orderBy(orderBy);
			return this.ifzChecadors.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ChecadorExt> listaExt = new ArrayList<ChecadorExt>();
		
		try {
			List<Checador> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(Checador var : lista) {
					listaExt.add(this.convertidor.ChecadorToChecadorExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzChecadors.orderBy(orderBy);
			return this.ifzChecadors.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<ChecadorExt> listaExt = new ArrayList<ChecadorExt>();
		
		try {
			List<Checador> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(Checador var : lista) {
					listaExt.add(this.convertidor.ChecadorToChecadorExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception {
		Respuesta resp = new Respuesta();
		/*double montoTotal = 0;
		
		try {
			if (fileExtension == null || "".equals(fileExtension))
				fileExtension = "xls";
			
			// leemos el archivo anexo
			resp = leerEXCEL(fileSrc);
			
			if (resp.getErrores().getCodigoError() != 0L)
				return resp;
			
			// Comprobamos y generamos las listas correpondientes de insumos
			if (comprobarInsumos()) {
				// Generamos el pojo de INSUMO con los totales de los detalles y total general.
				InsumosExt pojoInsumo = new InsumosExt();
				pojoInsumo.setCreadoPor((int) infoSesion.getAcceso().getId());
				pojoInsumo.setFechaCreacion(Calendar.getInstance().getTime());
				pojoInsumo.setModificadoPor((int) infoSesion.getAcceso().getId());
				pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
				
				montoTotal = 0;
				for(InsumosDetallesExt var : this.listMateriales)
					montoTotal += var.getMonto().doubleValue();
				pojoInsumo.setMontoMateriales(new BigDecimal(montoTotal));
				
				montoTotal = 0;
				for(InsumosDetallesExt var : this.listManoDeObra)
					montoTotal += var.getMonto().doubleValue();
				pojoInsumo.setMontoManoDeObra(new BigDecimal(montoTotal));
				
				montoTotal = 0;
				for(InsumosDetallesExt var : this.listHerramientas)
					montoTotal += var.getMonto().doubleValue();
				pojoInsumo.setMontoHerramientas(new BigDecimal(montoTotal));
				
				pojoInsumo.setTotal(pojoInsumo.getTotalCalculado());
	
				// Generamos la respuesta
				resp.getBody().addValor("pojoInsumo", pojoInsumo);
				resp.getBody().addValor("materiales", this.listMateriales);
				resp.getBody().addValor("manoDeObra", this.listManoDeObra);
				resp.getBody().addValor("herramientas", this.listHerramientas);
				resp.getBody().addValor("otros", this.listOtros);
				resp.getBody().addValor("productos", this.listNoEncontrados);
			} else {
				
			}
		} catch (Exception e) {
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			log.error("Error al leer el archivo XLSX", e);
			throw e;
		}*/
		
		return resp;
	}

	@Override
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzChecadors.findByDates(fechaDesde, fechaHasta);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDates(fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public Checador convertChecadorExtToChecador(ChecadorExt entity) throws Exception {
		return this.convertidor.ChecadorExtToChecador(entity);
	}

	@Override
	public ChecadorExt convertChecadorToChecadorExt(Checador entity) throws Exception {
		return this.convertidor.ChecadorToChecadorExt(entity);
	}
}
