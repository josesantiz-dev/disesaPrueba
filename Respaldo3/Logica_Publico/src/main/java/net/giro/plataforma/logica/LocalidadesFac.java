package net.giro.plataforma.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;
import net.giro.respuesta.Respuesta;
import net.giro.publico.util.Errores;

import org.apache.log4j.Logger;

@Stateless
public class LocalidadesFac implements LocalidadesRem{
	private static Logger log = Logger.getLogger(LocalidadesFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private LocalidadDAO ifzLocalidades;
	private MunicipioDAO ifzMunicipios;
	private EstadoDAO ifzEstados;
	private ConValoresDAO ifzConValores;
	private ConGrupoValoresDAO ifzConGrupoValores;
	private String modulo = "PUBLICO";
	
	public LocalidadesFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(environment);
    		ifzLocalidades = (LocalidadDAO) ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		ifzMunicipios = (MunicipioDAO) ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
    		ifzEstados = (EstadoDAO) ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
			ifzConValores = (ConValoresDAO) ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			ifzConGrupoValores = (ConGrupoValoresDAO) ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto , no se pudo crear", e);
    		ctx = null;
		}
	}
	
	public Localidad findById(long id) {
		try {
			return ifzLocalidades.findById(id);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Respuesta buscarLocalidades(String tipo, String valor) {
		Respuesta respuesta = new Respuesta();
		try {
			List<Localidad> listLocalidades = ifzLocalidades.findLikeColumnName(tipo, valor);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listLocalidades", listLocalidades);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_LOCALIDADES);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.buscarLocalidades", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta buscarMunicipios(Estado pojoEstado, String nombre) {
		Respuesta respuesta = new Respuesta();
		try {
			List<Municipio> listMunicipios = ifzMunicipios.findByPropertyLikeValor("estado", pojoEstado, "nombre", nombre);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMunicipios", listMunicipios);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_BUSCAR_MUNICIPIOS);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.buscarMunicipios", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta cargarEstados() {
		Respuesta respuesta = new Respuesta();
		try {
			List<Estado> listEstados = ifzEstados.findAll();
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listEstados", listEstados);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_CARGAR_ESTADOS);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.cargarEstados");
		}
		return respuesta;
	}
	
	@Override
	public Respuesta cargarMunicipios(Estado pojoEstado) {
		Respuesta respuesta = new Respuesta();
		try {
			List<Municipio> listMunicipios = ifzMunicipios.findByColumnName("estado", pojoEstado);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("listMunicipios", listMunicipios);
		} catch (Exception e) {
			
		}
		return respuesta;
	}

	@Override
	public List<ConValores> buscarZonas() throws Exception {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ZONA");
		for (ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for (ConValores cv : listaTmpConValores)
				listaConValores.add(cv);
		}
		
		return listaConValores;
	}

	@Override
	public List<ConValores> buscarGradoMarginalidad() throws Exception {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_MARGINALIDAD");
		for (ConGrupoValores cGV : listaConGrupoValores) {
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for (ConValores cv : listaTmpConValores)
				listaConValores.add(cv);
		}
		
		return listaConValores;
	}

	@Override
	public Respuesta eliminarLocalidad(Localidad pojoLocalidad) {
		return null;
	}
	
	@Override
	public Respuesta guardarLocalidad(Localidad pojoLocalidad) {
		Respuesta respuesta = new Respuesta();
		try {
			if (pojoLocalidad.getMunicipio() == null) {
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_MUNICIPIO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoLocalidad.setModificadoPor(infoSesion.getAcceso().getId());
			pojoLocalidad.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoLocalidad.getId() == 0L) {
				pojoLocalidad.setCreadoPor(infoSesion.getAcceso().getId());
				pojoLocalidad.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoLocalidad.setId(ifzLocalidades.save(pojoLocalidad, null));
			} else 
				ifzLocalidades.update(pojoLocalidad);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoLocalidad", pojoLocalidad);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_LOCALIDAD);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.guardarLocalidad", e);
		}
		return respuesta;
	}
	
	@Override
	public Respuesta guardarMunicipio(Municipio pojoMunicipio) {
		Respuesta respuesta = new Respuesta();
		try {
			if (pojoMunicipio.getEstado() == null) {
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_FALTA_ESTADO);
				respuesta.setBody(null);
				return respuesta;
			}
			
			pojoMunicipio.setModificadoPor(infoSesion.getAcceso().getId());
			pojoMunicipio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (pojoMunicipio.getId() == 0L) {
				pojoMunicipio.setCreadoPor(infoSesion.getAcceso().getId());
				pojoMunicipio.setFechaCreacion(Calendar.getInstance().getTime());
				
				pojoMunicipio.setId(ifzMunicipios.save(pojoMunicipio, null));
			} else 
				ifzMunicipios.update(pojoMunicipio);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoMunicipio", pojoMunicipio);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GUARDAR_MUNICIPIO);
			respuesta.setBody(null);
			log.error("Error en LocalidadesFac.guardarMunicipio", e);
		}
		return respuesta;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
}
