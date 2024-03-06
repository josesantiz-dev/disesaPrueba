package net.giro.bancos.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.bancos.beans.CuentaBancaria;

@Remote
public interface CuentaBancariaDAO extends DAO<CuentaBancaria> {

}
