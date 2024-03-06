package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.CampoAutorizacion;

@Remote
public interface CampoAutorizacionDAO extends DAO<CampoAutorizacion> {

}
