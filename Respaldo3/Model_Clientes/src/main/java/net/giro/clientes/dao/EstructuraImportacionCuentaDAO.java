package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.EstructuraImportacionCuenta;

@Remote
public interface EstructuraImportacionCuentaDAO extends DAO<EstructuraImportacionCuenta> {

}
