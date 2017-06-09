package com.silicolife.textmining.patentpipeline.bulkData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.silicolife.textmining.core.datastructures.documents.PublicationImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationSourcesDefaultEnum;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.patentpipeline.bulkData.bdss.BDSSUtils;

public class MainTest {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException{
		Set<String> fileNames = BDSSUtils.getFileNames("2017");
		for (String filename:fileNames){
			String filePath = BDSSUtils.getBulkPatentFile("2017",filename);
			String patentsFolder = filePath.replace(".zip", "");
			BDSSUtils.unzipPatentFullTextFile(filePath, patentsFolder);
			Set<IPublication> pubs = BDSSUtils.parseXMLfile(new File(patentsFolder).listFiles()[0].toString());
			String toSave="";
			for(IPublication pub:pubs){
				toSave+="\n\n" + PublicationImpl.getPublicationExternalIDForSource(pub,PublicationSourcesDefaultEnum.patent.name()) +"\n";
				toSave+=pub.toString();
				System.out.println(pub);
			}
			saveToFile(patentsFolder + "/" + "pubTXTFiles.txt", toSave);
		}
	}



	private static void saveToFile(String filename, String toSaveString) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.write(toSaveString);
		writer.close();
	}
}
