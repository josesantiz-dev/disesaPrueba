package net.giro.cxc.fe;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

public class Util {
	static public void log(Exception e) {
		Logger.getAnonymousLogger().info(e.toString());
		String msg = "";
		
		for( int i=0; i!= e.getStackTrace().length; i++) {
             msg += e.getStackTrace()[i].toString() + "\n";
        }
		
		Logger.getAnonymousLogger().info(msg);
	}
	
	public static void ProcesarXSLT(String xml, String xsltFileName, Writer out) throws MalformedURLException, IOException, TransformerException {
		javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
        //javax.xml.transform.Source xmlSource = 
        //    new javax.xml.transform.stream.StreamSource
        //                 (new java.net.URL("file","", xmlFileName).openStream());
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new StringReader(xml));
		javax.xml.transform.Source xslSource = new javax.xml.transform.stream.StreamSource(new StringReader(xsltFileName));
        // Generate the transformer.
        javax.xml.transform.Transformer transformer = tFactory.newTransformer(xslSource);
        // Perform the transformation, sending the output to the response.
        transformer.transform(xmlSource, new javax.xml.transform.stream.StreamResult(out));
	}
}
