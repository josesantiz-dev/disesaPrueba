package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.AuditoriaDetalle;

@Remote
public interface AuditoriaDetalleDAO extends DAO<AuditoriaDetalle> {

}
