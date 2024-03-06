/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package net.giro;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.*;

/**
 * Permite a la vista interactuar con el bean que procesa el login, obtiene los
 * resultados obtenidos en el proceso y los expone para que puedan obtenerse desde
 * la vista, ademas, recolecta los niveles de informacion por parte del usuario conforme
 * vaya accediendo �ste a la aplicacion.
 *
 */

public class ModuloManager implements Serializable {
	private static Logger log = Logger.getLogger(ModuloManager.class); 
	private static final long serialVersionUID = 1L;
	//private PropertyResourceBundle prop;
    private List<Modulo> lstModulos;
	private Modulo modulo;

	
    public ModuloManager() {
        /*FacesContext facesContext = null;
        Application app = null;
        ValueExpression dato = null;
        
        facesContext =  FacesContext.getCurrentInstance();
        app = facesContext.getApplication();
        dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
		this.prop = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());*/
		
    	this.lstModulos = new ArrayList<Modulo>();
    	this.lstModulos.add(new Modulo("Compras","Quantity.png","/Compras/index.faces"));
    	this.lstModulos.add(new Modulo("Contabilidad","contabilidad.png","/Contabilidad/index.faces"));
    	this.lstModulos.add(new Modulo("Cuentas Por Cobrar","invoice.png","/CuentasPorCobrar/index.faces"));
    	this.lstModulos.add(new Modulo("Cuentas Por Pagar","Accounting.png","/CuentasPorPagar/index.faces"));
    	this.lstModulos.add(new Modulo("Gestion de Obras","Project.png","/GestionProyectos/index.faces"));
    	this.lstModulos.add(new Modulo("Inventarios","inventarios.png","/Inventarios/index.faces"));
    	this.lstModulos.add(new Modulo("Movil","Smartphone.png","/Mobile/index.faces"));
    	this.lstModulos.add(new Modulo("Personas","clientes.png","/Clientes/index.faces"));
    	this.lstModulos.add(new Modulo("Publico","publico.png","/Publico/index.faces"));
    	this.lstModulos.add(new Modulo("Recursos Humanos","rechum.png","/RecHum/index.faces"));
    	this.lstModulos.add(new Modulo("Administracion","tesoreria.png","/tyg/index.faces"));
    	this.lstModulos.add(new Modulo("Reportes","reports.png","/PROCESOS/admin/procesos.faces"));
    }

    
    public String getFecha(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		return sdf.format(Calendar.getInstance().getTime());
	}
    
    public void setModulo(Modulo modulo) { 
    	this.modulo = modulo;
    }
    
    public List<Modulo> getLstModulos()  { 
    	return lstModulos;
    }
    
    public String getUserName() { 
    	FacesContext facesContext =  FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRemoteUser();
    }
  
    public void seleccionaModulo() {
    	ExternalContext ec = null;
    	
		try {
			ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(modulo.getUrl());
		} catch (IOException e) {
			log.error("Error al seleccionaModulo", e);
		}
    }
    

	public String cerrarSession() { 
		ExternalContext ec = null;
		String res = "CerroSession";
		
		try {
			ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect("/cas/logout");
		} catch(Exception re) {
			log.error("Error al cerrarSession", re);
			res = "ERROR";
		}
		
		return res;
	}
	
    public void dispose() {
        if (log.isDebugEnabled()) {
            log.debug(" Disposing ModuloManager");
        }       
    }
}
