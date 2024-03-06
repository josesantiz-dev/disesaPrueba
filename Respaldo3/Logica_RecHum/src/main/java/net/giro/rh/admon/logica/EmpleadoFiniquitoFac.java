package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.dao.EmpleadoFiniquitoDAO;

@Stateless
public class EmpleadoFiniquitoFac implements EmpleadoFiniquitoRem {
	private static Logger log = Logger.getLogger(EmpleadoFiniquitoFac.class);
	private InitialContext ctx;
	private EmpleadoFiniquitoDAO ifzEmpleadoFiniquito;
	private ConvertExt convertidor;
	private InfoSesion infoSesion;
	
	
	public EmpleadoFiniquitoFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzEmpleadoFiniquito = (EmpleadoFiniquitoDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoFiniquitoImp!net.giro.rh.admon.dao.EmpleadoFiniquitoDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear EmpleadoFiniquitoFac", e);
			ctx = null;
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(EmpleadoFiniquito entity) throws Exception {
		try {
			return this.ifzEmpleadoFiniquito.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoFiniquito> saveOrUpdateList(List<EmpleadoFiniquito> entities) throws Exception {
		try {
			return this.ifzEmpleadoFiniquito.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			log.error("Error en EmpleadoFiniquitoFac.saveOrUpdateList(List<EmpleadoFiniquito> entities)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void update(EmpleadoFiniquito entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.update(entity);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cancelar(EmpleadoFiniquito entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(entity);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(EmpleadoFiniquito entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public EmpleadoFiniquito finiquitar(EmpleadoFiniquito entity) throws Exception {
		try {
			if (entity.getEstatus() == 0 && entity.getAprobacion() == 1) {
				entity.setFechaModificacion(Calendar.getInstance().getTime());
				entity.setEstatus(2);
			}
			
			this.ifzEmpleadoFiniquito.update(entity);
			boFiniquito(entity.getIdEmpleado().getId(), entity.getId());
		} catch (Exception re) {
			throw re;
		} 
		
		return entity;
	}

	@Override
	public EmpleadoFiniquito findById(Long id) {
		try {
			return this.ifzEmpleadoFiniquito.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado) {
		try {
			return this.findByIdEmpleado(idEmpleado, 0L, false, false);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato) {
		try {
			return this.ifzEmpleadoFiniquito.findByIdEmpleado(idEmpleado, idContrato, false, false);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado) {
		try {
			return this.findByIdEmpleado(idEmpleado, 0L, incluyeAprobados, incluyeCancelado);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato, boolean incluyeAprobados, boolean incluyeCancelado) {
		try {
			return this.ifzEmpleadoFiniquito.findByIdEmpleado(idEmpleado, idContrato, incluyeAprobados, incluyeCancelado);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findAll() {
		try {
			return this.ifzEmpleadoFiniquito.findAll(getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado) {
		try {
			return this.findFiniquitosByEmpleado(idEmpleado, false, false);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado) {
		try {
			return this.ifzEmpleadoFiniquito.findFiniquitosByEmpleado(idEmpleado, incluyeAprobados, incluyeCancelado);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado) {
		try{
			return this.ifzEmpleadoFiniquito.findByEmpleado(nombreEmpleado, getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoFiniquito.findByProperty(propertyName, value, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public boolean comprobarFiniquitos(long idEmpleado) {
		List<EmpleadoFiniquito> finiquitos = null;
		
		try {
			finiquitos = this.findFiniquitosByEmpleado(idEmpleado);
			if (finiquitos != null && ! finiquitos.isEmpty()) {
				for (EmpleadoFiniquito item : finiquitos) {
					if (item.getEstatus() == 1)
						continue;
					if (item.getEstatus() == 2)
						continue;
					return true;
				}
			}
		} catch (Exception re) {
			throw re;
		}
		
		return false;
	}

	// -----------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------

	@Override
	public Long save(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			return this.save(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		} finally {
			
		}
	}

	@Override
	public void delete(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			this.ifzEmpleadoFiniquito.delete(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(EmpleadoFiniquitoExt entity) throws Exception {
		try {
			this.update(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entity));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquitoExt finiquitarExt(EmpleadoFiniquitoExt entityExt) throws Exception {
		EmpleadoFiniquito entity = null;
		
		try {
			entity = this.finiquitar(this.convertidor.EmpleadoFiniquitoExtToEmpleadoFiniquito(entityExt));
			entityExt = this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(entity);
		} catch (Exception re) {	
			throw re;
		}
		
		return entityExt;
	}

	@Override
	public EmpleadoFiniquitoExt findByIdExt(Long id) {
		try {
			return this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(this.findById(id));
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public EmpleadoFiniquitoExt findByIdEmpleadoExt(long idEmpleado) {
		return this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(this.findByIdEmpleado(idEmpleado));
	}

	@Override
	public List<EmpleadoFiniquitoExt> findAllExt() {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<EmpleadoFiniquitoExt>();
		List<EmpleadoFiniquito> lista = null;
		
		try {
			lista = this.findAll();
			for (EmpleadoFiniquito ef : lista)
				listaExt.add(this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef));
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoFiniquitoExt> findFiniquitosByEmpleadoExt(long idEmpleado) {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<EmpleadoFiniquitoExt>();
		List<EmpleadoFiniquito> lista = null;
		
		try {
			lista = this.findFiniquitosByEmpleado(idEmpleado);
			for (EmpleadoFiniquito ef : lista)
				listaExt.add(this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef));
		} catch (Exception re) {		
			throw re;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado) {
		List<EmpleadoFiniquitoExt> listaExt = new ArrayList<EmpleadoFiniquitoExt>();
		try{
			List<EmpleadoFiniquito> lista = new ArrayList<EmpleadoFiniquito>();
			lista = this.findByEmpleado(nombreEmpleado);
			for(EmpleadoFiniquito ef: lista){
				listaExt.add( this.convertidor.EmpleadoFiniquitoToEmpleadoFiniquitoExt(ef) );
			}
		}catch (Exception re) {		
			throw re;
		}
		return listaExt;
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------
	
	private void boFiniquito(Long idEmpleado, Long idFiniquito) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idEmpleado == null)
				idEmpleado = 0L;
			if (idFiniquito == null)
				idFiniquito = 0L;
			target = idEmpleado.toString();
			referencia = idFiniquito.toString();
			msgTopic = new MensajeTopic(TopicEventosRH.FINIQUITO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-CO\n\n" + comando + "\n\n", e);
		}
	}

	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
