package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.adp.beans.PresupuestoDetalle;

@Remote
public interface PresupuestoDetalleDAO extends DAO<PresupuestoDetalle> {
	
	public void delete(PresupuestoDetalle entity);

	public void update(PresupuestoDetalle entity);

	public PresupuestoDetalle findById(long id);

	public List<PresupuestoDetalle> findByProperty(String propertyName, final Object value, int max);

	public List<PresupuestoDetalle> findLikeProperty(String propertyName, final Object value, int max);
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoDetalleDAO