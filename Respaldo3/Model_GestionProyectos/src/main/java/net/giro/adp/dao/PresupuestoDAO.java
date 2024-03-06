package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO; 
import net.giro.adp.beans.Presupuesto;

@Remote
public interface PresupuestoDAO extends DAO<Presupuesto> {
	public long save(Presupuesto entity, long codigoEmpresa) throws Exception;
	
	public List<Presupuesto> saveOrUpdateList(List<Presupuesto> entities, long codigoEmpresa) throws Exception;

	public Presupuesto findActual(long idObra, long idEmpresa);

	public List<Presupuesto> findAll(long idObra, long idEmpresa, int limite);

	public List<Presupuesto> findByProperty(String propertyName, final Object value, long idEmpresa, int limite);

	public List<Presupuesto> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite);
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoDAO