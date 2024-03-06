#!/bin/bash
# Script para Crear repositorio con una estructura siguiente
# Parametros: NOMBRE DEL REPOSITORIO

### Capturo el nombre del repositorio nuevo
parametro=$1
if [ "$parametro" == "" ]; then
	echo "Debe indicar el nombre del repositorio"
	exit 1
fi

if [ -d $parametro ]; then
	echo "El Repositorio indicado ya existe"
	exit 1
fi

### Creamos el repositorio con su estructura
svnadmin create /opt/svn/$parametro
svn mkdir -message="Directorios base del subversion" file:///opt/svn/$parametro/trunk file:///opt/svn/$parametro/tags file:///opt/svn/$parametro/branches

### Cambiamos el propietario y permisos
chown -R www-data:www-data /opt/svn/$parametro
chmod -R 775 /opt/svn/$parametro

exit 0