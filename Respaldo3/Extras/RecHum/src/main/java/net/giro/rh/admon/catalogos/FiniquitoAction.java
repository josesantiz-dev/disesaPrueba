package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;
import net.giro.rh.admon.logica.EmpleadoFiniquitoRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ManagedBean
@ViewScoped
public class FiniquitoAction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FiniquitoAction.class);
	

	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	private int numPaginaEmpleados;
	
	private boolean operacionCompleta;
	private int tipoMensaje;
	private String resOperacion;
	private String valorBusqueda;
	
	private String valorBusquedaEmpleados;
	private int tipoBusquedaEmpleados;
	
	
	private EmpleadoFiniquitoRem ifzFiniquito;
	private EmpleadoRem ifzEmpleado;
	
	
	private EmpleadoFiniquitoExt pojoFiniquito;
	private List<EmpleadoFiniquitoExt> listaFiniquitosGrid;
	
	private List<Empleado> listaEmpleadosGrid;
	
	private EmpleadoExt pojoEmpleado;
	private Empleado pojoEmpleadoBusqueda; 	//Para solo buscar por empleados y no convertir todos, cuando la busqueda regrese, se convierte el empleado seleccionado a Extendido
	
	private List<Empleado> listaSolicitadoPor;
	private List<SelectItem> listaCboSolicitadoPor;
	
	
	public FiniquitoAction() throws NamingException,Exception {
		String ejbName;
		
		this.operacionCompleta = false;
		this.numPagina = 1;

		this.ctx = new InitialContext();
		
		ejbName = "ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem";
		this.ifzFiniquito = (EmpleadoFiniquitoRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		pojoFiniquito = new EmpleadoFiniquitoExt();
		listaFiniquitosGrid = new ArrayList<>();
		
		cargarListaSolicitadoPor();
		cargarListaCboSolicitadoPor();
		
		listaEmpleadosGrid = new ArrayList<>();
	}
	
	
	public void buscarEmpleados(){
		this.listaEmpleadosGrid.clear();
		
		if(this.valorBusquedaEmpleados.equals("")){	//si la búsqueda está vacia, no importa como se busque
			this.listaEmpleadosGrid = this.ifzEmpleado.findAllActivos();
		}else{
			if(tipoBusquedaEmpleados==1){	//por nombre
				this.listaEmpleadosGrid = this.ifzEmpleado.findByNombreEmpleado(this.valorBusquedaEmpleados);
			}else{
				//por id empleado
				try{
					long bIdEmpleado = Long.parseLong( this.valorBusquedaEmpleados );	
					Empleado bEmpleado = this.ifzEmpleado.findById( bIdEmpleado );
					this.listaEmpleadosGrid.add(bEmpleado);
				}catch(Exception e){
					
				}
			}
		}
	}
	
	public void seleccionarEmpleado(){
	}
	
	private void cargarListaSolicitadoPor(){
		listaSolicitadoPor = this.ifzEmpleado.findAllActivos();
	}
	
	private void cargarListaCboSolicitadoPor(){
		listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = null;
		this.listaCboSolicitadoPor = new ArrayList<SelectItem>();
		//this.listaCboSolicitadoPor.add( new SelectItem("0", "" ) );
		
		for(Empleado e: this.listaSolicitadoPor){
			this.listaCboSolicitadoPor.add( new SelectItem(e.getId().toString(), e.getNombre() ) );
		}
	}

	public void nuevo(){
		this.pojoFiniquito = new EmpleadoFiniquitoExt();
		this.pojoEmpleadoBusqueda = new Empleado();
	}
	
	public void guardar(){
		this.operacionCompleta = false;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		try{
			if(! validaGuardarFiniquito()) {
				this.operacionCompleta = false;
				this.tipoMensaje = -1;
				return;
			}
			
			if(this.pojoFiniquito.getId() == null) {
				this.pojoFiniquito.setCreadoPor(this.usuarioId);
				this.pojoFiniquito.setFechaCreacion( Calendar.getInstance().getTime() );
				this.pojoFiniquito.setModificadoPor( this.usuarioId );
				this.pojoFiniquito.setFechaModificacion( Calendar.getInstance().getTime() );
				
				//actualizar el estatus del empleado a baja --> 2 cuando Aprobacion{estatus} sea true
				if( this.pojoFiniquito.getEstatus() == 1 )
					bajaEmpleado(this.pojoFiniquito.getEmpleado().getId());
				
				this.pojoFiniquito.setId( this.ifzFiniquito.save( this.pojoFiniquito ) );
				
				this.listaFiniquitosGrid.add(this.pojoFiniquito);
			} else {
				if( this.pojoFiniquito.getEstatus() == 1 )
					bajaEmpleado(this.pojoFiniquito.getEmpleado().getId());
				
				this.pojoFiniquito.setModificadoPor( this.usuarioId );
				this.pojoFiniquito.setFechaModificacion( Calendar.getInstance().getTime() );
				this.ifzFiniquito.update( this.pojoFiniquito );				
			}
		}catch(Exception e) {
			log.error("error al buscar", e);
			this.resOperacion = "ERROR";
			return;
		}
		
		this.operacionCompleta = true;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		log.info("Guardado");
	}
	
	public void bajaEmpleado(long idEmpleado){
		try {
			Empleado e = this.ifzEmpleado.findById(idEmpleado);
			e.setEstatus(2);
			this.ifzEmpleado.update(e);
		} catch (ExcepConstraint e1) {
			log.info("Error dando de baja al empleado");
		}
	}
	
	private boolean validaGuardarFiniquito(){
		if (this.pojoFiniquito.getEmpleado()==null){
			this.resOperacion="Debe seleccionar empleado";
			return false;
		}
		
		if(this.pojoFiniquito.getFechaSolicitudBaja()==null){
			this.resOperacion="Debe indicar fecha de Solicitud de Baja";
			return false;
		}
		
		if (this.pojoFiniquito.getFechaElaboracionEnvio() != null) {
			if (this.pojoFiniquito.getFechaSolicitudBaja().compareTo(this.pojoFiniquito.getFechaElaboracionEnvio()) >= 0) {
				this.resOperacion="La Fecha Elaboracion y Envío Finiquito debe ser mayor a la Fecha Solicitud Baja";
				return false;
			}
		}
		
		if(this.pojoFiniquito.getMonto() <= 0) {
			if (this.pojoFiniquito.getVoBoRh() == 1) {
				this.resOperacion = "Ingrese el monto del finiquito";
				return false;
			}
		}
		
		return true;
	}
	
	public void editar() {
		this.operacionCompleta = false;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		this.operacionCompleta = true;
	}
	
	public void buscar(){
		this.operacionCompleta = false;
		this.resOperacion = "";
		this.tipoMensaje = 0;
		
		try{
			if( "".equals(this.valorBusqueda) ){
				this.listaFiniquitosGrid = this.ifzFiniquito.findAllExt();
			} else {
				this.listaFiniquitosGrid = this.ifzFiniquito.findByEmpleadoExt( this.valorBusqueda );	//busqueda por nombre de empleado
			}
			
			// Comprobamos resultados
			if (this.listaFiniquitosGrid.isEmpty()) {
				this.tipoMensaje = 2;
				this.resOperacion = "ERROR";
				return;
			}
			
			this.operacionCompleta = true;
			this.resOperacion = "";
			this.tipoMensaje = 0;
		} catch(Exception e) {
			log.error("error al buscar", e);
			this.resOperacion = "ERROR";
		}
	}

	// ----------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------------
	
	public long getSolicitadoPor(){
		return this.pojoFiniquito.getSolicitadoPor();
	}
	
	public void setSolicitadoPor(long idEmpleado){
		this.pojoFiniquito.setSolicitadoPor( idEmpleado );
	}
	
	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public EmpleadoFiniquitoExt getPojoFiniquito() {
		return pojoFiniquito;
	}

	public void setPojoFiniquito(EmpleadoFiniquitoExt pojoFiniquito) {
		this.pojoFiniquito = pojoFiniquito;
	}

	public List<EmpleadoFiniquitoExt> getListaFiniquitosGrid() {
		return listaFiniquitosGrid;
	}

	public void setListaFiniquitosGrid(List<EmpleadoFiniquitoExt> listaFiniquitosGrid) {
		this.listaFiniquitosGrid = listaFiniquitosGrid;
	}

	public EmpleadoExt getPojoEmpleado() {
		return pojoEmpleado;
	}

	public void setPojoEmpleado(EmpleadoExt pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public void setFirmaRenuncia(boolean firmaRenuncia) {
		if(firmaRenuncia)
			this.pojoFiniquito.setFirmaRenuncia( (short) 1 ); 
		else
			this.pojoFiniquito.setFirmaRenuncia( (short) 0) ;
	}
	
	public boolean isFirmaRenuncia(){
		return this.pojoFiniquito.getFirmaRenuncia()== 0 ? false : true;
	}
	
	public void setVoBoRH(boolean voBoRh){
		if (voBoRh)
			this.pojoFiniquito.setVoBoRh((short) 1);
		else
			this.pojoFiniquito.setVoBoRh((short) 0);
	}
	
	public boolean isVoBoRH(){
		return this.pojoFiniquito.getVoBoRh() == 1 ? true : false;
	}
	
	public boolean isAprobacion(){
		return this.pojoFiniquito.getEstatus()==1 ? true:false;
	}
	
	public void setAprobacion(boolean aprobacion){
		if(aprobacion)
			this.pojoFiniquito.setEstatus( (short) 1 );
		else
			this.pojoFiniquito.setEstatus( (short) 0 );
	}

	public List<SelectItem> getListaCboSolicitadoPor() {
		return listaCboSolicitadoPor;
	}

	public void setListaCboSolicitadoPor(List<SelectItem> listaCboSolicitadoPor) {
		this.listaCboSolicitadoPor = listaCboSolicitadoPor;
	}
	
	public Date getFechaElaboracionEnvio(){
		return pojoFiniquito.getFechaElaboracionEnvio()==null ? Calendar.getInstance().getTime() : pojoFiniquito.getFechaElaboracionEnvio();
	}
	
	public void setFechaElaboracionEnvio(Date fechaElaboracionEnvio){
		this.pojoFiniquito.setFechaElaboracionEnvio(fechaElaboracionEnvio);
	}
	
	public Date getFechaSolicitudBaja(){
		return pojoFiniquito.getFechaSolicitudBaja() ==null ? Calendar.getInstance().getTime() : pojoFiniquito.getFechaSolicitudBaja();
	}
	
	public void setFechaSolicitudBaja(Date fechaSolicitudBaja){
		this.pojoFiniquito.setFechaSolicitudBaja(fechaSolicitudBaja);
	}

	public String getValorBusquedaEmpleados() {
		return valorBusquedaEmpleados;
	}

	public void setValorBusquedaEmpleados(String valorBusquedaEmpleados) {
		this.valorBusquedaEmpleados = valorBusquedaEmpleados;
	}

	public int getTipoBusquedaEmpleados() {
		return tipoBusquedaEmpleados;
	}

	public void setTipoBusquedaEmpleados(int tipoBusquedaEmpleados) {
		this.tipoBusquedaEmpleados = tipoBusquedaEmpleados;
	}

	public int getNumPaginaEmpleados() {
		return numPaginaEmpleados;
	}

	public void setNumPaginaEmpleados(int numPaginaEmpleados) {
		this.numPaginaEmpleados = numPaginaEmpleados;
	}

	public List<Empleado> getListaEmpleadosGrid() {
		return listaEmpleadosGrid;
	}

	public void setListaEmpleadosGrid(List<Empleado> listaEmpleadosGrid) {
		this.listaEmpleadosGrid = listaEmpleadosGrid;
	}

	public Empleado getPojoEmpleadoBusqueda() {
		return pojoEmpleadoBusqueda;
	}

	public void setPojoEmpleadoBusqueda(Empleado pojoEmpleadoBusqueda) {
		this.pojoEmpleadoBusqueda = pojoEmpleadoBusqueda;
		this.pojoFiniquito.setEmpleado( this.ifzEmpleado.findByIdExt( pojoEmpleadoBusqueda.getId() ) );
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  1.0	| 2016-09-20 | Javier Tirado 			 | Se creo el metodo editar
 *    3.1   | 2017-01-24 | Javier Tirado 			 | Valido que el monto del finiquito solo es requerido para VoBo.
 */