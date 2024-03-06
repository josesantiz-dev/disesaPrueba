#!/bin/bash
# Actualizador de modulos DISESA
# 

parametro=$1
if [ "x$parametro" != "x" ]; then
	if [[ $parametro == *"/" ]]; then
	    parametro=$(expr "$parametro" : '\(.*\).')
	fi

	if [ ! -d $parametro ]; then
		echo "NO EXISTE DIRECTORIO"
		exit 1
	fi

	chown jboss7:apps $parametro

	MOD_DIR=''
	case $parametro in 
		'Model_Cargas_Documentos.jar')	MOD_DIR='net/giro/model/cargasdocumentos/main' 	;;
		'Model_Clientes.jar') 			MOD_DIR='net/giro/model/clientes/main' 			;;
		'Model_Compras.jar') 			MOD_DIR='net/giro/model/compras/main' 			;;
		'Model_Contabilidad.jar') 		MOD_DIR='net/giro/model/contabilidad/main' 		;;
		'Model_Credito.jar') 			MOD_DIR='net/giro/model/credito/main' 			;;
		'Model_CuentasPorCobrar.jar') 	MOD_DIR='net/giro/model/cuentasporcobrar/main' 	;;
		'Model_CuentasPorPagar.jar') 	MOD_DIR='net/giro/model/cuentasporpagar/main' 	;;
		'Model_GestionProyectos.jar') 	MOD_DIR='net/giro/model/gestionproyectos/main' 	;;
		'Model_Inventarios.jar')		MOD_DIR='net/giro/model/inventarios/main' 		;;
		'Model_Publico.jar') 			MOD_DIR='net/giro/model/publico/main' 			;;
		'Model_RecHum.jar') 			MOD_DIR='net/giro/model/rechum/main' 			;;
		'Model_TYG.jar') 				MOD_DIR='net/giro/model/tyg/main' 				;;
		'Navegador.jar')				MOD_DIR='net/giro/navegador/3.3.0' 				;;
		'Navegador4.jar')				MOD_DIR='net/giro/navegador/4.3.0' 				;;
		'Logica_Cargas_Documentos.jar')	MOD_DIR='net/giro/logica/cargasdocumentos/main' ;;
		'Logica_Clientes.jar') 			MOD_DIR='net/giro/logica/clientes/main' 		;;
		'Logica_Compras.jar') 			MOD_DIR='net/giro/logica/compras/main' 			;;
		'Logica_Contabilidad.jar') 		MOD_DIR='net/giro/logica/contabilidad/main' 	;;
		'Logica_CuentasPorCobrar.jar') 	MOD_DIR='net/giro/logica/cuentasporcobrar/main' ;;
		'Logica_CuentasPorPagar.jar') 	MOD_DIR='net/giro/logica/cuentasporpagar/main' 	;;
		'Logica_GestionProyectos.jar') 	MOD_DIR='net/giro/logica/gestionproyectos/main' ;;
		'Logica_Inventarios.jar') 		MOD_DIR='net/giro/logica/inventarios/main' 		;;
		'Logica_Publico.jar') 			MOD_DIR='net/giro/logica/publico/main' 			;;
		'Logica_RecHum.jar') 			MOD_DIR='net/giro/logica/rechum/main' 			;;
		'Logica_TYG.jar') 				MOD_DIR='net/giro/logica/tyg/main' 				;;
		*) 								MOD_DIR='' 										;;
	esac

	echo -n "MODULO : $parametro "
	cp /opt/jboss-as-7.1.1/standalone/deployments/$parametro /opt/jboss-as-7.1.1/standalone/pre-update/$parametro.ori
	echo -n "."

	if [ "x$MOD_DIR" != "x" ]; then
		cp $parametro /opt/jboss-as-7.1.1/modules/$MOD_DIR
		echo -n "."
	fi

	mv $parametro /opt/jboss-as-7.1.1/standalone/deployments
	echo ". OK"
	exit 0
fi

chown jboss7:apps *.*ar

for item in `ls *.*ar`; do
	MOD_DIR=''
	case $item in 
		'Model_Cargas_Documentos.jar')	MOD_DIR='net/giro/model/cargasdocumentos/main' 	;;
		'Model_Clientes.jar') 			MOD_DIR='net/giro/model/clientes/main' 			;;
		'Model_Compras.jar') 			MOD_DIR='net/giro/model/compras/main' 			;;
		'Model_Contabilidad.jar') 		MOD_DIR='net/giro/model/contabilidad/main' 		;;
		'Model_Credito.jar') 			MOD_DIR='net/giro/model/credito/main' 			;;
		'Model_CuentasPorCobrar.jar') 	MOD_DIR='net/giro/model/cuentasporcobrar/main' 	;;
		'Model_CuentasPorPagar.jar') 	MOD_DIR='net/giro/model/cuentasporpagar/main' 	;;
		'Model_GestionProyectos.jar') 	MOD_DIR='net/giro/model/gestionproyectos/main' 	;;
		'Model_Inventarios.jar')		MOD_DIR='net/giro/model/inventarios/main' 		;;
		'Model_Publico.jar') 			MOD_DIR='net/giro/model/publico/main' 			;;
		'Model_RecHum.jar') 			MOD_DIR='net/giro/model/rechum/main' 			;;
		'Model_TYG.jar') 				MOD_DIR='net/giro/model/tyg/main' 				;;
		'Navegador.jar')				MOD_DIR='net/giro/navegador/3.3.0' 				;;
		'Navegador4.jar')				MOD_DIR='net/giro/navegador/4.3.0' 				;;
		'Logica_Cargas_Documentos.jar')	MOD_DIR='net/giro/logica/cargasdocumentos/main' ;;
		'Logica_Clientes.jar') 			MOD_DIR='net/giro/logica/clientes/main' 		;;
		'Logica_Compras.jar') 			MOD_DIR='net/giro/logica/compras/main' 			;;
		'Logica_Contabilidad.jar') 		MOD_DIR='net/giro/logica/contabilidad/main' 	;;
		'Logica_CuentasPorCobrar.jar') 	MOD_DIR='net/giro/logica/cuentasporcobrar/main' ;;
		'Logica_CuentasPorPagar.jar') 	MOD_DIR='net/giro/logica/cuentasporpagar/main' 	;;
		'Logica_GestionProyectos.jar') 	MOD_DIR='net/giro/logica/gestionproyectos/main' ;;
		'Logica_Inventarios.jar') 		MOD_DIR='net/giro/logica/inventarios/main' 		;;
		'Logica_Publico.jar') 			MOD_DIR='net/giro/logica/publico/main' 			;;
		'Logica_RecHum.jar') 			MOD_DIR='net/giro/logica/rechum/main' 			;;
		'Logica_TYG.jar') 				MOD_DIR='net/giro/logica/tyg/main' 				;;
		*) 								MOD_DIR='' 										;;
	esac

	echo -n "MODULO : $item "
	cp /opt/jboss-as-7.1.1/standalone/deployments/$item /opt/jboss-as-7.1.1/standalone/pre-update/$item.ori
	echo -n "."

	if [ "x$MOD_DIR" != "x" ]; then
		cp $item /opt/jboss-as-7.1.1/modules/$MOD_DIR
		echo -n "."
	fi

	mv $item /opt/jboss-as-7.1.1/standalone/deployments
	echo ". OK"
done

exit 0