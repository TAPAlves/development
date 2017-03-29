package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PUGRestValidateResponseParser extends DefaultHandler{

	private String tempString;
	private String message;

	public PUGRestValidateResponseParser (){
	}

	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{
	}


	public void endElement(String s, String s1, String element) throws SAXException{

		if (element.equalsIgnoreCase("Message")){
			message=tempString;
			endDocument();
		}
	}


	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);
	}


	public String getMessage(){
		return this.message;

	}

}
