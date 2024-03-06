package net.giro.plataforma.seguridad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Perfil;

@Remote
public interface PerfilesRem {
	public long save(Perfil entity) throws ExcepConstraint;

    public void update(Perfil entity)  throws ExcepConstraint;

    public void delete(long id) throws ExcepConstraint;

    public Perfil findById(long id);

    public List<Perfil> findAll();
	
	public List<Perfil> findByProperty(String columnName, Object value);
}
