<!DOCTYPE composition PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:s="http://jboss.com/products/seam/taglib"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:a4j="http://richfaces.org/a4j" 
    xmlns:rich="http://richfaces.org/rich">
	
	<rich:popupPanel header="Responsabilidades" id="pnlSeleccionResponsabilidad" autosized="true">
	  	 	 
	        <f:facet name="controls">
	           <h:graphicImage value="#{msg['navegacion.gral.cerrar.img']}" style="cursor:pointer;" onclick="#{rich:component('pnlSeleccionResponsabilidad')}.hide()"/>
	         </f:facet>
	         <h:form id="frmSelResp">
			         <rich:dataTable id="dtSelResponsabilidad" value="#{loginManager.usuarioResponsabilidades}" rows="10" var="ur" style="width:250px" rowClasses="gralRow1, gralRow2" rendered="#{loginManager.tieneResponsabilidades}">
				  	 	 <rich:column style="width:250px; text-align:center;">
				  	 	 	<a4j:commandLink value="#{ur.responsabilidad.responsabilidad}" action="#{loginManager.seleccionaResponsabilidad}" oncomplete="goUrl('#{navegador.urlHome}')" style="padding:3px auto; font-weight: bold;">
				  	 	 		<f:setPropertyActionListener target="#{loginManager.usuarioResponsabilidad}" value="#{ur}" />
				  	 	 	</a4j:commandLink>
				  	 	 </rich:column>
				  	 </rich:dataTable>
				  	 
				  	 <h:panelGroup rendered="#{!loginManager.tieneResponsabilidades}" style="display:block; width:250px; text-align:center;">
				  	 	<h:outputText value="#{msg['mensajes.error.sinResponsabilidades']}" styleClass="TituloGral" style="margin:auto;"/>
				  	 </h:panelGroup>
				  	 
				  	 <f:facet name="footer" >
		               	<rich:dataScroller align="center" for="dtSelResponsabilidad" maxPages="10" ajaxSingle="true" renderIfSinglePage="false"
		            		page="#{navegador.numPag}" id="dScrollSelResp"/>
	               </f:facet>
		  	 </h:form>
	</rich:popupPanel>
		
	<rich:popupPanel id="wait" header="#{msg['navegacion.mensaje.procesando']}" autosized="true" moveable="false" resizeable="false">
        <f:facet name="controls">
           <h:graphicImage value="#{msg['navegacion.gral.cerrar.img']}" style="cursor:pointer;" onclick="#{rich:component('wait')}.hide()"/>
         </f:facet>
        <h:form>
	        <h:panelGrid id="pnlWait" columns="1" width="200" style="text-align:center; margin-top:5px;">
	        	<h:graphicImage value="#{msg['navegacion.loading.loadingGrande.img']}" style="text-align:center; display:block; margin:10px auto auto auto;"/>
			</h:panelGrid>
		</h:form>
    </rich:popupPanel>
    

	<rich:popupPanel id="pnlCambioPass" header="#{msg['cambiarContrasena']} #{loginManager.tituloCambioContrasena}" autosized="true">
        <f:facet name="controls">
           <h:graphicImage value="#{msg['navegacion.gral.cerrar.img']}" style="cursor:pointer;" onclick="#{rich:component('pnlCambioPass')}.hide()"/>
         </f:facet>
                  
         	<rich:messages id="msgErrorCambioPass" showDetail="false" showSummary="true" globalOnly="false" ajaxRendered="false">
				<f:facet name="errorMarker">
                    <h:graphicImage value="#{msg['alert16.img']}" style="margin-right:1em;"/>   
                </f:facet>
			</rich:messages>
       
		  	 <h:panelGrid columns="2" columnClasses="alignRight, alignLeft" style="display:block; width:100%; text-align:center;">
		  	 	<h:outputText value="#{msg['contrasenaActual']}:" styleClass="TituloGral"/>
		  	 	<h:inputSecret value="#{loginManager.actualPassword}" rendered="true" size="15" maxlength="9" >
		  	 		
		  	 	</h:inputSecret>
		  	 	<h:outputText value="#{msg['contrasenaNueva']}:" styleClass="TituloGral"/>
		  	 	<h:inputSecret value="#{loginManager.nuevoPassword}" rendered="true" size="15" maxlength="9">

		  	 	</h:inputSecret>
		  	 	<h:outputText value="#{msg['contrasenaRepetir']}:" styleClass="TituloGral"/>
		  	 	<h:inputSecret value="#{loginManager.nuevoPassword2}" rendered="true" size="15" maxlength="9">
		  	 		
		  	 	</h:inputSecret>
		  	 </h:panelGrid>
		  	 
		  	 <div style="display: block; width: 300px; height: 22px; margin-top:10px;">
				<a4j:commandButton
					id="cmdCambiaPassword" action="#{loginManager.cambiarContrasena}" oncomplete="cambiarPass('#{loginManager.mensaje}', 'pnlCambioPass', 'pnlMsgPass', #{rich:element('msgErrorCambioPass')}, '#{navegador.origenCambioPass}', 'panelResponsabilidades');"
					value="#{msg['aceptar']}" reRender="pnlMsgPass, msgErrorCambioPass, panelResponsabilidades"
					style="vertical-align: middle; position:relative; float:center; margin:auto; display:block;">
				</a4j:commandButton>
			</div>
		
	</rich:popupPanel>
	
	<rich:popupPanel id="pnlMsgPass" header="#{loginManager.mensaje == '' ? msg['informacion'] : msg['atencion']}" autosized="true">
		 <f:facet name="controls">
           <h:graphicImage value="#{msg['navegacion.gral.cerrar.img']}" style="cursor:pointer;" onclick="#{rich:component('pnlMsgPass')}.hide()"/>
         </f:facet>
         
     	
       		<h:panelGroup style="display:block; width:300px; float:center;">
	       		<h:outputText value="#{loginManager.mensaje == '' ? msg['mensaje.info.contrasenaGuardada'] : loginManager.mensaje}" style="display:block;" styleClass="TituloGral"/>
       		</h:panelGroup>
       		<a4j:commandButton value="#{msg['aceptar']}" onclick="#{rich:component('pnlMsgPass')}.hide()" style="margin-top:10px; cursor:pointer;"/>
   		
    </rich:popupPanel>

</ui:composition>