package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.Auditoria;

@Remote
public interface AuditoriaDAO extends DAO<Auditoria> {

}
