package net.giro.cxc.dao;

import java.util.ArrayList;
import java.util.List;
import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturasRelacionadas;

@Stateless
public class FacturasRelacionadasImp extends DAOImpl<FacturasRelacionadas> implements FacturasRelacionadasDAO {

    private static final long serialVersionUID = 1L;
    
	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public long save(FacturasRelacionadas entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public void deleteFacturasRelacionadas(long idFactura) throws Exception {
		String queryString = "delete from FacturasRelacionadas model where model.idFactura = :idFactura ";
		
		try {			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idFactura", idFactura);
			query.executeUpdate();
		} catch (Exception re) {		
			throw re;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public List<FacturasRelacionadas> findFacturasById(long idFactura) throws Exception {
		String queryString = "select model from FacturasRelacionadas model where model.idFactura = :idFactura ";
		
		try {
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idFactura", idFactura);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		} 
	}
	

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------

	private List<String> recuperaValores(String valor, String separador) {
		List<String> valores = null;
		String[] splitted = null;
		
		if ((valor == null || "".equals(valor.trim())) || (separador == null || "".equals(separador.trim())) || (! valor.trim().contains(separador.trim().replace("\\", ""))))
			return null;
		
		splitted = valor.split(separador);
		valores = new ArrayList<String>();
		for (int i = 0; i < splitted.length; i++)
			valores.add(splitted[i].trim());
		return valores;
	}
	
	private String multiplicaConsulta(String queryOriginal, List<String> valores) {
		String queryModificada = "";
		
		if (valores == null || valores.isEmpty() || valores.size() == 1)
			return queryOriginal;
		
		for (String valor : valores)
			queryModificada += (! "".equals(queryModificada.trim()) ? " or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":value", "'%" + valor + "%'") + ")";
		return "select model from Factura model where (" + queryModificada.trim() + ") ";
	}
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}


}
