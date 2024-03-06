package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Perfil;

@Remote
public interface PerfilDAO extends DAO<Perfil> {

}
