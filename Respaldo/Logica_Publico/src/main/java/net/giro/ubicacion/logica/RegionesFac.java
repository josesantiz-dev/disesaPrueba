package net.giro.ubicacion.logica;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Regiones;
import net.giro.ne.dao.RegionesDAO;


@Stateless
public class RegionesFac implements RegionesRem {
	private static Logger log = Logger.getLogger(RegionesFac.class);
	private InitialContext ctx = null;
	
	@Resource
	private RegionesDAO ifzRegiones;
	
	public RegionesFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		
			ifzRegiones = (RegionesDAO)ctx.lookup("ejb:/Model_Publico//RegionesImp!net.giro.ne.dao.RegionesDAO");
			
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}
	}

	@Override
	public List<Regiones> buscarRegiones(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint {
		return ifzRegiones.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}

	@Override
	public long salvar(Regiones regiones) throws ExcepConstraint {
		if(regiones.getId() == 0){
			return ifzRegiones.save(regiones);
		}else{
			ifzRegiones.update(regiones);
		}
		return regiones.getId();
	}

	@Override
	public void eliminar(Regiones regiones) throws ExcepConstraint {
		ifzRegiones.delete(regiones.getId());
	}
}
