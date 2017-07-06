package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.bulkData.bdss;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PatentIDXMLParser extends DefaultHandler{

	private String tempString;
	private String patentID;
	private boolean fillElement=false;

	public PatentIDXMLParser(String patentID){
		this.patentID=patentID;

	}

	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{

		if (elementName.equalsIgnoreCase("publication-reference")){
			fillElement=true;
		}

	}


	public void endElement(String s, String s1, String element) throws SAXException{

		if (element.equalsIgnoreCase("country") && fillElement){

			if (patentID==null||patentID.isEmpty()){
				patentID=tempString;
			}
		}

		if (element.equalsIgnoreCase("doc-number") && fillElement){

			if (!patentID.contains(tempString)){
				patentID+=tempString;
			}
		}	
		if (element.equalsIgnoreCase("kind") && fillElement){

			if (!patentID.contains(tempString)){
				patentID+=tempString;
			}
		}

		if (element.equalsIgnoreCase("publication-reference")){
			fillElement=false;
			endDocument();

		}
	}


	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);//initialize the string with the correspondent information


	}
	
	
	public String getPatentIDString(){
		return patentID;
		
	}
	
}
