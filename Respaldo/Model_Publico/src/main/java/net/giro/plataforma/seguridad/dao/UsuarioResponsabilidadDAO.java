package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;

@Remote
public interface UsuarioResponsabilidadDAO extends DAO<UsuarioResponsabilidad> {

}
