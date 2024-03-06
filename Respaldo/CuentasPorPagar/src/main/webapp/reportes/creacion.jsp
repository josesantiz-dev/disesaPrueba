<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
   xmlns:ui="http://java.sun.com/jsf/facelets"
   xmlns:f="http://java.sun.com/jsf/core"
   xmlns:h="http://java.sun.com/jsf/html"
   xmlns:a4j="http://richfaces.org/a4j" 
   xmlns:rich="http://richfaces.org/rich"
   xmlns:s="http://jboss.com/products/seam/taglib">
  <head>
  </head>
	<f:view>
     	<a4j:form  id="frmPrincipal" style="text-align:center; float:center; width:100%;">
	          <a4j:region id="loadGral2"> 	
		        <a4j:status id="statusMensaje" for="loadGral2" onstart="#{rich:component('wait')}.show()" onstop="#{rich:component('wait')}.hide()"/>
	 			<a4j:queue id="quePrincipal" name="miQueue" ignoreDupResponses="true" requestDelay="500" size="1"/>
		          			
	      		<div id="div_footer">
      				<h:outputText id="txtFooterUsuario" value="#{msg['usuario']}: #{loginManager.loginBean.usuario.usuario}" styleClass="div_footer_txt"/>
      				<h:outputText id="txtFooterResp" value="#{msg['responsabilidad']}: #{loginManager.usuarioResponsabilidad.responsabilidad.responsabilidad}" styleClass="div_footer_txt"/>
      				<h:outputText id="txtFooterFecha" value="#{msg['navegacion.label.fecha']}: #{navegador.fecha}" styleClass="div_footer_txt"/>
	      		</div>
	      		
	      		<h:panelGroup style="border:2px solid #9CADBA; color:#6A6868; background-color:#E4EDFD; font-weight: bold; padding:.2em; margin:1em .5em; text-align:center; display:block; height:26px">
						
						<h:outputLabel for="cmdEjecutar" value="#{msg['seleccionar']}" style="cursor:pointer;float: right;" styleClass="Titulo"/>
						<a4j:commandButton id="cmdEjecutar" 
							onclick="#{rich:element('cmdEjecutar')}.disabled = true;" 
							action="#{ContDinamicoAction.ejecutaRep}" 
							image="#{msg['aplicar22.img']}"  
							oncomplete="#{rich:element('cmdEjecutar')}.disabled = false; javascript:window.open('/Focir_WEB/reportes/ReporteGeneric.faces', 'Reporte', 'width=700, height=500, menubar=0, toolbar=0, scrollbars=1, location=0, status=1');" 
							style="float:right;cursor: pointer;padding-right: 5px;" reRender="pnlDinamico">
						</a4j:commandButton>
						
				</h:panelGroup>
				 
			    <body style="text-align:left;">
					<a4j:keepAlive beanName="ContDinamicoAction"/>
					<a4j:loadStyle src="resource:///css/general/general.css" />
			  		<a4j:loadStyle src="resource:///css/xml/layouts.css" />
			  		<a4j:loadScript src="resource:///scripts/general/general.js"/>
					<h:panelGroup styleClass="fondoGeneral">
						<h:panelGrid id = "pnlDinamico" binding="#{ContDinamicoAction.pnlComponentes}"/>
					</h:panelGroup>
				</body>
				<ui:include src="/layout/modalsGenerales.jsp"/>
			</a4j:region>		
		</a4j:form>				
	</f:view>					 
</html>