package net.giro.compras.logica;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.inventarios.beans.MovimientosDetalle;


@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/INVENTARIOS")
	}, 
	mappedName = "topic/INVENTARIOS")
public class InventariosTopic implements MessageListener {
	private static Logger log = Logger.getLogger(InventariosTopic.class);
	
	private InitialContext ctx;
	private OrdenCompraDetalleRem ifzCompraDetalle;
	private OrdenCompraRem ifzCompra;
	
	@SuppressWarnings("unused")
	private final String objeto = "compras";
	@SuppressWarnings("unused")
	private final String BACK_ORDER = "backorder";	//para hacer las comparaciones sin problemas de escritura
	
	public InventariosTopic() throws NamingException,Exception {			//Cola para escuchar desde compras
		String ejbName;

		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem";
		this.ifzCompraDetalle = (OrdenCompraDetalleRem) ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem";
		this.ifzCompra = (OrdenCompraRem) ctx.lookup(ejbName);
		
		log.info("Listener hacia inventarios creado satisfactoriamente");
	}
	
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
    	try {
	    	if (message instanceof TextMessage) {
				TextMessage mensaje = (TextMessage) message;
				Gson gson = new Gson();
				
				//String textBase = "{ objeto: 'compras', evento: backorder, id: '"+ String.valueOf( idOrdenCompra ) +"', atributos: 'null' }";
				
				log.info("****************************** Logica Compras: Evento recibido: , "+new Date());

				HashMap<String,Object> hm = new HashMap<String,Object>();				
				hm = gson.fromJson(mensaje.getText(), HashMap.class);
				
				Type tipo = new TypeToken<List<MovimientosDetalle>>() {}.getType();
				List<MovimientosDetalle> listaEntrada = new Gson().fromJson(hm.get("atributos").toString(), tipo);
				
				long idOrdenCompra = Long.valueOf(hm.get("id").toString());
				log.info("Recibido correctamente, idOrdenCompra: "+idOrdenCompra);
				
				//recorrer la lista y de ahí, actualizar el detalle de la compra
				actualizaBackOrder(idOrdenCompra,listaEntrada );
			}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private void actualizaBackOrder(long idOrdenCompra, List<MovimientosDetalle> listaEntrada) {
		try {
			List<OrdenCompraDetalle> listaDetalles = this.ifzCompraDetalle.findByProperty("idOrdenCompra", idOrdenCompra, 1000);
			log.info("Cantidad detalles de la lista: "+listaDetalles.size());
			
			for(OrdenCompraDetalle var: listaDetalles) {
				double cantidad = getCantidad(var.getIdProducto(), listaEntrada);
				log.info("Actualizar OC: Cantidad -> "+var.getCantidadRecibida() + ", Sumar: "+cantidad);
				var.setCantidadRecibida( var.getCantidadRecibida() + cantidad );
				this.ifzCompraDetalle.update(var);
			}
			
			//Una vez se actualizó, verificar si la compra ya esta completa:
			//	Consultar nuevamente los detalles
			listaDetalles = this.ifzCompraDetalle.findByProperty("idOrdenCompra", idOrdenCompra, 1000);
			
			boolean completa = true;
			
			for( OrdenCompraDetalle var: listaDetalles ){
				if (  var.getCantidad() != var.getCantidadRecibida() ){
					completa = false;	//si por lo menos un solo detalle que sea diferente la cantidad a la recibida, aun no esta completa
					break;
				}
			}
			
			if( completa ){
				OrdenCompra compra  = ifzCompra.findById(idOrdenCompra);
				compra.setCompleta(1);
				this.ifzCompra.update(compra);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double getCantidad(Long idProducto, List<MovimientosDetalle> listaEntrada){
		for(MovimientosDetalle var: listaEntrada){
			if(idProducto.equals( var.getIdProducto())){
				log.info("Producto Encontrado: "+idProducto+", cant: "+var.getCantidad());
				return var.getCantidad();
			}
		}
		
		return 0;
	}
}
