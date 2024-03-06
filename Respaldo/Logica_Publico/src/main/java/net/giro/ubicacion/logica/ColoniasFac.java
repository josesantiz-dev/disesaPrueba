package net.giro.ubicacion.logica;

import java.util.Calendar;
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
import net.giro.publico.respuesta.Respuesta;
import net.giro.publico.util.Errores;


@Stateless
public class ColoniasFac implements ColoniasRem {
	private static Logger log = Logger.getLogger(ColoniasFac.class);
	private InitialContext ctx = null;
	
	private InfoSesion infoSesion;
	
	@Resource
	private ColoniaDAO ifzColonias;
	private LocalidadDAO ifzLocalidades;
	private MunicipioDAO ifzMunicipios;
	private EstadoDAO ifzEstados;
	
	private String modulo = "PUBLICO";
	
	public ColoniasFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		
			ifzColonias = (ColoniaDAO)ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
			
			ifzLocalidades = (LocalidadDAO)ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			
			ifzMunicipios =  (MunicipioDAO)ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
    		ifzEstados =  (EstadoDAO)ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}
	}
	
	public Colonia findById(long id) {
		try{
			return ifzColonias.findById(id);
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
		try{
			List<Localidad> listLocalidades = ifzLocalidades.findByPropertyLikeValor("municipio", pojoMunicipio, "nombre", valor);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listLocalidades", listLocalidades);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_LOCALIDADES);
			respuesta.setBody(null);
			log.error("Error en ColoniasFac.buscarLocalidades", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre){
		Respuesta respuesta = new Respuesta();
		try{
			List<Municipio> listMunicipios = ifzMunicipios.findByPropertyLikeValor("estado", pojoEstado, "nombre", nombre);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMunicipios", listMunicipios);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MUNICIPIOS);
			respuesta.setBody(null);
			log.error("Error en ColoniasFac.buscarMunicipios", e);
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
		try{
			List<Estado> listEstados = ifzEstados.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstados);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_ESTADOS);
			respuesta.setBody(null);
			log.error("Error en SucursalesFac.cargarEstados");
		}
		return respuesta;
	}

	@Override
	public Respuesta salvar(Colonia pojoColonia){
		Respuesta respuesta = new Respuesta();
		try{
			if(pojoColonia.getLocalidad() == null){
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_LOCALIDAD);
				respuesta.setBody(null);
				return respuesta;
			}
			
			if(pojoColonia.getLocalidad().getId() == 0L){
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_LOCALIDAD);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoColonia.setModificadoPor(infoSesion.getAcceso().getId());
			pojoColonia.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(pojoColonia.getId() == 0L){
				pojoColonia.setCreadoPor(infoSesion.getAcceso().getId());
				pojoColonia.setFechaCreacion(Calendar.getInstance().getTime());
				
				ifzColonias.save(pojoColonia);
			} else
				ifzColonias.update(pojoColonia);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_COLONIA);
			respuesta.setBody(null);
			log.error("Error en ColoniasFac.salvar", e);
		}
		return respuesta;
	}

	@Override
	public void eliminar(Colonia colonia) throws ExcepConstraint {
		ifzColonias.delete(colonia.getId());
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
}
