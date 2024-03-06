package net.giro.bancos.dao;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.bancos.beans.Banco;

@Remote
public interface BancoDAO extends DAO<Banco> {

}
