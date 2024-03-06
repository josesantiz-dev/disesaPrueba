#!/bin/bash
### ----------------------------------------------------------------------------
### Javaz0 #!
### ----------------------------------------------------------------------------
### Rutina de actualizacion de empaquetados java
### Formato de Instrucciones: YYYY-MM-DD,HH:MM,SCRIPT,PARAMETROS
### Ejemplo: 2019-12-11,16:14,air-updater.sh,parametro1

cd /opt/tmp
fechaActual=`/bin/date +"%Y-%m-%d"`
horaActual=`/bin/date +"%H:%M"`
reiniciarAIR=0
completo=0

if [ -f actualizar ]; then
	# Extraemos las partes que conformaran la programacion
	fechaProg=`/usr/bin/cut -d , -f 1 actualizar`
	horaProg=`/usr/bin/cut -d , -f 2 actualizar`
	script=`/usr/bin/cut -d , -f 3 actualizar`
	parametros=`/usr/bin/cut -d , -f 4 actualizar`

	# Comprobamos si en los parametros existe uno con extension jar
	if [ $parametros ?? 'jar' ]; then
		reiniciarAIR=1
	else
		# Comprobamos si en los empaquetados existe uno con extension jar
		parametros=`/bin/ls *.jar`
		if [ $parametros ?? 'jar' ]; then
			reiniciarAIR=1
		fi
		parametros=""
	fi

	# Comprobamos si debemos ejecutar los comandos
	if [ "$fechaActual" = "$fechaProg" ] && [ "$horaActual" = "$horaProg" ]; then
		./$script $parametros
		completo=$?

		# Si no hay problema, reiniciamos los servicios java
		if [ $completo -eq 0 ] && [ $reiniciarAIR -eq 1 ]; then
			# Rutina de reinicio de AIR
			pkill java
			sleep 10
			pkill java
			rm -rf /opt/jboss-as-7.1.1/standalone/tmp/*
			sleep 20
			#su - jboss7 -c "sh bin/standalone.sh -b disesa.condese.net -c standalone-full.xml &"
			sh /opt/jboss-as-7.1.1/bin/standalone.sh -b disesa.condese.net -c standalone-full.xml &
			sleep 60
			#su - rsproceso -c "sh bin/startup.sh &"
			#sleep 60
		fi
	fi
fi

exit $completo