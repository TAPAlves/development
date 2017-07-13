package com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.silicolife.textmining.utils.http.ResponseHandler;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;

public class PUGRestPatentIDSHandler implements ResponseHandler<Map<String,Set<String>>>{

	private Map<String,Set<String>> patentIDs;

	public PUGRestPatentIDSHandler(Map<String,Set<String>> patentIDs) {
		this.patentIDs=patentIDs;
	}


	@Override
	public Map<String,Set<String>> buildResponse(InputStream response, String responseMessage,
			Map<String, List<String>> headerFields, int status) throws ResponseHandlingException {

		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();//Using sax parser in order to read inputstream.
			SAXParser sp = spf.newSAXParser();
			PUGRestPatentIDParser parseEventsHandler = new PUGRestPatentIDParser(patentIDs);
			Reader reader = new InputStreamReader(response,"UTF-8");//conversion to inputstream reader in order to encoding to UTF-8
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			sp.parse(is,parseEventsHandler);
			patentIDs=parseEventsHandler.getPatentIDs();

		} catch (SAXException | IOException | ParserConfigurationException e) {
		}

		return patentIDs;

	}

}
