package net.giro.plataforma.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.beans.ExpVariable;

@Remote
public interface ExpVariableDAO extends DAO<ExpVariable> {

}
