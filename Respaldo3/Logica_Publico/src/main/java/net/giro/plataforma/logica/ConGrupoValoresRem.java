package net.giro.plataforma.logica;

import java.io.IOException;
import java.util.List;
import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.respuesta.Respuesta;

@Remote
public interface ConGrupoValoresRem {
	public Long save(ConGrupoValores entity);
	
	public void delete(ConGrupoValores entity) throws ExcepConstraint, RuntimeException, IOException;
	
	public ConGrupoValores update(ConGrupoValores entity);

	public ConGrupoValores findById(Long id);
	
	public Respuesta findByProperty(String propertyName,Object value);
	
	public List<ConGrupoValores> findLikeClaveNombre(Object value,int max);

	public List<ConGrupoValores> findAll();
	
	public void setInfoSesion(InfoSesion infoSesion);

	public ConGrupoValores findByName(String nombre);
}