package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PUGRestPatentIDParser extends DefaultHandler{


	private String tempString;
	private Set<String> patentIDs;

	public PUGRestPatentIDParser (Set<String> patentIDs){
		this.patentIDs=patentIDs;

	}

@Override
	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{
		if (elementName.equalsIgnoreCase("PatentID")){
			tempString=new String();
		}

	}



@Override
	public void endElement(String s, String s1, String element) throws SAXException{

		if (element.equalsIgnoreCase("PatentID")){
			patentIDs.add(tempString);
		}
		
		if (element.equalsIgnoreCase("Information")){
			endDocument();
		}

		
	}


@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);


	}


	public Set<String> getPatentIDs(){
		return this.patentIDs;

	}








}