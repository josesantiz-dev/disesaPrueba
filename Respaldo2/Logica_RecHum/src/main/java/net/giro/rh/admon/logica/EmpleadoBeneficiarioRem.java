package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoBeneficiario;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;

@Remote
public interface EmpleadoBeneficiarioRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoBeneficiario entity) throws Exception;
	
	public List<EmpleadoBeneficiario> saveOrUpdateList(List<EmpleadoBeneficiario> entities) throws Exception;

	public void update(EmpleadoBeneficiario entity) throws Exception;

	public void delete(EmpleadoBeneficiario entity) throws Exception;

	public EmpleadoBeneficiario findById(Long id);

	public List<EmpleadoBeneficiario> findAll();
	
	public List<EmpleadoBeneficiarioExt> findByIdEmpleado(long idEmpleado);

	public List<EmpleadoBeneficiario> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------

	public Long save(EmpleadoBeneficiarioExt entityExt) throws Exception;
	
	public void update(EmpleadoBeneficiarioExt entity) throws Exception;
	
	public void delete(EmpleadoBeneficiarioExt entityExt) throws Exception;
}
