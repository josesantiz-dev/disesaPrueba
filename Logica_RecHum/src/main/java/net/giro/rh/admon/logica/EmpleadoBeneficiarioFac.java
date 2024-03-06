package net.giro.rh.admon.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoBeneficiario;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;
import net.giro.rh.admon.dao.EmpleadoBeneficiarioDAO;

@Stateless
public class EmpleadoBeneficiarioFac implements EmpleadoBeneficiarioRem {
	private static Logger log = Logger.getLogger(EmpleadoBeneficiarioFac.class);
	private InitialContext ctx;
	private EmpleadoBeneficiarioDAO ifzEmpleadoBeneficiario;
	private ConvertExt  convertidor;
	private InfoSesion infoSesion;
	
	public EmpleadoBeneficiarioFac(){
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_RecHum//EmpleadoBeneficiarioImp!net.giro.rh.admon.dao.EmpleadoBeneficiarioDAO";
            this.ifzEmpleadoBeneficiario = (EmpleadoBeneficiarioDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear EmpleadoBeneficiarioFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	public Long save(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoBeneficiario.save(entity, getCodigoEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<EmpleadoBeneficiario> saveOrUpdateList(List<EmpleadoBeneficiario> entities) throws Exception {
		try {
			return this.ifzEmpleadoBeneficiario.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoBeneficiarioFac.saveOrUpdateList(List<EmpleadoBeneficiario> entities)", e);
			throw e;
		}
	}


	public Long save(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoBeneficiario.delete(entity);;
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public void delete(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			EmpleadoBeneficiario entity = this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt);
			this.ifzEmpleadoBeneficiario.delete(entity.getId());
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public void update(EmpleadoBeneficiario entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoBeneficiario.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public void update(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint {
		try {
			this.ifzEmpleadoBeneficiario.update(this.convertidor.EmpBeneficiarioExtToEmpBeneficiario(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public EmpleadoBeneficiario findById(Long id) {
		try {
			return this.ifzEmpleadoBeneficiario.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	public List<EmpleadoBeneficiario> findByProperty(String propertyName, final Object value) {
		try {
			return this.ifzEmpleadoBeneficiario.findByProperty(propertyName, value, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	public List<EmpleadoBeneficiario> findAll() {
		try {
			return this.ifzEmpleadoBeneficiario.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	public List<EmpleadoBeneficiarioExt> findByIdEmpleado(long idEmpleado) {
		List<EmpleadoBeneficiarioExt> listaExt = new ArrayList<>();
		try { 
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByIdEmpleado(idEmpleado, getIdEmpresa());
			for(EmpleadoBeneficiario eb:lista){
				listaExt.add(  this.convertidor.EmpBeneficiarioToEmpBeneficiarioExt(eb)  );
			}
		} catch (Exception re) {		
			throw re;
		}
		return listaExt;
	}
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		try{
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
			return lista;
		}catch(Exception re){
			throw re;
		}
	}
	
	public List<EmpleadoBeneficiarioExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value){
		List<EmpleadoBeneficiarioExt> listaExt = new ArrayList<EmpleadoBeneficiarioExt>();
		
		try{
			List<EmpleadoBeneficiario> lista = this.ifzEmpleadoBeneficiario.findByPropertyPojoCompleto(propertyName, tipo, value, getIdEmpresa());
			
			for (EmpleadoBeneficiario empleadoBeneficiario : lista) {
				EmpleadoBeneficiarioExt pojoAux = this.convertidor.EmpBeneficiarioToEmpBeneficiarioExt(empleadoBeneficiario);
				if ("".equals(tipo))
					listaExt.add(pojoAux);
				else
					System.out.println("No se agregó este item a la lista: "+empleadoBeneficiario.getId() );
			}
			System.out.println("---> LOGICA :: Lista Extendida :: " + listaExt.size());
		}catch(Exception re){
			throw re;
		}

		return listaExt;
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}
