package net.giro.tyg.dao;

import java.util.Date;
import java.util.List;

import net.giro.DAO;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Cheques;

import javax.ejb.Remote;


@Remote
public interface ChequesDAO extends DAO<Cheques> {
	public long save(Cheques entity);

	public void delete(Cheques entity);

	public void update(Cheques entity);

	public Cheques findById(Integer id);

	public List<Cheques> findByProperty(String propertyName, Object value);
	
	public List<Cheques> findByFolioCtaCheque(String folio,String ctaCheques)throws ExcepConstraint, RuntimeException;

	public List<Cheques> findAll();
	
	public Cheques findChequeCompleto(String noCheque,Short ctaBancariaa,String tipo,String estatus);
	
	public  List<Cheques> findChequeConContrato(String control, String folioCheque,String banco,Date fecha,String estatus, Double importe);
}
