<!DOCTYPE composition PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:s="http://jboss.com/products/seam/taglib"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a="http://richfaces.org/a4j" 
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich" 
	template="/layout/template.xhtml">

	
	<ui:define name="menuFrag">
			<rich:toolBarGroup id="tbgNuevo" location="right">
				<a4j:commandButton id="cmdSelecciona" onclick="#{rich:element('cmdSelecciona')}.disabled = true;" 
							action="#{SeleccionReporteAction.seleccionaRep}" 
							image="#{msg['aplicar22.img']}"  
		 					oncomplete="#{rich:element('cmdSelecciona')}.disabled = false; javascript:window.open('/Focir_WEB/reportes/creacion.faces', 'Reporte', 'width=700, height=500, menubar=0, toolbar=0, scrollbars=1, location=0, status=1');" 
		 					style="cursor: pointer;" >
		 		</a4j:commandButton>
		 </rich:toolBarGroup>
	
	</ui:define>	
		
	<ui:define name="body">		
		<a4j:keepAlive beanName="SeleccionReporteAction"/>
		<a4j:loadStyle src="resource:///css/general/general.css" />
		<a4j:loadScript src="resource:///scripts/general/general.js"/>
			<div class="div_pagina">
				<h:panelGrid columns="2">
						<h:outputText value="Reporte:" styleClass="Titulo"/>
						<h:selectOneMenu value="#{SeleccionReporteAction.repSelect}"  style="width:250px;" styleClass="Descripcion">
							<f:selectItems value="#{SeleccionReporteAction.listCombo}"/>
						</h:selectOneMenu>
				</h:panelGrid>	
			</div>	
	</ui:define>
</ui:composition>