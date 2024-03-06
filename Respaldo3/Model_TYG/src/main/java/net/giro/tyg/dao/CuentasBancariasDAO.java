package net.giro.tyg.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CuentaBancaria;

@Remote
public interface CuentasBancariasDAO extends DAO<CuentaBancaria>{
    public long save(CuentaBancaria entity, Long idEmpresa) throws ExcepConstraint;
    
    public List<CuentaBancaria> saveOrUpdateList(List<CuentaBancaria> entities, Long idEmpresa) throws Exception;
	
	public void delete(CuentaBancaria entity) throws Exception;

	public CuentaBancaria findById(long id);

	public CuentaBancaria findAllById(long id);
	
	public List<CuentaBancaria> findAll(long idEmpresa, String orderBy, int limite) throws Exception;

	public List<CuentaBancaria> findAll(String empresas);
	
	public List<CuentaBancaria> findTodas();
	
	public List<CuentaBancaria> findAllByProperty(String propertyName, String value, int maximo, String empresas);
	
	public List<CuentaBancaria> findByProperty(String propertyName, Object value, String empresas);
	
	public List<CuentaBancaria> findLikeClaveNombreCuenta(String value, int max, String empresas, Long empresaId);
	
	public boolean esBancoCierre(final Object value);
}
