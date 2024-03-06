package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.GrupoReportes;

@Remote
public interface GrupoReportesDAO extends DAO<GrupoReportes> {

}
