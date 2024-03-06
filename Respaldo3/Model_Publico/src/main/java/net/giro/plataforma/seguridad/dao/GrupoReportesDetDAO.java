package net.giro.plataforma.seguridad.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.seguridad.beans.GrupoReportesDet;

@Remote
public interface GrupoReportesDetDAO extends DAO<GrupoReportesDet> {

}
