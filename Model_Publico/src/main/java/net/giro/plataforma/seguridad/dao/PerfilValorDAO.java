package net.giro.plataforma.seguridad.dao;

import java.util.HashMap;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Perfil;
import net.giro.plataforma.seguridad.beans.PerfilValor;

@Remote
public interface PerfilValorDAO extends DAO<PerfilValor> {
	public PerfilValor findByPerfilNivelValor(Perfil pg, Long nivel, Long valorNivel) throws Exception;
	
	public PerfilValor findPerfil(Perfil pg, Long nivel) throws Exception;
	
	public String findPerfil(Perfil entity, HashMap<Integer, String> datosUsuario) throws Exception;
}
