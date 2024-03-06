package net.giro.ubicacion.logica;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;
import net.giro.plataforma.ubicacion.dao.MunicipioDAO;


@Stateless
public class MunicipiosFac implements MunicipiosRem {
	private static Logger log = Logger.getLogger(MunicipiosFac.class);
	private InitialContext ctx = null;
	
	@Resource
	private MunicipioDAO ifzMunicipios;
	private EstadoDAO ifzEstado;
	
	public MunicipiosFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		
			ifzMunicipios = (MunicipioDAO)ctx.lookup("ejb:/Model_Publico//MunicipioImp!net.giro.plataforma.ubicacion.dao.MunicipioDAO");
			
			ifzEstado = (EstadoDAO)ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}
	}

	@Override
	public List<Municipio> buscarMunicipios(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint {
		return ifzMunicipios.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}

	@Override
	public List<Estado> buscarEstados() throws ExcepConstraint {
		return ifzEstado.findAll();
	}

	@Override
	public long salvar(Municipio municipio) throws ExcepConstraint {
		if(municipio.getId() == 0){
			return ifzMunicipios.save(municipio);
		}else{
			ifzMunicipios.update(municipio);
		}
		return municipio.getId();
	}

	@Override
	public void eliminar(Municipio municipio) throws ExcepConstraint {
		ifzMunicipios.delete(municipio.getId());
	}
}
