package net.giro.plataforma;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author MyEclipse Persistence Tools
 */
public class LogUtil {
	
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger("cdeProjectEJBPU");
		logger.setLevel(Level.ALL);
	}
	
	public static void log(String info, Level level, Throwable ex) {
    	logger.log(level, info, ex);
    }
    
    public static Logger getLogger() {
    	return logger;
    }
    
}
