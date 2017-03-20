package main.com.silicolife.textmining.patentpipeline.bulkData.BDSS;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.silicolife.textmining.core.datastructures.dataaccess.database.dataaccess.implementation.utils.PublicationFieldTypeEnum;
import com.silicolife.textmining.core.datastructures.documents.PublicationExternalSourceLinkImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationSourcesDefaultEnum;
import com.silicolife.textmining.core.datastructures.documents.structure.PublicationFieldImpl;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.core.document.IPublicationExternalSourceLink;
import com.silicolife.textmining.core.interfaces.core.document.structure.IPublicationField;

public class BDSSXMLSAXparser extends DefaultHandler{

	private String tempString;
	private IPublication actualPub;
	private String applicants,inventors,patentID,claims,abtract,claimIdentifier,description;
	private boolean fillElement,abstractInit,descriptionInit=false;

	//	public BDSSXMLSAXparser(File patentXMLFile){
	//		this.patentXMLFile=patentXMLFile;
	//
	//	}

	public BDSSXMLSAXparser() {
		actualPub=new PublicationImpl();
	}

	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException{

		if (elementName.equalsIgnoreCase("publication-reference")){
			fillElement=true;
		}
		if (elementName.equalsIgnoreCase("us-applicants")){
			fillElement=true;
		}
		if (elementName.equalsIgnoreCase("inventors")){
			fillElement=true;
		}
		if (elementName.equalsIgnoreCase("claims")){
			fillElement=true;
		}
		if (elementName.equalsIgnoreCase("claim")){
			claimIdentifier=attributes.getValue("id");
		}
		if (elementName.equalsIgnoreCase("abstract")){
			abstractInit=true;
		}
		if (elementName.equalsIgnoreCase("description")){
			descriptionInit=true;
		}



		if (elementName.equalsIgnoreCase("?brief-description-of-drawings") && descriptionInit){

			if (description==null||description.isEmpty()){
				description= attributes.getValue("description") + " ";
			}
			else if(!inventors.contains(tempString)){
				description+= " " + attributes.getValue("description") + " ";;
			}
		}


		if (elementName.equalsIgnoreCase("?BRFSUM") && descriptionInit){

			if (description==null||description.isEmpty()){
				description= attributes.getValue("description");
			}
			else if(!inventors.contains(tempString)){
				description+= " " + attributes.getValue("description") + " ";;
			}
		}

		if (elementName.equalsIgnoreCase("?DETDESC") && descriptionInit){

			if (description==null||description.isEmpty()){
				description= attributes.getValue("description");
			}
			else if(!inventors.contains(tempString)){
				description+= " " + attributes.getValue("description") + " ";;
			}
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

		if (element.equalsIgnoreCase("p") && abstractInit){
			if (abtract==null||abtract.isEmpty()){
				abtract=tempString;
			}
			else if(!abtract.contains(tempString)){
				abtract+= " " + tempString;
			}
		}

		if (element.equalsIgnoreCase("abstract")){
			abstractInit=false;
			if (actualPub.getAbstractSection()==null || actualPub.getAbstractSection().isEmpty()){
				actualPub.setAbstractSection(abtract);
			}
			else if (!actualPub.getAbstractSection().contains(abtract)){
				actualPub.setAbstractSection(abtract + actualPub.getAbstractSection());
			}

		}
		if (element.equalsIgnoreCase("p") && descriptionInit){
			if (description==null||description.isEmpty()){
				description=tempString;
			}
			else if(!description.contains(tempString)){
				description+= " " + tempString;
			}
		}

		if (element.equalsIgnoreCase("heading") && descriptionInit){
			if (description==null||description.isEmpty()){
				description=tempString +" ";
			}
			else {
				description+= " " + tempString + ": ";
			}
		}
		if (element.equalsIgnoreCase("description")){
			descriptionInit=false;
			int intEndAbstract = actualPub.getAbstractSection().length();
			String descriptionConnection = " DESCRIPTION: ";
			int startDescription = intEndAbstract + 1;
			String newAbstract = actualPub.getAbstractSection() + descriptionConnection + description;
			int enddescritpion = newAbstract.length();
			IPublicationField publicationFieldDescription = new PublicationFieldImpl(startDescription, enddescritpion, "Description", PublicationFieldTypeEnum.abstracttext);
			actualPub.getPublicationFields().add(publicationFieldDescription);
		}

		if (element.equalsIgnoreCase("claim-text")){
			if (claims==null||claims.isEmpty()){
				claims=claimIdentifier + ": " + tempString;
			}
			else if(!claims.contains(tempString)){
				claims+= " AND " + claimIdentifier + ": " + tempString;
			}
		}

		if (element.equalsIgnoreCase("claims")){
			fillElement=false;
			int intEndAbstract = actualPub.getAbstractSection().length();
			String claimsConnection = " CLAIMS: ";
			int startClaims = intEndAbstract + 1;
			String newAbstract = actualPub.getAbstractSection() + claimsConnection + claims;
			int endclaims = newAbstract.length();
			IPublicationField publicationFieldClaims = new PublicationFieldImpl(startClaims, endclaims, "Claims", PublicationFieldTypeEnum.abstracttext);
			actualPub.getPublicationFields().add(publicationFieldClaims);
		}

		if (element.equalsIgnoreCase("us-patent-grant")){
			endDocument();
			//			pubs.add(actualPub);
			//			applicants="";
			//			inventors="";
			//			patentID="";
			//			claims="";
			//			description="";
			//			abtract="";
			//			claimIdentifier="";
		}

	}

	public void characters(char[] ac, int i, int j) throws SAXException {
		tempString=new String(ac,i,j);//initialize the string with the correspondent information


	}


	public IPublication getFilledIPublication(){
		return actualPub;
	}

}


