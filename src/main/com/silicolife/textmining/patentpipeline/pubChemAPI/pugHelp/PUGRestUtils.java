package main.com.silicolife.textmining.patentpipeline.pubChemAPI.pugHelp;

import java.util.HashMap;
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
	private static boolean nameTypeCompoundSign=false;

	private static HTTPClient client = new HTTPClient();


	public static Map<String, Set<String>> getPatentIDsUsingCID(String identifier){

		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + identifier
				+ SEPARATOR + operation + SEPARATOR + outputFormat;

		Map<String, Set<String>> patentIDs = httpClientGetRequest(urlPatentsForAID);
		return patentIDs;

	}

	public static Map<String, Set<String>> getPatentIDsUsingCompoundName (String compound){

		String[] cFractions = compound.split(" ");
		if (cFractions.length>1){
			compound=buildCompoundName(cFractions);
		}
		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundName.toString() + SEPARATOR + compound
				+ SEPARATOR + operation + SEPARATOR + outputFormat;
		Map<String, Set<String>> patentIDs = httpClientGetRequest(urlPatentsForAID);
		return patentIDs;



	}

	private static String buildCompoundName(String[] cFractions){
		String resultStr = new String();
		for (String frac:cFractions){
			resultStr+=frac +"%20";
		}
		return resultStr.substring(0,resultStr.length()-3);
	}


	private static Map<String,Set<String>> httpClientGetRequest(String url){
		Map<String, String> headers = new HashMap<String, String>();

		Map<String,Set<String>> patentIDs = new HashMap<>();

		if (!nameTypeCompoundSign){
			try {
				patentIDs=client.get(url,headers, new PUGRestPatentIDSHandler(patentIDs));
			} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
					| ResponseHandlingException e) {
			}
		}
		else{
			nameTypeCompoundSign=false;
			patentIDs=httpClientGetRequest(url+"?name_type=word");
		}
		return patentIDs;

	}


	private static String httpClientValidateResponse(String url,boolean nameTypeRun, boolean compoundName){
		Map<String, String> headers = new HashMap<String, String>();
		String message = new String();
		try {
			message = client.get(url,headers, new PUGRestValidateResponseHandler());
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
			if (!nameTypeRun && compoundName){
				message=httpClientValidateResponse(url+"?name_type=word", true,true);
				nameTypeCompoundSign=true;
			}
			else{
				message= e.getMessage();
			}
		}
		return message;
	}


	public static String verifyValideInputResponse(String compound){
		String message=new String();
		try{
			String[] cFractions = compound.split(",");
			for (String identifier:cFractions){
				Integer.parseInt(identifier);
			}
			String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
					+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + compound
					+ SEPARATOR + operation + SEPARATOR + outputFormat;
			message = httpClientValidateResponse(urlPatentsForAID,false,false);

		}catch (NumberFormatException e) {
			String[] cFractions = compound.split(" ");
			if (cFractions.length>1){
				compound=buildCompoundName(cFractions);
			}
			String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
					+ PUGRestInputEnum.compoundName.toString() + SEPARATOR + compound
					+ SEPARATOR + operation + SEPARATOR + outputFormat;
			message = httpClientValidateResponse(urlPatentsForAID,false,true);
		}
		return message;
	}

}
