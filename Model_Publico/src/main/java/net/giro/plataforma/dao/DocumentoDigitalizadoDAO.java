package net.giro.plataforma.dao;

import net.giro.DAO;
import javax.ejb.Remote;

import net.giro.plataforma.beans.DocumentoDigitalizado;

@Remote
public interface DocumentoDigitalizadoDAO extends DAO<DocumentoDigitalizado> {

}
