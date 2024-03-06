package net.giro.clientes.plataforma;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class MiLog extends AppenderSkeleton {
	PatternLayout p = null;
	
	public MiLog(){
		this.p = new PatternLayout("%m%n");
	}
	
	public void detallesLog(){
		this.p = new PatternLayout("%-5p %d [%t]:  %m%n");
	}
	
	private StringBuffer  log = new StringBuffer();
	@Override
	protected void append(LoggingEvent arg0) {
		 log.append(p.format(arg0));

	}

	public void close() {/* no hace nada*/}

	public boolean requiresLayout() {
		return false;
	}

	public void setLog(StringBuffer log) {
		this.log = log;
	}

	public StringBuffer getLog() {
		return log;
	}
}
