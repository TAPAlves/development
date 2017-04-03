package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PUGRestPatentIDParser extends DefaultHandler{


	private String tempString;
	private Map<String, Set<String>> patentIDs;
	private String compoundIdentifier=new String();
	private Set<String> patentsForAUniqueID;

	public PUGRestPatentIDParser (Map<String, Set<String>> patentIDs){
		this.patentIDs=patentIDs;

	}

	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{
		if (elementName.equalsIgnoreCase("CID")){
			if (patentsForAUniqueID!=null && !patentsForAUniqueID.isEmpty()){
				patentIDs.put(compoundIdentifier, patentsForAUniqueID);
				patentsForAUniqueID=new HashSet<>();
			}
			else{
				patentsForAUniqueID=new HashSet<>();
			}
		}			
	}


	public void endElement(String s, String s1, String element) throws SAXException{

		if (element.equalsIgnoreCase("PatentID")){
			if (tempString.matches("[A-Z]{1,3}\\d+[A-Z]{0,1}\\d{0,1}") && tempString.length()>=5){
				patentsForAUniqueID.add(tempString);

			}
		}
		if (element.equalsIgnoreCase("CID")){
			compoundIdentifier=tempString;
		}
	}


	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);
	}


	public Map<String,Set<String>> getPatentIDs(){
		return this.patentIDs;

	}
}