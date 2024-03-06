package net.giro.bancos.dao;

import javax.ejb.Stateless;

import net.giro.DAOImpl;
import net.giro.bancos.beans.Banco;

@Stateless
public class BancoImp extends DAOImpl<Banco> implements BancoDAO  {

}
