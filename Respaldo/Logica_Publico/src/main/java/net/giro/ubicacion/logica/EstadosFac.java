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
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConGrupoValoresDAO;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.dao.EstadoDAO;


@Stateless
public class EstadosFac implements EstadosRem {
	private static Logger log = Logger.getLogger(EstadosFac.class);
	private InitialContext ctx = null;
	
	@Resource
	private EstadoDAO ifzEstado;
	private ConValoresDAO ifzConValores;
	private ConGrupoValoresDAO ifzConGrupoValores;
	
	public EstadosFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);
		
			ifzEstado = (EstadoDAO)ctx.lookup("ejb:/Model_Publico//EstadoImp!net.giro.plataforma.ubicacion.dao.EstadoDAO");
			
			ifzConValores =  (ConValoresDAO)ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
			
			ifzConGrupoValores =  (ConGrupoValoresDAO)ctx.lookup("ejb:/Model_Publico//ConGrupoValoresImp!net.giro.plataforma.dao.ConGrupoValoresDAO");
		} catch(Exception e) {
    		log.error("Error en el metodo contexto , no se pudo crear ", e);
    		ctx = null;
    	}
	}

	@Override
	public List<Estado> buscarEstados(String tipoBusqueda, String valorBusqueda) throws ExcepConstraint {
		return ifzEstado.findLikeColumnName(tipoBusqueda, valorBusqueda);
	}

	@Override
	public List<ConValores> buscarZonasEconomicas() throws ExcepConstraint {
		List<ConValores> listaConValores = new ArrayList<ConValores>();
		List<ConValores> listaTmpConValores = new ArrayList<ConValores>();
		List<ConGrupoValores> listaConGrupoValores = ifzConGrupoValores.findByColumnName("nombre","SYS_ZONAECONOMICA");
		for(ConGrupoValores cGV : listaConGrupoValores){
			listaTmpConValores = ifzConValores.findByColumnName("grupoValorId", cGV);
			for(ConValores cv : listaTmpConValores){
				listaConValores.add(cv);
			}
		}
		return listaConValores;
	}

	@Override
	public long salvar(Estado estado) throws ExcepConstraint {
		if(estado.getId() == 0){
			return ifzEstado.save(estado);
		}else{
			ifzEstado.update(estado);
		}
		return estado.getId();
	}

	@Override
	public void eliminar(Estado estado) throws ExcepConstraint {
		ifzEstado.delete(estado.getId());
	}
}
