package net.giro.co.dao;
import java.util.List;


import javax.ejb.Local;

import net.giro.co.beans.Correlativo;
import net.giro.co.beans.CorrelativoId;


@Local
public interface CorrelativoInterfaceLocal {
	public void save(Correlativo transientInstance);
	public void delete(Correlativo persistentInstance);
	public Correlativo update(Correlativo detachedInstance);
	public Correlativo findById(CorrelativoId id);
	public List<Correlativo> findByProperty(String propertyName, Object value); 
	public List<Correlativo> findByNumeroCorrelativo(Object numeroCorrelativo); 
	public List<Correlativo> findByDescriTabla(Object descriTabla); 
	public List<Correlativo> findByUsuarioInsercion(Object usuarioInsercion);
	public List<Correlativo> findByTerminalInsercion(Object terminalInsercion);
	public List<Correlativo> findByIpInsercion(Object ipInsercion);
	public List<Correlativo> findByUsuarioModificacion(Object usuarioModificacion); 
	public List<Correlativo> findByTerminalModificacion(Object terminalModificacion);
	public List<Correlativo> findByIpModificacion(Object ipModificacion);
	public List<Correlativo> findAll();
	public Long GenerarCorrelativo(Long strCodEmpr,String strNombTab);	
	public String GenerarCorrelativoBatch(Long strCodEmpr,String strNombTab );	
}
