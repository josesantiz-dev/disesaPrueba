package net.giro.cxp.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.ProgPagosExt;
import net.giro.cxp.beans.SucursalExt;

@Remote
public interface ProgPagosRem {
	public Long save(ProgPagosExt entity) throws Exception;
	
	public void delete(ProgPagosExt entity) throws ExcepConstraint;
	
	public ProgPagosExt update(ProgPagosExt entity) throws Exception;

	public ProgPagosExt findById(Long id) throws Exception;
	
	public ProgPagosExt findByIdPojoCompleto(long id) throws Exception;
	
	public List<ProgPagosExt> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<ProgPagosExt> findByRevisiones(Date fech1, Date fech2, int max) throws Exception;
	
	public List<ProgPagosExt> findByProgramaciones(long value, int max) throws Exception;

	public List<ProgPagosExt> findAll() throws Exception;
	
	public SucursalExt findSucursalById(long id);
	
	public PersonaExt findPersonaById(long id);
	
	public PersonaExt findPersonaById(long id, String tipoPersona);
}
