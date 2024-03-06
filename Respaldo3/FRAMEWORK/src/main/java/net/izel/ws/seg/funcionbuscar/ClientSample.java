package net.izel.ws.seg.funcionbuscar;

//import net.izel.ws.seg.funcionbuscar.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        FuncionBuscarIfzService service1 = new FuncionBuscarIfzService();
	        System.out.println("Create Web Service...");
	        FuncionBuscarIfz port1 = service1.getFuncionBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("Create Web Service...");
	        FuncionBuscarIfz port2 = service1.getFuncionBuscarIfzPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.procesar(null));
	        //Please input the parameters instead of 'null' for the upper method!
	
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
