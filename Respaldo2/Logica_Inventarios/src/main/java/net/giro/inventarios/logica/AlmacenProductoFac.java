package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.dao.AlmacenProductoDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class AlmacenProductoFac implements AlmacenProductoRem {
	private static Logger log = Logger.getLogger(AlmacenProductoFac.class);
	private InitialContext ctx;
	private AlmacenProductoDAO ifzAlmacenProductos;
	private ProductoRem ifzProductos;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	
	public AlmacenProductoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzAlmacenProductos = (AlmacenProductoDAO) this.ctx.lookup("ejb:/Model_Inventarios//AlmacenProductoImp!net.giro.inventarios.dao.AlmacenProductoDAO");
            this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
            this.convertidor = new ConvertExt();
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear AlmacenProductoFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(AlmacenProducto entity) throws Exception {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.save(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.saveOrUpdateList(entities);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenProducto entity) throws Exception {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			this.ifzAlmacenProductos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenProducto entity) throws Exception {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			this.ifzAlmacenProductos.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenProducto findById(Long id) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findAll() {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findAllActivos() {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<AlmacenProducto> findBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findBy(idAlmacen, value, idFamilia, tipoMaestro, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findByProperty(String propertyName, Object value) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findLike(idAlmacen, value, idFamilia, tipoMaestro, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findLikeProperty(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public double findCantidadEnAlmacen(long idAlmacen, long idProducto) {
		List<AlmacenProducto> producto = null;

		this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
		//Debe regresar solo 1, ya que debe ser unico tanto idproducto como almacen
		producto = this.ifzAlmacenProductos.findCantidadEnAlmacen(idAlmacen, idProducto);
		//si no regresa nulo, revisar que la lista no este vacia, ya que el producto puede existir, pero no estar en almacen
		return producto == null ? 0 : producto.size() == 0 ? 0 : producto.get(0).getExistencia();
	}

	@Override
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto) {
		List<AlmacenProducto> lista = null;
		AlmacenProducto resultado = null;
		
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			// Debe regresar solo 1, ya que debe ser unico tanto idproducto como almacen
			lista = this.ifzAlmacenProductos.findAlmacenProducto(idAlmacen, idProducto);
			//si la lista es nula o regresa cero, quiere decir que no hay este producto en almacen
			if (lista != null && ! lista.isEmpty())
				resultado = lista.get(0);
		} catch (Exception e) {
			log.error("Error el buscar el producto en Alamcenes", e);
		}
		
		return resultado;
	}

	@Override
	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findExistentes(campoBusqueda, idAlmacen, valor);
		} catch (Exception re) {		
			throw re;
		}
	}
	
	@Override
	public List<AlmacenProducto> findExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findExistentes(idAlmacen, value, idFamilia, tipoMaestro, limite, excluyeExistencia);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findExistentes(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite, excluyeExistencia);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> findInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProducto> resultados = new ArrayList<AlmacenProducto>();
		List<AlmacenProducto> listInventario = null;
		List<Producto> listProductos = null;
		AlmacenProducto item = null;
		
		try {
			// Consultamos productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			if ("*".equals(propertyName))
				listProductos = this.ifzProductos.findLike(propertyValue, idFamilia, tipoMaestro, limite);
			else
				listProductos = this.ifzProductos.findLikeProperty(propertyName, propertyValue, idFamilia, limite);
			
			// Compruebo resultados y recupero existencias si corresponde
			if (listProductos != null && ! listProductos.isEmpty()) {
				for (Producto var : listProductos) {
					listInventario = this.ifzAlmacenProductos.findAlmacenProducto(idAlmacen, var.getId());
					if (listInventario != null && ! listInventario.isEmpty()) {
						resultados.addAll(listInventario);
						continue;
					}
					
					item = new AlmacenProducto();
					item.setId(0);
					item.setIdAlmacen(idAlmacen);
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
	public List<AlmacenProducto> findInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProducto> resultados = new ArrayList<AlmacenProducto>();
		List<AlmacenProducto> listInventario = null;
		List<Producto> listProductos = null;
		
		try {
			// Consultamos productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			if ("*".equals(propertyName))
				listProductos = this.ifzProductos.findLike(propertyValue, idFamilia, tipoMaestro, limite);
			else
				listProductos = this.ifzProductos.findLikeProperty(propertyName, propertyValue, idFamilia, limite);
			
			// Compruebo resultados y recupero existencias si corresponde
			if (listProductos != null && ! listProductos.isEmpty()) {
				for (Producto var : listProductos) {
					listInventario = this.ifzAlmacenProductos.findAlmacenProducto(idAlmacen, var.getId());
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
	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor) {
		try {
			this.ifzAlmacenProductos.setEmpresa(getIdEmpresa());
			return this.ifzAlmacenProductos.findProductoExistente(campoBusqueda, idAlmacen, valor);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public AlmacenProducto convertir(AlmacenProductoExt entity) {
		return this.convertidor.AlmacenProductoExtToAlmacenProducto(entity);
	}

	@Override
	public AlmacenProductoExt convertir(AlmacenProducto entity) {
		return this.convertidor.AlmacenProductoToAlmacenProductoExt(entity);
	}

	// ------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------

	@Override
	public Long save(AlmacenProductoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(AlmacenProductoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(AlmacenProductoExt entityExt) throws Exception {
		try {
			this.delete(this.convertidor.AlmacenProductoExtToAlmacenProducto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public AlmacenProductoExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenProductoToAlmacenProductoExt(this.findById(id));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<AlmacenProductoExt> findAllExt() {
		List<AlmacenProductoExt> listaExt = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findAll();
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto ap : lista)
					listaExt.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(ap));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenProductoExt> findExtBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProductoExt> extendidos = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findBy(idAlmacen, value, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					extendidos.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<AlmacenProductoExt> findExtByProperty(String propertyName, Object value) {
		List<AlmacenProductoExt> listaExt = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findByProperty(propertyName, value);
			if (lista != null && ! lista.isEmpty()) {
				for(AlmacenProducto ap: lista)
					listaExt.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(ap));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<AlmacenProductoExt> findExtLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProductoExt> extendidos = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findLike(idAlmacen, value, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					extendidos.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<AlmacenProductoExt> findExtLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProductoExt> extendidos = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findLikeProperty(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					extendidos.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
		} catch (Exception re) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<AlmacenProductoExt> findExistentesExt(String campoBusqueda, long idAlmacen, String valor) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findExistentes(campoBusqueda, idAlmacen, valor);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					resultado.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenProductoExt> findExtExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findExistentes(idAlmacen, value, idFamilia, tipoMaestro, limite, excluyeExistencia);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					resultado.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
			
			return resultado;
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
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					resultado.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenProductoExt> findExtInventario(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findInventario(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					resultado.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenProductoExt> findExtInventarioExistente(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		List<AlmacenProductoExt> resultado = new ArrayList<AlmacenProductoExt>();
		List<AlmacenProducto> lista = null;
		
		try {
			lista = this.findInventarioExistente(idAlmacen, propertyName, propertyValue, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (AlmacenProducto var : lista)
					resultado.add(this.convertidor.AlmacenProductoToAlmacenProductoExt(var));
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public AlmacenProductoExt findProductoExistenteExt(String campoBusqueda, long idAlmacen, String valor) {
		try {
			return this.convertidor.AlmacenProductoToAlmacenProductoExt(this.ifzAlmacenProductos.findProductoExistente(campoBusqueda, idAlmacen, valor));
		} catch (Exception re) {		
			throw re;
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	// PRIVADAS
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
