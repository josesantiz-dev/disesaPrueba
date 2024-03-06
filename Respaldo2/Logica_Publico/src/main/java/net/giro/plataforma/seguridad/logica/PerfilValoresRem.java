package net.giro.plataforma.seguridad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;

@Remote
public interface PerfilValoresRem {
	public long save(PerfilValor entity) throws ExcepConstraint;

    public void update(PerfilValor entity)  throws ExcepConstraint;

    public void delete(long id) throws ExcepConstraint;

    public PerfilValor findById(long id);

    public List<PerfilValor> findAll();
	
	public List<PerfilValor> findByProperty(String columnName, Object value);
	
	public PerfilValor findByPerfilNivelValor(Perfil pg, Long nivel, Long valorNivel) throws Exception;
	
	public String findPerfil(Perfil entity, HashMap<Integer, String> datosUsuario) throws Exception;
}
