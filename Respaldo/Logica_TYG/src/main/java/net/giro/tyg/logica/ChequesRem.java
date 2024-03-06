package net.giro.tyg.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.Cheques;

@Remote
public interface ChequesRem {
	public long save(Cheques entity);

	public void delete(Cheques entity);

	public Cheques update(Cheques entity);

	public Cheques findById(Integer id);

	public List<Cheques> findByProperty(String propertyName, Object value);
	
	public Cheques findByFolioCtaCheque(String folio,String ctaCheques)throws ExcepConstraint, RuntimeException;

	public List<Cheques> findAll();
	
	public Cheques findChequeCompleto(String noCheque,Short ctaBancariaa,String tipo,String estatus);
	
	public  List<Cheques> findChequeConContrato(String control, String folioCheque,String banco,Date fecha,String estatus, Double importe);
}
