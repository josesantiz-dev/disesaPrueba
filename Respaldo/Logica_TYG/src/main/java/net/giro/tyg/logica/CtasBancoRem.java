package net.giro.tyg.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;

@Remote
public interface CtasBancoRem {
	public long save(CtasBanco entity) throws ExcepConstraint, RuntimeException;

	public void delete(CtasBanco entity);
	
	public CtasBanco update(CtasBanco entity) throws ExcepConstraint;

	public CtasBanco findById(long id);

	public CtasBanco findAllById(long id);

	public List<CtasBanco> findAllByProperty(String propertyName, String value, int maximo,String empresas);
	
	public List<CtasBanco> findByProperty(String propertyName, Object value,String empresas);
	
	public List<CtasBanco> findLikeColumnName(String tipoBusqueda, String valorBusqueda);
	
	public List<CtasBanco> findLikeClaveNombreCuenta(String value, int max, String empresas, Integer empresaId);
	
	public List<CtasBanco> findAll(String empresas);
	
	public List<CtasBanco> findTodas();
	
	public String aplicaCargo(Double monto, CtasBanco ctaBancaria);
	
	public boolean haySaldoSuficiente(double montoSolicitado, long bancoId);
	
	public boolean esBancoCierre(final Object value);
}

