package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.DesgloceSBO;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoHistorial;
import net.giro.inventarios.dao.AlmacenDAO;
import net.giro.inventarios.dao.AlmacenProductoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AlmacenProductoFac implements AlmacenProductoRem {
	private static Logger log = Logger.getLogger(AlmacenProductoFac.class);
	private AlmacenProductoDAO ifzAlmacenProductos;
	private AlmacenDAO ifzAlmacen;
	private ProductoRem ifzProductos;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	public AlmacenProductoFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
            this.ifzAlmacenProductos = (AlmacenProductoDAO) ctx.lookup("ejb:/Model_Inventarios//AlmacenProductoImp!net.giro.inventarios.dao.AlmacenProductoDAO");
            this.ifzAlmacen = (AlmacenDAO) ctx.lookup("ejb:/Model_Inventarios//AlmacenImp!net.giro.inventarios.dao.AlmacenDAO");
            this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear AlmacenProductoFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(AlmacenProducto entity) throws Exception {
		try {
			entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			return this.ifzAlmacenProductos.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception {
		try {
			for (AlmacenProducto entity : entities) 
				entity.setIdEmpresa((entity.getIdEmpresa() <= 0L) ? getIdEmpresa() : entity.getIdEmpresa());
			return this.ifzAlmacenProductos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(AlmacenProducto entity) throws Exception {
		try {
			if (entity.getIdEmpresa() <= 0L)
				entity.setIdEmpresa(getIdEmpresa());
			this.ifzAlmacenProductos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenProducto entity) throws Exception {
		try {
			this.ifzAlmacenProductos.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenProducto findById(long idAlmacenProducto) {
		try {
			return this.ifzAlmacenProductos.findById(idAlmacenProducto);
		} catch (Exception e) {
			log.error("Ocurrio un problema al encontrar el AlmacenProducto indicado", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia, String orderBy) throws Exception {
		try {
			return this.ifzAlmacenProductos.findAll(idAlmacen, excluyeSinExistencia, getIdEmpresa(), orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al encontrar los Productos del Almacen indicado", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia) throws Exception {
		try {
			return this.findAll(idAlmacen, excluyeSinExistencia, "");
		} catch (Exception re) {
			log.error("Ocurrio un problema al encontrar los Productos del Almacen indicado", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findAll(long idAlmacen) throws Exception {
		try {
			return this.findAll(idAlmacen, true, "");
		} catch (Exception re) {
			log.error("Ocurrio un problema al encontrar los Productos del Almacen indicado", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite) {
		try {
			return this.findLike(idAlmacen, value, idFamilia, tipoMaestro, true, "", limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite) {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzAlmacenProductos.findLike(idAlmacen, value, idFamilia, tipoMaestro, excluyeSinExistencia, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			log.error("Ocurrio un problema al encontrar los Productos del Almacen indicado :: findLike(idAlmacen, value, idFamilia, tipoMaestro, excluyeSinExistencia, orderBy, limite)", re);
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite) {
		try {
			if (value instanceof java.lang.String) {
				if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
					return this.findLike(idAlmacen, value.toString(), idFamilia, tipoMaestro, excluyeSinExistencia, orderBy, limite);
				
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzAlmacenProductos.findLikeProperty(idAlmacen, propertyName, value, idFamilia, tipoMaestro, excluyeSinExistencia, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {	
			log.error("Ocurrio un problema al encontrar los Productos del Almacen indicado :: findLikeProperty(idAlmacen, propertyName, value, idFamilia, tipoMaestro, excluyeSinExistencia, orderBy, limite)", re);	
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		try {
			return this.findLikeProperty(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, true, "", limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite) {
		try {
			return this.ifzAlmacenProductos.findByProperty(idAlmacen, propertyName, value, idFamilia, tipoMaestro, excluyeSinExistencia, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, int limite) {
		try {
			return this.findByProperty(idAlmacen, propertyName, value, idFamilia, tipoMaestro, true, "", limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> findAlmacenProductos(long idAlmacen, long idProducto) throws Exception {
		try {
			return this.ifzAlmacenProductos.findAlmacenProductos(idAlmacen, idProducto, getIdEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al encontrar el Producto en el Almacen indicado", e);
			throw e;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto) throws Exception {
		try {
			return this.ifzAlmacenProductos.findAlmacenProducto(idAlmacen, idProducto, getIdEmpresa());
		} catch (Exception e) {
			log.error("Ocurrio un problema al encontrar el Producto en el Almacen indicado", e);
			throw e;
		}
	}

	@Override
	public double findCantidadEnAlmacen(long idAlmacen, long idProducto) throws Exception {
		AlmacenProducto item = null;
		
		try {
			item = this.findAlmacenProducto(idAlmacen, idProducto);
			if (item != null && item.getId() != null && item.getId() > 0L)
				return item.getExistencia();
			return 0;
		} catch (Exception e) {
			log.error("Ocurrio un problema al encontrar la existencia del Producto en el Almacen indicado", e);
			throw e;
		}
	}

	@Override
	public List<AlmacenProducto> findExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		try {
			return this.findLikeProperty(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, excluyeExistencia, "", limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) throws Exception {
		List<AlmacenProducto> resultados = new ArrayList<AlmacenProducto>();
		List<AlmacenProducto> listInventario = null;
		List<Producto> listProductos = null;
		AlmacenProducto item = null;
		Almacen alm = null;
		
		try {
			// Consultamos productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				listProductos = this.ifzProductos.findLike(propertyValue, idFamilia, tipoMaestro, "", limite); //findLike(propertyValue, idFamilia, tipoMaestro, limite);
			else
				listProductos = this.ifzProductos.findLikeProperty(propertyName, propertyValue, idFamilia, limite);
			
			// Compruebo resultados y recupero existencias si corresponde
			if (listProductos != null && ! listProductos.isEmpty()) {
				alm = this.ifzAlmacen.findById(idAlmacen);
				
				for (Producto var : listProductos) {
					listInventario = this.ifzAlmacenProductos.findAlmacenProductos(idAlmacen, var.getId(), getIdEmpresa());
					if (listInventario != null && ! listInventario.isEmpty()) {
						resultados.addAll(listInventario);
						continue;
					}
					
					item = new AlmacenProducto();
					item.setId(0);
					item.setIdAlmacen(alm);
					item.setIdProducto(var.getId());
					item.setExistencia(0);
					item.setEstatus(0);
					item.setCreadoPor(var.getCreadoPor());
					item.setFechaCreacion(var.getFechaCreacion());
					item.setModificadoPor(var.getModificadoPor());
					item.setFechaModificacion(var.getFechaModificacion());
					resultados.add(item);
				}
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return resultados;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> findInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) throws Exception {
		List<AlmacenProducto> resultados = new ArrayList<AlmacenProducto>();
		List<AlmacenProducto> listInventario = null;
		List<Producto> listProductos = null;
		
		try {
			// Consultamos productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				listProductos = this.ifzProductos.findLike(propertyValue, idFamilia, tipoMaestro, "", limite);
			else
				listProductos = this.ifzProductos.findLikeProperty(propertyName, propertyValue, idFamilia, limite);
			
			// Compruebo resultados y recupero existencias si corresponde
			if (listProductos != null && ! listProductos.isEmpty()) {
				for (Producto var : listProductos) {
					listInventario = this.ifzAlmacenProductos.findAlmacenProductos(idAlmacen, var.getId(), getIdEmpresa());
					if (listInventario != null && ! listInventario.isEmpty()) {
						resultados.addAll(listInventario);
						continue;
					}
				}
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return resultados;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public AlmacenProducto findProductoExistente(long idAlmacen, String propertyName, Object value) {
		try {
			return this.ifzAlmacenProductos.findProductoExistente(idAlmacen, propertyName, value, getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos, boolean excluyeSinExistencia, String orderBy) throws Exception {
		try {
			return this.ifzAlmacenProductos.encontrarExistencia(idAlmacen, listProductos, excluyeSinExistencia, getIdEmpresa(), orderBy);
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar encontrar las existencias de productos en el Almacen " + idAlmacen, re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos) throws Exception {
		try {
			return this.encontrarExistencia(idAlmacen, listProductos, false, "");
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar encontrar las existencias de productos en el Almacen " + idAlmacen, re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public HashMap<Long, Double> encontrarExistenciaProductos(long idAlmacen, List<Long> listProductos) throws Exception {
		HashMap<Long, Double> resultado = new HashMap<Long, Double>();
		List<AlmacenProducto> existencias = null;
		
		try {
			existencias = this.encontrarExistencia(idAlmacen, listProductos);
			if (existencias == null || existencias.isEmpty())
				return resultado;
			for (AlmacenProducto item : existencias) 
				resultado.put(item.getIdProducto(), item.getExistencia());
		} catch (Exception re) {
			log.error("Ocurrio un problema al intentar encontrar las existencias de productos en el Almacen " + idAlmacen, re);
			throw re;
		}
		
		return resultado;
	}

	@Override
	public List<ProductoHistorial> historial(long idAlmacen, long idProducto, Date fechaDesde, Date fechaHasta) throws Exception {
		return new ArrayList<ProductoHistorial>();
	}

	@Override
	public List<DesgloceSBO> desgloceSBO(long idAlmacen, long idProducto) throws Exception {
		return new ArrayList<DesgloceSBO>();
	}

	@Override
	public AlmacenProducto convertir(AlmacenProductoExt entity) {
		return this.convertidor.getPojo(entity);
	}

	@Override
	public AlmacenProductoExt convertir(AlmacenProducto entity) {
		return this.convertidor.getExtendido(entity);
	}

	// ------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenProductoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenProductoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenProductoExt findByIdExt(long idAlmacenProducto) {
		try {
			return this.convertidor.getExtendido(this.findById(idAlmacenProducto));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenProductoExt> findExtExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findExistentes(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite, excluyeExistencia);
			if (lista == null || lista.isEmpty()) 
				return resultado;
			for (AlmacenProducto var : lista)
				resultado.add(this.convertidor.getExtendido(var));
			return resultado;
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las Existencias del Almacen indicado: " + idAlmacen, re);
			throw re;
		}
	}

	@Override
	public List<AlmacenProductoExt> findLikePropertyExt(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, String orderBy, int limite) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findLikeProperty(idAlmacen, propertyName, value, idFamilia, tipoMaestro, excluyeSinExistencia, orderBy, limite);
			if (lista == null || lista.isEmpty()) 
				return resultado;
			for (AlmacenProducto var : lista)
				resultado.add(this.convertidor.getExtendido(var));
			return resultado;
		} catch (Exception re) {
			log.error("Ocurrio un problema al consultar las Existencias del Almacen indicado: " + idAlmacen, re);
			throw re;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
