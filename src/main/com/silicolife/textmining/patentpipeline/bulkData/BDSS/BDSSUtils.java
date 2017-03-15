package main.com.silicolife.textmining.patentpipeline.bulkData.BDSS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.silicolife.textmining.utils.http.HTTPClient;
import com.silicolife.textmining.utils.http.exceptions.ClientErrorException;
import com.silicolife.textmining.utils.http.exceptions.ConnectionException;
import com.silicolife.textmining.utils.http.exceptions.RedirectionException;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;
import com.silicolife.textmining.utils.http.exceptions.ServerErrorException;



public class BDSSUtils {

	private static String generalURL="https://bulkdata.uspto.gov/data2/patent/grant/redbook/fulltext/";

	private static String outputDir="tempZipFiles/";

	private static HTTPClient client = new HTTPClient();



	public static Set<String> getFileNames (String year){

		Map<String, String> headers = new HashMap<String, String>();

		String urlPatentFullTextFilesList= generalURL + year;

		Set<String> filenames = new HashSet<>();

		try {
			client.get(urlPatentFullTextFilesList, headers, new BDSSYearFileListHandler(filenames));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		return filenames;

	}



	public static void getBulkPatentFile(String year, String filename) throws IOException{
		Path docPath = Paths.get(outputDir);	
		if (!Files.exists(docPath)){
			Files.createDirectories(docPath);
		}
		
		Map<String, String> headers = new HashMap<String, String>();

		String urlPatentFullTextFile= generalURL + year + "/" + filename;
		long totalFileSize = getFileLenght(urlPatentFullTextFile);
		
		try {
			client.get(urlPatentFullTextFile, headers, new BDSSYearFileDownloadHandler(docPath.toString()+ "/" +filename, totalFileSize));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		
		


	}


public static long getFileLenght(String urlConnection){
	long totalFileSize = 0;
	try {
	    URL url = new URL(urlConnection);;
	    URLConnection conn = url.openConnection();
	    conn.connect();
	   totalFileSize = conn.getContentLengthLong();
	} catch (Exception e) {
		
	}
	return totalFileSize;
}



	public static void getPatentIDs (){

	}


	public static void getMapWithMetainformationFromAllPatents(){

	}


	public static Document createJDOMDocument(InputStream response) throws ParserConfigurationException, SAXException, IOException {
		String stream = IOUtils.toString(response, "UTF-8");
		stream = stream.replaceAll("\n", "");
		stream = stream.replaceAll("\\s{2,}", "");
		InputStream imputstream = new ByteArrayInputStream(stream.getBytes("UTF-8"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = parser.parse(imputstream);
		return doc;

	}
	
	






}
