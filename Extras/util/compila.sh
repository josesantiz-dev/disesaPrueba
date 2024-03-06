#!/bin/bash
# Script para compilar y deployar modulos DISESA
# Parametros:
# 	- Ninguno					: Compila y deploya todos los modulos
# 	- Nombre modulo 			: Compila y deploya el modulo especifico.
# 	- VISTAS					: compila todos los modulos VISTA.
# 	- LOGICAS					: Compila todos los modulos Logica_XXXX.
# 	- MODELOS					: Compila todos los modulos Model_XXXX.
# 	- MOD2JBOSS					: Copia los modules.xml al JBOSS desde la carpeta Extras del repositorio.
# 	- MOD2REPO					: Copia los modules.xml a la carpeta Extras del repositorio desde el JBOSS.

seccion=0
parametro=$1
base_dir=$(pwd)
extras_dir=$base_dir/Extras/modulos/7.1.1

modelos=(
	"Comun"
	"Model_Cargas_Documentos"
	"Model_Clientes"
	"Model_Compras"
	"Model_Contabilidad"
	"Model_Credito"
	"Model_CuentasPorCobrar"
	"Model_CuentasPorPagar"
	"Model_Factoraje"
	"Model_GestionProyectos"
	"Model_Inventarios"
	"Model_Publico"
	"Model_RecHum"
	"Model_Tesoreria"
	"Model_TYG"
)

logicas=(
	"FRAMEWORK"
	"Logica_Publico"
	"Logica_Cargas_Documentos"
	"Logica_Clientes"
	"Logica_TYG"
	"Logica_RecHum"
	"Logica_Inventarios"
	"Logica_GestionProyectos"
	"Logica_Compras"
	"Logica_Contabilidad"
	"Logica_CuentasPorCobrar"
	"Logica_CuentasPorPagar"
	"Logica_Factoraje"
)

vistas=(
	"DISESA"
	"Clientes"
	"Compras"
	"Contabilidad"
	"CuentasPorCobrar"
	"CuentasPorPagar"
	"GestionProyectos"
	"Inventarios"
	"Mobile"
	"PROCESOS"
	"Publico"
	"RecHum"
	"VISTA_PROCESOS"
	"tyg"
)

if [ "$parametro" == "MODELOS" ]; then
	parametro=""
	seccion=1
fi

if [ "$parametro" == "LOGICAS" ]; then
	parametro=""
	seccion=2
fi

if [ "$parametro" == "VISTAS" ]; then
	parametro=""
	seccion=3
fi

if [ "$parametro" == "MOD2JBOSS" ]; then
	parametro=""
	seccion=4
fi

if [ "$parametro" == "MOD2REPO" ]; then
	parametro=""
	seccion=5
fi

# Modulo especifico
if [ "x$parametro" != "x" ]; then
	if [ ! -d $parametro ]; then
		echo "NO EXISTE DIRECTORIO"
		exit 1
	fi

	MOD_DIR=''
	case $parametro in 
		'Comun')					MOD_DIR='net/giro/comun'; 					extension='jar' ;;
		'FRAMEWORK')				MOD_DIR='net/izel/framework'; 				extension='jar' ;;
		'Logica_Cargas_Documentos')	MOD_DIR='net/giro/logica/cargasdocumentos'; extension='jar' ;;
		'Logica_Clientes') 			MOD_DIR='net/giro/logica/clientes'; 		extension='jar' ;;
		'Logica_Compras') 			MOD_DIR='net/giro/logica/compras'; 			extension='jar' ;;
		'Logica_Contabilidad') 		MOD_DIR='net/giro/logica/contabilidad'; 	extension='jar' ;;
		'Logica_CuentasPorCobrar') 	MOD_DIR='net/giro/logica/cuentasporcobrar'; extension='jar' ;;
		'Logica_CuentasPorPagar') 	MOD_DIR='net/giro/logica/cuentasporpagar'; 	extension='jar' ;;
		'Logica_Factoraje') 		MOD_DIR='net/giro/logica/factoraje'; 		extension='jar' ;;
		'Logica_GestionProyectos') 	MOD_DIR='net/giro/logica/gestionproyectos'; extension='jar' ;;
		'Logica_Inventarios') 		MOD_DIR='net/giro/logica/inventarios'; 		extension='jar' ;;
		'Logica_Publico') 			MOD_DIR='net/giro/logica/publico'; 			extension='jar' ;;
		'Logica_RecHum') 			MOD_DIR='net/giro/logica/rechum'; 			extension='jar' ;;
		'Logica_TYG') 				MOD_DIR='net/giro/logica/tyg'; 				extension='jar' ;;
		'Model_Cargas_Documentos')	MOD_DIR='net/giro/model/cargasdocumentos'; 	extension='jar' ;;
		'Model_Clientes') 			MOD_DIR='net/giro/model/clientes'; 			extension='jar' ;;
		'Model_Compras') 			MOD_DIR='net/giro/model/compras'; 			extension='jar' ;;
		'Model_Contabilidad') 		MOD_DIR='net/giro/model/contabilidad'; 		extension='jar' ;;
		'Model_Credito') 			MOD_DIR='net/giro/model/credito'; 			extension='jar' ;;
		'Model_CuentasPorCobrar') 	MOD_DIR='net/giro/model/cuentasporcobrar'; 	extension='jar' ;;
		'Model_CuentasPorPagar') 	MOD_DIR='net/giro/model/cuentasporpagar'; 	extension='jar' ;;
		'Model_Factoraje') 			MOD_DIR='net/giro/model/factoraje'; 		extension='jar' ;;
		'Model_GestionProyectos') 	MOD_DIR='net/giro/model/gestionproyectos'; 	extension='jar' ;;
		'Model_Inventarios')		MOD_DIR='net/giro/model/inventarios'; 		extension='jar' ;;
		'Model_Publico') 			MOD_DIR='net/giro/model/publico'; 			extension='jar' ;;
		'Model_RecHum') 			MOD_DIR='net/giro/model/rechum'; 			extension='jar' ;;
		'Model_Tesoreria') 			MOD_DIR='net/giro/model/tesoreria'; 		extension='jar' ;;
		'Model_TYG') 				MOD_DIR='net/giro/model/tyg'; 				extension='jar' ;;
		*)							MOD_DIR=''; 								extension='war' ;;
	esac

	cd $parametro
	echo -n "Compilando $parametro ... "
	mvn clean install > maven_log
	compilo=$?

	if [ $compilo -eq 0 ]; then
		rm maven_log
		echo "OK"
		echo -n "	Copiando a DEPLOYMENTS ... "; cp ./target/$parametro.$extension $JBOSS_HOME/standalone/deployments; echo "OK"
		if [ "x$MOD_DIR" != "x" ]; then
			mkdir -p $JBOSS_HOME/modules/$MOD_DIR/main
			echo -n "	Copiando a MODULES ... "; cp ./target/$parametro.$extension $JBOSS_HOME/modules/$MOD_DIR/main; echo "OK"
		fi 
		cd $base_dir
		exit 0
	else
		echo "ERROR"
		cat maven_log
		cd $base_dir
		exit $compilo
	fi
fi

if [ $seccion -eq 0 ]; then
	echo "--------------------------------------------------------------------------"
	echo " COMPILAR EL REPOSITORIO: $(pwd)"
	echo "--------------------------------------------------------------------------"
fi

if [ $seccion -eq 0 ] || [ $seccion -eq 1 ]; then
	echo ""
	echo "> MODELOS"
	echo "--------------------------------------------------------------------------"

	for item in ${modelos[*]}; do
		MOD_DIR=''
		case $item in 
			'Comun')					MOD_DIR='net/giro/comun'; 					extension='jar' ;;
			'Model_Cargas_Documentos')	MOD_DIR='net/giro/model/cargasdocumentos'; 	extension='jar' ;;
			'Model_Clientes') 			MOD_DIR='net/giro/model/clientes'; 			extension='jar' ;;
			'Model_Compras') 			MOD_DIR='net/giro/model/compras'; 			extension='jar' ;;
			'Model_Contabilidad') 		MOD_DIR='net/giro/model/contabilidad'; 		extension='jar' ;;
			'Model_Credito') 			MOD_DIR='net/giro/model/credito'; 			extension='jar' ;;
			'Model_CuentasPorCobrar') 	MOD_DIR='net/giro/model/cuentasporcobrar'; 	extension='jar' ;;
			'Model_CuentasPorPagar') 	MOD_DIR='net/giro/model/cuentasporpagar'; 	extension='jar' ;;
			'Model_Factoraje') 			MOD_DIR='net/giro/model/factoraje'; 		extension='jar' ;;
			'Model_GestionProyectos') 	MOD_DIR='net/giro/model/gestionproyectos'; 	extension='jar' ;;
			'Model_Inventarios')		MOD_DIR='net/giro/model/inventarios'; 		extension='jar' ;;
			'Model_Publico') 			MOD_DIR='net/giro/model/publico'; 			extension='jar' ;;
			'Model_RecHum') 			MOD_DIR='net/giro/model/rechum'; 			extension='jar' ;;
			'Model_Tesoreria') 			MOD_DIR='net/giro/model/tesoreria'; 		extension='jar' ;;
			'Model_TYG') 				MOD_DIR='net/giro/model/tyg'; 				extension='jar' ;;
		esac

		cd $item
		echo -n "Compilando $item ... "
		mvn clean install > maven_log
		compilo=$?

		if [ $compilo -eq 0 ]; then
			rm maven_log
			mkdir -p $JBOSS_HOME/modules/$MOD_DIR/main

			echo "OK"
			echo -n "	Copiando a MODULES ....... "; cp ./target/$item.jar $JBOSS_HOME/modules/$MOD_DIR/main; echo "OK"
			echo -n "	Copiando a DEPLOYMENTS ... "; cp ./target/$item.jar $JBOSS_HOME/standalone/deployments; echo "OK"
			cd $base_dir
		else
			echo "ERROR"
			cat maven_log
			cd $base_dir
			exit $compilo
		fi
	done
fi

if [ $seccion -eq 0 ] || [ $seccion -eq 2 ]; then
	echo ""
	echo "> LOGICAS"
	echo "--------------------------------------------------------------------------"

	for item in ${logicas[*]}; do
		MOD_DIR=''
		case $item in 
			'FRAMEWORK')				MOD_DIR='net/izel/framework'; 				extension='jar' ;;
			'Logica_Cargas_Documentos')	MOD_DIR='net/giro/logica/cargasdocumentos'; extension='jar' ;;
			'Logica_Clientes') 			MOD_DIR='net/giro/logica/clientes'; 		extension='jar' ;;
			'Logica_Compras') 			MOD_DIR='net/giro/logica/compras'; 			extension='jar' ;;
			'Logica_Contabilidad') 		MOD_DIR='net/giro/logica/contabilidad'; 	extension='jar' ;;
			'Logica_CuentasPorCobrar') 	MOD_DIR='net/giro/logica/cuentasporcobrar'; extension='jar' ;;
			'Logica_CuentasPorPagar') 	MOD_DIR='net/giro/logica/cuentasporpagar'; 	extension='jar' ;;
			'Logica_Factoraje') 		MOD_DIR='net/giro/logica/factoraje'; 		extension='jar' ;;
			'Logica_GestionProyectos') 	MOD_DIR='net/giro/logica/gestionproyectos'; extension='jar' ;;
			'Logica_Inventarios') 		MOD_DIR='net/giro/logica/inventarios'; 		extension='jar' ;;
			'Logica_Publico') 			MOD_DIR='net/giro/logica/publico'; 			extension='jar' ;;
			'Logica_RecHum') 			MOD_DIR='net/giro/logica/rechum'; 			extension='jar' ;;
			'Logica_TYG') 				MOD_DIR='net/giro/logica/tyg'; 				extension='jar' ;;
		esac


		cd $item
		echo -n "Compilando $item ... "
		mvn clean install > maven_log
		compilo=$?

		if [ $compilo -eq 0 ]; then
			rm maven_log
			mkdir -p $JBOSS_HOME/modules/$MOD_DIR/main

			echo "OK"
			echo -n "	Copiando a MODULES ....... "; cp ./target/$item.jar $JBOSS_HOME/modules/$MOD_DIR/main; echo "OK"
			echo -n "	Copiando a DEPLOYMENTS ... "; cp ./target/$item.jar $JBOSS_HOME/standalone/deployments; echo "OK"
			cd $base_dir
		else
			echo "ERROR"
			cat maven_log
			cd $base_dir
			exit $compilo
		fi
	done
fi

if [ $seccion -eq 0 ] || [ $seccion -eq 3 ]; then
	echo ""
	echo "> VISTAS"
	echo "--------------------------------------------------------------------------"

	for item in ${vistas[*]}; do
		cd $item
		echo -n "Compilando $item ... "
		mvn clean install > maven_log
		compilo=$?

		if [ $compilo -eq 0 ]; then
			rm maven_log
			
			echo "OK"
			echo -n "	Copiando a DEPLOYMENTS ... "; cp ./target/$item.war $JBOSS_HOME/standalone/deployments; echo "OK"
			cd $base_dir
		else
			echo "ERROR"
			cat maven_log
			cd $base_dir
			exit $compilo
		fi
	done
fi

if [ $seccion -eq 0 ] || [ $seccion -eq 4 ] || [ $seccion -eq 5 ]; then
	echo ""
	echo "> MODULES "
	echo "--------------------------------------------------------------------------"

	cd $extras_dir
	modules=("${modelos}" "${logicas}")
	origen=''
	destino=''
	for item in ${modules[*]}; do
		MOD_DIR=''
		case $item in 
			'Comun')					MOD_DIR='net/giro/comun'; 					;;
			'FRAMEWORK')				MOD_DIR='net/izel/framework'; 				;;
			'Model_Cargas_Documentos')	MOD_DIR='net/giro/model/cargasdocumentos'; 	;;
			'Model_Clientes') 			MOD_DIR='net/giro/model/clientes'; 			;;
			'Model_Compras') 			MOD_DIR='net/giro/model/compras'; 			;;
			'Model_Contabilidad') 		MOD_DIR='net/giro/model/contabilidad'; 		;;
			'Model_Credito') 			MOD_DIR='net/giro/model/credito'; 			;;
			'Model_CuentasPorCobrar') 	MOD_DIR='net/giro/model/cuentasporcobrar'; 	;;
			'Model_CuentasPorPagar') 	MOD_DIR='net/giro/model/cuentasporpagar'; 	;;
			'Model_Factoraje') 			MOD_DIR='net/giro/model/factoraje'; 		;;
			'Model_GestionProyectos') 	MOD_DIR='net/giro/model/gestionproyectos'; 	;;
			'Model_Inventarios')		MOD_DIR='net/giro/model/inventarios'; 		;;
			'Model_Publico') 			MOD_DIR='net/giro/model/publico'; 			;;
			'Model_RecHum') 			MOD_DIR='net/giro/model/rechum'; 			;;
			'Model_Tesoreria') 			MOD_DIR='net/giro/model/tesoreria'; 		;;
			'Model_TYG') 				MOD_DIR='net/giro/model/tyg'; 				;;
			'Logica_Cargas_Documentos')	MOD_DIR='net/giro/logica/cargasdocumentos'; ;;
			'Logica_Clientes') 			MOD_DIR='net/giro/logica/clientes'; 		;;
			'Logica_Compras') 			MOD_DIR='net/giro/logica/compras'; 			;;
			'Logica_Contabilidad') 		MOD_DIR='net/giro/logica/contabilidad'; 	;;
			'Logica_CuentasPorCobrar') 	MOD_DIR='net/giro/logica/cuentasporcobrar'; ;;
			'Logica_CuentasPorPagar') 	MOD_DIR='net/giro/logica/cuentasporpagar'; 	;;
			'Logica_Factoraje') 		MOD_DIR='net/giro/logica/factoraje'; 		;;
			'Logica_GestionProyectos') 	MOD_DIR='net/giro/logica/gestionproyectos'; ;;
			'Logica_Inventarios') 		MOD_DIR='net/giro/logica/inventarios'; 		;;
			'Logica_Publico') 			MOD_DIR='net/giro/logica/publico'; 			;;
			'Logica_RecHum') 			MOD_DIR='net/giro/logica/rechum'; 			;;
			'Logica_TYG') 				MOD_DIR='net/giro/logica/tyg'; 				;;
		esac

		if [ $seccion -eq 4 ]; then # modules to JBOSS
			origen=$extras_dir/$MOD_DIR/main/module.xml
			destino=$JBOSS_HOME/modules/$MOD_DIR/main
		fi

		if [ $seccion -eq 5 ]; then # modules to REPO EXTRAS
			origen=$JBOSS_HOME/modules/$MOD_DIR/main/module.xml
			destino=$extras_dir/$MOD_DIR/main
		fi

		echo -n "$item. Copiar module.xml a $destino ... "
		# si no existe el directorio lo creamos
		mkdir -p $destino
		cp $origen $destino; echo "OK"
		cd $extras_dir
	done

	cd $base_dirfi

exit 0