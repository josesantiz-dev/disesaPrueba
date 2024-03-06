package net.giro.scheduler;


import org.xml.sax.InputSource;
import org.w3c.dom.*; 

import java.io.IOException;
import java.io.StringReader; 
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException;

public class DomParser {
	private Document document;
	private HashMap<String, Double> monedas;
	
	public DomParser() {
		this.monedas = new HashMap<String, Double>();
	}

	public void runExample(String xml) {
		//parse the xml file and get the dom object
		parseXmlFile(xml);
		//get each employee element and create a Employee object
		parseDocument();
	}
	
	private void parseXmlFile(String xml){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xml));
			this.document = db.parse(is);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    
	    return "?";
	}
	 
	private void parseDocument(){
		//get the root elememt
		Element docEle = this.document.getDocumentElement();
		
		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("bm:Series");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the employee element
				Element el = (Element)nl.item(i);
				String type = el.getAttribute("BANXICO_UNIT_TYPE");
				
				if (type.equals("PesoxDoll") && ! this.monedas.containsKey("PesoxDoll")) {
				   NodeList name = el.getElementsByTagName("bm:Obs");
		           Element line = (Element) name.item(0);
		           this.monedas.put("PesoxDoll", new Double(line.getAttribute("OBS_VALUE")));
			    } 
				
				if (type.equals("Udi") && ! this.monedas.containsKey("Udi")) {
				   NodeList name = el.getElementsByTagName("bm:Obs");
		           Element line = (Element) name.item(0);
		           this.monedas.put("Udi", new Double(line.getAttribute("OBS_VALUE")));
			    }
			}
		}
	}
	
	public HashMap<String, Double> getMonedas() {
		return monedas;
	}
}