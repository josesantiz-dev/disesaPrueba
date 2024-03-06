package net.giro.clientes.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaDocumento;

@Remote
public interface PersonaDocumentoDAO extends DAO<PersonaDocumento> {

}
