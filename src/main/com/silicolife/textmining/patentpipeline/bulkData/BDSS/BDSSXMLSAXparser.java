package main.com.silicolife.textmining.patentpipeline.bulkData.BDSS;

import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.silicolife.textmining.core.datastructures.documents.PublicationExternalSourceLinkImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationSourcesDefaultEnum;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.core.document.IPublicationExternalSourceLink;

public class BDSSXMLSAXparser extends DefaultHandler{

	String tempString;
	Set<IPublication> pubs;
	IPublication actualPub;
	String applicants;
	String inventors;
	String patentID;
	boolean fillElement=false;

	public BDSSXMLSAXparser(Set<IPublication> pubs){
		this.pubs=pubs;

	}

	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{
		if (elementName.equalsIgnoreCase("us-patent-grant")){
			actualPub=new PublicationImpl();	
		}
		if (elementName.equalsIgnoreCase("publication-reference")){
			fillElement=true;

		}
		if (elementName.equalsIgnoreCase("us-applicants")){
			fillElement=true;
		}
		if (elementName.equalsIgnoreCase("inventors")){
			fillElement=true;
		}






		if (elementName.equalsIgnoreCase("document-id") && fillElement){


		}


	}

	public void endElement(String s, String s1, String element) throws SAXException{
		if (element.equalsIgnoreCase("date") && fillElement){

			if (actualPub.getFulldate()==null||actualPub.getFulldate().isEmpty()){
				actualPub.setYeardate(tempString);
			}
		}

		if (element.equalsIgnoreCase("country") && fillElement){

			if (patentID==null||patentID.isEmpty()){
				patentID=tempString;
			}
		}

		if (element.equalsIgnoreCase("doc-number") && fillElement){

			if (patentID.contains(tempString)){
				patentID+=tempString;
			}
		}	
		if (element.equalsIgnoreCase("kind") && fillElement){

			if (patentID.contains(tempString)){
				patentID+=tempString;
			}
		}

		if (element.equalsIgnoreCase("publication-reference")){
			fillElement=false;
			IPublicationExternalSourceLink e = new PublicationExternalSourceLinkImpl(patentID, PublicationSourcesDefaultEnum.patent.name());
			actualPub.getPublicationExternalIDSource().add(e);

		}

		if (element.equalsIgnoreCase("invention-title")){
			if (actualPub.getTitle()==null||actualPub.getTitle().isEmpty()){
				actualPub.setTitle(tempString);	
			}
		}

		if (element.equalsIgnoreCase("orgname") && fillElement){
			if (applicants==null||applicants.isEmpty()){
				applicants="APPLICANTS: " + tempString;
			}
			else if(!applicants.contains(tempString)){
				applicants=applicants+" AND "+tempString;
			}
		} 

		if (element.equalsIgnoreCase("us-applicants")){
			fillElement=false;
			//			if (actualPub.getAuthors()==null||actualPub.getAuthors().isEmpty()){
			//				actualPub.setAuthors(applicants);
			//			}

		}


		if (element.equalsIgnoreCase("last-name") && fillElement){
			if (inventors==null||inventors.isEmpty()){
				inventors="INVENTORS: " + tempString;
			}
			else if(!inventors.contains(tempString)){
				inventors=inventors+" AND "+tempString;

			}
		}

		if (element.equalsIgnoreCase("first-name") && fillElement){
			if(!inventors.contains(tempString)){
				inventors=inventors+","+tempString;
			}

		}
		if (element.equalsIgnoreCase("inventors")){
			fillElement=false;
			if (actualPub.getAuthors()==null||actualPub.getAuthors().isEmpty()){
				actualPub.setAuthors(applicants + " " + inventors);
			}

		}












		if (element.equalsIgnoreCase("name")){
			if (applicants==null||applicants.isEmpty()){
				//pub.setAuthors(tempString);
				applicants=tempString;
			}
			else if(!applicants.contains(tempString)){
				//pub.setAuthors(pub.getAuthors()+" AND "+tempString);
				applicants=applicants+" AND "+tempString;
			}
		}
		if (element.equalsIgnoreCase("abstract")){
			if (actualPub.getAbstractSection()==null||actualPub.getAbstractSection().isEmpty()){
				actualPub.setAbstractSection(tempString);
			}
		}
		if (element.equalsIgnoreCase("applicants")){
			if (actualPub.getAuthors()==null||actualPub.getAuthors().isEmpty()){
				actualPub.setAuthors(applicants);
			}

		}

	}

	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);//initialize the string with the correspondent information


	}

}


