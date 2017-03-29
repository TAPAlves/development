package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.silicolife.textmining.utils.http.HTTPClient;
import com.silicolife.textmining.utils.http.exceptions.ClientErrorException;
import com.silicolife.textmining.utils.http.exceptions.ConnectionException;
import com.silicolife.textmining.utils.http.exceptions.RedirectionException;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;
import com.silicolife.textmining.utils.http.exceptions.ServerErrorException;

public class PUGRestUtils {

	public static final String PUGRestSearch = "PUG Rest Patent Search";
	private static String SEPARATOR="/";
	private static String generalURL="https://pubchem.ncbi.nlm.nih.gov/rest/pug";
	private static String database= "compound";
	private static String operation="xrefs/patentID";
	private static String outputFormat="xml"; //xml,json,csv,sdf,txt,png

	private static HTTPClient client = new HTTPClient();


	public static Set<String> getPatentIDsUsingCID(String identifier){

		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + identifier
				+ SEPARATOR + operation + SEPARATOR + outputFormat;

		Set<String> patentIDs = httpClientGetRequest(urlPatentsForAID);
		return patentIDs;

	}

	public static Set<String> getPatentIDsUsingCompoundName (String compound){

		String[] cFractions = compound.split(" ");
		if (cFractions.length>1){
			compound=buildCompoundName(cFractions);
		}
		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundName.toString() + SEPARATOR + compound
				+ SEPARATOR + operation + SEPARATOR + outputFormat;
		Set<String> patentIDs = httpClientGetRequest(urlPatentsForAID);
		return patentIDs;



	}

	private static String buildCompoundName(String[] cFractions){
		String resultStr = new String();
		for (String frac:cFractions){
			resultStr+=frac +"%20";
		}
		return resultStr.substring(0,resultStr.length()-3);
	}
	
	
	
	
	private static Set<String> httpClientGetRequest(String url){
		Map<String, String> headers = new HashMap<String, String>();

		Set<String> patentIDs = new HashSet<>();

		try {
			patentIDs=client.get(url,headers, new PUGRestPatentIDSHandler(patentIDs));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		return patentIDs;

	}
	
	
	
	private static String httpClientValidateResponse(String url){
		Map<String, String> headers = new HashMap<String, String>();

		String message = new String();

		try {
			message=client.get(url,headers, new PUGRestValidateResponseHandler());
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		return message;

	}
	
	
	public static String verifyValideInputResponse(String compound){
		String message=new String();
		try{
			Integer.parseInt(compound);
			String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
					+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + compound
					+ SEPARATOR + operation + SEPARATOR + outputFormat;
			message = httpClientValidateResponse(urlPatentsForAID);
				
		}catch (NumberFormatException e) {
			String[] cFractions = compound.split(" ");
			if (cFractions.length>1){
				compound=buildCompoundName(cFractions);
			}
			String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
					+ PUGRestInputEnum.compoundName.toString() + SEPARATOR + compound
					+ SEPARATOR + operation + SEPARATOR + outputFormat;
			message = httpClientValidateResponse(urlPatentsForAID);
		}
		return message;
		
		
	}
	
	
	











}
