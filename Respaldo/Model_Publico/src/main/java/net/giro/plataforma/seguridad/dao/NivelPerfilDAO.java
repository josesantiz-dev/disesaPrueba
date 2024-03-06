package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.NivelPerfil;

@Remote
public interface NivelPerfilDAO extends DAO<NivelPerfil> {

}
