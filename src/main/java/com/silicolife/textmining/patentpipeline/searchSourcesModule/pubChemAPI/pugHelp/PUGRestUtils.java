package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	private static String outputFormat=PUGRestOutputEnum.xml.toString(); //xml,json,csv,sdf,txt,png
	private static boolean nameTypeCompoundSign=false;
	private static String fastidentityString="fastidentity";


	private static HTTPClient client = new HTTPClient();


	public static Map<String, Set<String>> getPatentIDsUsingCID(String identifier){

		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR 
				+ PUGRestInputEnum.compoundIdentifier.toString() + SEPARATOR + identifier
				+ SEPARATOR + operation + SEPARATOR + outputFormat;

		Map<String, Set<String>> patentIDs = httpClientGetRequest(urlPatentsForAID);
		return patentIDs;

	}


	public static Map<String, Set<String>> getPatentIDsUsingSMILEs(String identifier){
		Map<String, Set<String>> patentIDs = new HashMap<>();
		try {
			identifier=URLEncoder.encode(identifier,"UTF-8");

			String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR
					+ fastidentityString + SEPARATOR + PUGRestInputEnum.smiles.toString()
					+ SEPARATOR + identifier
					+ SEPARATOR + "cids" + SEPARATOR + PUGRestOutputEnum.json.toString();

			ListIterator<Long> iterator = getJsonIteratorUsingURL(urlPatentsForAID);
			while (iterator.hasNext()){
				patentIDs.putAll(getPatentIDsUsingCID((iterator.next().toString())));
			}
		} catch (Exception e) {
		}

		return patentIDs;

	}


	public static Map<String, Set<String>> getPatentIDsUsingInchiKey(String identifier) {
		Map<String, Set<String>> patentIDs = new HashMap<>();

		//			identifier=URLEncoder.encode(identifier,"UTF-8");

		String urlPatentsForAID= generalURL + SEPARATOR + database + SEPARATOR
				+ PUGRestInputEnum.inchikey.toString()
				+ SEPARATOR + identifier + SEPARATOR + "cids" + SEPARATOR + PUGRestOutputEnum.json.toString();
		//https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/inchikey/SJWWTRQNNRNTPU-ABBNZJFMSA-N/cids/xml
		try{
			ListIterator<Long> iterator = getJsonIteratorUsingURL(urlPatentsForAID);
			while (iterator.hasNext()){
				patentIDs.putAll(getPatentIDsUsingCID((iterator.next().toString())));
			}
		} catch (Exception e) {
		}

		return patentIDs;

	}


	@SuppressWarnings("unchecked")
	private static ListIterator<Long> getJsonIteratorUsingURL(String urlPatentsForAID){
		URL url;
		try {
			url = new URL(urlPatentsForAID);
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();

			JSONParser parser= new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(new InputStreamReader(in));
			jsonObj = (JSONObject) jsonObj.get("IdentifierList");
			JSONArray cids = (JSONArray) jsonObj.get("CID");
			ListIterator<Long> iterator = cids.listIterator();
			return iterator;
		} catch (IOException | ParseException e) {;
		}
		return null;
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
