package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.FormaDePago;

@Remote
public interface FormaDePagoDAO extends DAO<FormaDePago> {

}
