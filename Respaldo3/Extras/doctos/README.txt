-------------------------------------------------------------------------------------------------------------------------------
ERROR "Could not generate DH keypair" al iniciar JBOSS en windows
-------------------------------------------------------------------------------------------------------------------------------
1. Ve a "%JAVA_HOME%\jre\lib\security" 
2. Busca y edita el archivo "java.security"
3. Añade DH como algoritmo deshabilitado en "jdk.tls.disabledAlgorithms"
	# Example:
	#	jdk.tls.disabledAlgorithms=MD5, SHA1, DSA, RSA keySize < 2048
		jdk.tls.disabledAlgorithms=SSLv3, RC4, DH
4. Reinicia el JBOSS

Referencia: http://stackoverflow.com/questions/10687200/java-7-and-could-not-generate-dh-keypair


-------------------------------------------------------------------------------------------------------------------------------
ERROR "JBAS015052: Did not receive a response to the deployment operation within the allowed timeout period" al iniciar JBOSS
-------------------------------------------------------------------------------------------------------------------------------
Ocurre al levantar el JBOSS desde un equipo distinto al de la BD, causando que ningun jar y/o war pueda hacer deploy.

Busca en la configuracion del JBOSS (standalone.xml o standalone-full.xml)
	<deployment-scanner path="deployments" relative-to="jboss.server.base.dir" scan-interval="5000" />

Añade ---> deployment-timeout="1200", quedando de la siguiente manera:
	<deployment-scanner path="deployments" relative-to="jboss.server.base.dir" scan-interval="5000" deployment-timeout="1200" />