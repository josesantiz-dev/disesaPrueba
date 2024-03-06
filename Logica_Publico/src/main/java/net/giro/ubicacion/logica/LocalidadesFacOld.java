package net.giro.ubicacion.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;


@Stateless
public class LocalidadesFacOld implements LocalidadesRem {
	private static Logger log = Logger.getLogger(LocalidadesFacOld.class);
	private InitialContext ctx = null;
	@Resource
	private LocalidadDAO ifzLocalidades;
	private MunicipioDAO ifzMunicipios;
	private ConValoresDAO ifzConValores;
	private ConGrupoValoresDAO ifzConGrupoValores;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	
	public LocalidadesFacOld(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		
			ifzLocalidades = (LocalidadDAO)ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			
			ifzMunicipios = (MunicipioDAO)ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
			
			ifzConValores =  (ConValoresDAO)ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			
			ifzConGrupoValores =  (ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public long salvar(Localidad localidad) throws ExcepConstraint {
		if(localidad.getId() == 0){
			return ifzLocalidades.save(localidad, null);
		}else{
			ifzLocalidades.update(localidad);
		}
		return localidad.getId();
	}

	@Override
	public List<Localidad> saveOrUpdateList(List<Localidad> entities) throws Exception {
		try {
			return this.ifzLocalidades.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.LocalidadesFacOld.saveOrUpdateList(List<Localidad> entities)", e);
			throw e;
		}
	}

	@Override
	public void eliminar(Localidad localidad) throws ExcepConstraint {
		ifzLocalidades.delete(localidad.getId());
	}

	@Override
	public List<Localidad> buscarLocalidades(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint {
		return ifzLocalidades.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}

	@Override
	public List<ConValores> buscarZonas() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ZONA");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public List<Municipio> buscarMunicipios() throws ExcepConstraint {
		return ifzMunicipios.findAll();
	}

	@Override
	public List<ConValores> buscarGradoMarginalidad() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_MARGINALIDAD");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}
}
