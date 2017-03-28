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

	private static String SEPARATOR="/";
	private static String generalURL="https://pubchem.ncbi.nlm.nih.gov/rest/pug";
	private static String database= "compound";
	private static String operation="xrefs/patentID";
	private static String outputFormat="xml"; //xml,json,csv,sdf,txt,png

	private static HTTPClient client = new HTTPClient();


	public static Set<String> getPatentIDs(String identifier){

		Map<String, String> headers = new HashMap<String, String>();

		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + identifier
				+ SEPARATOR + operation + SEPARATOR + outputFormat;

		Set<String> patentIDs = new HashSet<>();

		try {
			patentIDs=client.get(urlPatentsForAID,headers, new PUGRestPatentIDSHandler(patentIDs));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		return patentIDs;






	}




}
