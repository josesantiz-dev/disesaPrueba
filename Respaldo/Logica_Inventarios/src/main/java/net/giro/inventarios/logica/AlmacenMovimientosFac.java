package net.giro.inventarios.logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.dao.AlmacenMovimientosDAO;

@Stateless
public class AlmacenMovimientosFac  implements AlmacenMovimientosRem {

	private static Logger log = Logger.getLogger(AlmacenMovimientosFac.class);
	
	InitialContext ctx;
	private ConvertExt convertidor;
	private AlmacenMovimientosDAO ifzAlmacenMovimientos;
	
	public AlmacenMovimientosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(p);
            
            String ejbName= "ejb:/Model_Inventarios//AlmacenMovimientosImp!net.giro.inventarios.dao.AlmacenMovimientosDAO";
            this.ifzAlmacenMovimientos = (AlmacenMovimientosDAO) ctx.lookup(ejbName);
            
            this.convertidor = new ConvertExt(); 
            
		}catch(Exception e){
			log.error("Error en el método contexto, no se pudo crear Logica Fac", e);
			ctx = null;
		}
	}

	
	public void salida()  throws JMSException, NamingException{
		 String textBase = " nombre = " + "DANIEL AZAMAR" + "\n correo = " + "DAZAMAR01";
		 System.out.println(textBase);
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		Connection conn = qcf.createQueueConnection();

		Topic topicA = (Topic) iniCtx.lookup("topic/INVENTARIOS");
		Session session = conn.createSession(false,
		QueueSession.AUTO_ACKNOWLEDGE);
		conn.start();

		MessageProducer sendtopic = session.createProducer(topicA);
		TextMessage tm = session.createTextMessage(textBase);

		sendtopic.send(tm);			// aqui traia un simbolo raro
		conn.stop();
		session.close();
		conn.close();
		
		log.info("logica: escribiendo en la cola...");
		
	}
	
	public Long save(AlmacenMovimientos entity) throws ExcepConstraint {
		try {
			
			return this.ifzAlmacenMovimientos.save(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public Long save(AlmacenMovimientosExt entityExt) throws ExcepConstraint {
		/*
		try{
			salida();
		}catch(Exception e){
			
		}*/
		
		try {
			return this.ifzAlmacenMovimientos.save( this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(AlmacenMovimientos entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenMovimientos.delete(entity);;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public void delete(AlmacenMovimientosExt entityExt) throws ExcepConstraint {
		try {
			this.ifzAlmacenMovimientos.delete( this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt) );;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public AlmacenMovimientos update(AlmacenMovimientos entity) throws ExcepConstraint {
		try {
			this.ifzAlmacenMovimientos.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public AlmacenMovimientos update(AlmacenMovimientosExt entityExt) throws ExcepConstraint {
		try {
			AlmacenMovimientos entity = this.convertidor.AlmacenMovimientosExtToAlmacenMovimientos(entityExt);
			this.ifzAlmacenMovimientos.update(entity);
			return entity;
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public AlmacenMovimientos findById(Long id) {
		try {
			return this.ifzAlmacenMovimientos.findById(id);
		} catch (RuntimeException re) {	
			throw re;
		}
	}
	
	public AlmacenMovimientosExt findByIdExt(Long id) {
		try {
			return this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt( this.ifzAlmacenMovimientos.findById(id) );
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value) {
		try {
			return this.ifzAlmacenMovimientos.findByProperty(propertyName, value);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenMovimientos> findByEspecificField(String propertyName, Object value, int tipoMovimiento) {
		try {
			return this.ifzAlmacenMovimientos.findByEspecificField(propertyName, value, tipoMovimiento);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<AlmacenMovimientosExt> findExtByEspecificField(String propertyName, Object value, int tipoMovimiento ) {
		try {
			List<AlmacenMovimientos> lista = this.ifzAlmacenMovimientos.findByEspecificField(propertyName, value, tipoMovimiento);
			List<AlmacenMovimientosExt> listaExt = new ArrayList<>();
			for(AlmacenMovimientos am:lista){
				listaExt.add(this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am));
			}
			return listaExt;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<AlmacenMovimientos> findAll() {
		try {
			return this.ifzAlmacenMovimientos.findAll();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AlmacenMovimientosExt> findAllExt() {
		try {
			List<AlmacenMovimientos> lista = this.ifzAlmacenMovimientos.findAll();
			List<AlmacenMovimientosExt> listaExt = new ArrayList<>();
			
			for(AlmacenMovimientos am: lista){
				listaExt.add( this.convertidor.AlmacenMovimientosToAlmacenMovimientosExt(am) );
			}
			
			return listaExt;
		} catch (RuntimeException re) {		
			throw re;
		}
	}
	
	public List<AlmacenMovimientos> findAllActivos() {
		try {
			return this.ifzAlmacenMovimientos.findAllActivos();
		} catch (RuntimeException re) {		
			throw re;
		}
	}
}
