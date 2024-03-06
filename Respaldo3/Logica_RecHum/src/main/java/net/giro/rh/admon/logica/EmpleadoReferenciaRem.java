package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoReferencia;
import net.giro.rh.admon.beans.EmpleadoReferenciaExt;

@Remote
public interface EmpleadoReferenciaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoReferencia entity) throws Exception;

	public void update(EmpleadoReferencia entity) throws Exception;
	
	public void delete(EmpleadoReferencia entity) throws Exception;

	public EmpleadoReferencia findById(Long id);
	
	public List<EmpleadoReferencia> findAll();

	public List<EmpleadoReferencia> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	// -------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// -------------------------------------------------------------------------------------------------------
	
	public Long save(EmpleadoReferenciaExt entityExt) throws Exception;
	
	public void update(EmpleadoReferenciaExt entityExt) throws Exception;
	
	public void delete(EmpleadoReferenciaExt entityExt) throws Exception;
	
	public List<EmpleadoReferenciaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
