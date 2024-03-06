package net.giro.co.dao;


import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.giro.co.beans.Correlativo;
import net.giro.co.beans.CorrelativoId;
import net.giro.comun.util.TextUtil;




@Stateless
public class CorrelativoBloqueoSession implements CorrelativoBloqueoInterfaceLocal{
	
	//@EJB
	//Correlativo correlativo = null;
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public synchronized 
	Long GenerarCorrelativo( Long strCodEmpr, String strNombTab){	

		int numeroDigitos = 10;
		Long correlativoSec = new Long(0);

		String strCorrelativosdeCeros = null;
		String strCorrelativo = "";
		CorrelativoId cbCorrelativoId=null;
		Correlativo cbCorrelativo=null;
		Long numeroCorrelativo=null;
		Long correlativo=new Long(0);
		
		//HibernateEntityManager hem = entityManager.unwrap(HibernateEntityManager.class);
		try{
		
			 cbCorrelativoId = new CorrelativoId(strCodEmpr,strNombTab); 
			 cbCorrelativo =entityManager.find(Correlativo.class, cbCorrelativoId); 
			 //System.out.println("========================================================================="+cbCorrelativo);
			 //List obj = entityManager.createNativeQuery("select N_NUMCOR from cbtgenc where C_CODEMP="+strCodEmpr.toString()+" and C_NOMTAB='"+strNombTab+"'").getResultList();	
			 if(cbCorrelativo == null){
				cbCorrelativo = new Correlativo();
				cbCorrelativo.setId(new CorrelativoId(strCodEmpr,strNombTab));
				cbCorrelativo.setCbEmpresa(1);
				cbCorrelativo.setNumeroCorrelativo(new Long(1));
				entityManager.merge(cbCorrelativo);
			 }else
				 correlativo = cbCorrelativo.getNumeroCorrelativo();
			
			cbCorrelativo.setNumeroCorrelativo(correlativo + 1);
			numeroCorrelativo=cbCorrelativo.getNumeroCorrelativo();
			strCorrelativo=numeroCorrelativo.toString();
			
			 		if (!"".equals(strCodEmpr) || strNombTab != null){
			            strCorrelativosdeCeros = (TextUtil.llenaCeros("0", numeroDigitos - 3) + strCorrelativo).substring(strCorrelativo.length(),strCorrelativo.length()+ numeroDigitos- 3);
			 			strCorrelativo = strCodEmpr + strCorrelativosdeCeros;
			        }
		}catch(Exception ex){
			ex.printStackTrace();
		}

		correlativoSec = new Long(strCorrelativo);
		return correlativoSec;

	}

	
}
