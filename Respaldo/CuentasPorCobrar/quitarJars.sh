
echo "eliminando archivos deployed"
rm -rf $JBOSS_HOME/standalone/deployments/cas.war.deployed
rm -rf $JBOSS_HOME/standalone/deployments/Comun.jar.deployed
rm -rf $JBOSS_HOME/standalone/deployments/Giro.war.deployed


echo "moviendo jars"
mv $JBOSS_HOME/standalone/deployments/cas.war /Users/Daniel/Documents/temporal\ deploy/

mv $JBOSS_HOME/standalone/deployments/Model_Cargas_Documentos.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_Clientes.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_Credito.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_CuentasPorCobrar.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_CuentasPorPagar.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_Factoraje.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_GestionProyectos.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_Publico.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_RecHum.jar /Users/Daniel/Documents/temporal\ deploy/
mv $JBOSS_HOME/standalone/deployments/Model_Tesoreria.jar /Users/Daniel/Documents/temporal\ deploy/

