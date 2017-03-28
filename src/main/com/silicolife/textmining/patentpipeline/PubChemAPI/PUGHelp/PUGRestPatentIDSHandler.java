package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.silicolife.textmining.utils.http.ResponseHandler;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;

public class PUGRestPatentIDSHandler implements ResponseHandler<Set<String>>{

	private Set<String> patentIDs;
	private PUGRestPatentIDParser parseEventsHandler;

	public PUGRestPatentIDSHandler(Set<String> patentIDs) {
		this.patentIDs=patentIDs;
		this.parseEventsHandler= new PUGRestPatentIDParser(patentIDs);
	}


	@Override
	public Set<String> buildResponse(InputStream response, String responseMessage,
			Map<String, List<String>> headerFields, int status) throws ResponseHandlingException {

		try {
			String theString = IOUtils.toString(response, "UTF-8"); 
			//			PrintWriter a=new PrintWriter("test.txt");
			//			a.print(theString);
			//			a.close();
			SAXParserFactory spf = SAXParserFactory.newInstance();//Using sax parser in order to read inputstream.
			SAXParser sp = spf.newSAXParser();
			//			System.out.println(theString);
//			sPUGRestPatentIDParser parseEventsHandler = new PUGRestPatentIDParser(patentIDs);
			Reader reader = new InputStreamReader(response,"UTF-8");//conversion to inputstream reader in order to encoding to UTF-8
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			sp.parse(is,parseEventsHandler);
//			patentIDs=;
			//			sp.parse(response,parseEventsHandler);

		} catch (SAXException | IOException | ParserConfigurationException e) {

		}

		return parseEventsHandler.getPatentIDs();



	}

}
