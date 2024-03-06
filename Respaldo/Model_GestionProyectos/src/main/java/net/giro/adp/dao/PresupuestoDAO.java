package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO; 
import net.giro.adp.beans.Presupuesto;

@Remote
public interface PresupuestoDAO extends DAO<Presupuesto> {

	public void delete(Presupuesto entity);

	public void update(Presupuesto entity);

	public Presupuesto findById(long id);

	public List<Presupuesto> findByProperty(String propertyName, final Object value, int max);

	public List<Presupuesto> findLikeProperty(String propertyName, final Object value, int max);
	
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoDAO