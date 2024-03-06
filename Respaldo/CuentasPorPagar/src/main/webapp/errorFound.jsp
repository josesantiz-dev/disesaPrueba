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
    
  <ui:define name="body">
		<div class="div_pagina">
			<h:graphicImage value="#{msg['navegacion.msg.warning.img']}"/>
			<h:outputText value="#{navegador.mensaje}" styleClass="TituloGral" style="margin-left:15px;"/>
		</div>
  </ui:define>
</ui:composition>

