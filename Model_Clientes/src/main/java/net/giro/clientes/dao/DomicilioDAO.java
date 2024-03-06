package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.Negocio;

@Remote
public interface DomicilioDAO extends DAO<Domicilio> {
	public List<Domicilio> findPrincipalByNegocio(Negocio pojoNegocio) throws Exception;
}
