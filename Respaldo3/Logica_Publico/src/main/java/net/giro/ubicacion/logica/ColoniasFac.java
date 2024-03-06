package net.giro.ubicacion.logica;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;
import net.giro.respuesta.Respuesta;
import net.giro.publico.util.Errores;


@Stateless
public class ColoniasFac implements ColoniasRem {
	private static Logger log = Logger.getLogger(ColoniasFac.class);
	private InfoSesion infoSesion;
	@Resource
	private ColoniaDAO ifzColonias;
	private LocalidadDAO ifzLocalidades;
	private MunicipioDAO ifzMunicipios;
	private EstadoDAO ifzEstados;
	private String modulo = "PUBLICO";
	
	public ColoniasFac() {
		Hashtable<String, Object> environmnet = null;
		InitialContext ctx = null;
		
		try {
			environmnet = new Hashtable<String, Object>();
			environmnet.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environmnet);
			ifzColonias = (ColoniaDAO)ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
			ifzLocalidades = (LocalidadDAO)ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			ifzMunicipios =  (MunicipioDAO)ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
    		ifzEstados =  (EstadoDAO)ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    	}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Respuesta salvar(Colonia pojoColonia){
		Respuesta respuesta = new Respuesta();
		
		try {
			if (pojoColonia == null || pojoColonia.getLocalidad() == null || pojoColonia.getLocalidad().getId() == 0L) {
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_LOCALIDAD);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if (pojoColonia.getId() == 0L) {
				pojoColonia.setCreadoPor(this.infoSesion.getAcceso().getId());
				pojoColonia.setFechaCreacion(Calendar.getInstance().getTime());
				pojoColonia.setModificadoPor(this.infoSesion.getAcceso().getId());
				pojoColonia.setFechaModificacion(Calendar.getInstance().getTime());
				pojoColonia.setId(this.ifzColonias.save(pojoColonia, getCodigoEmpresa()));
			} else {
				pojoColonia.setModificadoPor(this.infoSesion.getAcceso().getId());
				pojoColonia.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzColonias.update(pojoColonia);
			}
			
			respuesta.getBody().addValor("id", pojoColonia.getId());
			respuesta.getBody().addValor("colonia", pojoColonia);
		} catch (Exception e) {
			log.error("Error en ColoniasFac.salvar", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_COLONIA);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	public List<Colonia> saveOrUpdateList(List<Colonia> entities) throws Exception {
		try {
			return this.ifzColonias.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.FACADE.saveOrUpdateList(List<Colonia> entities)", e);
			throw e;
		}
	}

	@Override
	public void eliminar(Colonia colonia) throws ExcepConstraint {
		ifzColonias.delete(colonia.getId());
	}

	public Colonia findById(long idColonia) {
		try{
			return ifzColonias.findById(idColonia);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Colonia> buscarColonias(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint {
		return ifzColonias.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}
	
	@Override
	public Respuesta buscarLocalidades(Municipio pojoMunicipio, String valor) {
		Respuesta respuesta = new Respuesta();
		List<Localidad> listLocalidades = null;
		
		try {
			listLocalidades = ifzLocalidades.findByPropertyLikeValor("municipio", pojoMunicipio, "nombre", valor);
			if (listLocalidades != null && ! listLocalidades.isEmpty()) {
				Collections.sort(listLocalidades, new Comparator<Localidad>() {
					@Override
					public int compare(Localidad o1, Localidad o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("localidades", listLocalidades);
		} catch (Exception e) {
			log.error("Error en ColoniasFac.buscarLocalidades", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_LOCALIDADES);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre){
		Respuesta respuesta = new Respuesta();
		List<Municipio> listMunicipios = null;
		
		try {
			listMunicipios = ifzMunicipios.findByPropertyLikeValor("estado", pojoEstado, "nombre", nombre);
			if (listMunicipios != null && ! listMunicipios.isEmpty()) {
				Collections.sort(listMunicipios, new Comparator<Municipio>() {
					@Override
					public int compare(Municipio o1, Municipio o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("municipios", listMunicipios);
		} catch (Exception e) {
			log.error("Error en ColoniasFac.buscarMunicipios", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MUNICIPIOS);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	public List<Localidad> buscarLocalidades() throws ExcepConstraint {
		return ifzLocalidades.findAll();
	}
	
	@Override
	public Respuesta cargarEstados(){
		Respuesta respuesta = new Respuesta();
		List<Estado> listEstados = null;
		
		try {
			listEstados = ifzEstados.findAll();
			if (listEstados != null && ! listEstados.isEmpty()) {
				Collections.sort(listEstados, new Comparator<Estado>() {
					@Override
					public int compare(Estado o1, Estado o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstados);
		} catch (Exception e) {
			log.error("Error en SucursalesFac.cargarEstados");
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_ESTADOS);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	private long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
